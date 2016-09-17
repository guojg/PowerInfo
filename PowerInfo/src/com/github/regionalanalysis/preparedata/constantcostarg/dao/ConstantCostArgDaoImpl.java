package com.github.regionalanalysis.preparedata.constantcostarg.dao;

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

import com.github.regionalanalysis.preparedata.constantcostarg.entity.ConstantCostArg;
import com.github.totalquantity.calculatePlan.entity.CalculatePlan;



@Repository
public class ConstantCostArgDaoImpl implements ConstantCostArgDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	

	@Override
	public void save(final List<ConstantCostArg> list) {
		String deleteSql = "delete from constant_cost_arg where jz_id=?" ;
		this.jdbcTemplate.update(deleteSql, new Object[]{list.get(0).getJz_id()});
		String sql = "insert into constant_cost_arg(index_type,index_value,jz_id) value (?,?,?)";
		this.jdbcTemplate.batchUpdate(sql,
		      new BatchPreparedStatementSetter() {
		        public void setValues(PreparedStatement ps, int i) throws SQLException {
		            ps.setInt(1, Integer.parseInt(list.get(i).getIndex_type()));//指标
		            ps.setString(2, list.get(i).getIndex_value());//指标值
		            ps.setString(3, list.get(i).getJz_id());
		          }
		          public int getBatchSize() {
		            return list.size();
		          }

		        });
		
	}


	@Override
	public List<ConstantCostArg> getDataById(String id) {
		String sql = "SELECT index_type,index_value FROM constant_cost_arg c WHERE jz_id=?" ;
		//List<CalculatePlan> list = this.jdbcTemplate.queryForList(sql, CalculatePlan.class, new Object[]{taskid});
		List<ConstantCostArg> list = this.jdbcTemplate.query(sql, new Object[]{id}, new ParameterizedRowMapper<ConstantCostArg>() {
                    @Override
                    public ConstantCostArg mapRow(ResultSet rs, int index)
                            throws SQLException {
 
                    	ConstantCostArg cp = new ConstantCostArg();
                    	cp.setIndex_type(rs.getString("index_type"));
                    	cp.setIndex_value(rs.getString("index_value"));
                       
 
                        return cp;
                    }
                });
		return list;
	}


	@Override
	public List<Map<String, Object>> queryPlant() {
		String sql ="select id code ,plant_name value from electricpowerplant_analysis_data order by id desc";
		List<Map<String, Object>> list =  this.jdbcTemplate.queryForList(sql);
		return list;
	}


}
