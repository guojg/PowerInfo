package com.github.balance.parparedata.hinderedidleCapacity.dao;

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

@Repository
public class HinderedIdleCapacityDaoImpl implements HinderedIdleCapacityDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Map<String, Object>> queryData(JSONObject param)
			throws Exception {
		String indexs[] = param.get("indexs") == null ? null : param
				.get("indexs").toString().split(",");
		StringBuffer sb = new StringBuffer();

		sb.append("SELECT tb.id index_item,tb.name index_name,tb.ORD");

		for (String year : param.get("years").toString().split(",")) {
			sb.append(",SUM(CASE tb.yr WHEN ");
			sb.append(year);
			sb.append(" THEN tb.value END) '");
			sb.append(year);
			sb.append("'");
		}
		sb.append("  FROM (SELECT t2.id,t2.name,t2.ORD,t1.value,t1.yr FROM ");
		sb.append(" hinderedIdleCapacity_data");
		sb.append("         t1 RIGHT JOIN (");
		sb.append("         select id,name,ord from sys_menu where id in(");
		String InSql = "";
		for (int i = 0; i < indexs.length; i++) {
			InSql = InSql + "?,";
		}
		sb.append(InSql.substring(0, InSql.length() - 1));
		sb.append("        )");
		sb.append(" ) t2        ON t1.index_item= t2.id) tb");
		sb.append(" GROUP BY tb.id,tb.name,tb.ORD");

		return jdbcTemplate.queryForList(sb.toString(), indexs);
	}

	/**
	 * 保存基础数据业务数据
	 */
	@Override
	public String saveData(JSONArray rows) throws Exception {
		// TODO Auto-generated method stub
		List<BasicData> basicdataList = new ArrayList<BasicData>();
		for (int i = 0; i < rows.size(); i++) {
			JSONObject row = rows.getJSONObject(i);
			Iterator<String> its = row.keys();

			String index_item = "";
			while (its.hasNext()) {
				String it = its.next();
				index_item = row.getString("index_item");

				if (it.equals("index_item") || it.equals("index_name"))
					continue;

				BasicData basicData = createModel(index_item, it,
						row.getString(it));
				basicdataList.add(basicData);
			}
		}
		executeSQLS(basicdataList);
		return "";
	}

	private BasicData createModel(String indexid, String yr, String value)
			throws Exception {

		value = "".equals(value) ? null : value;
		BasicData basicdata = new BasicData();
		basicdata.setIndexItem(indexid);
		basicdata.setYear(yr);
		basicdata.setValue(value);
		return basicdata;
	}

	private void executeSQLS(final List<BasicData> basicDatas)
			throws Exception {

		String deletesql = "delete from  hinderedIdleCapacity_data"
				+ " where INDEX_ITEM=? and YR=?";
		BatchPreparedStatementSetter setdelete = new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i)
					throws SQLException {
				// TODO Auto-generated method stub
				BasicData basicdata = basicDatas.get(i);
				ps.setString(1, basicdata.getIndexItem());
				ps.setString(2, basicdata.getYear());
			}

			@Override
			public int getBatchSize() {
				// TODO Auto-generated method stub
				return basicDatas.size();
			}
		};
		jdbcTemplate.batchUpdate(deletesql, setdelete);

		String insertsql = "insert  hinderedIdleCapacity_data"
				+ "(INDEX_ITEM,YR,VALUE) VALUES(?,?,?)";
		BatchPreparedStatementSetter setinsert = new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i)
					throws SQLException {
				// TODO Auto-generated method stub
				BasicData basicdata = basicDatas.get(i);
				ps.setString(1, basicdata.getIndexItem());
				ps.setString(2, basicdata.getYear());
				ps.setString(3, basicdata.getValue());
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
