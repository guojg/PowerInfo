package com.github.basicData.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.github.basicData.model.BasicData;
import com.github.basicData.model.BasicIndex;
import com.github.basicData.model.BasicYear;
import com.github.common.util.Contans;

@Repository
public class BasicDataDaoImpl implements BasicDataDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Map<String, Object>> queryData(JSONObject param)
			throws Exception {
		String indexs[] = param.get("indexs") == null ? null : param
				.get("indexs").toString().split(",");

		String tablename = getTableNameByIndex(indexs[0]);
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
		sb.append(tablename);
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

	private String getTableNameByIndex(String index) throws Exception {

		String sql = "SELECT table_name FROM sys_menu WHERE  id=?";

		return jdbcTemplate.queryForMap(sql, new Object[] { index })
				.get("table_name").toString();

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
		String tablename = getTableNameByIndex(rows.getJSONObject(0)
				.get("index_item").toString());
		executeSQLS(basicdataList, tablename);
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

	private void executeSQLS(final List<BasicData> basicDatas, String tablename)
			throws Exception {

		String deletesql = "delete from " + tablename
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

		String insertsql = "insert " + tablename
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

	/**
	 * 判断是否含子节点返回1代表有子节点
	 * 
	 * @param id
	 * @return
	 */
	private int isLeaf(String id) {

		int count = jdbcTemplate.queryForInt(
				"select count(id) from shiro.sys_menu where p_id=?",
				new Object[] { id });
		int flag = 0;
		if (count > 0) {
			flag = 1;
		}
		return flag;
	}

	@Override
	/**
	 * 如果pid=1先创建对应表然后新增指标节点，新增纪录完成后返回对应的纪录更新前台tree
	 */
	public List<Map<String, Object>> addLeaf(JSONObject row) throws Exception {
		// TODO Auto-generated method stub
		String pid = row.getString("p_id");
		String tablename = row.getString("tablename");
		String name = row.getString("name");
		String unit_code=row.getString("unit_code");
		if("".equals(unit_code)){
			unit_code=null;
		}
		if ("1".equals(pid)) {
			tablename = CreateTable();
		}
		StringBuffer buffer = new StringBuffer();

		buffer.append("INSERT INTO shiro.sys_menu");
		buffer.append("( p_id, NAME, url, table_name,unit_code)");
		buffer.append("values(?, ?, ?, ?,?)");
		jdbcTemplate.update(buffer.toString(), new Object[] { pid, name,
				Contans.BASEURL, tablename ,unit_code});
		// 查询最新插入的值
		StringBuffer qusb = new StringBuffer(
				"SELECT sys_menu.id,sys_menu.name TEXT,");
		qusb.append("sys_menu.p_id parent_id,sys_menu.url,sys_menu.table_name tablename FROM shiro.sys_menu");
		qusb.append(" where id=(SELECT MAX(sys_menu.id) FROM shiro.sys_menu)");
		return jdbcTemplate.queryForList(qusb.toString());
	}

	private String CreateTable() {
		DateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
		String date = format.format(new Date());
		StringBuffer buffer = new StringBuffer();
		String tablename = "shiro.basic_data_" + date;
		buffer.append("CREATE TABLE shiro.basic_data_" + date);
		buffer.append("(ID int(11) NOT NULL AUTO_INCREMENT,");
		buffer.append("yr int(4) DEFAULT NULL,");
		buffer.append("index_item VARCHAR(11) DEFAULT NULL,");
		buffer.append("value double(13,2) DEFAULT NULL,");
		buffer.append("PRIMARY KEY (ID))");
		jdbcTemplate.execute(buffer.toString());
		return tablename;
	}

	@Override
	public String updatLeaf(JSONObject row) throws Exception {
		// TODO Auto-generated method stub
		String returnFlag = "1";
		String id = row.getString("id");
		String name = row.getString("name");
		String unit_code = row.getString("unit_code");
		if("".equals(unit_code)){
			unit_code=null;
		}
		StringBuffer buffer = new StringBuffer();

		buffer.append("update shiro.sys_menu");
		buffer.append(" set name=?,unit_code=?");
		buffer.append(" where id=?");
		int count = jdbcTemplate.update(buffer.toString(), new Object[] { name,unit_code,
				id });
		if (count < 1) {
			returnFlag = "0";
		}
		return returnFlag;
	}

	@Override
	public String deleteLeaf(String id) throws Exception {
		// TODO Auto-generated method stub
		StringBuffer buffer = new StringBuffer();
		String returnFlag = "1";
		buffer.append("delete from shiro.sys_menu");
		buffer.append(" where id=? or p_id=?");
		int count = jdbcTemplate.update(buffer.toString(), new Object[] { id,
				id });
		if (count < 1) {
			returnFlag = "0";
		}
		return returnFlag;
	}

	/**
	 * 先删除年份表内数据，然后插入新的年份数据
	 */

	@Override
	public String addYear(JSONObject row) throws Exception {
		// TODO Auto-generated method stub
		String returnFlag = "1";
		String del = "delete from shiro.base_year";
		jdbcTemplate.update(del);
		Integer startyear = Integer.parseInt(row.getString("startyear"));
		Integer endyear = Integer.parseInt(row.getString("endyear"));
		final List<BasicYear> years = createYears(startyear, endyear);
		StringBuffer buffer = new StringBuffer();

		buffer.append("insert into  shiro.base_year");
		buffer.append(" (year,year_name)");
		buffer.append(" values(?,?)");
		BatchPreparedStatementSetter setinsert = new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i)
					throws SQLException {
				// TODO Auto-generated method stub
				BasicYear basicyear = years.get(i);
				ps.setString(1, basicyear.getYear());
				ps.setString(2, basicyear.getYearName());
			}

			@Override
			public int getBatchSize() {
				// TODO Auto-generated method stub
				return years.size();
			}
		};
		int count[] = jdbcTemplate.batchUpdate(buffer.toString(), setinsert);

		if (count.length < 1) {
			returnFlag = "0";
		}
		return returnFlag;
	}

	private List<BasicYear> createYears(Integer startyear, Integer endyear) {
		List<BasicYear> years = new ArrayList<BasicYear>();
		for (int year = startyear; year <= endyear; year++) {
			BasicYear basicyear = new BasicYear();
			basicyear.setYear(year + "");
			basicyear.setYearName(year + Contans.YEARUNIT);
			years.add(basicyear);
		}
		return years;
	}

	@Override
	public List<BasicYear> getYears() throws Exception {
		// TODO Auto-generated method stub

		String sql = "select year,year_name from base_year";
		List<BasicYear> list = jdbcTemplate.query(sql,
				new BeanPropertyRowMapper(BasicYear.class));
		return list;
	}

	@Override
	public List<BasicIndex> getIndexs(final String pid) throws Exception {
		// TODO Auto-generated method stub
		String sql = "";
		if (isLeaf(pid) == 1) {
			sql = "select id index_item,name index_name from sys_menu where p_id=?";
		} else {
			sql = "select id index_item,name index_name  from sys_menu where id=?";
		}
		List<BasicIndex> list = jdbcTemplate.query(sql, new Object[] { pid },
				new BeanPropertyRowMapper(BasicIndex.class));
		return list;
	}

	@Override
	public String isOnly(String name) throws Exception {
		StringBuffer sb = new StringBuffer();
		String flag="1";
		// this.jdbcTemplate.execute("call pro_show_childLst(?)");
		this.jdbcTemplate.execute(new CallableStatementCreator() {
			public CallableStatement createCallableStatement(Connection con)
					throws SQLException {
				String storedProc = "{call pro_show_childLst(?)}";// 调用的sql
				CallableStatement cs = con.prepareCall(storedProc);
				cs.setInt(1, 1);// 设置输入参数的值
				return cs;
			}
		}, new CallableStatementCallback() {
			public Object doInCallableStatement(CallableStatement cs)
					throws SQLException, DataAccessException {
				cs.execute();
				return null;// 获取输出参数的值
			}

		});

		sb.append("  SELECT count(1)  FROM tmpLst,shiro.sys_menu WHERE tmpLst.id=shiro.sys_menu.id and sys_menu.name like ?");
		int count= jdbcTemplate.queryForInt(sb.toString(),new Object[]{name});
		if(count<=0){
			flag="0";
		}
		return flag;
	}
}
