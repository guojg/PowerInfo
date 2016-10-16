package com.github.totalquantity.task.dao;


import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.github.basicData.model.BasicYear;
import com.github.totalquantity.task.entity.TotalTask;
import com.github.totalquantity.task.entity.TotalYear;


@Repository
public class TaskDaoImpl implements TaskDao{
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public void saveData(TotalTask task) {
		try{
		String sql ="insert into total_task(task_name,baseyear,planyear,algorithm) value(?,?,?,?)";
		this.jdbcTemplate.update(sql, new Object[]{task.getTask_name(),task.getBaseyear(),task.getPlanyear(),task.getAlgorithm()});
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public List<Map<String, Object>> queryData(JSONObject param) {
		String pageSize = param.getString("pageSize");
		String pageNum = param.getString("pageNum");

			int psize = Integer.parseInt(pageSize);
			int pNum = Integer.parseInt(pageNum);
			int  startNum = psize*(pNum-1);
			int  endNum = psize*pNum;
		String sql = "select id,task_name,baseyear,planyear,algorithm,algorithmradio from total_task order by id desc"
				+ " limit ?,?";
		 List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql,new Object[]{startNum,endNum});
		return list;
	}

	@Override
	public void updateData(TotalTask task) {
		String sql = "update total_task set task_name=?,baseyear=?,planyear=?,algorithm=?,algorithmradio=? where  id=?";
		this.jdbcTemplate.update(sql, new Object[]{task.getTask_name(),task.getBaseyear(),task.getPlanyear(),task.getAlgorithm(),task.getAlgorithmRadio(),task.getId()});
		
	}

	@Override
	public int queryDataCount(JSONObject param) {
		String sql ="select count(1) from total_task";
		int count =this.jdbcTemplate.queryForInt(sql);
		return count;
	}

	@Override
	public List<TotalYear> getBaseYears() throws Exception {
		String sql = "select year,year_name from base_year ";
		List<TotalYear> list = jdbcTemplate.query(sql,
				new BeanPropertyRowMapper(TotalYear.class));
		return list;
	}

	@Override
	public List<TotalYear> getPlanYears() throws Exception {
		String sql = "select year,year_name from total_year where flag=1";
		List<TotalYear> list = jdbcTemplate.query(sql,
				new BeanPropertyRowMapper(TotalYear.class));
		return list;
	}

	@Override
	public List<Map<String, Object>> initData(String id) {
		String sql ="select id,task_name,baseyear,planyear,algorithm,algorithmradio from total_task where id=?";
		return  this.jdbcTemplate.queryForList(sql,new Object[]{id});
	}
	
	@Override
	public String deleteRecord(String[] delectArr) throws Exception {
		// TODO Auto-generated method stub
		StringBuffer  buffer=new StringBuffer("delete from total_task where id in(");
		String InSql = "";
		for (int i = 0; i < delectArr.length; i++) {
			InSql = InSql + "?,";
		}
		buffer.append(InSql.substring(0, InSql.length() - 1));
		buffer.append(")");
		jdbcTemplate.update(buffer.toString(),delectArr);
		return "1";
	}
}
