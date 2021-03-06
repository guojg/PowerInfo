package com.github.regionalanalysis.db.generatorcontrast.dao;



import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class GeneratorContrastDbDaoImpl implements GeneratorContrastDbDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Map<String, Object>> queryData(JSONObject param) {
		String ids[] = param.get("id") == null ? null : param
				.get("id").toString().split(",");
		
		StringBuffer sb = new StringBuffer();
		String index_ys = param.get("index_ys").toString();
		List l = new ArrayList();
		//String area_id = param.getString("area_id") ;
		//String area_id = "1" ;
		String task_id = param.getString("task_id") ;
		
		sb.append("SELECT b.task_id,b.mjz_id jz_id,b.jz_name,concat(b.plant_name,b.jz_name )'dc_jz_name',b.plant_id,b.plant_name,b.code 'unit',b.unit_name ,b.index_y_name,b.code index_y");
		for (String index_x : param.get("index_xs").toString().split(",")) {
			sb.append(",sum(CASE a.index_x WHEN ");
			sb.append(index_x);
			sb.append(" THEN a.value END) '");
			sb.append(index_x);
			sb.append("'");
		}
		sb.append("   FROM  generator_contrast_db a")
		.append("  RIGHT JOIN  (")
		.append("	  SELECT x.*,y.* FROM (")

		.append("  		SELECT dc.id plant_id,jz.jz_id mjz_id,dc.plant_name,jz.jz_name,jz.task_id ")
		.append("		  FROM (SELECT  task_id,jz_id, MAX(CASE j.index_type WHEN 100 THEN j.index_value END) 'jz_name',")
		.append("			MAX(CASE j.index_type WHEN 200 THEN j.index_value END) 'dc_id' FROM constant_cost_arg_db j  WHERE j.index_type IN(100,200) and j.task_id=? GROUP BY task_id,jz_id")
		.append(" 				) jz JOIN electricpowerplant_analysis_data_db dc  	 ON jz.dc_id =dc.id  and jz.task_id=dc.task_id ") ;
		String InSql = "";
		StringBuffer  buffer=new StringBuffer("AND jz.jz_id in (");
		l.add(task_id) ;
		for (int i = 0; i < ids.length; i++) {
			InSql = InSql + "?,";
			l.add(ids[i]);

		}
		 buffer.append(InSql.substring(0, InSql.length() - 1));
		buffer.append(") ");
		sb.append(buffer.toString());
		sb.append(" 					 )X   JOIN (SELECT s1.code,s1.value index_y_name,s2.value unit_name,s1.ord FROM sys_dict_table  s1 " )
		.append( "        			 JOIN sys_dict_table s2 ON s1.domain_id = 36  AND s2.domain_id=37  AND s1.code=s2.code  and instr(?,s1.code)>0 ORDER BY  s1.ord) Y   " )
		.append("	) b")
		.append("	ON a.jz_id=b.mjz_id AND a.index_y=b.code  and a.task_id=b.task_id and a.task_id=? ") ;
		
		sb.append("	GROUP BY  b.task_id,b.mjz_id,b.code  order by b.mjz_id desc ,index_y asc ") ;
		l.add(index_ys);
		l.add(task_id) ;
		return jdbcTemplate.queryForList(sb.toString(),l.toArray());
	}


}
