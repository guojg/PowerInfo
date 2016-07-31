package com.github.balance.powerbalance.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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
		sb.append(" d ON p.pcode=d.p_index_item AND p.code_2=d.index_item");

		List<Map<String, Object>>  list = this.jdbcTemplate.queryForList(sb.toString(),new Object[]{11});
		return list;
	}

}
