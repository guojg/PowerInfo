package com.github.balance.powerbalance.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.github.balance.common.util.ConvertTools;

import net.sf.json.JSONObject;

@Repository
public class PowerBalanceDaoImpl implements PowerBalanceDao {

	private static final String LOADSQL=" SELECT yr,NULL,100,VALUE FROM loadelectricquantity_data  WHERE index_item=1" ;//全社会(统调)最大负荷
	private static final String ZJRLSQL=" SELECT yr,200,1,VALUE FROM loadelectricquantity_data  WHERE index_item=2 " ;//有效备用容量
	private static final String BYRLSQL=" SELECT yr,200,2,VALUE FROM loadelectricquantity_data  WHERE index_item=3" ;//有效备用系数
	//需要有效装机容量
	private static final String BYLSQL=" SELECT l1.yr,NULL,200,l1.VALUE*l2.VALUE FROM loadelectricquantity_data l1 JOIN loadelectricquantity_data l2  WHERE l1.yr=l2.yr AND  l1.index_item=2 AND l2.index_item=3" ;//需要有效装机容量
	private static final String SZKXRLSQL=" SELECT yr,NULL,600,SUM(VALUE) FROM hinderedidlecapacity_data GROUP BY yr ";//受阻及空闲容量
	private static final String SZKXRLSUBSQL=" SELECT yr,600,index_item,VALUE FROM hinderedidlecapacity_data ";//受阻及空闲容量子项

	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<Map<String, Object>> queryData(JSONObject param) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT p.pcode _parentId ,p.VALUE pcode_name,p.code_2 id,p.value_2 code_name,d.*  ") ;
		sb.append(" FROM (");
		sb.append(" SELECT pcode,VALUE,code_2,value_2 FROM power4  ORDER BY ORD,ord_2 )p");
		sb.append(" LEFT JOIN  (SELECT p_index_item,index_item ");
		//for (String yearStr : param.get("year").toString().split(",")) {
		for (String yearStr : "2016,2017,2018".split(",")) {
			sb.append(",SUM(CASE year WHEN ");
			sb.append(yearStr);
			sb.append(" THEN value END) '");
			sb.append(yearStr);
			sb.append("'");
		}
		sb.append(" FROM power_data where task_id= ? GROUP BY p_index_item,index_item) ") ;
		sb.append(" d ON  (p.pcode = d.p_index_item OR (p.pcode IS NULL AND d.p_index_item IS NULL) ) AND p.code_2=d.index_item");

		List<Map<String, Object>>  list = this.jdbcTemplate.queryForList(sb.toString(),new Object[]{11});
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
		//String year = obj.getString("year");"2016,2017,2018"
		String year = "2016,2017,2018";
		String yearRateSql=getYearRateSQL(year);
		StringBuffer sb = new StringBuffer();
		sb.append(" insert into power_data(year,p_index_item,index_item,value) ");
		sb.append(  LOADSQL);
		sb.append("    UNION ALL  ");
		sb.append(yearRateSql);
		sb.append("    UNION ALL  ");
		sb.append( ZJRLSQL);
		sb.append("    UNION ALL  ");
		sb.append(  BYRLSQL );
		sb.append("    UNION ALL  ");
		sb.append( BYLSQL);
		sb.append("    UNION ALL  ");
		sb.append( getExistingCapacitySQL(year));
		sb.append("    UNION ALL  ");
		sb.append( getRetireCapacitySQL(year));
		sb.append("    UNION ALL  ");
		sb.append(getOperationalCapacitySQL(year));
		sb.append("    UNION ALL  ");
		sb.append(SZKXRLSQL);
		sb.append("    UNION ALL  ");
		sb.append(SZKXRLSUBSQL);
		//sb.append("    UNION ALL  ");
		//sb.append(" SELECT YEAR ,index_item,800,VALUE FROM t_dldln_dylxrl WHERE sbzt=3  ");
		int count = this.jdbcTemplate.update(sb.toString());
		int operationalCount = this.execOperationalCapacitySum();
		int endYearCount =this.execEndYearCapacitySum();
		int currentYearCount = this.execCurrentYearCapacitySum();
		int surplusCount = this.execSurplusCapacitySum();
		return count+operationalCount+endYearCount+currentYearCount+surplusCount;
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
		sbRate.append("   FROM loadelectricquantity_data t,loadelectricquantity_data t2,(");
		for(int i=0 ; i <sourceArr.length-1 ;++i){
			sbRate.append("select ").append(destinationArr[i])
			.append(" as yr1,").append(sourceArr[i]).append(" as yr2")
			.append(" from dual union all ");
		}
		sbRate.append("select ").append(destinationArr[sourceArr.length-1])
		.append(" as yr1,").append(sourceArr[sourceArr.length-1]).append(" as yr2")
		.append(" from dual ");
		sbRate.append("  )t3  WHERE t.index_item = 1  AND t2.index_item = 1   AND t.yr = t3.yr2 AND t2.yr = t3.yr1");
		return  sbRate.toString();
	}
	/**
	 * 获得现有装机容量sql，有合计
	 * @param year
	 * @return
	 */
	private String getExistingCapacitySQL(String year){
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT yr,null,1000,SUM(plant_capacity) FROM electricpowerplant_data JOIN  ")
		.append(getYearDual(year))
		.append("  ON SUBSTR(DATE_FORMAT(start_date,'%Y-%c-%d'),1,4)<t.yr ")
		.append( " AND (SUBSTR(DATE_FORMAT(end_date,'%Y-%c-%d'),1,4)>t.yr OR end_date IS NULL)")
		.append("  GROUP BY yr") 
		.append(" union all ")
		.append(" SELECT yr,1000,index_item,SUM(plant_capacity) FROM electricpowerplant_data JOIN  ")
		.append(getYearDual(year))
		.append("  ON SUBSTR(DATE_FORMAT(start_date,'%Y-%c-%d'),1,4)<t.yr ")
		.append( " AND (SUBSTR(DATE_FORMAT(end_date,'%Y-%c-%d'),1,4)>t.yr OR end_date IS NULL)")
		.append("  GROUP BY index_item,yr") ;
		return sb.toString();
	}
	/**
	 * 获得退役装机容量sql，有合计
	 * @param year
	 * @return
	 */
	private String getRetireCapacitySQL(String year){
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT yr,null,400,SUM(plant_capacity) FROM electricpowerplant_data JOIN  ")
		.append(getYearDual(year))
		.append("  ON SUBSTR(DATE_FORMAT(end_date,'%Y-%c-%d'),1,4)=t.yr ")
		.append("  GROUP BY yr") 
		.append(" union all ")
		.append(" SELECT yr,400,index_item,SUM(plant_capacity) FROM electricpowerplant_data JOIN  ")
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
	private String getOperationalCapacitySQL(String year){
		StringBuffer sb = new StringBuffer();
		sb.append("  SELECT yr, p,m.index_item,plant_capacity *VALUE/100 FROM (  ")
		.append(" SELECT yr,300 p,index_item,SUM(plant_capacity) plant_capacity FROM electricpowerplant_data JOIN  ")
		.append(getYearDual(year))
		.append("  ON SUBSTR(DATE_FORMAT(start_date,'%Y-%c-%d'),1,4)<t.yr ")
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
	private int execOperationalCapacitySum(){
		StringBuffer sb = new StringBuffer();
		/*sb.append(" insert into power_data(year,index_item,value) ")
		.append(" SELECT m.yr ,300 ,SUM(m.subvalue) FROM (")
		.append(" SELECT p.index_item,p.year AS yr,p.value*t.value AS subvalue FROM power_data p  JOIN quotient_data t")
		//暂时删掉AND p.task_id = t.task_id
		.append(" ON p.index_item = t.index_item  AND p.year = t.year AND  p.p_index_item=300")
		.append(" )m GROUP BY m.yr,m.index_item");*/
		sb.append(" insert into power_data(year,index_item,value) ")
		.append( " select year,300,sum(value) from power_data where p_index_item=300 group by year");
		int count = this.jdbcTemplate.update(sb.toString()) ;
		return count;
	}
	
	/**
	 * 执行年底装机容量
	 * @return
	 */
	private int execEndYearCapacitySum(){
		StringBuffer sb = new StringBuffer();
		sb.append(" insert into power_data(p_index_item,index_item,year,value) ")
		.append( " SELECT 500,t.index_item,t.year,SUM(t.value) FROM (")
		.append(" SELECT index_item ,YEAR, VALUE FROM power_data WHERE  p_index_item IN (1000,300) ")
		.append(" UNION ALL")
		.append(" SELECT index_item ,YEAR,0-VALUE FROM power_data WHERE  p_index_item IN (400)")
		.append(" ) t GROUP BY t.year,t.index_item")
		.append(" UNION ALL ")
		.append(" SELECT NULL,500,m.year,SUM(m.value) FROM (")
		.append(" SELECT YEAR,VALUE FROM power_data WHERE  index_item IN (1000,300) ")
		.append(" UNION ALL")
		.append(" SELECT YEAR,0-VALUE FROM power_data WHERE  index_item IN (400)")
		.append(" ) m GROUP BY m.year") ;
		int count = this.jdbcTemplate.update(sb.toString()) ;
		return count;
	}
	
	/**
	 *当年可用容量
	 * @return
	 */
	private int execCurrentYearCapacitySum(){
		StringBuffer sb = new StringBuffer();
		sb.append(" insert into power_data(p_index_item,index_item,year,value) ")
		.append( " SELECT NULL,700,m.year,SUM(m.value) FROM (")
		.append(" SELECT YEAR,VALUE FROM power_data WHERE  index_item =500 ")
		.append(" UNION ALL")
		.append(" SELECT YEAR,0-VALUE FROM power_data WHERE  index_item=600 ")
		.append(" UNION ALL")
		.append(" SELECT YEAR,0-VALUE FROM power_data WHERE  p_index_item=300 AND index_item=3  ")
		.append(" ) m GROUP BY m.year");
		int count = this.jdbcTemplate.update(sb.toString()) ;
		return count;
	}
	
	/**
	 *装机盈余
	 * @return
	 */
	private int execSurplusCapacitySum(){
		StringBuffer sb = new StringBuffer();
		sb.append(" insert into power_data(p_index_item,index_item,year,value) ")
		.append( " SELECT NULL,900,m.year,SUM(m.value) FROM (")
		.append(" SELECT YEAR,VALUE FROM power_data WHERE  index_item =700 ")
		.append(" UNION ALL")
		.append(" SELECT YEAR,0-VALUE FROM power_data WHERE  index_item=200  ")
		.append(" UNION ALL")
		.append(" SELECT YEAR,0-VALUE FROM power_data WHERE   index_item=800 ")
		.append(" ) m GROUP BY m.year");
		int count = this.jdbcTemplate.update(sb.toString()) ;
		return count;
	}


}
