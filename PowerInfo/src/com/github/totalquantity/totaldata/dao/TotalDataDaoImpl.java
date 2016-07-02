package com.github.totalquantity.totaldata.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.github.totalquantity.totaldata.entity.TotalData;
@Repository
public class TotalDataDaoImpl implements TotalDataDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public void saveData(final List<TotalData> list) {
		// TODO Auto-generated method stub
		String sql = "insert into total_data(task_id,algorithm,year,value) value (?,?,?,?)";
		this.jdbcTemplate.batchUpdate(sql,
		      new BatchPreparedStatementSetter() {
		        public void setValues(PreparedStatement ps, int i) throws SQLException {
		            ps.setString(1, list.get(i).getTask_id());//任务号
		            ps.setString(2, list.get(i).getAlgorithm());//算法代号
		            ps.setInt(3, list.get(i).getYear());//年
		            ps.setDouble(4, list.get(i).getValue());//值
		          }
		          public int getBatchSize() {
		            return list.size();
		          }

		        });
	}

	@Override
	public List<Map<String, Object>> queryData(JSONObject param) {
		String algorithm = param.getString("algorithm") ;
		String year = param.getString("year") ;
		String taskid = param.getString("taskid") ;
		StringBuffer sb = new StringBuffer();
		String sql ="select task_id,algorithm ";
		sb.append(sql) ;
		for (String yearStr : param.get("year").toString().split(",")) {
			sb.append(",SUM(CASE year WHEN ");
			sb.append(yearStr);
			sb.append(" THEN value END) '");
			sb.append(yearStr);
			sb.append("'");
		}
		sb.append(" from total_data where task_id=? ") ;
		List<String> params = new ArrayList<String>();
		params.add(taskid) ;
		if(!"".equals(algorithm) && algorithm!=null){
			sb.append(" and instr(?,algorithm)>0") ;
			params.add(algorithm) ;
		}
		sb.append(" group by task_id,algorithm");
	
		/*if(!"".equals(year) && year!=null){
			sb.append(" and instr(?,year)>0") ;
			params.add(year) ;

		}*/
		List<Map<String, Object>>  list = this.jdbcTemplate.queryForList(sb.toString(),params.toArray());
		return list;
	}
	
	

}
