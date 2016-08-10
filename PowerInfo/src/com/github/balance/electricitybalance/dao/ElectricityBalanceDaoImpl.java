package com.github.balance.electricitybalance.dao;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.github.balance.common.util.ConvertTools;

import net.sf.json.JSONObject;

@Repository
public class ElectricityBalanceDaoImpl implements ElectricityBalanceDao {
	private static final String ELECSQL=" SELECT yr,NULL,100,VALUE FROM loadelectricquantity_data  WHERE index_item=2 and task_id=?" ;//全社会(统调)最大负荷

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<Map<String, Object>> queryData(JSONObject param) {
		String year = param.getString("year");
		String task_id=param.getString("task_id");
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT y.*,x.hour_num FROM power_hour_copy X RIGHT JOIN ( ") ;
		sb.append("SELECT p.pcode _parentId ,p.VALUE pcode_name,p.code_2 id,p.value_2 code_name,d.*  ") ;
		sb.append(" FROM (");
		sb.append(" SELECT pcode,VALUE,code_2,value_2 FROM electricity4  ORDER BY ORD,ord_2 )p");
		sb.append(" LEFT JOIN  (SELECT p_index_item,index_item ");
		//for (String yearStr : param.get("year").toString().split(",")) {
		for (String yearStr :year.split(",")) {
			sb.append(",SUM(CASE year WHEN ");
			sb.append(yearStr);
			sb.append(" THEN value END) '");
			sb.append(yearStr);
			sb.append("'");
		}
		sb.append(",task_id FROM electricity_data where task_id= ? GROUP BY task_id,p_index_item,index_item) ") ;
		sb.append(" d ON  (p.pcode = d.p_index_item OR (p.pcode IS NULL AND d.p_index_item IS NULL) ) AND p.code_2=d.index_item");
		sb.append(" ) Y ON y.id=x.index_item and x.task_id=y.task_id") ;
		List<Map<String, Object>>  list = this.jdbcTemplate.queryForList(sb.toString(),new Object[]{task_id});
		return list;
	}
	/**
	 * 全社会用电量100
	 * 同比增长率
	 * 外购(+)外送(-)200
	 * 需自发用电量300
	 * 煤电利用小时400
	 */
	@Override
	public  int extractData(JSONObject obj) {
		//String year = obj.getString("year");"2016,2017,2018"
				String year = obj.getString("year");
				String task_id=obj.getString("task_id");
				this.execCopyHour(task_id);

				deleteData(task_id);
				String yearRateSql=getYearRateSQL(year);
				StringBuffer sb = new StringBuffer();
				sb.append(" insert into electricity_data(year,p_index_item,index_item,value,task_id) ");
				sb.append(  ELECSQL);
				sb.append("    UNION ALL  ");
				sb.append(yearRateSql);
				int count = this.jdbcTemplate.update(sb.toString(),new Object[]{task_id});
				 execSeftElec(task_id);
				 execSeftElecByType(task_id);
				 execCoalHour(task_id);
		return 0;
	}
	
	private String getYearRateSQL(String year){
		String[] sourceArr = year.split(",");
		String[] destinationArr = ConvertTools.convertIncreaseRate (sourceArr);
		
		/**
		 * 增长率
		 */
		StringBuffer sbRate = new StringBuffer();
		sbRate.append("  SELECT t.yr,100,1,");
		sbRate.append("         CASE WHEN t.yr = t2.yr THEN NULL ");
		sbRate.append("   WHEN t.value IS NULL OR t2.value IS NULL OR t2.value = 0 THEN NULL");
		sbRate.append("              ELSE  POWER(t.value / t2.value, 1 / (t.value - t2.value)) - 1");
		sbRate.append("    END AS tbzzl ");
		sbRate.append("   ,t.task_id  FROM loadelectricquantity_data t,loadelectricquantity_data t2,(");
		for(int i=0 ; i <sourceArr.length-1 ;++i){
			sbRate.append("select ").append(destinationArr[i])
			.append(" as yr1,").append(sourceArr[i]).append(" as yr2")
			.append(" from dual union all ");
		}
		sbRate.append("select ").append(destinationArr[sourceArr.length-1])
		.append(" as yr1,").append(sourceArr[sourceArr.length-1]).append(" as yr2")
		.append(" from dual ");
		sbRate.append("  )t3  WHERE t.index_item = 2  AND t2.index_item = 2 and t.task_id=? and t2.task_id=?   AND t.yr = t3.yr2 AND t2.yr = t3.yr1");
		return  sbRate.toString();
	}
	/**
	 * 复制利用小时数
	 * @return
	 */
	private int  execCopyHour(String task_id ){
		String deleteSql="delete from power_hour_copy where task_id=?" ;
		this.jdbcTemplate.update(deleteSql,new Object[]{task_id});
		String sql ="insert power_hour_copy(task_id,index_item,hour_num) select task_id,index_item,hour_num from power_hour where task_id=? ";
		int count =this.jdbcTemplate.update(sql,new Object[]{task_id});
		return count;
	}
	
	
	/**
	 *需自发电量
	 * @return
	 */
	private int  execSeftElec(String task_id){

		StringBuffer sb = new StringBuffer();
		sb.append(" insert into electricity_data(p_index_item,index_item,year,value,task_id) ")
		.append( " SELECT NULL ,300,m.year,SUM(m.value),m.task_id FROM (")
		.append(" SELECT YEAR,VALUE,task_id FROM electricity_data WHERE  index_item =100  and task_id=?")
		.append(" UNION ALL")
		.append(" SELECT YEAR,0-VALUE,task_id FROM electricity_data WHERE  index_item=200 and task_id=? ")
		.append(" ) m GROUP BY m.task_id,m.year");
		int count = this.jdbcTemplate.update(sb.toString()) ;
		return count;
	}
	/**
	 *需自发电量分类型（子项）
	 * @return
	 */
	private int  execSeftElecByType(String task_id){

		StringBuffer sb = new StringBuffer();
		sb.append(" insert into electricity_data(p_index_item,index_item,year,value,task_id) ")
		.append( " SELECT 300,t1.index_item ,t1.year,t1.value*t2.hour_num/10000,t1.task_id FROM power_data t1 ")
		.append(" JOIN power_hour_copy  t2 ON  t1.p_index_item=500 AND t1.index_item=t2.index_item and t1.task_id=t2.task_id and t1.task_id=? AND t1.index_item !=3 ");
		int count = this.jdbcTemplate.update(sb.toString()) ;
		StringBuffer sub = new StringBuffer();
		sub.append(" insert into electricity_data(p_index_item,index_item,year,value,task_id) ")
		.append( " SELECT 300 ,3,m.year,SUM(m.value),m.task_id FROM (")
		.append(" SELECT YEAR,VALUE,task_id FROM electricity_data WHERE  index_item =300 and task_id=?")
		.append(" UNION ALL")
		.append(" SELECT YEAR,0-VALUE,task_id FROM electricity_data WHERE  p_index_item=300 and task_id=? AND index_item !=3  ")
		.append(" ) m GROUP BY m.task_id,m.year");
		int subCount = this.jdbcTemplate.update(sub.toString(),new Object[]{task_id,task_id,task_id}) ;
		return count;
	}
	
	/**
	 * 煤电利用小时数
	 * @return
	 */
	private int  execCoalHour(String task_id){

		StringBuffer sub = new StringBuffer();
		sub.append(" insert into power_hour_copy(index_item,value) ")
		.append( "  SELECT x.YEAR,CASE WHEN y.value=0 THEN NULL ELSE x.VALUE/y.value*10000 END AS VALUE  FROM electricity_data X  JOIN ( ")
		.append(" SELECT m.year,SUM(m.value) VALUE,task_id FROM ( ")
		.append(" SELECT YEAR,VALUE,task_id FROM power_data WHERE  p_index_item =500  and task_id=? AND index_item=3")
		.append(" UNION ALL")
		.append("   SELECT YEAR,0-VALUE,task_id FROM power_data WHERE  p_index_item =300  AND index_item=3  and task_id=? ")
		.append("   )m GROUP BY m.task_id,m.year ) Y ON x.year = y.value AND x.index_item=3 AND x.p_index_item=300 and x.task_id=y.task_id");
		int count =this.jdbcTemplate.update(sub.toString(),new Object[]{task_id,task_id});
		return count;
	}
	/**
	 *删除数据
	 * @return
	 */
	private int deleteData(String task_id){
		String sql = " delete from electricity_data where task_id=?" ;
		int count = this.jdbcTemplate.update(sql , new Object[]{task_id} ) ;
		return count;
	}

}
