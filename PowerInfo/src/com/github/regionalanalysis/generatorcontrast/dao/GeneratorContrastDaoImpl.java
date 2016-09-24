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
		sb.append("SELECT b.mjz_id jz_id,b.plant_id,b.plant_name,b.code 'unit',b.unit_name ,b.index_y_name,b.code index_y");
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
		.append("  				SELECT jz.index_value plant_id,jz.jz_id mjz_id,dc.plant_name  FROM constant_cost_arg jz  JOIN electricpowerplant_analysis_data dc ")
		.append(" 				ON jz.index_type=200 AND  jz.index_value =dc.id  )X   JOIN  ")
		.append(" 					 (SELECT s1.code,s1.value index_y_name,s2.value unit_name,s1.ord FROM sys_dict_table  s1 " )
		.append( "        			 JOIN sys_dict_table s2 ON s1.domain_id = 36  AND s2.domain_id=37  AND s1.code=s2.code  ORDER BY  s1.ord) Y   " )
		.append("	) b")
		.append("	ON a.jz_id=b.mjz_id AND a.index_y=b.code   AND b.mjz_id=2016091608461600 ")
		.append("	GROUP BY b.code  order by b.mjz_id") ;
		return jdbcTemplate.queryForList(sb.toString());
	}


}
