package com.github.regionalanalysis.preparedata.electricpowerplant.dao;

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
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;

import com.github.balance.parparedata.electricpowerplant.model.PowerPlant;
import com.github.basicData.model.BasicData;
import com.github.regionalanalysis.preparedata.electricpowerplant.model.PlantAnalysis;

@Repository
public class PlantAnalysisDaoImpl implements PlantAnalysisDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Override
	public List<Map<String, Object>> queryData(JSONObject param) throws Exception {
		int psize = 0;
		int pNum = 0;
		List<Object> params = new ArrayList<Object>();
		if (param.get("pageSize") != null) {
			psize = Integer.parseInt(param.getString("pageSize"));
		}
		if (param.get("pageNum") != null) {
			pNum = Integer.parseInt(param.getString("pageNum"));
		}
		String name = param.getString("name");
		String area_id = param.getString("area_id");
		int startNum = psize * (pNum - 1);
		int endNum = psize * pNum;
		StringBuffer buffer = new StringBuffer("SELECT id,plant_name,plant_capacity,product_year,build_year,start_outlay,");
		buffer.append("consumption_rate,electricity_consumption,(SELECT value FROM  shiro.sys_dict_table where domain_id=12 and code=power_type ) power_type_name,");
		buffer.append("power_type,cooling_type,(SELECT value FROM  shiro.sys_dict_table where domain_id=302 and code=cooling_type ) cooling_type_name,");
		buffer.append("materials_cost,salary,repairs_cost,other_cost");
		buffer.append(" from shiro.electricpowerplant_analysis_data where 1=1");
		if (!"".equals(name)) {
			buffer.append(" and plant_name like ?");
			params.add("%" + name + "%");
		}
		if (!"".equals(area_id)) {
			buffer.append(" and area_id=?");
			params.add(area_id);
		}
		if (psize != 0) {
			buffer.append(" limit ?,?");
			params.add(startNum);
			params.add(psize);
			return jdbcTemplate.queryForList(buffer.toString(), params.toArray());
		} else {
			return jdbcTemplate.queryForList(buffer.toString(), params.toArray());
		}

	}

	@Override
	public String addRecord(final PlantAnalysis plantAnalysis) throws Exception {
		// TODO Auto-generated method stub
		StringBuffer insertsql = new StringBuffer();
		insertsql.append("insert  electricpowerplant_analysis_data");
		insertsql.append("(plant_name,plant_capacity,product_year,build_year,start_outlay,");
		insertsql.append("consumption_rate,electricity_consumption,power_type,Cooling_type,");
		insertsql.append("materials_cost,salary,repairs_cost,other_cost,area_id)");
		insertsql.append(" VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		PreparedStatementSetter setinsert = new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
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
				ps.setString(14, plantAnalysis.getAreaId());

			}

		};
		jdbcTemplate.update(insertsql.toString(), setinsert);

		return "1";
	}

	@Override
	public String updateRecord(final PlantAnalysis plantAnalysis) throws Exception {
		// TODO Auto-generated method stub
		StringBuffer updateSql = new StringBuffer();
		updateSql.append("update  electricpowerplant_analysis_data");
		updateSql.append(" set  plant_name=?,plant_capacity=?,product_year=?,build_year=?,start_outlay=?");
		updateSql.append(" ,consumption_rate=?,electricity_consumption=?,power_type=?,Cooling_type=?");
		updateSql.append(" ,materials_cost=?,salary=?,repairs_cost=?,other_cost=?,area_id=?");
		updateSql.append(" where id=?");
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
				ps.setString(14, plantAnalysis.getAreaId());
				ps.setString(15, plantAnalysis.getId());
			}
		};
		jdbcTemplate.update(updateSql.toString(), setupdate);
		return "1";
	}

	@Override
	public String deleteRecord(String[] delectArr) throws Exception {
		// TODO Auto-generated method stub
		StringBuffer buffer = new StringBuffer("delete from electricpowerplant_analysis_data where id in(");
		String InSql = "";
		for (int i = 0; i < delectArr.length; i++) {
			InSql = InSql + "?,";
		}
		buffer.append(InSql.substring(0, InSql.length() - 1));
		buffer.append(")");
		jdbcTemplate.update(buffer.toString(), delectArr);
		StringBuffer buff=new StringBuffer("DELETE FROM constant_cost_arg WHERE jz_id IN(");
		buff.append("SELECT a.jz_id FROM (SELECT  jz_id FROM constant_cost_arg WHERE  index_type=200 AND index_value IN(");

		buff.append(InSql.substring(0, InSql.length() - 1));
		buff.append(")) a)");
		jdbcTemplate.update(buff.toString(), delectArr);
		return "1";
	}

	@Override
	public String importRecord(List<PowerPlant> list) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getTotalCount(JSONObject param) {
		// TODO Auto-generated method stub
		String area_id = param.getString("area_id");
		List<Object> params = new ArrayList<Object>();

		String Sql = "select count(1) from shiro.electricpowerplant_analysis_data";
		if (!"".equals(area_id)) {
			Sql += " where area_id=?";
			params.add(area_id);
		}

		return jdbcTemplate.queryForInt(Sql, params.toArray());
	}

	@Override
	public List<Map<String, Object>> getPlantById(String id) throws Exception {
		String sql = "select * from shiro.electricpowerplant_analysis_data where id=?";

		return jdbcTemplate.queryForList(sql, new Object[] { id });
	}

	@Override
	public List<Map<String, Object>> getFdjByDc(String id) throws Exception {
		// TODO Auto-generated method stub
		String sql = "SELECT jz_id id FROM shiro.constant_cost_arg WHERE  index_type=200 AND index_value=?";
		return jdbcTemplate.queryForList(sql, new Object[] { id });
	}

	@Override
	public List<Map<String, Object>> queryTemplateData(String id) throws Exception {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		String[] ids=null;
		if(id!=null&&!"".equals(id)){
		ids=id.split(",");
		}
		String InSql = "";
		for (int i = 0; i < ids.length; i++) {
			InSql = InSql + "?,";
		}
		sb.append(
				"SELECT b.code index_item,b.value index_name,a.value FROM shiro.`electricalsource_analysis_templete` a RIGHT JOIN  ");

		sb.append(" (SELECT CODE,VALUE,ord  FROM shiro.`sys_dict_table` WHERE domain_id='301'");
		sb.append(" and code in(");
		sb.append(InSql.substring(0, InSql.length() - 1));
		sb.append("  )) b ON  a.index_item=b.code order by b.ord");

		return jdbcTemplate.queryForList(sb.toString(),ids);

		
	}

	/**
	 * 保存基础数据业务数据
	 */
	@Override
	public String saveTemplateData(JSONArray rows) throws Exception {
		// TODO Auto-generated method stub
		List<BasicData> basicdataList = new ArrayList<BasicData>();
		for (int i = 0; i < rows.size(); i++) {
			JSONObject row = rows.getJSONObject(i);
			String index_item = row.getString("index_item");
			String value = row.getString("value");

			BasicData basicData = createModel(index_item, value);
			basicdataList.add(basicData);

		}
		executeSQLS(basicdataList);
		return "";
	}

	private BasicData createModel(String indexid, String value) throws Exception {

		value = "".equals(value) ? null : value;
		BasicData basicdata = new BasicData();
		basicdata.setIndexItem(indexid);
		basicdata.setValue(value);
		return basicdata;
	}

	private void executeSQLS(final List<BasicData> basicDatas) throws Exception {

		String deletesql = "delete from electricalsource_analysis_templete where index_item=?";
		BatchPreparedStatementSetter setdelete = new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i)
					throws SQLException {
				// TODO Auto-generated method stub
				BasicData basicdata = basicDatas.get(i);
				ps.setString(1, basicdata.getIndexItem());
			}

			@Override
			public int getBatchSize() {
				// TODO Auto-generated method stub
				return basicDatas.size();
			}
		};
		jdbcTemplate.batchUpdate(deletesql, setdelete);
		String insertsql = "insert  electricalsource_analysis_templete" + "(INDEX_ITEM,VALUE) VALUES(?,?)";
		BatchPreparedStatementSetter setinsert = new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				// TODO Auto-generated method stub
				BasicData basicdata = basicDatas.get(i);
				ps.setString(1, basicdata.getIndexItem());
				ps.setString(2, basicdata.getValue());
			}

			@Override
			public int getBatchSize() {
				// TODO Auto-generated method stub
				return basicDatas.size();
			}
		};
		jdbcTemplate.batchUpdate(insertsql, setinsert);
	}
}
