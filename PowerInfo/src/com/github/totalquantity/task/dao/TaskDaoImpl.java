package com.github.totalquantity.task.dao;


import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.github.totalquantity.task.entity.TotalTask;


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
	

}
