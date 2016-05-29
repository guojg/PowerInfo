package com.github.basicData.nationalEconomy.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


@Repository
public class NationalEconomyDaoImpl implements NationalEconomyDao{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Override
	public List<Map<String, Object>> queryData(List<String> years) {
		StringBuffer sb = new StringBuffer();
		sb.append( "SELECT tb.id index_item,tb.index_name,tb.ORD,tb.area_id") ;
		for(String year : years){
			sb.append( ",SUM(CASE tb.yr WHEN ") ;
			sb.append( year);
			sb.append( " THEN tb.value END) '");
			sb.append(year) ;
			sb.append("'") ;
		}
          sb.append("  FROM (SELECT t2.id,t2.index_name,t2.ORD,t1.area_id,t1.value,t1.yr ");
          sb.append("        FROM  b_r_national_economy t1 RIGHT JOIN diagnosis_report_type t2 ") ;
          sb.append("        ON t1.INDEX_ITEM= t2.id) tb"); 
          sb.append(" GROUP BY tb.id,tb.index_name,tb.ORD,tb.area_id"); 

	        return jdbcTemplate.queryForList(sb.toString());
	}

}
