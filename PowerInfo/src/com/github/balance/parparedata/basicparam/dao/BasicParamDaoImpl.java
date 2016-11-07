package com.github.balance.parparedata.basicparam.dao;



import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.github.balance.parparedata.powerquotient.entity.PowerHour;
import com.github.balance.parparedata.powerquotient.entity.QuotientData;
import com.github.totalquantity.prepareData.entity.PrepareData;


@Repository
public class BasicParamDaoImpl implements BasicParamDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	
	@Override
	public List<Map<String, Object>> queryData() {
		

		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT s.code,s.value displayvalue,s.ord,q.value quotient,h.hour_num FROM ( ")
		.append(" SELECT CODE ,VALUE,ORD FROM sys_dict_table WHERE  domain_id=12 ) s LEFT JOIN  quotient_data q" )
		.append(" ON s.code=q.index_item LEFT JOIN power_hour h ON s.code=h.index_item AND h.task_id IS NULL ORDER BY s.ord") ;

		List<Map<String, Object>>  list = this.jdbcTemplate.queryForList(sb.toString());
		return list;
	}


	@Override
	public void save(JSONArray rows) {
		// TODO Auto-generated method stub
		List<PowerHour> powers = new ArrayList<PowerHour>();
		String task_id=null;
		List<QuotientData> quotients = new ArrayList<QuotientData>();
		for (int i = 0; i < rows.size(); i++) {
			JSONObject row = rows.getJSONObject(i);
			Iterator<String> its = row.keys();

			String index_type =  row.getString("code");
			while (its.hasNext()) {
				String it = its.next();
				if (it.equals("code") || it.equals("displayvalue") || it.equals("ord")){
					continue;
				}else if(it.equals("hour_num")){
					powers.add(setPowerHour(row,it,index_type));
				}else{		
					quotients.add(setQuotientData(row,it,index_type));
				}

			}
		}
		int powerCount = executePowerHourSQL(powers);
		int quotientcount = executeQuotientDataSQL(quotients);
		int result =powerCount +quotientcount;
	}


	@Override
	public void saveYr(String startYear, String stopYear) {
		String deleteSql="delete from balance_param_year ";
		String insertSql="insert into balance_param_year(start_year,stop_year) value(?,?)";
		this.jdbcTemplate.update(deleteSql) ;
		this.jdbcTemplate.update(insertSql,new Object[]{startYear,stopYear}) ;

		
	}


	@Override
	public void saveByl(String byl) {
		String deleteSql="delete from balance_param_byl ";
		String insertSql="insert into balance_param_byl(byl) value(?)";
		this.jdbcTemplate.update(deleteSql) ;
		this.jdbcTemplate.update(insertSql,new Object[]{Double.parseDouble(byl)}) ;
		
	}
	
	/**
	 * 组装利用小时数对象
	 * @param row
	 * @param it
	 * @param index_type
	 * @param task_id
	 * @return
	 */
	private  PowerHour setPowerHour(JSONObject row,String it,String index_type){
		PowerHour hour = new PowerHour();
		hour.setIndex_item(index_type);
		if(!"".equals(row.getString(it))){
			hour.setHour_num(row.getDouble(it));
		}else{
			hour.setHour_num(null);
		}
		return hour;
	}
	/**
	 * 组装装机系数
	 * @param row
	 * @param it
	 * @param index_type
	 * @param task_id
	 * @return
	 */
	private  QuotientData setQuotientData(JSONObject row,String it,String index_type){
		QuotientData qd = new QuotientData();
		qd.setIndex_item(index_type);
		if(!"".equals(row.getString(it))){
			qd.setValue(row.getDouble(it));
		}else{
			qd.setValue(null);
		}
		qd.setYear(it);
		return qd;
	}
	
	/**
	 * 删除装机系数的sql语句
	 * @param quotients
	 * @return
	 */
	private int deleteQuotientDataSQL(final List<QuotientData> quotients){
		String deletesql = "delete from quotient_data where INDEX_item=? ";
		BatchPreparedStatementSetter setdelete = new BatchPreparedStatementSetter() {
	
			@Override
			public void setValues(PreparedStatement ps, int i)
					throws SQLException {
				// TODO Auto-generated method stub
				QuotientData pd = quotients.get(i);
				ps.setString(1, pd.getIndex_item());
	
	
			}
	
			@Override
			public int getBatchSize() {
				// TODO Auto-generated method stub
				return quotients.size();
			}
		};
		int[] deletes = jdbcTemplate.batchUpdate(deletesql, setdelete);
		return deletes.length;
	}
	/**
	 * 新增装机系数的sql语句
	 * @param quotients
	 * @return
	 */
	private int insertQuotientDataSQL(final List<QuotientData> quotients){
	
		String insertsql = "insert quotient_data(INDEX_item,value) VALUES(?,?)";
		
		BatchPreparedStatementSetter setinsert = new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i)
					throws SQLException {
				// TODO Auto-generated method stub
				QuotientData qd =quotients.get(i);
	
					ps.setString(1, qd.getIndex_item());
					ps.setObject(2, "".equals(qd.getValue())?null:qd.getValue());
	
			}
	
			@Override
			public int getBatchSize() {
				// TODO Auto-generated method stub
				return quotients.size();
			}
			
		};
		int[] inserts=jdbcTemplate.batchUpdate(insertsql, setinsert);
		return inserts.length;
	}
	/**
	 * 获得执行装机系数的数量
	 * @param quotients
	 * @return
	 */
	private int executeQuotientDataSQL(final List<QuotientData> quotients) {
		int deleteCount = deleteQuotientDataSQL(quotients);
		int insertCount = insertQuotientDataSQL(quotients);
		return insertCount- deleteCount;
	}
	/**
	 * 获得执行的数量
	 * @param powers
	 * @return
	 */
	private int executePowerHourSQL(final List<PowerHour> powers) {
		int deleteCount = deletePowerHourSQL( powers);
		int insertCount = insertPowerHourSQL(powers);
		return insertCount- deleteCount;

	}

	/**
	 * 删除利用小时数的sql语句
	 * @param powers
	 * @return
	 */
	private int deletePowerHourSQL(final List<PowerHour> powers) {
		String deletesql = "delete from power_hour where INDEX_item=? and task_id is null ";
		BatchPreparedStatementSetter setdelete = new BatchPreparedStatementSetter() {
	
			@Override
			public void setValues(PreparedStatement ps, int i)
					throws SQLException {
				// TODO Auto-generated method stub
				PowerHour pd = powers.get(i);
				ps.setString(1, pd.getIndex_item());
	
			}
	
			@Override
			public int getBatchSize() {
				// TODO Auto-generated method stub
				return powers.size();
			}
		};
		int[] deletes = jdbcTemplate.batchUpdate(deletesql, setdelete);
		return deletes.length;
	}
	/**
	 * 新增利用小时数的sql语句
	 * @param powers
	 * @return
	 */
	private int insertPowerHourSQL(final List<PowerHour> powers) {
		String insertsql = "insert power_hour(INDEX_item,hour_num) VALUES(?,?)";
		
		BatchPreparedStatementSetter setinsert = new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i)
					throws SQLException {
				// TODO Auto-generated method stub
				PowerHour hour = powers.get(i);
	
					ps.setString(1, hour.getIndex_item());
					ps.setObject(2, "".equals(hour.getHour_num())?null:hour.getHour_num());
	
			}
	
			@Override
			public int getBatchSize() {
				// TODO Auto-generated method stub
				return powers.size();
			}
			
		};
		int[] inserts=jdbcTemplate.batchUpdate(insertsql, setinsert);
		return inserts.length;
	}


	@Override
	public List<Map<String, Object>> initData() {
		String sql ="SELECT y.start_year,y.stop_year,b.byl FROM balance_param_year Y JOIN balance_param_byl b";
		return  this.jdbcTemplate.queryForList(sql);
	}
	
	@Override
	public int countData() {
		String sql ="SELECT count(1) FROM balance_param_year ";
		return  this.jdbcTemplate.queryForInt(sql);
	
	}
}
