package com.github.totalquantity.prepareData.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Repository;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.github.basicData.model.BasicData;
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
		String sql = " SELECT index_type,YEAR,task_id,VALUE FROM prepare_data  WHERE taskid=? and year=? and instr(?，index_type)>0" ;
		//List<CalculatePlan> list = this.jdbcTemplate.queryForList(sql, CalculatePlan.class, new Object[]{taskid});
		List<PrepareData> list = this.jdbcTemplate.query(sql, new Object[]{taskid,planyear,index_type}, new ParameterizedRowMapper<PrepareData>() {
                    @Override
                    public PrepareData mapRow(ResultSet rs, int index)
                            throws SQLException {
                    	PrepareData pd = new PrepareData();
                    	pd.setIndex_type(rs.getString("index_type"));
                    	pd.setYear(rs.getString("year"));
                    	pd.setTask_id(rs.getString("task_id"));
                    	pd.setValue(rs.getDouble("value"));
                        return pd;
                    }
                });
		return list;
	}
	@Override
	public List<PrepareData> getAllPrepareData(JSONObject obj) {
		int planyear=obj.getInt("planyear");	//预测年
		String taskid=obj.getString("taskid");	//预测年
		String sql = " SELECT index_type,YEAR,task_id,VALUE FROM prepare_data  WHERE task_id=? and year=?" ;
		List<PrepareData> list = this.jdbcTemplate.query(sql, new Object[]{taskid,planyear}, new ParameterizedRowMapper<PrepareData>() {
                    @Override
                    public PrepareData mapRow(ResultSet rs, int index)
                            throws SQLException {
                    	PrepareData pd = new PrepareData();
                    	pd.setIndex_type(rs.getString("index_type"));
                    	pd.setYear(rs.getString("year"));
                    	pd.setTask_id(rs.getString("task_id"));
                    	pd.setValue(rs.getDouble("value"));
                        return pd;
                    }
                });
		return list;
	}
	
	@Override
	public List<Map<String, Object>> queryData(JSONObject param) {
		String index_type = param.getString("index_type") ;
		String year = param.getString("year") ;
		String taskid = param.getString("taskid") ;
		StringBuffer sb = new StringBuffer();
		String sql ="SELECT t.value index_name, t.code index_type  ";
		sb.append(sql) ;
		for (String yearStr : param.get("year").toString().split(",")) {
			sb.append(",SUM(CASE year WHEN ");
			sb.append(yearStr);
			sb.append(" THEN d.value END) '");
			sb.append(yearStr);
			sb.append("'");
		}
		sb.append(" FROM prepare_data d right join sys_dict_table t on d.index_type=t.code  and task_id=? ") ;
		List<String> params = new ArrayList<String>();
		params.add(taskid) ;
		String indexsql = "";
		if(!"".equals(index_type) && index_type!=null){
			sb.append(" and instr(?,index_type)>0") ;
			indexsql=" and instr(?,t.code)>0";
			params.add(index_type) ;
			params.add(index_type) ;
		}

		sb.append(" where t.domain_id=11 ");
		sb.append(indexsql);
		sb.append(" group by task_id,index_type,t.value order by t.ord");
	
	
		List<Map<String, Object>>  list = this.jdbcTemplate.queryForList(sb.toString(),params.toArray());
		return list;
	}
	@Override
	public void saveData(JSONArray rows,String taskid) {
		List<PrepareData> list = new ArrayList<PrepareData>();
		for (int i = 0; i < rows.size(); i++) {
			JSONObject row = rows.getJSONObject(i);
			Iterator<String> its = row.keys();

			String index_type = "";
			while (its.hasNext()) {
				String it = its.next();
				index_type = row.getString("index_type");

				if (it.equals("index_type") || it.equals("index_name"))
					continue;
				PrepareData pd = new PrepareData();
				pd.setIndex_type(index_type);
				pd.setTask_id(taskid);
				pd.setYear(it);
				if(!"".equals(row.getString(it))){
					pd.setValue(row.getDouble(it));
				}else{
					pd.setValue(null);
				}
				
				list.add(pd);
			}
		}
		try {
			executeSQLS(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void executeSQLS(final List<PrepareData> list) throws Exception {
		
		String deletesql = "delete from shiro.prepare_data where INDEX_type=? and year=? and task_id=?";
		BatchPreparedStatementSetter setdelete = new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i)
					throws SQLException {
				// TODO Auto-generated method stub
				PrepareData pd = list.get(i);
				ps.setString(1, pd.getIndex_type());
				ps.setString(2, pd.getYear());
				ps.setString(3, pd.getTask_id());

			}

			@Override
			public int getBatchSize() {
				// TODO Auto-generated method stub
				return list.size();
			}
		};
		jdbcTemplate.batchUpdate(deletesql, setdelete);

		String insertsql = "insert shiro.prepare_data(INDEX_type,Year,VALUE,task_id) VALUES(?,?,?,?)";
	
		BatchPreparedStatementSetter setinsert = new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i)
					throws SQLException {
				// TODO Auto-generated method stub
				PrepareData pd = list.get(i);

					ps.setString(1, pd.getIndex_type());
					ps.setString(2, pd.getYear());
					ps.setObject(3, "".equals(pd.getValue())?null:pd.getValue());
					ps.setString(4, pd.getTask_id());

			}

			@Override
			public int getBatchSize() {
				// TODO Auto-generated method stub
				return list.size();
			}
			
		};
		jdbcTemplate.batchUpdate(insertsql, setinsert);
	}
}
