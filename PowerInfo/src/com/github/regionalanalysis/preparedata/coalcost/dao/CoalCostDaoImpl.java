package com.github.regionalanalysis.preparedata.coalcost.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.github.regionalanalysis.preparedata.coalcost.model.CoalCostData;

@Repository
public class CoalCostDaoImpl implements CoalCostDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Map<String, Object>> queryData(JSONObject param)
			throws Exception {
		StringBuffer sb = new StringBuffer();
		String fdj_id=param.get("fdj_id").toString();
		sb.append("SELECT b.code unit,b.unit_name,");
		sb.append("b.index_y_name,b.code index_y");
		for (String index_x : param.get("index_xs").toString().split(",")) {
			sb.append(",sum(CASE a.index_x WHEN ");
			sb.append(index_x);
			sb.append(" THEN a.value END) '");
			sb.append(index_x);
			sb.append("'");
		}
		sb.append("  FROM (select * from  coal_cost_data  where fdj_id=?) a");
		sb.append(" RIGHT JOIN  (SELECT  s1.code, s1.value index_y_name, s2.value unit_name,  s1.ord  FROM  sys_dict_table s1 ");
		sb.append(" INNER JOIN sys_dict_table s2  ON s1.domain_id = 30  AND s2.domain_id = 32   AND s1.code = s2.code) b ");
		sb.append(" ON a.index_y = b.code   GROUP BY b.code ");



		return jdbcTemplate.queryForList(sb.toString(),new Object[]{fdj_id});
	}

	/**
	 * 保存基础数据业务数据
	 */
	@Override
	public String saveData(JSONObject param) throws Exception {
		// TODO Auto-generated method stub
		JSONArray rows = null;
		String fdj_id=null;
		if (param.get("editObj") != null) {
			rows = JSONArray.fromObject(param.get("editObj"));
		}
		if (param.get("fdj_id") != null) {
			fdj_id = param.get("fdj_id").toString();
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
						row.getString(it),unit,fdj_id);
				basicdataList.add(basicData);
			}
		}
		executeSQLS(basicdataList);
		sumData(fdj_id);
		totalData(Integer.parseInt(fdj_id));
		return "1";
	}
	private CoalCostData createModel(String index_y, String index_x, String value,String unit,String fdj_id)
			throws Exception {

		value = "".equals(value) ? null : value;
		CoalCostData basicdata = new CoalCostData();
		basicdata.setIndexY(index_y);
		basicdata.setIndexX(index_x);
		basicdata.setValue(value);
		basicdata.setUnit(unit);
		basicdata.setFdjId(fdj_id);

		return basicdata;
	}

	private void executeSQLS(final List<CoalCostData> coalcostdata)
			throws Exception {

		String deletesql = "delete from  coal_cost_data"
				+ " where index_x=? and index_y=? and fdj_id=?";
		BatchPreparedStatementSetter setdelete = new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i)
					throws SQLException {
				// TODO Auto-generated method stub
				CoalCostData basicdata = coalcostdata.get(i);
				ps.setString(1, basicdata.getIndexX());
				ps.setString(2, basicdata.getIndexY());
				ps.setString(3, basicdata.getFdjId());
			}

			@Override
			public int getBatchSize() {
				// TODO Auto-generated method stub
				return coalcostdata.size();
			}
		};
		jdbcTemplate.batchUpdate(deletesql, setdelete);

		String insertsql = "insert  coal_cost_data"
				+ "(index_x,unit,index_y,value,fdj_id) VALUES(?,?,?,?,?)";
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
				ps.setString(5, basicdata.getFdjId());
			}

			@Override
			public int getBatchSize() {
				// TODO Auto-generated method stub
				return coalcostdata.size();
			}
		};
		jdbcTemplate.batchUpdate(insertsql, setinsert);
	}

	private void sumData(String fdj_id) throws Exception {
		// TODO Auto-generated method stub
		StringBuffer delbuffer=new StringBuffer("delete from coal_cost_data where index_y in (5,6)");
		jdbcTemplate.update(delbuffer.toString());
		StringBuffer buffer=new StringBuffer("INSERT INTO coal_cost_data (index_x,unit,index_y,value,fdj_id)");
		buffer.append("SELECT t1.index_x,'5','5',t1.value*t2.value*0.0005 VALUE,t2.fdj_id FROM ");
		buffer.append("(SELECT VALUE,index_x FROM  coal_cost_data WHERE index_y=1 and fdj_id=?) t1  INNER JOIN ");
		buffer.append("(SELECT VALUE,index_x,fdj_id FROM  coal_cost_data WHERE index_y=4 and fdj_id=?) t2 ON t1.index_x=t2.index_x");
		buffer.append(" union all");
		buffer.append(" SELECT a.index_x,'6','6',a.VALUE*index_value VALUE,a.fdj_id FROM");
		buffer.append(" ( SELECT t1.index_x,t1.value*t2.value*0.0005 VALUE,t2.fdj_id FROM ");
		buffer.append(" (SELECT VALUE,index_x FROM  coal_cost_data WHERE index_y=1 AND fdj_id=?) t1  INNER JOIN ");
		buffer.append(" (SELECT VALUE,index_x,fdj_id FROM  coal_cost_data WHERE index_y=4 AND fdj_id=?) t2 ON t1.index_x=t2.index_x ) a ");
		buffer.append(" INNER JOIN (SELECT index_value,jz_id FROM constant_cost_arg  WHERE index_type='1400' and jz_id=?) b ON a.fdj_id=b.jz_id");

		jdbcTemplate.update(buffer.toString(),new Object[]{fdj_id,fdj_id,fdj_id,fdj_id,fdj_id});
	}

	@Override
	public void totalData(int  fdj_id) throws Exception {
		// TODO Auto-generated method stub
		final int jsonParam = fdj_id;

		StringBuffer sb = new StringBuffer();
		 //this.jdbcTemplate.execute("call pro_show_childLst(?)"); 
		 this.jdbcTemplate.execute(   
		         new CallableStatementCreator() {   
		             public CallableStatement createCallableStatement(Connection con) throws SQLException {   
		                String storedProc = "{call pro_total_cost(?)}";// 调用的sql   
		                CallableStatement cs = con.prepareCall(storedProc);   
		                cs.setInt(1, jsonParam);// 设置输入参数的值   
		                return cs;   
		             }
		             }, new CallableStatementCallback() {   
		                 public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {   
		                     cs.execute();   
		                     return null;// 获取输出参数的值   
		               }

  
		          } 
		       ); 
	}
}
