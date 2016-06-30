package com.github.totalquantity.prepareData.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Repository;

import net.sf.json.JSONObject;

import com.github.totalquantity.calculatePlan.entity.CalculatePlan;
import com.github.totalquantity.prepareData.entity.PrepareData;

@Repository
public class PrepareDataDaoImpl implements PrepareDataDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Override
	public List<PrepareData> getPrepareDataByIndexType(JSONObject obj) {
		// TODO Auto-generated method stub
		String planyear = "" ;
		String taskid="";
		String index_type="";
		String sql = " SELECT index_type,YEAR,task_id,VALUE FROM prepare_data  WHERE taskid=? and year=? and instr(index_type,?)>0" ;
		//List<CalculatePlan> list = this.jdbcTemplate.queryForList(sql, CalculatePlan.class, new Object[]{taskid});
		List<PrepareData> list = this.jdbcTemplate.query(sql, new Object[]{taskid,planyear,index_type}, new ParameterizedRowMapper<PrepareData>() {
                    @Override
                    public PrepareData mapRow(ResultSet rs, int index)
                            throws SQLException {
                    	PrepareData pd = new PrepareData();
                    	pd.setIndex_type(rs.getString("index_type"));
                    	pd.setYear(rs.getString("year"));
                    	pd.setTask_id(rs.getString("task_id"));
                    	pd.setValue(rs.getString("value"));
                        return pd;
                    }
                });
		return list;
	}
	@Override
	public List<PrepareData> getAllPrepareData(JSONObject obj) {
		int planyear=obj.getInt("planyear");	//预测年
		String taskid=obj.getString("taskid");	//预测年
		String sql = " SELECT index_type,YEAR,task_id,VALUE FROM prepare_data  WHERE taskid=? and year=?" ;
		//List<CalculatePlan> list = this.jdbcTemplate.queryForList(sql, CalculatePlan.class, new Object[]{taskid});
		List<PrepareData> list = this.jdbcTemplate.query(sql, new Object[]{taskid,planyear}, new ParameterizedRowMapper<PrepareData>() {
                    @Override
                    public PrepareData mapRow(ResultSet rs, int index)
                            throws SQLException {
                    	PrepareData pd = new PrepareData();
                    	pd.setIndex_type(rs.getString("index_type"));
                    	pd.setYear(rs.getString("year"));
                    	pd.setTask_id(rs.getString("task_id"));
                    	pd.setValue(rs.getString("value"));
                        return pd;
                    }
                });
		return list;
	}

}
