package com.github.balance.parparedata.powerquotient.dao;


import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.github.basicData.model.BasicData;
import com.github.totalquantity.totaldata.entity.TotalData;
@Repository
public class PowerQuotientDaoImpl implements PowerQuotientDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public String saveData(JSONArray rows) {
	
		return "";
	}
	
	@Override
	public List<Map<String, Object>> queryData(JSONObject param) {
		//String year = param.getString("year") ;
		String year ="2014,2015,2016";
		//String taskid = param.getString("taskid") ;
		String taskid ="1";
		String type = param.getString("type") ;
		List<String> params = new ArrayList<String>();
		params.add(taskid) ;

		StringBuffer sb = new StringBuffer();
		sb.append("SELECT m.*,n.hour_num FROM (") ;
		sb.append("     SELECT task_id,s.code,s.ord ,s.value displayvalue");
		for (String yearStr : year.split(",")) {
			sb.append(",SUM(CASE year WHEN ");
			sb.append(yearStr);
			sb.append(" THEN q.value END) '");
			sb.append(yearStr);
			sb.append("'");
		}
		sb.append(" FROM quotient_data q RIGHT JOIN ") ;
		sb.append(" (SELECT CODE ,VALUE,ORD FROM sys_dict_table WHERE  domain_id=12 ");
		if(!"".equals(type) && type!=null){
			sb.append(" and instr(?,code)>0") ;
			params.add(type) ;

		}
		sb.append( ") s ON q.index_item = s.code  AND q.task_id=?");
		sb.append(" GROUP BY task_id,index_item,displayvalue");
		sb.append("   ) m LEFT JOIN  power_hour   n");
		sb.append("  ON m.code=n.index_item AND n.task_id=?  ORDER BY m.ord");
		params.add(taskid) ;
		
		List<Map<String, Object>>  list = this.jdbcTemplate.queryForList(sb.toString(),params.toArray());
		return list;
	}

	
	
	

}
