package com.github.balance.parparedata.electricpowerplant.dao;


import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;

import com.github.balance.parparedata.electricpowerplant.model.PowerPlant;
import com.github.basicData.model.BasicData;
@Repository
public class ElectricPowerPlantDaoImpl implements ElectricPowerPlantDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Map<String, Object>> queryData(JSONObject param)
			throws Exception {
		int psize = Integer.parseInt(param.getString("pageSize"));
		int pNum = Integer.parseInt(param.getString("pageNum"));
		int  startNum = psize*(pNum-1);
		int  endNum = psize*pNum;
		StringBuffer buffer=new StringBuffer("SELECT id,plant_name,plant_capacity,(select value from sys_dict_table where domain_id=12 and code=index_item) index_itemname,index_item,date_format(start_date,'%Y-%m-%d') start_date,");
		buffer.append(" date_format(end_date,'%Y-%m-%d') end_date from shiro.electricpowerplant_data where 1=1 LIMIT ?, ? ");
		return jdbcTemplate.queryForList(buffer.toString(),new Object[]{startNum,endNum});
	}

	@Override
	public String addRecord(final PowerPlant powerPlant) throws Exception {
		// TODO Auto-generated method stub
		String insertsql = "insert  electricPowerPlant_data" 
				+ "(plant_name,plant_capacity,start_date,end_date,index_item) VALUES(?,?,?,?,?)";
		PreparedStatementSetter setinsert = new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				// TODO Auto-generated method stub
				ps.setString(1, powerPlant.getPlantName());
				ps.setString(2, powerPlant.getPlantCapacity());
				ps.setString(3, powerPlant.getStartDate());
				ps.setString(4, powerPlant.getEndDate());
				ps.setString(5, powerPlant.getIndexItem());
			}

		};
		jdbcTemplate.update(insertsql, setinsert);
		return "1";
	}

	@Override
	public String updateRecord(final PowerPlant powerPlant) throws Exception {
		// TODO Auto-generated method stub
		String insertsql = "update  electricPowerPlant_data" 
				+ " set  plant_name=?,plant_capacity=?,start_date=?,end_date=?,index_item=? where id=?";
		PreparedStatementSetter setupdate = new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				// TODO Auto-generated method stub
				ps.setString(1, powerPlant.getPlantName());
				ps.setString(2, powerPlant.getPlantCapacity());
				ps.setString(3, powerPlant.getStartDate());
				ps.setString(4, powerPlant.getEndDate());
				ps.setString(5, powerPlant.getIndexItem());

				ps.setString(6, powerPlant.getId());
			}

		};
		jdbcTemplate.update(insertsql, setupdate);
		return "1";
	}

	@Override
	public String deleteRecord(String[] delectArr) throws Exception {
		// TODO Auto-generated method stub
		StringBuffer  buffer=new StringBuffer("delete from electricPowerPlant_data where id in(");
		String InSql = "";
		for (int i = 0; i < delectArr.length; i++) {
			InSql = InSql + "?,";
		}
		buffer.append(InSql.substring(0, InSql.length() - 1));
		buffer.append(")");
		jdbcTemplate.update(buffer.toString(),delectArr);
		return "1";
	}

	@Override
	public String importRecord(List<PowerPlant> list) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
