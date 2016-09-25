package com.github.regionalanalysis.generatorcontrast.dao;



import java.util.List;
import java.util.Map;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class GeneratorContrastDaoImpl implements GeneratorContrastDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Map<String, Object>> queryData(JSONObject param) {
		StringBuffer sb = new StringBuffer();
		String index_ys = param.get("index_ys").toString();
		/*sb.append("SELECT a.jz_id,(SELECT index_value FROM constant_cost_arg WHERE index_type=100 AND jz_id = a.jz_id) 'jz_name',b.code 'unit',b.unit_name ,");
		sb.append("b.index_y_name,b.code index_y");
		for (String index_x : param.get("index_xs").toString().split(",")) {
			sb.append(",sum(CASE a.index_x WHEN ");
			sb.append(index_x);
			sb.append(" THEN a.value END) '");
			sb.append(index_x);
			sb.append("'");
		}
		sb.append("  FROM  generator_contrast a");
		sb.append(" RIGHT JOIN  (SELECT s1.code,s1.value index_y_name,s2.value unit_name,s1.ord FROM sys_dict_table  s1 JOIN sys_dict_table s2 ON s1.domain_id = 36  AND s2.domain_id=37  AND s1.code=s2.code ) b");
		sb.append("	ON a.`index_y`=b.code and a.jz_id=2016091608461600 GROUP BY b.code ORDER BY  b.ord");
*/
		sb.append("SELECT b.mjz_id jz_id,b.jz_name,concat(b.plant_name,b.jz_name )'dc_jz_name',b.plant_id,b.plant_name,b.code 'unit',b.unit_name ,b.index_y_name,b.code index_y");
		for (String index_x : param.get("index_xs").toString().split(",")) {
			sb.append(",sum(CASE a.index_x WHEN ");
			sb.append(index_x);
			sb.append(" THEN a.value END) '");
			sb.append(index_x);
			sb.append("'");
		}
		sb.append("   FROM  generator_contrast a")
		.append("  RIGHT JOIN  (")
		.append("	  SELECT x.*,y.* FROM (")

		.append("  		SELECT dc.id plant_id,jz.jz_id mjz_id,dc.plant_name,jz.jz_name ")
		.append("		  FROM (SELECT  jz_id, MAX(CASE j.index_type WHEN 100 THEN j.index_value END) 'jz_name',")
		.append("			MAX(CASE j.index_type WHEN 200 THEN j.index_value END) 'dc_id' FROM constant_cost_arg j  WHERE j.index_type IN(100,200) GROUP BY jz_id")
		.append(" 				) jz JOIN electricpowerplant_analysis_data dc  	 ON jz.dc_id =dc.id )X   JOIN  ")
		.append(" 					 (SELECT s1.code,s1.value index_y_name,s2.value unit_name,s1.ord FROM sys_dict_table  s1 " )
		.append( "        			 JOIN sys_dict_table s2 ON s1.domain_id = 36  AND s2.domain_id=37  AND s1.code=s2.code  and instr(?,s1.code)>0 ORDER BY  s1.ord) Y   " )
		.append("	) b")
		.append("	ON a.jz_id=b.mjz_id AND a.index_y=b.code   AND b.mjz_id=2016091608461600 ")
		.append("	GROUP BY  b.mjz_id,b.code  order by b.mjz_id,index_y") ;
		System.out.println(sb.toString());
		return jdbcTemplate.queryForList(sb.toString(),new Object[]{index_ys});
	}


}
