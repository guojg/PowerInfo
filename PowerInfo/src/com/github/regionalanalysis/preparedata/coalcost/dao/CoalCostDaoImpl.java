package com.github.regionalanalysis.preparedata.coalcost.dao;

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
import com.github.basicData.model.BasicData;
import com.github.regionalanalysis.preparedata.coalcost.model.CoalCostData;

@Repository
public class CoalCostDaoImpl implements CoalCostDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Map<String, Object>> queryData(JSONObject param)
			throws Exception {
		StringBuffer sb = new StringBuffer();

		sb.append("SELECT a.unit,(SELECT VALUE FROM sys_dict_table WHERE domain_id=32 AND CODE=a.unit) 'unit_name',");
		sb.append("b.value 'index_y_name',a.index_y");
		for (String index_x : param.get("index_xs").toString().split(",")) {
			sb.append(",sum(CASE a.index_x WHEN ");
			sb.append(index_x);
			sb.append(" THEN a.value END) '");
			sb.append(index_x);
			sb.append("'");
		}
		sb.append("  FROM  coal_cost_data a");
		sb.append(" RIGHT JOIN  (SELECT  VALUE ,CODE FROM sys_dict_table  WHERE domain_id = 30  ) b");
		sb.append("	ON a.`index_y`=b.code GROUP BY b.value ORDER BY  b.code");


		return jdbcTemplate.queryForList(sb.toString());
	}

	/**
	 * 保存基础数据业务数据
	 */
	@Override
	public String saveData(JSONObject param) throws Exception {
		// TODO Auto-generated method stub
		JSONArray rows = null;
		if (param.get("editObj") != null) {
			rows = JSONArray.fromObject(param.get("editObj"));
		}
		List<CoalCostData> basicdataList = new ArrayList<CoalCostData>();
		for (int i = 0; i < rows.size(); i++) {
			JSONObject row = rows.getJSONObject(i);
			Iterator<String> its = row.keys();

			String index_y = "";
			String unit="";
			index_y = row.getString("index_y");
			unit=row.getString("unit");
			while (its.hasNext()) {
				String it = its.next();				
				if (it.equals("index_y") || it.equals("index_y_name")||it.equals("unit")||it.equals("unit_name"))
					continue;

				CoalCostData basicData = createModel(index_y, it,
						row.getString(it),unit);
				basicdataList.add(basicData);
			}
		}
		executeSQLS(basicdataList);
		sumData();
		return "1";
	}
	private CoalCostData createModel(String index_y, String index_x, String value,String unit)
			throws Exception {

		value = "".equals(value) ? null : value;
		CoalCostData basicdata = new CoalCostData();
		basicdata.setIndexY(index_y);
		basicdata.setIndexX(index_x);
		basicdata.setValue(value);
		basicdata.setUnit(unit);
		return basicdata;
	}

	private void executeSQLS(final List<CoalCostData> coalcostdata)
			throws Exception {

		String deletesql = "delete from  coal_cost_data"
				+ " where index_x=? and index_y=?";
		BatchPreparedStatementSetter setdelete = new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i)
					throws SQLException {
				// TODO Auto-generated method stub
				CoalCostData basicdata = coalcostdata.get(i);
				ps.setString(1, basicdata.getIndexX());
				ps.setString(2, basicdata.getIndexY());
			}

			@Override
			public int getBatchSize() {
				// TODO Auto-generated method stub
				return coalcostdata.size();
			}
		};
		jdbcTemplate.batchUpdate(deletesql, setdelete);

		String insertsql = "insert  coal_cost_data"
				+ "(index_x,unit,index_y,value) VALUES(?,?,?,?)";
		BatchPreparedStatementSetter setinsert = new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i)
					throws SQLException {
				// TODO Auto-generated method stub
				CoalCostData basicdata = coalcostdata.get(i);
				ps.setString(1, basicdata.getIndexX());
				ps.setString(2, basicdata.getUnit());
				ps.setString(3, basicdata.getIndexY());
				ps.setString(4, basicdata.getValue());
			}

			@Override
			public int getBatchSize() {
				// TODO Auto-generated method stub
				return coalcostdata.size();
			}
		};
		jdbcTemplate.batchUpdate(insertsql, setinsert);
	}

	private void sumData() throws Exception {
		// TODO Auto-generated method stub
		StringBuffer delbuffer=new StringBuffer("delete from coal_cost_data where index_y in (5,6)");
		jdbcTemplate.update(delbuffer.toString());
		StringBuffer buffer=new StringBuffer("INSERT INTO coal_cost_data (index_x,unit,index_y,value)");
		buffer.append("SELECT t1.index_x,t2.unit,'5',FORMAT(t1.value*t2.value*0.0005,4) VALUE FROM ");
		buffer.append("(SELECT VALUE,index_x FROM  coal_cost_data WHERE index_y=1) t1  INNER JOIN ");
		buffer.append("(SELECT VALUE,index_x,unit FROM  coal_cost_data WHERE index_y=4) t2 ON t1.index_x=t2.index_x");
		buffer.append(" union all");
		buffer.append(" SELECT t1.index_x,t2.unit,'6',FORMAT(t1.value*t2.value*0.0005,4) VALUE FROM ");
		buffer.append("(SELECT VALUE,index_x FROM  coal_cost_data WHERE index_y=1) t1  INNER JOIN ");
		buffer.append("(SELECT VALUE,index_x,unit FROM  coal_cost_data WHERE index_y=4) t2 ON t1.index_x=t2.index_x");
		jdbcTemplate.update(buffer.toString());
	}
}
