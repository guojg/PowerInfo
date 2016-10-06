package com.github.regionalanalysis.db.constantcostarg.dao;

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

import com.github.regionalanalysis.db.constantcostarg.entity.ConstantCostArg;



@Repository
public class ConstantCostDbArgDaoImpl implements ConstantCostDbArgDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	

	@Override
	public String save(final List<ConstantCostArg> list) {
		String deleteSql = "delete from constant_cost_arg where jz_id=? and task_id=?" ;
		this.jdbcTemplate.update(deleteSql, new Object[]{list.get(0).getJz_id(),list.get(0).getTask_id()});
		String sql = "insert into constant_cost_arg(index_type,index_value,jz_id,area_id,task_id) value (?,?,?,?,?)";
		this.jdbcTemplate.batchUpdate(sql,
		      new BatchPreparedStatementSetter() {
		        public void setValues(PreparedStatement ps, int i) throws SQLException {
		            ps.setInt(1, Integer.parseInt(list.get(i).getIndex_type()));//指标
		            ps.setString(2, list.get(i).getIndex_value());//指标值
		            ps.setString(3, list.get(i).getJz_id());
		            ps.setString(4,  list.get(i).getArea_id());
		            ps.setString(5,  list.get(i).getTask_id());
		          }
		          public int getBatchSize() {
		            return list.size();
		          }

		        });
		return list.get(0).getJz_id();
		
	}


	@Override
	public List<ConstantCostArg> getDataById(String id,String task_id) {
		String sql = "SELECT index_type,index_value,area_id FROM constant_cost_arg_db c WHERE jz_id=? and task_id=?" ;
		//List<CalculatePlan> list = this.jdbcTemplate.queryForList(sql, CalculatePlan.class, new Object[]{taskid});
		List<ConstantCostArg> list = this.jdbcTemplate.query(sql, new Object[]{id,task_id}, new ParameterizedRowMapper<ConstantCostArg>() {
                    @Override
                    public ConstantCostArg mapRow(ResultSet rs, int index)
                            throws SQLException {
 
                    	ConstantCostArg cp = new ConstantCostArg();
                    	cp.setIndex_type(rs.getString("index_type"));
                    	cp.setIndex_value(rs.getString("index_value"));
                    	cp.setArea_id(rs.getString("area_id"));

 
                        return cp;
                    }
                });
		return list;
	}


	@Override
	public List<Map<String, Object>> queryPlant(String area_id,String task_id) {
		String sql ="select id code ,plant_name value from electricpowerplant_analysis_data_db where area_id=? and task_id=? order by id desc";
		List<Map<String, Object>> list =  this.jdbcTemplate.queryForList(sql,new Object[]{area_id});
		return list;
	}

	public Integer getPlantByJz(String jz_id, String task_id){
		Integer result=null;
		String sql="SELECT index_value FROM constant_cost_arg WHERE index_type=200 AND jz_id=? and task_id=?";
		List<Map<String, Object>> list=jdbcTemplate.queryForList(sql,new Object[]{jz_id,task_id});
		if(list!=null){
			result=Integer.parseInt(list.get(0).get("index_value").toString());
		}
		return result;
	}

}
