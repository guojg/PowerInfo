package com.github.balance.parparedata.senddata.dao;

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
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;

import com.github.balance.parparedata.electricpowerplant.model.PowerPlant;
import com.github.balance.parparedata.senddata.model.Domain;
import com.github.balance.parparedata.senddata.model.SendItemName;
import com.github.basicData.model.BasicData;
import com.github.basicData.model.BasicYear;

@Repository
public class SendDataDaoImpl implements SendDataDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Map<String, Object>> queryData(JSONObject param)
			throws Exception {
		StringBuffer sb = new StringBuffer();
		String years[]=param.get("years")==null?null:param.get("years").toString().split(",");
	    sb.append(" SELECT b.value 'pro_name',b.sdshl,b.wgwstdlyxss,b.ID,b.pid");	
	    sb.append(addYearSql(years));
	    sb.append(" FROM senddata_data  a ");
	    sb.append(" RIGHT JOIN (SELECT a.id ,a.pro_name,b.value,a.task_id,a.wgwstdlyxss,a.sdshl,a.pid FROM senddata_itemname a  RIGHT JOIN ");
	    sb.append("  sys_dict_table b ON pro_name=b.code WHERE b.domain_id=18 AND b.code IN(1,2,3,4,5)) b ON b.id=a.index_item");
	    sb.append("  WHERE b.task_id=1");
	                                                                                                                                             
	    sb.append(" UNION ALL");
	    sb.append("  SELECT b.pro_name,b.sdshl,b.wgwstdlyxss,b.ID ,b.pid");
	    sb.append(addYearSql(years));
	    sb.append(" FROM senddata_data  a ");
	    sb.append("  RIGHT JOIN senddata_itemname  b  ON a.index_item=b.id WHERE b.pid='5'");
	    sb.append(" UNION ALL");
	    sb.append(" SELECT b.value 'pro_name',b.sdshl,b.wgwstdlyxss,b.ID,b.pid");
	    sb.append(addYearSql(years));
	    sb.append(" FROM senddata_data  a ");
	    sb.append(" RIGHT JOIN ( SELECT a.id ,b.value,a.pro_name,a.task_id,a.wgwstdlyxss,a.sdshl,a.pid FROM senddata_itemname a");
	    sb.append("  RIGHT JOIN sys_dict_table b ON pro_name=b.code WHERE b.domain_id=18 AND b.code=6) b ON b.id=a.index_item");
	    sb.append("   WHERE b.task_id=1");
	    sb.append(" UNION ALL");
	    sb.append(" SELECT b.pro_name,b.sdshl,b.wgwstdlyxss,b.ID,b.pid");
	    sb.append(addYearSql(years));
	    sb.append(" FROM senddata_data  a ");
	    sb.append("  RIGHT JOIN senddata_itemname  b  ON a.index_item=b.id WHERE b.pid='6'");
	    sb.append(" UNION ALL");
	    sb.append(" SELECT b.value 'pro_name',b.sdshl,b.wgwstdlyxss,b.ID,b.pid");
	    sb.append(addYearSql(years));
	    sb.append(" FROM senddata_data  a ");
	    sb.append(" RIGHT JOIN ( SELECT a.id ,b.value,a.pro_name,a.task_id,a.wgwstdlyxss,a.sdshl,a.pid FROM senddata_itemname a");
	    sb.append("  RIGHT JOIN sys_dict_table b ON pro_name=b.code WHERE b.domain_id=18 AND b.code=7) b ON b.id=a.index_item WHERE");
	    sb.append("   b.task_id=1");
	    sb.append(" UNION ALL");
	    sb.append(" SELECT b.pro_name,b.sdshl,b.wgwstdlyxss,b.ID,b.pid");
	    sb.append(addYearSql(years));
	    sb.append(" FROM senddata_data  a ");
	    sb.append(" RIGHT JOIN senddata_itemname  b  ON a.index_item=b.id WHERE b.pid='7'");		
		return jdbcTemplate.queryForList(sb.toString());
	}
	
	private String addYearSql(String[]  years){
		StringBuffer sb=new StringBuffer("");
		if(years!=null){
			for (String year :years) {
				sb.append(",CASE a.yr WHEN ");
				sb.append(year);
				sb.append(" THEN a.value END '");
				sb.append(year);
				sb.append("'");
			}
		}
		return sb.toString();

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
		//executeSum();
		return "";
	}
	   private void executeSum(JSONArray rows) throws Exception{
		   String deleteSql="delete from senddata_data where index_item='205'";
		   jdbcTemplate.update(deleteSql);

		   StringBuffer buffer=new StringBuffer("");
		   buffer.append("INSERT INTO senddata_data (yr,VALUE,index_item)");
		   buffer.append(" SELECT ");
		   buffer.append(" yr,");
		   buffer.append(" SUM(a.`sdshl`*b.value),");
		   buffer.append(" '3' index_item");
		   buffer.append(" FROM");
		   buffer.append(" senddata_itemname  a");
		   buffer.append(" RIGHT JOIN ");
		   buffer.append(" (SELECT ");
		   buffer.append(" VALUE,");
		   buffer.append(" yr,");
		   buffer.append(" index_item ");
		   buffer.append(" FROM");
		   buffer.append(" senddata_data ");
		   buffer.append(" WHERE index_item IN ");
		   buffer.append(" (SELECT ");
		   buffer.append(" id ");
		   buffer.append(" FROM  senddata_itemname WHERE task_id=1 AND pid IS NOT NULL");
		   buffer.append(" )");
		   buffer.append(" ) b ON a.`id`=b.index_item GROUP  BY b.yr");
		   buffer.append(" UNION ALL");
		   buffer.append(" SELECT ");
		   buffer.append(" yr,");
		   buffer.append(" SUM(b.value)-SUM(a.`sdshl`*b.value),");
		   buffer.append(" '1' index_item");

		   buffer.append(" FROM");
		   buffer.append(" senddata_itemname  a");
		   buffer.append("  RIGHT JOIN ");
		   buffer.append(" (SELECT ");
		   buffer.append(" VALUE,");
		   buffer.append(" yr,");
		   buffer.append(" index_item ");
		   buffer.append(" FROM");
		   buffer.append(" senddata_data ");
		   buffer.append("  WHERE index_item IN ");
		   buffer.append(" (SELECT ");
		   buffer.append("  id ");
		   buffer.append(" FROM  senddata_itemname WHERE task_id=1 AND pid IS NOT NULL");
		   buffer.append(" )");
		   buffer.append(" ) b ON a.`id`=b.index_item GROUP  BY b.yr");
		   buffer.append(" UNION ALL");
		   buffer.append(" SELECT ");
		   buffer.append("  b.yr,");
		   buffer.append("  SUM(b.value*a.wgwstdlyxss/10000),");
		   buffer.append("  '4' index_item");
		   buffer.append(" FROM");
		   buffer.append("  senddata_itemname  a");
		   buffer.append("  RIGHT JOIN ");
		   buffer.append("  (SELECT ");
		   buffer.append("    VALUE,");
		   buffer.append("   yr,");
		   buffer.append("    index_item ");
		   buffer.append("  FROM");
		   buffer.append("    senddata_data ");
		   buffer.append("  WHERE index_item IN ");
		   buffer.append("  (SELECT ");
		   buffer.append("    id ");
		   buffer.append("   FROM  senddata_itemname WHERE task_id=1 AND pid IS NOT NULL");
		   buffer.append("    )");
		   buffer.append("    ) b ON a.`id`=b.index_item GROUP  BY b.yr");
		   buffer.append("  UNION ALL");
		   buffer.append("  SELECT yr,");
		   buffer.append("      SUM(VALUE) VALUE,");
		   buffer.append("       2 AS index_item");
		          
		   buffer.append("   FROM");
		   buffer.append("     senddata_data tb ");
		   buffer.append("  WHERE index_item IN ");
		   buffer.append(" (SELECT ");
		   buffer.append("     id ");
		   buffer.append("    FROM  senddata_itemname WHERE task_id=1 AND pid IS NOT NULL");
		   buffer.append("    )   GROUP BY yr");
		   buffer.append("  UNION ALL");
		   buffer.append("  SELECT  tb.yr,");
		   buffer.append(" 	SUM(tb.value) VALUE,");
		   buffer.append(" 	5 AS index_item");
		   buffer.append("  FROM");
		   buffer.append("    senddata_data tb ");
		   buffer.append("  WHERE tb.index_item IN ");
		   buffer.append("   (SELECT ");
		   buffer.append("     id ");
		   buffer.append("   FROM  senddata_itemname WHERE task_id=1 AND pid =5");
		   buffer.append("    )   GROUP BY yr");
		   buffer.append("    UNION ALL");
		   buffer.append("  SELECT ");
		   buffer.append(" 	tb.yr,");
		   buffer.append(" 	SUM(tb.value),");
		   buffer.append(" 	'6'  index_item");
		   buffer.append("  FROM");
		   buffer.append("   senddata_data tb ");
		   buffer.append("  WHERE index_item IN ");
		   buffer.append("    (SELECT ");
		   buffer.append("     id ");
		   buffer.append("  FROM  senddata_itemname WHERE task_id=1 AND pid =6");
		   buffer.append("   )   GROUP BY yr");
		   buffer.append("    UNION ALL");
		   buffer.append("    SELECT ");
		   buffer.append("     tb.yr,");
		   buffer.append("  SUM(tb.value),");
		   buffer.append("  7 AS index_item");
		   buffer.append("  FROM");
		   buffer.append("  senddata_data tb ");
		   buffer.append("  WHERE index_item IN ");
		   buffer.append("   (SELECT ");
		   buffer.append("    id ");
		   buffer.append("    FROM  senddata_itemname WHERE task_id=1 AND pro_name =7");
		   buffer.append("  )   GROUP BY yr ");
		   jdbcTemplate.update(buffer.toString());
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

	@Override
	public String addProData(final SendItemName sendItemName) throws Exception {
		String insertsql = "insert  senddata_itemname" 
				+ "(pro_name,pid,task_id) VALUES(?,?,?)";
		PreparedStatementSetter setinsert = new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				// TODO Auto-generated method stub
				ps.setString(1, sendItemName.getPro_name());
				ps.setString(2, sendItemName.getPid());
				ps.setString(3, sendItemName.getTask_id());
			}

		};
		jdbcTemplate.update(insertsql, setinsert);
		return "1";
	}

	@Override
	public String deleteProData(String ids) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Domain> getTypes() throws Exception {
		// TODO Auto-generated method stub

		String sql = "select code,value name from sys_dict_table where domain_id=18 and code in(5,6,7)";
		List<Domain> list = jdbcTemplate.query(sql,
				new BeanPropertyRowMapper(Domain.class));
		return list;
	}
}
