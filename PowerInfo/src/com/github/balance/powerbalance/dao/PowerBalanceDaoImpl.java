package com.github.balance.powerbalance.dao;

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
import com.github.balance.powerbalance.entity.PowerData;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Repository
public class PowerBalanceDaoImpl implements PowerBalanceDao {

	//需要有效装机容量
	
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<Map<String, Object>> queryData(JSONObject param) {
		String year = param.getString("year");
		String task_id=param.getString("task_id");
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT p.pcode _parentId ,p.VALUE pcode_name,p.code_2 ,CONCAT_WS('-',p.pcode,p.code_2) id,p.value_2 code_name,d.*  ") ;
		sb.append(" FROM (");
		sb.append(" SELECT pcode,VALUE,code_2,value_2,ORD,ord_2 FROM power4  ORDER BY ORD,ord_2 )p");
		sb.append(" LEFT JOIN  (SELECT p_index_item,index_item ");
		//for (String yearStr : param.get("year").toString().split(",")) {
		for (String yearStr : year.split(",")) {
			sb.append(",SUM(CASE year WHEN ");
			sb.append(yearStr);
			sb.append(" THEN value END) '");
			sb.append(yearStr);
			sb.append("'");
		}
		sb.append(" FROM power_data where task_id= ? GROUP BY p_index_item,index_item) ") ;
		sb.append(" d ON  (p.pcode = d.p_index_item OR (p.pcode IS NULL AND d.p_index_item IS NULL) ) AND p.code_2=d.index_item ORDER BY ORD,ord_2");

		List<Map<String, Object>>  list = this.jdbcTemplate.queryForList(sb.toString(),new Object[]{task_id});
		return list;
	}
	/**
	 * --全社会(统调)最大负荷100   loadelectricquantity_data
	 *--增长率   loadelectricquantity_data
	 *--需要有效装机容量200  loadelectricquantity_data
	 *---有效备用容量 loadelectricquantity_data
	 *---有效备用率  loadelectricquantity_data
	 *--投产机组容量300  t_dldln_dylxrl（合计后算）
	 *--退役关停机组容量400   t_dldln_dylxrl
	 *--年底装机容量500   t_dldln_dylxrl(后算）
	 *--受阻及空闲容量600 hinderedidlecapacity_data
	 *--当年可用容量700 （后算）
	 *--外购（+）外送（-）800
	 *--装机盈余900（后算）
	 */
	@Override
	public  int extractData(JSONObject obj) {
		String year = obj.getString("year");
		String task_id=obj.getString("task_id");
		deleteData(task_id);
		//String year = "2016,2017,2018";
		//String yearRateSql=getYearRateSQL(year,task_id);
		StringBuffer sb = new StringBuffer();
		sb.append(" insert into power_data(year,p_index_item,index_item,value,task_id) ");
		String LOADSQL=" SELECT yr,NULL,100,VALUE,"+task_id+" FROM loadelectricquantity_data  WHERE index_item=201  " ;//全社会(统调)最大负荷

		sb.append(  LOADSQL);
		//sb.append("    UNION ALL  ");
		//sb.append(yearRateSql);
		sb.append("    UNION ALL  ");
		 String ZJRLSQL=" SELECT t1.yr,200,1,t1.VALUE*t2.value/100,"+task_id+"  FROM loadelectricquantity_data t1 JOIN  loadelectricquantity_data  t2 ON t1.yr=t2.yr  AND t1.index_item=201 AND t2.index_item=203 " ;//有效备用容量

		sb.append( ZJRLSQL);
		sb.append("    UNION ALL  ");
		String BYRLSQL=" SELECT yr,200,2,VALUE,"+task_id+"  FROM loadelectricquantity_data  WHERE index_item=203 " ;//有效备用系数

		sb.append(  BYRLSQL );
		sb.append("    UNION ALL  ");
		String BYLSQL=" SELECT l1.yr,NULL,200,l1.VALUE*(1+l2.VALUE/100),"+task_id+" FROM loadelectricquantity_data l1 JOIN loadelectricquantity_data l2  WHERE l1.yr=l2.yr AND  l1.index_item=201 AND l2.index_item=203 " ;//需要有效装机容量

		sb.append( BYLSQL);
		sb.append("    UNION ALL  ");
		sb.append( getExistingCapacitySQL(year,task_id));
		sb.append("    UNION ALL  ");
		sb.append( getRetireCapacitySQL(year,task_id));
		sb.append("    UNION ALL  ");
		sb.append(getOperationalCapacitySQL(year,task_id));
		/*
		 * 受阻容量换成了比例
		 */
		//sb.append("    UNION ALL  ");
		 //String SZKXRLSQL=" SELECT yr,NULL,600,SUM(VALUE),"+task_id+" FROM hinderedidlecapacity_data where index_item=1 GROUP BY yr ";//受阻及空闲容量

		//sb.append(SZKXRLSQL);
		//sb.append("    UNION ALL  ");
		// String SZKXRLSUBSQL=" SELECT yr,600,index_item,VALUE,"+task_id+" FROM hinderedidlecapacity_data where  index_item !=1";//受阻及空闲容量子项

		//sb.append(SZKXRLSUBSQL);
		sb.append("    UNION ALL  ");
		sb.append(" SELECT  t1.yr,NULL,800,t1.value,"+task_id+" FROM senddata_data t1 JOIN   senddata_itemname t2 ON  t2.pro_name='2' AND t2.id=t1.index_item  ");
		int count = this.jdbcTemplate.update(sb.toString());
		int operationalCount = this.execOperationalCapacitySum(task_id);
		int endYearCount =this.execEndYearCapacitySum(task_id);
		exeSzSub(task_id);
		exeSzSum(task_id);
		int currentYearCount = this.execCurrentYearCapacitySum(task_id);
		int surplusCount = this.execSurplusCapacitySum(task_id);
		return count+operationalCount+endYearCount+currentYearCount+surplusCount;
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
		sbRate.append("   ,"+task_id+" FROM loadelectricquantity_data t,loadelectricquantity_data t2,(");
		for(int i=0 ; i <sourceArr.length-1 ;++i){
			sbRate.append("select ").append(destinationArr[i])
			.append(" as yr1,").append(sourceArr[i]).append(" as yr2")
			.append(" from dual union all ");
		}
		sbRate.append("select ").append(destinationArr[sourceArr.length-1])
		.append(" as yr1,").append(sourceArr[sourceArr.length-1]).append(" as yr2")
		.append(" from dual ");
		sbRate.append("  )t3  WHERE t.index_item = 201  AND t2.index_item = 201  AND t.yr = t3.yr2 AND t2.yr = t3.yr1");
		return  sbRate.toString();
	}
	/**
	 * 获得现有装机容量sql，有合计
	 * @param year
	 * @return
	 */
	private String getExistingCapacitySQL(String year,String task_id){
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT yr,null,1000,SUM(gene_capacity),");
		sb.append(task_id)
		.append	( " FROM generator_data JOIN  ")
		.append(getYearDual(year))
		.append("  ON SUBSTR(DATE_FORMAT(start_date,'%Y-%c-%d'),1,4)<t.yr ")
		.append( " AND (SUBSTR(DATE_FORMAT(end_date,'%Y-%c-%d'),1,4)>=t.yr OR end_date IS NULL)")
		.append("  GROUP BY yr") 
		.append(" union all ")
		.append(" SELECT yr,1000,index_item,SUM(gene_capacity),")
		.append(task_id)
		.append( " FROM generator_data JOIN  ")
		.append(getYearDual(year))
		.append("  ON SUBSTR(DATE_FORMAT(start_date,'%Y-%c-%d'),1,4)<t.yr ")
		.append( " AND (SUBSTR(DATE_FORMAT(end_date,'%Y-%c-%d'),1,4)>=t.yr OR end_date IS NULL)")
		.append("  GROUP BY index_item,yr") ;
		return sb.toString();
	}
	/**
	 * 获得退役装机容量sql，有合计
	 * @param year
	 * @return
	 */
	private String getRetireCapacitySQL(String year,String task_id){
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT yr,null,400,SUM(gene_capacity),");
		sb.append(task_id)
		.append	( " FROM generator_data JOIN  ")
		.append(getYearDual(year))
		.append("  ON SUBSTR(DATE_FORMAT(end_date,'%Y-%c-%d'),1,4)=t.yr ")
		.append("  GROUP BY yr") 
		.append(" union all ")
		.append(" SELECT yr,400,index_item,SUM(gene_capacity),")
		.append(task_id)		
		.append( " FROM generator_data JOIN  ")
		.append(getYearDual(year))
		.append("  ON SUBSTR(DATE_FORMAT(end_date,'%Y-%c-%d'),1,4)=t.yr ")
		.append("  GROUP BY index_item,yr") ;
		return sb.toString();
	}
	
	/**
	 * 获得投产装机容量sql，无合计
	 * @param year
	 * @return
	 */
	private String getOperationalCapacitySQL(String year,String task_id){
		StringBuffer sb = new StringBuffer();
		sb.append("  SELECT yr, p,m.index_item,gene_capacity *VALUE/100,")
		.append(task_id)
		.append(" FROM (  ")
		.append(" SELECT yr,300 p,index_item,SUM(gene_capacity) gene_capacity FROM generator_data JOIN  ")
		.append(getYearDual(year))
		.append("  ON SUBSTR(DATE_FORMAT(start_date,'%Y-%c-%d'),1,4)=t.yr ")
		.append("  GROUP BY index_item,yr")
		.append(" ) m JOIN quotient_data n ON m.index_item = n.index_item AND m.yr = n.year ");
		return sb.toString();
	}
	/**
	 * 组装年份表
	 * @param year
	 * @return
	 */
	private String getYearDual(String year){
		String[] aYears = year.split(",");
		StringBuffer strYears = new StringBuffer();
		strYears.append(" ( ") ;
		for(int i=0;i<aYears.length;i++)
		{
			strYears.append("select ").append(aYears[i]).append(" as yr from dual");
			if(i<aYears.length-1)
				strYears.append(" union all ");
		}
		strYears.append(" ) t");
		return strYears.toString();
	}
	/**
	 * 执行投产装机容量合计
	 * @return
	 */
	private int execOperationalCapacitySum(String task_id){
		StringBuffer sb = new StringBuffer();
		/*sb.append(" insert into power_data(year,index_item,value) ")
		.append(" SELECT m.yr ,300 ,SUM(m.subvalue) FROM (")
		.append(" SELECT p.index_item,p.year AS yr,p.value*t.value AS subvalue FROM power_data p  JOIN quotient_data t")
		//暂时删掉AND p.task_id = t.task_id
		.append(" ON p.index_item = t.index_item  AND p.year = t.year AND  p.p_index_item=300")
		.append(" )m GROUP BY m.yr,m.index_item");*/
		sb.append(" insert into power_data(year,index_item,value,task_id) ")
		.append( " select year,300,sum(value),task_id from power_data where p_index_item=300 and task_id=? group by task_id,year");
		int count = this.jdbcTemplate.update(sb.toString(),new Object[]{task_id}) ;
		return count;
	}
	
	/**
	 * 执行年底装机容量
	 * @return
	 */
	private int execEndYearCapacitySum(String task_id){
		StringBuffer sb = new StringBuffer();
		sb.append(" insert into power_data(p_index_item,index_item,year,value,task_id) ")
		.append( " SELECT 500,t.index_item,t.year,SUM(t.value),task_id FROM (")
		.append(" SELECT index_item ,YEAR, VALUE,task_id FROM power_data WHERE  p_index_item IN (1000,300) and task_id=? ")
		.append(" UNION ALL")
		.append(" SELECT index_item ,YEAR,0-VALUE,task_id FROM power_data WHERE  p_index_item IN (400) and task_id=?")
		.append(" ) t GROUP BY t.task_id, t.year,t.index_item")
		.append(" UNION ALL ")
		.append(" SELECT NULL,500,m.year,SUM(m.value),task_id FROM (")
		.append(" SELECT YEAR,VALUe,task_id FROM power_data WHERE  index_item IN (1000,300) and task_id=? ")
		.append(" UNION ALL")
		.append(" SELECT YEAR,0-VALUE,task_id FROM power_data WHERE  index_item IN (400) and task_id=? ")
		.append(" ) m GROUP BY m.task_id,m.year") ;
		int count = this.jdbcTemplate.update(sb.toString(),new Object[]{task_id,task_id,task_id,task_id}) ;
		return count;
	}
	
	/**
	 * 执行受阻容量子项（年底装机容量*比例）
	 * @return
	 */
	private int exeSzSub(String task_id){
		StringBuffer sb = new StringBuffer();
		sb.append(" insert into power_data(p_index_item,index_item,year,value,task_id) ")
		.append( " SELECT 600,p.index_item,p.year,p.value*h.value/100,")
		.append(task_id)
		.append( " FROM power_data p  ")
		.append(" JOIN hinderedidlecapacity_data h ON p.p_index_item=500 AND p.index_item=h.index_item AND p.year=h.yr AND p.task_id=? ") ;
		int count = this.jdbcTemplate.update(sb.toString(),new Object[]{task_id}) ;
		return count;
	}
	/**
	 * 执行受阻容量合计
	 * @return
	 */
	private int exeSzSum(String task_id){
		StringBuffer sb = new StringBuffer();
		sb.append(" insert into power_data(p_index_item,index_item,year,value,task_id) ")
		.append( " SELECT null,600,t.year,SUM(t.value),task_id ")
		.append(" FROM power_data t WHERE  p_index_item =600 and task_id=? group by t.task_id,t.year ");
		int count = this.jdbcTemplate.update(sb.toString(),new Object[]{task_id}) ;
		return count;
	}
	
	/**
	 *当年可用容量
	 * @return
	 */
	private int execCurrentYearCapacitySum(String task_id){
		StringBuffer sb = new StringBuffer();
		sb.append(" insert into power_data(p_index_item,index_item,year,value,task_id) ")
		.append( " SELECT NULL,700,m.year,SUM(m.value),m.task_id FROM (")
		.append(" SELECT YEAR,VALUE,task_id FROM power_data WHERE  index_item =500 and task_id=?")
		.append(" UNION ALL")
		.append(" SELECT YEAR,0-VALUE,task_id FROM power_data WHERE  index_item=600 and task_id=?")
		.append(" UNION ALL")
		.append(" SELECT YEAR,0-VALUE,task_id FROM power_data WHERE  p_index_item=300 AND index_item=3  and task_id=?")
		.append(" ) m GROUP BY m.task_id, m.year");
		int count = this.jdbcTemplate.update(sb.toString(),new Object[]{task_id,task_id,task_id}) ;
		return count;
	}
	
	/**
	 *装机盈余
	 * @return
	 */
	private int execSurplusCapacitySum(String task_id){
		StringBuffer sb = new StringBuffer();
		sb.append(" insert into power_data(p_index_item,index_item,year,value,task_id) ")
		.append( " SELECT NULL,900,m.year,SUM(m.value),m.task_id FROM (")
		.append(" SELECT YEAR,VALUE,task_id FROM power_data WHERE  index_item =700 and task_id=?")
		.append(" UNION ALL")
		.append(" SELECT YEAR,0-VALUE,task_id FROM power_data WHERE  index_item=200  and task_id=?")
		.append(" UNION ALL")
		.append(" SELECT YEAR,VALUE,task_id FROM power_data WHERE   index_item=800 and task_id=?")
		.append(" ) m GROUP BY m.task_id,m.year");
		int count = this.jdbcTemplate.update(sb.toString(),new Object[]{task_id,task_id,task_id}) ;
		return count;
	}
	/**
	 *删除数据
	 * @return
	 */
	private int deleteData(String task_id){
		String sql = " delete from power_data where task_id=?" ;
		int count = this.jdbcTemplate.update(sql , new Object[]{task_id} ) ;
		return count;
	}
	
	@Override
	public String saveData(JSONArray rows,JSONObject obj) {
		String task_id= obj.getString("taskid");
		deleteData(task_id) ;
		List<PowerData> powerData = new ArrayList<PowerData>();
		for (int i = 0; i < rows.size(); i++) {
			JSONObject row = rows.getJSONObject(i);
			Iterator<String> its = row.keys();

			String index_type =  row.getString("code_2");
			
			String parentId =null;
			if(row.get("_parentId")!=null){
				parentId= row.getString("_parentId");
			}
			while (its.hasNext()) {
				String it = its.next();
				if (it.equals("code_2") || it.equals("_parentId")){
					continue;
				}else{		
					powerData.add(setPowerData(row,it,index_type,parentId,task_id));
				}

			}
		}
		int result = executePowerDataSQL(powerData);
		return ""+result;
	}
	
	private int executePowerDataSQL(List<PowerData> powerData) {
		//int deleteCount = deletePowerDataSQL(powerData);
		int insertCount = insertPowerDataSQL(powerData);
		return insertCount;
	}
	
	/**
	 * 删除装机系数的sql语句
	 * @param quotients
	 * @return
	 */
	private int deletePowerDataSQL(final List<PowerData> powerDatas){
		String deletesql = "delete from power_data where p_INDEX_item=? and task_id=? and year=? and index_item=?";
		BatchPreparedStatementSetter setdelete = new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i)
					throws SQLException {
				// TODO Auto-generated method stub
				PowerData pd = powerDatas.get(i);
				ps.setString(1, pd.getParentId());
				ps.setString(2, pd.getTask_id());
				ps.setString(3, pd.getYear());
				ps.setString(4, pd.getCode_2());


			}

			@Override
			public int getBatchSize() {
				// TODO Auto-generated method stub
				return powerDatas.size();
			}
		};
		int[] deletes = jdbcTemplate.batchUpdate(deletesql, setdelete);
		return deletes.length;
	}
	/**
	 * 新增装机系数的sql语句
	 * @param quotients
	 * @return
	 */
	private int insertPowerDataSQL(final List<PowerData> powerDatas){
	
		String insertsql = "insert power_data(p_INDEX_item,INDEX_item,value,task_id,year) VALUES(?,?,?,?,?)";
		
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
