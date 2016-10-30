package com.github.balance.parparedata.powerquotient.dao;



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
public class PowerQuotientDaoImpl implements PowerQuotientDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public String saveData(JSONArray rows,JSONObject obj) {
		List<PowerHour> powers = new ArrayList<PowerHour>();
		String task_id=null;
		List<QuotientData> quotients = new ArrayList<QuotientData>();
		for (int i = 0; i < rows.size(); i++) {
			JSONObject row = rows.getJSONObject(i);
			Iterator<String> its = row.keys();

			String index_type =  row.getString("code");
			while (its.hasNext()) {
				String it = its.next();
				if (it.equals("code") || it.equals("displayvalue") || it.equals("ord")|| it.equals("task_id")){
					continue;
				}else if(it.equals("hour_num")){
					powers.add(setPowerHour(row,it,index_type,task_id));
				}else{		
					quotients.add(setQuotientData(row,it,index_type,task_id));
				}

			}
		}
		int powerCount = executePowerHourSQL(powers);
		int quotientcount = executeQuotientDataSQL(quotients);
		int result =powerCount +quotientcount;
		return ""+result;
	}
	/**
	 * 删除装机系数的sql语句
	 * @param quotients
	 * @return
	 */
	private int deleteQuotientDataSQL(final List<QuotientData> quotients){
		String deletesql = "delete from quotient_data where INDEX_item=? and year=?";
		BatchPreparedStatementSetter setdelete = new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i)
					throws SQLException {
				// TODO Auto-generated method stub
				QuotientData pd = quotients.get(i);
				ps.setString(1, pd.getIndex_item());
				ps.setString(2, pd.getYear());


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
	
		String insertsql = "insert quotient_data(INDEX_item,value,year) VALUES(?,?,?)";
		
		BatchPreparedStatementSetter setinsert = new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i)
					throws SQLException {
				// TODO Auto-generated method stub
				QuotientData qd =quotients.get(i);

					ps.setString(1, qd.getIndex_item());
					ps.setObject(2, "".equals(qd.getValue())?null:qd.getValue());
					ps.setString(3, qd.getYear());

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

	@Override
	public List<Map<String, Object>> queryData(JSONObject param) {
		String year = param.getString("year") ;
		//String year ="2014,2015,2016";
		//String taskid ="1";
		String type = param.getString("type") ;
		List<String> params = new ArrayList<String>();
		

		StringBuffer sb = new StringBuffer();
		sb.append("SELECT m.*, n.hour_num   FROM (") ;
		sb.append("     SELECT s.code,s.ord ,s.value displayvalue");
		for (String yearStr : year.split(",")) {
			sb.append(",SUM(CASE year WHEN ");
			sb.append(yearStr);
			sb.append(" THEN q.value END) '");
			sb.append(yearStr);
			sb.append("'");
		}
		sb.append(" FROM quotient_data q RIGHT JOIN ") ;
		sb.append(" (SELECT CODE ,VALUE,ORD FROM sys_dict_table WHERE  domain_id=12 ");
		if(!"".equals(type) && type!=null){
			sb.append(" and instr(?,code)>0") ;
			params.add(type) ;

		}
		sb.append( ") s ON q.index_item = s.code ");
		sb.append(" GROUP BY index_item,displayvalue");
		sb.append("   ) m LEFT JOIN  power_hour   n");
		sb.append("  ON m.code=n.index_item AND n.task_id is null  ORDER BY m.ord");
		List<Map<String, Object>>  list = this.jdbcTemplate.queryForList(sb.toString(),params.toArray());
		return list;
	}
	/**
	 * 组装利用小时数对象
	 * @param row
	 * @param it
	 * @param index_type
	 * @param task_id
	 * @return
	 */
	private  PowerHour setPowerHour(JSONObject row,String it,String index_type,String task_id){
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
	private  QuotientData setQuotientData(JSONObject row,String it,String index_type,String task_id){
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
	
	
	

}
