package com.github.regionalanalysis.fx.generatorset.dao;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.github.totalquantity.sysdict.dao.SysdictDao;
import com.github.totalquantity.sysdict.entity.Sysdict;



@Repository
public class GeneratorSetFxDaoImpl implements GeneratorSetFxDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private SysdictDao sysdictDao;

	
	
	@Override
	public List<Map<String, Object>> queryData(JSONObject param) {
		String pageSize = param.getString("pageSize");
		String pageNum = param.getString("pageNum");
		String gene_name = param.getString("gene_name");
		String elec_name = param.getString("elec_name");
		String task_id = param.getString("task_id");

			int psize = Integer.parseInt(pageSize);
			int pNum = Integer.parseInt(pageNum);
			int  startNum = psize*(pNum-1);
			int  endNum = psize*pNum;
			StringBuffer sb = new StringBuffer();
			JSONObject obj = new JSONObject();
			obj.put("domain_id", "35") ;
			List<Sysdict>  list =  sysdictDao.queryData(obj);
			sb.append("select jz.*,dc.plant_name,dc.id plant_id from (SELECT c.task_id,c.jz_id") ;
			for (Sysdict s : list) {
				sb.append(",max(CASE c.index_type WHEN ")
				.append(s.getCode())
				.append( " THEN c.index_value ELSE NULL END ) '")
				.append(s.getCode())
				.append("' ");
			}
		sb.append( "  FROM constant_cost_arg_fx  c where c.task_id=? GROUP BY c.jz_id order by c.jz_id desc) jz   JOIN  electricpowerplant_analysis_data_fx dc ON jz.200=dc.id  and dc.task_id=jz.task_id " );
		List<Object> l = new ArrayList<Object>();
		l.add(task_id);
		if(!"".equals(gene_name)){
			sb.append(" AND jz.100 LIKE ?  ");
			l.add("%"+gene_name+"%") ;

		}
		if(!"".equals(elec_name)){
			sb.append( "  AND dc.plant_name LIKE ?   " );
			l.add("%"+elec_name+"%") ;

		}
		l.add(startNum) ;
		l.add(psize) ;
				sb.append(  "  limit ?,?" );
		 List<Map<String, Object>> resultList = this.jdbcTemplate.queryForList(sb.toString(),l.toArray());
		return resultList;
	}

	
	@Override
	public List<Map<String, Object>> queryAllData(JSONObject param) {

		String gene_name = param.getString("gene_name");
		String elec_name = param.getString("elec_name");
		String task_id = param.getString("task_id");
			StringBuffer sb = new StringBuffer();
			JSONObject obj = new JSONObject();
			obj.put("domain_id", "35") ;
			List<Sysdict>  list =  sysdictDao.queryData(obj);
			sb.append("select jz.*,dc.plant_name from (SELECT c.task_id,c.jz_id") ;
			for (Sysdict s : list) {
				sb.append(",max(CASE c.index_type WHEN ")
				.append(s.getCode())
				.append( " THEN c.index_value ELSE NULL END ) '")
				.append(s.getCode())
				.append("' ");
			}
		sb.append( "  FROM constant_cost_arg_fx  c  where c.task_id=? GROUP BY c.task_id,c.jz_id order by c.jz_id desc) jz   JOIN  electricpowerplant_analysis_data_fx dc ON jz.200=dc.id  and dc.task_id =jz.task_id " );
		List<Object> l = new ArrayList<Object>();
		l.add(task_id);
		if(!"".equals(gene_name)){
			sb.append(" AND jz.100 LIKE ?  ");
			l.add("%"+gene_name+"%") ;

		}
		if(!"".equals(elec_name)){
			sb.append( "  AND dc.plant_name LIKE ?   " );
			l.add("%"+elec_name+"%") ;

		}

		 List<Map<String, Object>> resultList = this.jdbcTemplate.queryForList(sb.toString(),l.toArray());
		return resultList;
	}

	@Override
	public int queryDataCount(JSONObject param) {
		String sql ="SELECT COUNT(DISTINCT c.jz_id) FROM constant_cost_arg_fx  c  where task_id=?";
		int count =this.jdbcTemplate.queryForInt(sql,new Object[]{param.getString("task_id")});
		return count;
	}


	@Override
	public Object deleteData(String[] delectArr) {
		StringBuffer  buffer=new StringBuffer("delete from coal_cost_data_fx where fdj_id in(");
		String InSql = "";
		for (int i = 0; i < delectArr.length; i++) {
			InSql = InSql + "?,";
		}
		buffer.append(InSql.substring(0, InSql.length() - 1));
		buffer.append(")");
		jdbcTemplate.update(buffer.toString(),delectArr);
		StringBuffer  buffer1=new StringBuffer("delete from constant_cost_arg where jz_id in(");
		buffer1.append(InSql.substring(0, InSql.length() - 1));
		buffer1.append(")");
		jdbcTemplate.update(buffer1.toString(),delectArr);
		
		StringBuffer  buffer2=new StringBuffer("delete from generator_contrast where jz_id in(");
		buffer2.append(InSql.substring(0, InSql.length() - 1));
		buffer2.append(")");
		jdbcTemplate.update(buffer2.toString(),delectArr);


		return "1";
	}


}
