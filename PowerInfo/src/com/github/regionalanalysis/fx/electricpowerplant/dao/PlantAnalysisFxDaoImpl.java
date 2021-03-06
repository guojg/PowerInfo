package com.github.regionalanalysis.fx.electricpowerplant.dao;


import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;

import com.github.balance.parparedata.electricpowerplant.model.PowerPlant;
import com.github.regionalanalysis.preparedata.electricpowerplant.model.PlantAnalysis;
@Repository
public class PlantAnalysisFxDaoImpl implements PlantAnalysisFxDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Map<String, Object>> queryData(JSONObject param)
			throws Exception {
		int psize =0;
		int pNum=0;
		List<Object> params = new ArrayList<Object>();
		if(param.get("pageSize")!=null){
			psize= Integer.parseInt(param.getString("pageSize"));
		}
		if(param.get("pageNum")!=null){
		    pNum = Integer.parseInt(param.getString("pageNum"));
		}
		String name=param.getString("name");
		String task_id=param.getString("task_id");
		int  startNum = psize*(pNum-1);
		int  endNum = psize*pNum;
		StringBuffer buffer = new StringBuffer("SELECT id,plant_name,plant_capacity,product_year,build_year,start_outlay,");
		buffer.append("consumption_rate,electricity_consumption,(SELECT value FROM  shiro.sys_dict_table where domain_id=12 and code=power_type ) power_type_name,");
		buffer.append("power_type,cooling_type,(SELECT value FROM  shiro.sys_dict_table where domain_id=302 and code=cooling_type ) cooling_type_name,");
		buffer.append("materials_cost,salary,repairs_cost,other_cost");
		buffer.append(" from shiro.electricpowerplant_analysis_data_fx where 1=1");
		if(!"".equals(name)){
			buffer.append(" and plant_name like ?");
			params.add("%"+name+"%");
		}
		if(!"".equals(task_id)){
			buffer.append(" and task_id=?");
			params.add(task_id);
		}
		if(psize!=0){
			buffer.append(" limit ?,?");
			params.add(startNum);
			params.add(psize);
			return jdbcTemplate.queryForList(buffer.toString(),params.toArray());
		}else{
			return jdbcTemplate.queryForList(buffer.toString(),params.toArray());
		}	
			
		
	}
	
	


	@Override
	public String updateRecord(final PlantAnalysis plantAnalysis) throws Exception {
		// TODO Auto-generated method stub
		StringBuffer updateSql=new StringBuffer();
		updateSql.append("update  electricpowerplant_analysis_data_fx");
		updateSql.append(" set  plant_name=?,plant_capacity=?,product_year=?,build_year=?,start_outlay=?");
		updateSql.append(" ,consumption_rate=?,electricity_consumption=?,power_type=?,Cooling_type=?");
		updateSql.append(" ,materials_cost=?,salary=?,repairs_cost=?,other_cost=?");
		updateSql.append(" where id=? and task_id=? ");
		PreparedStatementSetter setupdate = new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				// TODO Auto-generated method stub
					// TODO Auto-generated method stub
				ps.setString(1, plantAnalysis.getPlantName());
				ps.setString(2, plantAnalysis.getPlantCapacity());
				ps.setString(3, plantAnalysis.getProductYear());
				ps.setString(4, plantAnalysis.getBuildYear());
				ps.setString(5, plantAnalysis.getStartOutlay());
				ps.setString(6, plantAnalysis.getConsumptionRate());
				ps.setString(7, plantAnalysis.getElectricityConsumption());
				ps.setString(8, plantAnalysis.getPowerType());
				ps.setString(9, plantAnalysis.getCoolingType());
				ps.setString(10, plantAnalysis.getMaterialsCost());
				ps.setString(11, plantAnalysis.getSalary());
				ps.setString(12, plantAnalysis.getRepairsCost());
				ps.setString(13, plantAnalysis.getOtherCost());
				ps.setString(14, plantAnalysis.getId());
				ps.setString(15, plantAnalysis.getTaskId());
				}
			};
		jdbcTemplate.update(updateSql.toString(), setupdate);
		return "1";
	}

	@Override
	public String deleteRecord(String[] delectArr,String task_id) throws Exception {
		// TODO Auto-generated method stub
		StringBuffer  buffer=new StringBuffer("delete from electricpowerplant_analysis_data_fx where id in(");
		String InSql = "";
		List l = new ArrayList();
		for (int i = 0; i < delectArr.length; i++) {
			InSql = InSql + "?,";
			l.add(delectArr[i]) ;
		}
		buffer.append(InSql.substring(0, InSql.length() - 1));
		buffer.append(") and task_id=?");
		l.add(task_id) ;
		jdbcTemplate.update(buffer.toString(),l.toArray());
		return "1";
	}

	@Override
	public String importRecord(List<PowerPlant> list) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public int getTotalCount(String task_id) {
		// TODO Auto-generated method stub
		
		String Sql="select count(1) from shiro.electricpowerplant_analysis_data_fx where task_id=?";
		
		return jdbcTemplate.queryForInt(Sql,new Object[]{task_id});
	}



	@Override
	public List<Map<String, Object>> getPlantById(String id,String task_id) throws Exception {
		String sql="select * from shiro.electricpowerplant_analysis_data_fx where id=? and task_id=?";
		
		return jdbcTemplate.queryForList(sql,new Object[]{id,task_id});
	}



	@Override
	public List<Map<String, Object>> getFdjByDc(String id,String task_id) throws Exception {
		// TODO Auto-generated method stub
		String sql ="SELECT jz_id id FROM shiro.constant_cost_arg_fx WHERE  index_type=200 AND index_value=? and task_id=?";
		return jdbcTemplate.queryForList(sql,new Object[]{id,task_id});
	}
}
