package com.github.balance.task.dao;



import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Repository;

import com.github.balance.parparedata.senddata.model.SendItemName;
import com.github.balance.task.entity.BalanceTask;
import com.github.balance.task.entity.BalanceYear;
import com.github.basicData.model.BasicYear;
import com.github.totalquantity.prepareData.entity.PrepareData;
import com.github.totalquantity.task.entity.TotalTask;


@Repository
public  class BalanceTaskDaoImpl implements BalanceTaskDao{
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public void saveData(BalanceTask task) {
		try{
		String sql ="insert into shiro.balance_task(task_name,year) value(?,?)";
		this.jdbcTemplate.update(sql, new Object[]{task.getTask_name(),task.getYear()});
		this.execSendData(task.getTask_name()) ;
		execPowerQuotient(task);
		execHourNum(task);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 外购外送默认指标
	 * @param task_name
	 */
	private void execSendData(String task_name){
		String querySql = " SELECT b.id,s.code FROM sys_dict_table s  JOIN balance_task  b ON s.domain_id=18 AND b.task_name=?";
		final List<SendItemName> list= this.jdbcTemplate.query(querySql, new Object[]{task_name} , new ParameterizedRowMapper<SendItemName>() {
            @Override
            public SendItemName mapRow(ResultSet rs, int index)
                    throws SQLException {
            	SendItemName st = new SendItemName();
            	st.setTask_id(rs.getString("id"));
            	st.setPro_name(rs.getString("code"));
            	return st;
            }
        });
		
		String insertsql = " insert into senddata_itemname(task_id,pro_name) values(?,?)";
		BatchPreparedStatementSetter setdelete = new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i)
					throws SQLException {
				SendItemName pd = list.get(i);
				ps.setString(1, pd.getTask_id());
				ps.setString(2, pd.getPro_name());

			}

			@Override
			public int getBatchSize() {
				return list.size();
			}
		};
		jdbcTemplate.batchUpdate(insertsql, setdelete);
	}
	/**
	 * 电源装机默认系数
	 * @param task_name
	 */
	private void execPowerQuotient(BalanceTask task){
		String querySql="select id from balance_task where task_name=?" ;
		Map<String ,Object> m = this.jdbcTemplate.queryForMap(querySql, new Object[]{task.getTask_name()});
		if(m != null){
			if(m.get("id") != null){
				StringBuffer sb = new StringBuffer();
				sb.append(" INSERT INTO quotient_data(index_item,VALUE,YEAR,task_id) ")
				.append(" SELECT q.index_item,q.value,t.yr, " )
				.append(m.get("id").toString())
				.append( " FROM quotient_init q JOIN ")
				.append(getYearDual(task.getYear()));
				this.jdbcTemplate.update(sb.toString());
			}
		}
		
	}
	/**
	 * 电源装机默认小时数
	 * @param task_name
	 */
	private void execHourNum(BalanceTask task){
		String querySql="select id from balance_task where task_name=?" ;
		Map<String ,Object> m = this.jdbcTemplate.queryForMap(querySql, new Object[]{task.getTask_name()});
		if(m != null){
			if(m.get("id") != null){
				StringBuffer sb = new StringBuffer();
				sb.append(" INSERT INTO power_hour (index_item,hour_num,task_id) ")
				.append(" SELECT q.index_item,q.hour_num, " )
				.append(m.get("id").toString())
				.append( " FROM quotient_init q ");
				this.jdbcTemplate.update(sb.toString());
			}
		}
		
	}
	@Override
	public List<Map<String, Object>> queryData(JSONObject param) {
		String pageSize = param.getString("pageSize");
		String pageNum = param.getString("pageNum");

			int psize = Integer.parseInt(pageSize);
			int pNum = Integer.parseInt(pageNum);
			int  startNum = psize*(pNum-1);
			int  endNum = psize*pNum;
		String sql = "select id,task_name,year from shiro.balance_task order by id desc"
				+ " limit ?,?";
		 List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql,new Object[]{startNum,endNum});
		return list;
	}

	@Override
	public void updateData(BalanceTask task) {
		String sql = "update shiro.balance_task set task_name=?,year=? where  id=?";
		this.jdbcTemplate.update(sql, new Object[]{task.getTask_name(),task.getYear(),task.getId()});
		
	}

	@Override
	public int queryDataCount(JSONObject param) {
		String sql ="select count(1) from shiro.balance_task";
		int count =this.jdbcTemplate.queryForInt(sql);
		return count;
	}

	@Override
	public List<BalanceYear> getYears() {
		String sql = "select year,year_name from shiro.balance_year";
		List<BalanceYear> list = jdbcTemplate.query(sql,
				new BeanPropertyRowMapper(BalanceYear.class));
		return list;
	}

	@Override
	public List<Map<String, Object>> initData(String id) {
		String sql ="select  id,task_name,year from shiro.balance_task where id=?";
		return  this.jdbcTemplate.queryForList(sql,new Object[]{id});
	}
	
	/**
	 * 组装年份
	 * @param year
	 * @return
	 */
	private String getYearDual(String year){
		String[] aYears = year.split(",");
		StringBuffer strYears = new StringBuffer();
		strYears.append(" ( ") ;
		for(int i=0;i<aYears.length;i++)
		{
			strYears.append("select ").append(aYears[i]).append(" as yr from dual");
			if(i<aYears.length-1)
				strYears.append(" union all ");
		}
		strYears.append(" ) t");
		return strYears.toString();
	}
	@Override
	public String deleteRecord(String[] delectArr) throws Exception {
		// TODO Auto-generated method stub
		StringBuffer  buffer=new StringBuffer("delete from balance_task where id in(");
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
	public String deleteOtherData(String[] delectArr) {
		this.deleteHinderedIdleCapacity(delectArr);
		this.deleteLoadelectricquantity(delectArr);
		this.deletePowerHour(delectArr);
		this.deletePowerQuotient(delectArr);
		this.deleteSenddata(delectArr);
		this.deleteElectricpowerplant(delectArr);
		this.deletePower(delectArr);
		return "1";
	}
	
	
	private void deletePowerQuotient(String[] delectArr){
		StringBuffer  buffer=new StringBuffer("delete from quotient_data where task_id in (");
		String InSql = "";
		for (int i = 0; i < delectArr.length; i++) {
			InSql = InSql + "?,";
		}
		buffer.append(InSql.substring(0, InSql.length() - 1));
		buffer.append(")");
		jdbcTemplate.update(buffer.toString(),delectArr);
	}
	private void deletePowerHour(String[] delectArr){
		StringBuffer  buffer=new StringBuffer("delete from power_hour where task_id in (");
		String InSql = "";
		for (int i = 0; i < delectArr.length; i++) {
			InSql = InSql + "?,";
		}
		buffer.append(InSql.substring(0, InSql.length() - 1));
		buffer.append(")");
		jdbcTemplate.update(buffer.toString(),delectArr);
	}
	
	
	private void deleteSenddata(String[] delectArr){
		StringBuffer  buffer1=new StringBuffer("delete from senddata_data where index_item in (select id from senddata_itemname where task_id in (");
		String InSql = "";
		for (int i = 0; i < delectArr.length; i++) {
			InSql = InSql + "?,";
		}
		buffer1.append(InSql.substring(0, InSql.length() - 1));
		buffer1.append("))");
		jdbcTemplate.update(buffer1.toString(),delectArr);
		
		StringBuffer  buffer=new StringBuffer("delete from senddata_itemname where task_id in (");
		buffer.append(InSql.substring(0, InSql.length() - 1));
		buffer.append(")");
		jdbcTemplate.update(buffer.toString(),delectArr);
		
	}
	
	private void  deleteLoadelectricquantity(String[] delectArr){
		StringBuffer  buffer=new StringBuffer("delete from  loadelectricquantity_data where task_id in (");
		String InSql = "";
		for (int i = 0; i < delectArr.length; i++) {
			InSql = InSql + "?,";
		}
		buffer.append(InSql.substring(0, InSql.length() - 1));
		buffer.append(")");
		jdbcTemplate.update(buffer.toString(),delectArr);
	}
	
	private void  deleteHinderedIdleCapacity(String[] delectArr){
		StringBuffer  buffer=new StringBuffer("delete from  hinderedIdleCapacity_data where task_id in (");
		String InSql = "";
		for (int i = 0; i < delectArr.length; i++) {
			InSql = InSql + "?,";
		}
		buffer.append(InSql.substring(0, InSql.length() - 1));
		buffer.append(")");
		jdbcTemplate.update(buffer.toString(),delectArr);
	}
	
	private void  deleteElectricpowerplant(String[] delectArr){
		StringBuffer  buffer=new StringBuffer("delete from  electricity_data where task_id in (");
		String InSql = "";
		for (int i = 0; i < delectArr.length; i++) {
			InSql = InSql + "?,";
		}
		buffer.append(InSql.substring(0, InSql.length() - 1));
		buffer.append(")");
		jdbcTemplate.update(buffer.toString(),delectArr);
	}
	private void  deletePower(String[] delectArr){
		StringBuffer  buffer=new StringBuffer("delete from  power_data where task_id in (");
		String InSql = "";
		for (int i = 0; i < delectArr.length; i++) {
			InSql = InSql + "?,";
		}
		buffer.append(InSql.substring(0, InSql.length() - 1));
		buffer.append(")");
		jdbcTemplate.update(buffer.toString(),delectArr);
	}
	
	
}
