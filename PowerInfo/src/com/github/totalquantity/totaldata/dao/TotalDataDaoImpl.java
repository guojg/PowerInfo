package com.github.totalquantity.totaldata.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

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
		String sql = "insert into totaldata(taskid,algorithm,year,value) value (?,?,?,?)";
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

}
