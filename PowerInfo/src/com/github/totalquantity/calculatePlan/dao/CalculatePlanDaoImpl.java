package com.github.totalquantity.calculatePlan.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Repository;

import com.github.totalquantity.calculatePlan.entity.CalculatePlan;
@Repository
public class CalculatePlanDaoImpl implements CalculatePlanDao{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 保存算法参数
	 * @param list
	 */
	@Override
	public void saveData(final List<CalculatePlan> list) {
		String deleteSql ="delete from  calculate_plan where taskid=?";
		this.jdbcTemplate.update(deleteSql, list.get(0).getTaskid());
		String sql = "insert into calculate_plan(taskid,algorithm,index_type,index_value) value (?,?,?,?)";
		this.jdbcTemplate.batchUpdate(sql,
		      new BatchPreparedStatementSetter() {
		        public void setValues(PreparedStatement ps, int i) throws SQLException {
		            ps.setString(1, list.get(i).getTaskid());//任务号
		            ps.setString(2, list.get(i).getAlgorithm());//算法代号
		            ps.setString(3, list.get(i).getIndex_type());//算法参数key
		           // ps.setDouble(4, list.get(i).getIndex_value());//算法参数值
		        	ps.setObject(4, "".equals(list.get(i).getIndex_value())?null:list.get(i).getIndex_value());
		          }
		          public int getBatchSize() {
		            return list.size();
		          }

		        });
		  
		//this.jdbcTemplate.update(sql, new Object[]{112,1,"测试啊",1});
		
	}
	/**
	 * 根据任务号获取输入参数值
	 * @param taskid 任务号
	 * @return 算法参数对象的数据集
	 */
	@Override
	public List<CalculatePlan> getDataBytask(String taskid) {
		String sql = "SELECT c.taskid,c.algorithm,c.index_type,c.index_value FROM calculate_plan c WHERE c.taskid=?" ;
		//List<CalculatePlan> list = this.jdbcTemplate.queryForList(sql, CalculatePlan.class, new Object[]{taskid});
		List<CalculatePlan> list = this.jdbcTemplate.query(sql, new Object[]{taskid}, new ParameterizedRowMapper<CalculatePlan>() {
                    @Override
                    public CalculatePlan mapRow(ResultSet rs, int index)
                            throws SQLException {
 
                    	CalculatePlan cp = new CalculatePlan();
                    	cp.setAlgorithm(rs.getString("Algorithm"));
                    	cp.setIndex_type(rs.getString("index_type"));
                    	//cp.setIndex_value(rs.getDouble("index_value"));
                    	cp.setIndex_value(rs.getString("index_value")==null?null:rs.getDouble("index_value"));
                    	cp.setTaskid(rs.getString("taskid"));
                       
 
                        return cp;
                    }
                });
		return list;
	}


}
