package com.github.balance.electricitybalance.dao;


import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.github.balance.common.util.ConvertTools;
import com.github.balance.parparedata.powerquotient.entity.PowerHour;
import com.github.balance.powerbalance.entity.PowerData;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Repository
public class ElectricityBalanceDaoImpl implements ElectricityBalanceDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<Map<String, Object>> queryData(JSONObject param) {
		String year = param.getString("year");
		String task_id=param.getString("task_id");
		StringBuffer sb = new StringBuffer();
		sb.append(" select t.* from (SELECT y.*,x.hour_num FROM power_hour X RIGHT JOIN ( ") ;
		sb.append("SELECT p.pcode _parentId ,p.VALUE pcode_name,p.code_2,CONCAT_WS('-',p.pcode,p.code_2) id,p.ORD,p.ord_2,p.value_2 code_name,d.*,  ").append(task_id).append( " task_id ") ;
		sb.append(" FROM (");
		sb.append(" SELECT pcode,VALUE,code_2,value_2,ORD,ord_2 FROM electricity4  ORDER BY ORD,ord_2 )p");
		sb.append(" LEFT JOIN  (SELECT p_index_item,index_item ");
		//for (String yearStr : param.get("year").toString().split(",")) {
		for (String yearStr :year.split(",")) {
			sb.append(",SUM(CASE year WHEN ");
			sb.append(yearStr);
			sb.append(" THEN value END) '");
			sb.append(yearStr);
			sb.append("'");
		}
		sb.append("  FROM electricity_data where task_id= ? GROUP BY task_id,p_index_item,index_item) ") ;
		sb.append(" d ON  (p.pcode = d.p_index_item OR (p.pcode IS NULL AND d.p_index_item IS NULL) ) AND p.code_2=d.index_item ");
		sb.append(" ) Y ON y.code_2=x.index_item and x.task_id=y.task_id  ORDER BY ORD,ord_2 ) t") ;
		/*
		 *   _parentId,pcode_name ,code_2,id,code_name,p_index_item,index_item     .... task_id,hour_num
		 * 			SELECT YEAR,VALUE,task_id FROM power_data WHERE  p_index_item =500  AND task_id=21 AND index_item=3
		UNION ALL
		SELECT YEAR,0-VALUE,task_id FROM power_data WHERE  p_index_item =300  AND index_item=3  AND task_id=21 
		 */
		sb.append(" union all   select a.* from (select null  _parentId,null pcode_name ,700 code_2,700 id,null ORD,null ord_2,700 _name,null p_index_item,null index_item");
		for (String yearStr :year.split(",")) {
			sb.append(",SUM(CASE year WHEN ");
			sb.append(yearStr);
			sb.append(" THEN value END) '");
			sb.append(yearStr);
			sb.append("'");
		}
		sb.append(" ,task_id,null hour_num from ( ")
		.append(" SELECT m.year,SUM(m.value) VALUE,task_id FROM ( ")
		.append(" SELECT YEAR,VALUE,task_id FROM power_data WHERE  p_index_item =500  AND task_id=? AND index_item=3 ")
		.append(" UNION ALL")
		.append(" SELECT YEAR,0-VALUE,task_id FROM power_data WHERE  p_index_item =300  AND index_item=3  AND task_id=? )m")
		.append(" GROUP BY m.task_id,m.year ) n) a ") ;
		
		sb.append(" union all  select p_index_item  _parentId,null pcode_name ,index_item code_2,CONCAT_WS('-',p_index_item,index_item) id,null ORD, null ord_2,index_item _name,null p_index_item1,null index_item1");
		for (String yearStr :year.split(",")) {
			sb.append(",SUM(CASE year WHEN ");
			sb.append(yearStr);
			sb.append(" THEN value END) '");
			sb.append(yearStr);
			sb.append("'");
		}
		sb.append(" ,task_id,null hour_num " );
		sb.append(" from power_data WHERE  (p_index_item =500 AND task_id=? AND index_item !=3)  OR index_item=500 "
				+ " GROUP BY p_index_item,index_item,task_id") ;
		List<Map<String, Object>>  list = this.jdbcTemplate.queryForList(sb.toString(),new Object[]{task_id,task_id,task_id,task_id});
		return list;
	}
	

	@Override
	public List<Map<String, Object>> exportData(JSONObject param) {
		String year = param.getString("year");
		String task_id=param.getString("task_id");
		StringBuffer sb = new StringBuffer();
		sb.append(" select t.* from (SELECT y.*,x.hour_num FROM power_hour X RIGHT JOIN ( ") ;
		sb.append("SELECT p.pcode _parentId ,p.VALUE pcode_name,p.code_2,CONCAT_WS('-',p.pcode,p.code_2) id,p.ORD,p.ord_2,p.value_2 code_name,d.*,  ").append(task_id).append( " task_id ") ;
		sb.append(" FROM (");
		sb.append(" SELECT pcode,VALUE,code_2,value_2,ORD,ord_2 FROM electricity4  ORDER BY ORD,ord_2 )p");
		sb.append(" LEFT JOIN  (SELECT p_index_item,index_item ");
		//for (String yearStr : param.get("year").toString().split(",")) {
		for (String yearStr :year.split(",")) {
			sb.append(",SUM(CASE year WHEN ");
			sb.append(yearStr);
			sb.append(" THEN value END) '");
			sb.append(yearStr);
			sb.append("'");
		}
		sb.append("  FROM electricity_data where task_id= ? GROUP BY task_id,p_index_item,index_item) ") ;
		sb.append(" d ON  (p.pcode = d.p_index_item OR (p.pcode IS NULL AND d.p_index_item IS NULL) ) AND p.code_2=d.index_item ");
		sb.append(" ) Y ON y.code_2=x.index_item and x.task_id=y.task_id  ORDER BY ORD,ord_2 ) t") ;
		/*
		 *   _parentId,pcode_name ,code_2,id,code_name,p_index_item,index_item     .... task_id,hour_num
		 * 			SELECT YEAR,VALUE,task_id FROM power_data WHERE  p_index_item =500  AND task_id=21 AND index_item=3
		UNION ALL
		SELECT YEAR,0-VALUE,task_id FROM power_data WHERE  p_index_item =300  AND index_item=3  AND task_id=21 
		 */
	
		

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
			//	this.execCopyHour(task_id);

				deleteData(task_id);
				String yearRateSql=getYearRateSQL(year,task_id);
				StringBuffer sb = new StringBuffer();
				sb.append(" insert into electricity_data(year,p_index_item,index_item,value,task_id) ");
				String ELECSQL=" SELECT yr,NULL,100,VALUE,"+task_id+"  FROM loadelectricquantity_data  WHERE index_item=202 " ;//全社会(统调)最大负荷

				sb.append(  ELECSQL);
				sb.append("    UNION ALL  ");
				sb.append(yearRateSql);
				sb.append(" union all ");
				sb.append(" SELECT  t1.yr,NULL,200,t1.value,"+task_id+" FROM senddata_data t1 "
						+ "JOIN   senddata_itemname t2 ON   t2.pro_name='4' AND t2.id=t1.index_item ");
				int count = this.jdbcTemplate.update(sb.toString());
				 execSeftElec(task_id);
				 execSeftElecByType(task_id);
				 execCoalHour(task_id);
		return 0;
	}
	
	private String getYearRateSQL(String year,String task_id){
		String[] sourceArr = year.split(",");
		String[] destinationArr = ConvertTools.convertIncreaseRate (sourceArr);
		
		/**
		 * 增长率
		 */
		StringBuffer sbRate = new StringBuffer();
		sbRate.append("  SELECT t.yr,100,100,");
		sbRate.append("         CASE WHEN t.yr = t2.yr THEN NULL ");
		sbRate.append("   WHEN t.value IS NULL OR t2.value IS NULL OR t2.value = 0 THEN NULL");
		sbRate.append("              ELSE  ROUND((POWER(t.value / t2.value, 1.0 / (t.yr - t2.yr)) - 1)*100,2)");
		sbRate.append("    END AS tbzzl ");
		sbRate.append("   ,"+task_id+"  FROM loadelectricquantity_data t,loadelectricquantity_data t2,(");
		for(int i=0 ; i <sourceArr.length-1 ;++i){
			sbRate.append("select ").append(destinationArr[i])
			.append(" as yr1,").append(sourceArr[i]).append(" as yr2")
			.append(" from dual union all ");
		}
		sbRate.append("select ").append(destinationArr[sourceArr.length-1])
		.append(" as yr1,").append(sourceArr[sourceArr.length-1]).append(" as yr2")
		.append(" from dual ");
		sbRate.append("  )t3  WHERE t.index_item = 202  AND t2.index_item = 202   AND t.yr = t3.yr2 AND t2.yr = t3.yr1");
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
		int count = this.jdbcTemplate.update(sb.toString(),new Object[]{task_id,task_id}) ;
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
		.append(" JOIN power_hour  t2 ON  t1.p_index_item=500 AND t1.index_item=t2.index_item and t1.task_id=t2.task_id and t1.task_id=? AND t1.index_item !=3 ");
		int count = this.jdbcTemplate.update(sb.toString(),new Object[]{task_id}) ;
		StringBuffer sub = new StringBuffer();
		sub.append(" insert into electricity_data(p_index_item,index_item,year,value,task_id) ")
		.append( " SELECT 300 ,3,m.year,SUM(m.value),m.task_id FROM (")
		.append(" SELECT YEAR,VALUE,task_id FROM electricity_data WHERE  index_item =300 and task_id=?")
		.append(" UNION ALL")
		.append(" SELECT YEAR,0-VALUE,task_id FROM electricity_data WHERE  p_index_item=300 and task_id=? AND index_item !=3  ")
		.append(" ) m GROUP BY m.task_id,m.year");
		int subCount = this.jdbcTemplate.update(sub.toString(),new Object[]{task_id,task_id}) ;
		return count;
	}
	
	/**
	 * 煤电利用小时数
	 * @return
	 */
	private int  execCoalHour(String task_id){

		StringBuffer sub = new StringBuffer();
		sub.append(" insert into electricity_data(p_index_item,index_item,year,value,task_id) ")
		.append( "  SELECT null,400,x.YEAR,CASE WHEN y.value=0 THEN NULL ELSE round(x.VALUE/y.value*10000,4) END AS VALUE,x.task_id  FROM electricity_data X  JOIN ( ")
		.append(" SELECT m.year,SUM(m.value) VALUE,task_id FROM ( ")
		.append(" SELECT YEAR,VALUE,task_id FROM power_data WHERE  p_index_item =500  and task_id=? AND index_item=3")
		.append(" UNION ALL")
		.append("   SELECT YEAR,0-VALUE,task_id FROM power_data WHERE  p_index_item =300  AND index_item=3  and task_id=? ")
		.append("   )m GROUP BY m.task_id,m.year ) Y ON x.year = y.year AND x.index_item=3 AND x.p_index_item=300 and x.task_id=y.task_id");
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
	@Override
	public String saveData(JSONArray rows,JSONObject obj) {
		String task_id= obj.getString("taskid");
		deleteData(task_id) ;
		 deleteHourSql( task_id);
		List<PowerData> powerData = new ArrayList<PowerData>();
		List<PowerHour> powerHour = new ArrayList<PowerHour>();

		for (int i = 0; i < rows.size(); i++) {
			JSONObject row = rows.getJSONObject(i);
			Iterator<String> its = row.keys();

			String index_type =  row.getString("code_2");
			
			String parentId =null;
			String hour_num=null;
			if(row.get("_parentId")!=null){
				parentId= row.getString("_parentId");
			}
			if(row.get("hour_num")!=null){
				hour_num= row.getString("hour_num");
			}
			while (its.hasNext()) {
				String it = its.next();
				if (it.equals("code_2") || it.equals("_parentId") || it.equals("hour_num")){
					continue;
				}else{		
					powerData.add(setPowerData(row,it,index_type,parentId,task_id));
				}

			}
			if("300".equals(parentId)){
				powerHour.add(setPowerHour(index_type,task_id,hour_num));
			}
		}
		 executePowerHourSQL(powerHour);
		int result = executePowerDataSQL(powerData);
		return ""+result;
	}
	
	private PowerHour setPowerHour(String index_type, String task_id,
			String hour_num) {
		PowerHour ph = new PowerHour();
		ph.setIndex_item(index_type);
		ph.setTask_id(task_id);
		if(hour_num != null &&!"".equals(hour_num)){
			ph.setHour_num(Double.parseDouble(hour_num));
		}else{
			ph.setHour_num(null);
		}
		return ph;
	}
	private int executePowerDataSQL(List<PowerData> powerData) {
		//int deleteCount = deletePowerDataSQL(powerData);
		int insertCount = insertPowerDataSQL(powerData);
		return insertCount;
	}
	
	private int executePowerHourSQL(final List<PowerHour> powerHours) {
		//int deleteCount = deletePowerDataSQL(powerData);
		
		 String sql = "insert into power_hour(task_id,index_item,hour_num) value(?,?,?)";
			
			BatchPreparedStatementSetter setinsert = new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i)
						throws SQLException {
					// TODO Auto-generated method stub
					PowerHour pd =powerHours.get(i);

						ps.setString(1, pd.getTask_id());
						ps.setString(2, pd.getIndex_item());
						ps.setObject(3, "".equals(pd.getHour_num())?null:pd.getHour_num());

				}

				@Override
				public int getBatchSize() {
					// TODO Auto-generated method stub
					return powerHours.size();
				}
				
			};
			int[] inserts=jdbcTemplate.batchUpdate(sql, setinsert);
			return inserts.length;
	}
	
	

	private int deleteHourSql(String task_id) {
		String sql="delete from power_hour where task_id=? ";
		int count = this.jdbcTemplate.update(sql , new Object[]{task_id} ) ;
		return count;
	}
	/**
	 * 新增装机系数的sql语句
	 * @param quotients
	 * @return
	 */
	private int insertPowerDataSQL(final List<PowerData> powerDatas){
	
		String insertsql = "insert electricity_data(p_INDEX_item,INDEX_item,value,task_id,year) VALUES(?,?,?,?,?)";
		
		BatchPreparedStatementSetter setinsert = new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i)
					throws SQLException {
				// TODO Auto-generated method stub
				PowerData pd =powerDatas.get(i);

					ps.setString(1, pd.getParentId());
					ps.setString(2, pd.getCode_2());
					ps.setObject(3, "".equals(pd.getValue())?null:pd.getValue());
					ps.setString(4, pd.getTask_id());
					ps.setString(5, pd.getYear());

			}

			@Override
			public int getBatchSize() {
				// TODO Auto-generated method stub
				return powerDatas.size();
			}
			
		};
		int[] inserts=jdbcTemplate.batchUpdate(insertsql, setinsert);
		return inserts.length;
	}
	/**
	 * 组装装机系数
	 * @param row
	 * @param it
	 * @param index_type
	 * @param task_id
	 * @return
	 */
	private  PowerData setPowerData(JSONObject row,String it,String index_type,String parentId,String task_id){
		PowerData pd = new PowerData();
		pd.setCode_2(index_type);
		pd.setTask_id(task_id);
		pd.setParentId(parentId);
		pd.setYear(it);
		if(!"".equals(row.getString(it))){
			pd.setValue(row.getDouble(it));
		}else{
			pd.setValue(null);
		}
		return pd;
	}
}
