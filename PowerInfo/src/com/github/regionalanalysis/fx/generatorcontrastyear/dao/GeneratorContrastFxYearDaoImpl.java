package com.github.regionalanalysis.fx.generatorcontrastyear.dao;



import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class GeneratorContrastFxYearDaoImpl implements GeneratorContrastFxYearDao {
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
		
		sb.append("SELECT b.task_id,b.mjz_id jz_id,b.jz_name,concat(b.plant_name,b.jz_name )'dc_jz_name',b.plant_id,b.plant_name,'元' as 'unit_name' ,b.index_y_name,b.code index_y,sum(a.value) value");
		sb.append("   FROM  generator_contrast_year_fx a")
		.append("  RIGHT JOIN  (")
		.append("	  SELECT x.*,y.* FROM (")

		.append("  		SELECT dc.id plant_id,jz.jz_id mjz_id,dc.plant_name,jz.jz_name,jz.task_id ")
		.append("		  FROM (SELECT  task_id,jz_id, MAX(CASE j.index_type WHEN 100 THEN j.index_value END) 'jz_name',")
		.append("			MAX(CASE j.index_type WHEN 200 THEN j.index_value END) 'dc_id' FROM constant_cost_arg_fx j  WHERE j.index_type IN(100,200) and j.task_id=? GROUP BY task_id,jz_id")
		.append(" 				) jz JOIN electricpowerplant_analysis_data_fx dc  	 ON jz.dc_id =dc.id  and jz.task_id=dc.task_id ") ;
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
		sb.append(" 					 )X   JOIN (SELECT s1.code,s1.value index_y_name,s1.ord FROM sys_dict_table  s1 " )
		.append( "        			where s1.domain_id = 303   and instr(?,s1.code)>0 ORDER BY  s1.ord) Y   " )
		.append("	) b")
		.append("	ON a.jz_id=b.mjz_id AND a.index_y=b.code  and a.task_id=b.task_id and a.task_id=? ") ;
		
		sb.append("	GROUP BY  b.task_id,b.mjz_id,b.code  order by b.mjz_id desc ,index_y asc ") ;
		l.add(index_ys);
		l.add(task_id) ;
		return jdbcTemplate.queryForList(sb.toString(),l.toArray());
	}

	@Override
	public List<Map<String, Object>> queryDataPie() {
		String sql ="SELECT index_item,VALUE FROM  quotient_data " ;
		return this.jdbcTemplate.queryForList(sql);
	}


}
