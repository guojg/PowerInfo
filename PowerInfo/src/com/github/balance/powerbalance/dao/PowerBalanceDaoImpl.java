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
	 *--投产机组容量300  t_dldln_dylxrl
	 *--退役关停机组容量400   t_dldln_dylxrl
	 *--年底装机容量500   t_dldln_dylxrl
	 *--受阻及空闲容量600 hinderedidlecapacity_data
	 *--当年可用容量700
	 *--外购（+）外送（-）800
	 *--装机盈余900
	 */
	@Override
	public  int extractData(JSONObject obj) {
		//String year = obj.getString("year");"2016,2017,2018"
		String year = "2016,2017,2018";
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
			.append("as yr1,").append(sourceArr[i]).append("as yr2")
			.append(" from dual union all ");
		}
		sbRate.append("select ").append(destinationArr[sourceArr.length-1])
		.append("as yr1,").append(sourceArr[sourceArr.length-1]).append("as yr2")
		.append(" from dual ");
		sbRate.append("  )t3  WHERE t.index_item = 1  AND t2.index_item = 1   AND t.yr = t3.yr2 AND t2.yr = t3.yr1");

		StringBuffer sb = new StringBuffer();
		sb.append(" insert into power_data(year,p_index_item,index_item,value) ");
		sb.append(" SELECT yr,NULL,100,VALUE FROM loadelectricquantity_data  WHERE index_item=1 ");
		sb.append("    UNION ALL  ");
		sb.append(sbRate.toString());
		sb.append("    UNION ALL  ");
		sb.append(" SELECT yr,200,1,VALUE FROM loadelectricquantity_data  WHERE index_item=2 ");
		sb.append("    UNION ALL  ");
		sb.append(" SELECT yr,200,2,VALUE FROM loadelectricquantity_data  WHERE index_item=3  ");
		sb.append("    UNION ALL  ");
		sb.append(" SELECT l1.yr,NULL,200,l1.VALUE*l2.VALUE FROM loadelectricquantity_data l1 JOIN loadelectricquantity_data l2  ");
		sb.append("  WHERE l1.yr=l2.yr AND  l1.index_item=2 AND l2.index_item=3  ");
		sb.append("    UNION ALL  ");
		sb.append("  SELECT YEAR ,NULL,300,SUM(VALUE) FROM t_dldln_dylxrl WHERE sbzt=1  GROUP BY YEAR ");
		sb.append("    UNION ALL  ");
		sb.append("  SELECT YEAR ,index_item,300,VALUE FROM t_dldln_dylxrl WHERE sbzt=1  ");
		sb.append("    UNION ALL  ");
		sb.append(" SELECT YEAR ,NULL,400,SUM(VALUE) FROM t_dldln_dylxrl WHERE sbzt=2  GROUP BY YEAR  ");
		sb.append("    UNION ALL  ");
		sb.append(" SELECT YEAR ,index_item,400,VALUE FROM t_dldln_dylxrl WHERE sbzt=2 ");
		sb.append("    UNION ALL  ");
		sb.append("  SELECT YEAR ,NULL,500,SUM(VALUE) FROM t_dldln_dylxrl WHERE sbzt=3  GROUP BY YEAR ");
		sb.append("    UNION ALL  ");
		sb.append(" SELECT YEAR ,index_item,500,VALUE FROM t_dldln_dylxrl WHERE sbzt=3  ");
		sb.append("    UNION ALL  ");
		sb.append(" SELECT yr,NULL,600,SUM(VALUE) FROM hinderedidlecapacity_data GROUP BY yr ");
		sb.append("    UNION ALL  ");
		sb.append(" SELECT yr,600,index_item,VALUE FROM hinderedidlecapacity_data ");


		return 0;
	}

}
