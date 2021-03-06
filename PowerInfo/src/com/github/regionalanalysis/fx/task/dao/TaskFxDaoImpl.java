package com.github.regionalanalysis.fx.task.dao;


import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.github.basicData.model.BasicYear;
import com.github.regionalanalysis.db.task.entity.DbTask;
import com.github.totalquantity.task.entity.TotalTask;
import com.github.totalquantity.task.entity.TotalYear;


@Repository
public class TaskFxDaoImpl implements TaskFxDao{
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public void saveData(DbTask task) {
		try{
		String sql ="insert into fx_task(task_name,area_id) value(?,?)";
		this.jdbcTemplate.update(sql, new Object[]{task.getTask_name(),task.getArea_id()});
		DbTask db = this.getDataByName(task);
		String area_id = db.getArea_id() ;
		String id = db.getId() ;
		this.insertCoalCost(id, area_id);
		this.insertCon(id, area_id);
		this.insertElec(id, area_id);
		this.insertElecCon(id, area_id);
		this.insertGeneCon(id, area_id);
		this.insertElecConYear(id, area_id);
		this.insertGeneConYear(id, area_id);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public DbTask getDataByName(DbTask task){
		String sql = "select * from fx_task where task_name=? and area_id=?" ;
		List<Map<String, Object>> l = this.jdbcTemplate.queryForList(sql, new Object[]{task.getTask_name(),task.getArea_id()}) ;
	    if(l.size()>0){
	    	String id = l.get(0).get("ID").toString();
	    	task.setId(id);
	    }
	    return task;
	}
	@Override
	public List<Map<String, Object>> queryData(JSONObject param) {
		String pageSize = param.getString("pageSize");
		String pageNum = param.getString("pageNum");
		String area_id = param.getString("area_id");

			int psize = Integer.parseInt(pageSize);
			int pNum = Integer.parseInt(pageNum);
			int  startNum = psize*(pNum-1);
			int  endNum = psize*pNum;
		String sql = "select id,task_name,bn.value area_name,area_id from fx_task db join bn_code_company bn on db.area_id=bn.code and db.area_id=? order by id desc"
				+ " limit ?,?";
		 List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql,new Object[]{area_id,startNum,psize});
		return list;
	}

	@Override
	public void updateData(DbTask task) {
		String sql = "update fx_task set task_name=? where  id=?";
		this.jdbcTemplate.update(sql, new Object[]{task.getTask_name(),task.getId()});
		
	}

	@Override
	public int queryDataCount(JSONObject param) {
		String area_id = param.getString("area_id");

		String sql ="select count(1) from fx_task where area_id = ?";
		int count =this.jdbcTemplate.queryForInt(sql,new Object[]{area_id});
		return count;
	}

	
	@Override
	public List<Map<String, Object>> initData(String id) {
		String sql ="select id,task_name,area_id from fx_task where id=?";
		return  this.jdbcTemplate.queryForList(sql,new Object[]{id});
	}
	
	@Override
	public String deleteRecord(String[] delectArr) throws Exception {
		// TODO Auto-generated method stub
		StringBuffer  buffer=new StringBuffer("delete from fx_task where id in(");
		String InSql = "";
		for (int i = 0; i < delectArr.length; i++) {
			InSql = InSql + "?,";
		}
		buffer.append(InSql.substring(0, InSql.length() - 1));
		buffer.append(")");
		jdbcTemplate.update(buffer.toString(),delectArr);
		this.deleteCoalCost(delectArr);
		this.deleteCon(delectArr);
		this.deleteElec(delectArr);
		this.deleteElecCon(delectArr);
		this.deleteGeneCon(delectArr);
		this.deleteElecConYear(delectArr);
		this.deleteGeneConYear(delectArr);
		return "1";
	}
	/**
	 * 删除电厂
	 * @param delectArr
	 */
	public void deleteElec(String[] delectArr){
		StringBuffer  buffer=new StringBuffer("delete from electricpowerplant_analysis_data_fx where task_id in(");
		String InSql = "";
		for (int i = 0; i < delectArr.length; i++) {
			InSql = InSql + "?,";
		}
		buffer.append(InSql.substring(0, InSql.length() - 1));
		buffer.append(")");
		jdbcTemplate.update(buffer.toString(),delectArr);
		
	}
	/**
	 * 删除电厂对比信息
	 * @param delectArr
	 */
	public void deleteElecCon(String[] delectArr){
		StringBuffer  buffer=new StringBuffer("delete from electricity_contrast_fx where task_id in(");
		String InSql = "";
		for (int i = 0; i < delectArr.length; i++) {
			InSql = InSql + "?,";
		}
		buffer.append(InSql.substring(0, InSql.length() - 1));
		buffer.append(")");
		jdbcTemplate.update(buffer.toString(),delectArr);
		
	}
	/**
	 * 删除电厂年分析信息
	 * @param delectArr
	 */
	public void deleteElecConYear(String[] delectArr){
		StringBuffer  buffer=new StringBuffer("delete from electricity_contrast_year_fx where task_id in(");
		String InSql = "";
		for (int i = 0; i < delectArr.length; i++) {
			InSql = InSql + "?,";
		}
		buffer.append(InSql.substring(0, InSql.length() - 1));
		buffer.append(")");
		jdbcTemplate.update(buffer.toString(),delectArr);
		
	}
	
	/**
	 * 删除燃煤信息
	 * @param delectArr
	 */
	public void deleteCoalCost(String[] delectArr){
		StringBuffer  buffer=new StringBuffer("delete from coal_cost_data_fx where task_id in(");
		String InSql = "";
		for (int i = 0; i < delectArr.length; i++) {
			InSql = InSql + "?,";
		}
		buffer.append(InSql.substring(0, InSql.length() - 1));
		buffer.append(")");
		jdbcTemplate.update(buffer.toString(),delectArr);
		
	}
	/**
	 * 删除机组基本信息
	 * @param delectArr
	 */
	public void deleteCon(String[] delectArr){
		StringBuffer  buffer=new StringBuffer("delete from constant_cost_arg_fx where task_id in(");
		String InSql = "";
		for (int i = 0; i < delectArr.length; i++) {
			InSql = InSql + "?,";
		}
		buffer.append(InSql.substring(0, InSql.length() - 1));
		buffer.append(")");
		jdbcTemplate.update(buffer.toString(),delectArr);
		
	}
	/**
	 * 删除机组分析信息
	 * @param delectArr
	 */
	public void deleteGeneCon(String[] delectArr){
		StringBuffer  buffer=new StringBuffer("delete from generator_contrast_fx where task_id in(");
		String InSql = "";
		for (int i = 0; i < delectArr.length; i++) {
			InSql = InSql + "?,";
		}
		buffer.append(InSql.substring(0, InSql.length() - 1));
		buffer.append(")");
		jdbcTemplate.update(buffer.toString(),delectArr);
		
	}
	/**
	 * 删除机组分析信息
	 * @param delectArr
	 */
	public void deleteGeneConYear(String[] delectArr){
		StringBuffer  buffer=new StringBuffer("delete from generator_contrast_year_fx where task_id in(");
		String InSql = "";
		for (int i = 0; i < delectArr.length; i++) {
			InSql = InSql + "?,";
		}
		buffer.append(InSql.substring(0, InSql.length() - 1));
		buffer.append(")");
		jdbcTemplate.update(buffer.toString(),delectArr);
		
	}
	
	/**
	 * 复制电厂
	 * @param delectArr
	 */
	public void insertElec(String id,String area_id){
		StringBuffer  buffer=new StringBuffer("INSERT INTO   electricpowerplant_analysis_data_fx (id,plant_name,plant_capacity,generating_capatity,plant_loss,start_outlay,product_year, "
				+ "economical_life,  equired_return,financial_cost, generation_coal, operation_rate, operation_cost,unit_cost,area_id, task_id,materials_cost,salary,repairs_cost,other_cost,build_year,consumption_rate,electricity_consumption,power_type,Cooling_type)"
				+ " select id,plant_name,plant_capacity,generating_capatity,plant_loss,start_outlay,product_year,economical_life,  equired_return,"
				+ "financial_cost, generation_coal, operation_rate, operation_cost,unit_cost,area_id, " );
		buffer.append(id + " 'task_id'") ;
		buffer.append( ",materials_cost,salary,repairs_cost,other_cost,build_year,consumption_rate,electricity_consumption,power_type,Cooling_type FROM electricpowerplant_analysis_data where area_id=? ");
		jdbcTemplate.update(buffer.toString(),new Object[]{area_id});
		
	}
	/**
	 * 复制电厂对比信息
	 * @param delectArr
	 */
	public void insertElecCon(String id,String area_id){
		StringBuffer  buffer=new StringBuffer("INSERT INTO electricity_contrast_fx(index_x,unit,index_y,VALUE,dc_id,area_id,task_id)"
				+ " SELECT index_x,unit,index_y,VALUE,dc_id,area_id, " );
		buffer.append(id + " 'task_id'") ;
		buffer.append( "FROM electricity_contrast where area_id=? ");
		jdbcTemplate.update(buffer.toString(),new Object[]{area_id});
		
	}
	/**
	 * 复制电厂对比信息年值
	 * @param delectArr
	 */
	public void insertElecConYear(String id,String area_id){
		StringBuffer  buffer=new StringBuffer("INSERT INTO electricity_contrast_year_fx(unit,index_y,VALUE,dc_id,area_id,task_id)"
				+ " SELECT unit,index_y,VALUE,dc_id,area_id, " );
		buffer.append(id + " 'task_id'") ;
		buffer.append( "FROM electricity_contrast_year where area_id=? ");
		jdbcTemplate.update(buffer.toString(),new Object[]{area_id});
		
	}
	
	/**
	 * 复制燃煤信息
	 * @param delectArr
	 */
	public void insertCoalCost(String id,String area_id){
		
		StringBuffer  buffer=new StringBuffer("INSERT INTO coal_cost_data_fx(index_x,unit,index_y,value,fdj_id,area_id,task_id)"
				+ " SELECT index_x,unit,index_y,value,fdj_id,area_id, " );
		buffer.append(id + " 'task_id'") ;
		buffer.append( "FROM coal_cost_data where area_id=? ");
		jdbcTemplate.update(buffer.toString(),new Object[]{area_id});
		
	}
	/**
	 * 插入机组基本信息
	 * @param delectArr
	 */
	public void insertCon(String id,String area_id){
		StringBuffer  buffer=new StringBuffer("INSERT INTO constant_cost_arg_fx(index_type,index_value,jz_id,area_id,task_id)"
				+ " SELECT index_type,index_value,jz_id,area_id, " );
		buffer.append(id + " 'task_id'") ;
		buffer.append( "FROM constant_cost_arg where area_id=? ");
		jdbcTemplate.update(buffer.toString(),new Object[]{area_id});
		
	}
	/**
	 * 复制机组对比信息
	 * @param delectArr
	 */
	public void insertGeneCon(String id,String area_id){
		StringBuffer  buffer=new StringBuffer("INSERT INTO generator_contrast_fx(index_x,unit,index_y,VALUE,jz_id,area_id,task_id)"
				+ " SELECT index_x,unit,index_y,VALUE,jz_id,area_id, " );
		buffer.append(id + " 'task_id'") ;
		buffer.append( "FROM generator_contrast where area_id=? ");
		jdbcTemplate.update(buffer.toString(),new Object[]{area_id});
		
	}
	/**
	 * 复制机组对比信息年值
	 * @param delectArr
	 */
	public void insertGeneConYear(String id,String area_id){
		StringBuffer  buffer=new StringBuffer("INSERT INTO generator_contrast_year_fx(unit,index_y,VALUE,jz_id,area_id,task_id)"
				+ " SELECT unit,index_y,VALUE,jz_id,area_id, " );
		buffer.append(id + " 'task_id'") ;
		buffer.append( "FROM generator_contrast_year where area_id=? ");
		jdbcTemplate.update(buffer.toString(),new Object[]{area_id});
		
	}
}
