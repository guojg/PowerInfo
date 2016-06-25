package com.github.totalquantity.task.dao;


import java.util.List;
import java.util.Map;

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
	

}
