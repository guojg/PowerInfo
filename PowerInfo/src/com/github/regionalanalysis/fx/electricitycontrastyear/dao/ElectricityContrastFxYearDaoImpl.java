package com.github.regionalanalysis.fx.electricitycontrastyear.dao;



import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ElectricityContrastFxYearDaoImpl implements ElectricityContrastFxYearDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Map<String, Object>> queryData(JSONObject param) {
		String ids[] = param.get("id") == null ? null : param
				.get("id").toString().split(",");
		
		StringBuffer sb = new StringBuffer();
		String index_ys = ","+param.get("index_ys").toString()+",";
		
		List l = new ArrayList();
		//String area_id = param.getString("area_id") ;
		//String area_id = "1" ;
		String task_id = param.getString("task_id") ;
		
		sb.append("SELECT b.id 'plant_id',b.plant_name,b.index_y_name,'元' as 'unit_name',b.code,sum(a.value) value");
		sb.append("   FROM  electricity_contrast_year_fx a")
		.append("  RIGHT JOIN  (")
		.append("	  SELECT x.*,y.* FROM (  	SELECT * FROM electricpowerplant_analysis_data_fx WHERE id IN (");
		StringBuffer  buffer=new StringBuffer("");

		String InSql = "";
	

		for (int i = 0; i < ids.length; i++) {
			InSql = InSql + "?,";
			l.add(ids[i]);

		}
		 buffer.append(InSql.substring(0, InSql.length() - 1));
		buffer.append(") ");
		sb.append(buffer.toString());

		sb.append("  	AND task_id = ? ) X JOIN ( ")
		.append("		    SELECT s1.code,s1.value index_y_name,s1.ord FROM sys_dict_table  s1     ")
		.append("			 where s1.domain_id = 303    AND INSTR(?,CONCAT(',',s1.code,','))>0 ORDER BY  s1.ord)Y");
		sb.append(" 				)b ON a.dc_id=b.id AND a.task_id=b.task_id AND a.index_y=b.code and a.task_id=? ") 
		.append(" 			GROUP BY  b.task_id,b.id,b.code		 ORDER BY plant_id DESC,b.code ASC ") ;
		l.add(task_id);
		l.add(index_ys);
		l.add(task_id) ;
		return jdbcTemplate.queryForList(sb.toString(),l.toArray());
	}


}
