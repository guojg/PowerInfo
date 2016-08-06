package com.github.balance.electricitybalance.dao;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import net.sf.json.JSONObject;

@Repository
public class ElectricityBalanceDaoImpl implements ElectricityBalanceDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<Map<String, Object>> queryData(JSONObject param) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT p.pcode _parentId ,p.VALUE pcode_name,p.code_2 id,p.value_2 code_name,d.*  ") ;
		sb.append(" FROM (");
		sb.append(" SELECT pcode,VALUE,code_2,value_2 FROM electricity4  ORDER BY ORD,ord_2 )p");
		sb.append(" LEFT JOIN  (SELECT p_index_item,index_item ");
		//for (String yearStr : param.get("year").toString().split(",")) {
		for (String yearStr : "2016,2017,2018".split(",")) {
			sb.append(",SUM(CASE year WHEN ");
			sb.append(yearStr);
			sb.append(" THEN value END) '");
			sb.append(yearStr);
			sb.append("'");
		}
		sb.append(" FROM electricity_data where task_id= ? GROUP BY p_index_item,index_item) ") ;
		sb.append(" d ON  (p.pcode = d.p_index_item OR (p.pcode IS NULL AND d.p_index_item IS NULL) ) AND p.code_2=d.index_item");

		List<Map<String, Object>>  list = this.jdbcTemplate.queryForList(sb.toString(),new Object[]{11});
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
	
		StringBuffer sb = new StringBuffer();
		sb.append(" insert into power_data(year,p_index_item,index_item,value) ");
		sb.append(" SELECT yr,NULL,100,VALUE FROM loadelectricquantity_data  WHERE index_item=1 ");
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
		sb.append("  SELECT YEAR ,inex_item,300,VALUE FROM t_dldln_dylxrl WHERE sbzt=1  ");
		sb.append("    UNION ALL  ");
		sb.append(" SELECT YEAR ,NULL,400,SUM(VALUE) FROM t_dldln_dylxrl WHERE sbzt=2  GROUP BY YEAR  ");
		sb.append("    UNION ALL  ");
		sb.append(" SELECT YEAR ,inex_item,400,VALUE FROM t_dldln_dylxrl WHERE sbzt=2 ");
		sb.append("    UNION ALL  ");
		sb.append("  SELECT YEAR ,NULL,500,SUM(VALUE) FROM t_dldln_dylxrl WHERE sbzt=3  GROUP BY YEAR ");
		sb.append("    UNION ALL  ");
		sb.append(" SELECT YEAR ,inex_item,500,VALUE FROM t_dldln_dylxrl WHERE sbzt=3  ");
		sb.append("    UNION ALL  ");
		sb.append(" SELECT yr,NULL,600,SUM(VALUE) FROM hinderedidlecapacity_data GROUP BY yr ");
		sb.append("    UNION ALL  ");
		sb.append(" SELECT yr,600,index_item,VALUE FROM hinderedidlecapacity_data ");


		return 0;
	}

}
