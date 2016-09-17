package com.github.regionalanalysis.generatorset.dao;


import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.github.totalquantity.sysdict.dao.SysdictDao;
import com.github.totalquantity.sysdict.entity.Sysdict;



@Repository
public class GeneratorSetDaoImpl implements GeneratorSetDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private SysdictDao sysdictDao;

	
	
	@Override
	public List<Map<String, Object>> queryData(JSONObject param) {
		String pageSize = param.getString("pageSize");
		String pageNum = param.getString("pageNum");

			int psize = Integer.parseInt(pageSize);
			int pNum = Integer.parseInt(pageNum);
			int  startNum = psize*(pNum-1);
			int  endNum = psize*pNum;
			StringBuffer sb = new StringBuffer();
			JSONObject obj = new JSONObject();
			obj.put("domain_id", "35") ;
			List<Sysdict>  list =  sysdictDao.queryData(obj);
			sb.append("SELECT c.jz_id") ;
			for (Sysdict s : list) {
				sb.append(",max(CASE c.index_type WHEN ")
				.append(s.getCode())
				.append( " THEN c.index_value ELSE NULL END ) '")
				.append(s.getCode())
				.append("' ");
			}
		sb.append( "  FROM constant_cost_arg  c GROUP BY c.jz_id order by c.jz_id desc limit ?,?" );
		 List<Map<String, Object>> resultList = this.jdbcTemplate.queryForList(sb.toString(),new Object[]{startNum,endNum});
		return resultList;
	}



	@Override
	public int queryDataCount(JSONObject param) {
		String sql ="select count(1) FROM constant_cost_arg  c GROUP BY c.jz_id ";
		int count =this.jdbcTemplate.queryForInt(sql);
		return count;
	}


}
