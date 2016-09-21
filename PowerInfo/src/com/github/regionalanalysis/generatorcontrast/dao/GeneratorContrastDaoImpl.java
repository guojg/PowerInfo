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

		sb.append("SELECT a.jz_id,b.code 'unit',b.unit_name ,");
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
		sb.append("	ON a.`index_y`=b.code and a.jz_id=1 GROUP BY b.code ORDER BY  b.ord");


		return jdbcTemplate.queryForList(sb.toString());
	}


}
