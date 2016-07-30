package com.github.balance.task.dao;



import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.github.balance.task.entity.BalanceTask;
import com.github.balance.task.entity.BalanceYear;
import com.github.basicData.model.BasicYear;
import com.github.totalquantity.task.entity.TotalTask;


@Repository
public  class BalanceTaskDaoImpl implements BalanceTaskDao{
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public void saveData(BalanceTask task) {
		try{
		String sql ="insert into shiro.balance_task(task_name,year) value(?,?)";
		this.jdbcTemplate.update(sql, new Object[]{task.getTask_name(),task.getYear()});
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
		String sql = "select id,task_name,year from shiro.balance_task order by id desc"
				+ " limit ?,?";
		 List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql,new Object[]{startNum,endNum});
		return list;
	}

	@Override
	public void updateData(BalanceTask task) {
		String sql = "update shiro.balance_task set task_name=?,year=? where  id=?";
		this.jdbcTemplate.update(sql, new Object[]{task.getTask_name(),task.getYear(),task.getId()});
		
	}

	@Override
	public int queryDataCount(JSONObject param) {
		String sql ="select count(1) from shiro.balance_task";
		int count =this.jdbcTemplate.queryForInt(sql);
		return count;
	}

	@Override
	public List<BalanceYear> getYears() {
		String sql = "select year,year_name from shiro.balance_year";
		List<BalanceYear> list = jdbcTemplate.query(sql,
				new BeanPropertyRowMapper(BalanceYear.class));
		return list;
	}

	@Override
	public List<Map<String, Object>> initData(String id) {
		String sql ="select  id,task_name,year from shiro.balance_task where id=?";
		return  this.jdbcTemplate.queryForList(sql,new Object[]{id});
	}
	

}
