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
	    sb.append(" SELECT b.value 'pro_name',b.sdshl,b.wgwstdlyxss,b.id,b.pid");	
	    sb.append(addYearSql(years));
	    sb.append(" FROM senddata_data  a ");
	    sb.append(" RIGHT JOIN (SELECT a.id ,a.pro_name,b.value,a.task_id,a.wgwstdlyxss,a.sdshl,a.pid FROM senddata_itemname a  RIGHT JOIN ");
	    sb.append("  sys_dict_table b ON pro_name=b.code WHERE b.domain_id=18 AND b.code IN(1,2,3,5)) b ON b.id=a.index_item");
	    sb.append("   group by b.sdshl,b.wgwstdlyxss,b.id,b.pid");
	                                                                                                                                             
	    sb.append(" UNION ALL");
	    sb.append("  SELECT b.pro_name,b.sdshl,b.wgwstdlyxss,b.id ,b.pid");
	    sb.append(addYearSql(years));
	    sb.append(" FROM senddata_data  a ");
	    sb.append("  RIGHT JOIN senddata_itemname  b  ON a.index_item=b.id WHERE b.pid='5' GROUP BY   b.pro_name, b.sdshl,b.wgwstdlyxss,b.id, b.pid");
	    sb.append(" UNION ALL");
	    sb.append(" SELECT b.value 'pro_name',b.sdshl,b.wgwstdlyxss,b.id,b.pid");
	    sb.append(addYearSql(years));
	    sb.append(" FROM senddata_data  a ");
	    sb.append(" RIGHT JOIN ( SELECT a.id ,b.value,a.pro_name,a.task_id,a.wgwstdlyxss,a.sdshl,a.pid FROM senddata_itemname a");
	    sb.append("  RIGHT JOIN sys_dict_table b ON pro_name=b.code WHERE b.domain_id=18 AND b.code=6) b ON b.id=a.index_item");
	    sb.append("  ");
	    sb.append(" UNION ALL");
	    sb.append(" SELECT b.pro_name,b.sdshl,b.wgwstdlyxss,b.id,b.pid");
	    sb.append(addYearSql(years));
	    sb.append(" FROM senddata_data  a ");
	    sb.append("  RIGHT JOIN senddata_itemname  b  ON a.index_item=b.id WHERE b.pid='6'  GROUP BY   b.pro_name, b.sdshl,b.wgwstdlyxss,b.id, b.pid");
	    sb.append(" UNION ALL");
	    sb.append(" SELECT b.value 'pro_name',b.sdshl,b.wgwstdlyxss,b.id,b.pid");
	    sb.append(addYearSql(years));
	    sb.append(" FROM senddata_data  a ");
	    sb.append(" RIGHT JOIN ( SELECT a.id ,b.value,a.pro_name,a.task_id,a.wgwstdlyxss,a.sdshl,a.pid FROM senddata_itemname a");
	    sb.append("  RIGHT JOIN sys_dict_table b ON pro_name=b.code WHERE b.domain_id=18 AND b.code=7) b ON b.id=a.index_item ");
	    sb.append("   ");
	    sb.append(" UNION ALL");
	    sb.append(" SELECT b.pro_name,b.sdshl,b.wgwstdlyxss,b.id,b.pid");
	    sb.append(addYearSql(years));
	    sb.append(" FROM senddata_data  a ");
	    sb.append(" RIGHT JOIN senddata_itemname  b  ON a.index_item=b.id WHERE b.pid='7'  GROUP BY   b.pro_name, b.sdshl,b.wgwstdlyxss,b.id, b.pid");	
	    sb.append(" UNION ALL");
	    sb.append(" SELECT b.value 'pro_name',b.sdshl,b.wgwstdlyxss,b.id,b.pid");	
	    sb.append(addYearSql(years));
	    sb.append(" FROM senddata_data  a ");
	    sb.append(" RIGHT JOIN (SELECT a.id ,a.pro_name,b.value,a.task_id,a.wgwstdlyxss,a.sdshl,a.pid FROM senddata_itemname a  RIGHT JOIN ");
	    sb.append("  sys_dict_table b ON pro_name=b.code WHERE b.domain_id=18 AND b.code IN(4)) b ON b.id=a.index_item");
	    sb.append("   group by b.sdshl,b.wgwstdlyxss,b.id,b.pid");
		return jdbcTemplate.queryForList(sb.toString());
	}
	
	private String addYearSql(String[]  years){
		StringBuffer sb=new StringBuffer("");
		if(years!=null){
			for (String year :years) {
				sb.append(",sum(CASE a.yr WHEN ");
				sb.append(year);
				sb.append(" THEN a.value END) '");
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
	public String saveData(JSONObject param) throws Exception {
		// TODO Auto-generated method stub
		JSONArray rows = null;
		if (param.get("editObj") != null) {
			rows = JSONArray.fromObject(param.get("editObj"));
		}
		String taskid=null;
		List<BasicData> basicdataList = new ArrayList<BasicData>();
		List<SendItemName> senditemnameList = new ArrayList<SendItemName>();

		for (int i = 0; i < rows.size(); i++) {
			JSONObject row = rows.getJSONObject(i);
			Iterator<String> its = row.keys();
			String id = row.getString("id");
			String sdshl=row.get("sdshl")==null?null:row.get("sdshl").toString();
			String wgwstdlyxss=row.get("wgwstdlyxss")==null?null:row.get("wgwstdlyxss").toString();
			SendItemName itemname = createItemName(id, sdshl,
					wgwstdlyxss);
			senditemnameList.add(itemname);
			executeSQLItem(senditemnameList);
			while (its.hasNext()) {
				String it = its.next();
				if (it.equals("id") || it.equals("pro_name")||it.equals("sdshl")
						||it.equals("wgwstdlyxss")||it.equals("pid"))
					continue;
				BasicData basicData = createModel(id, it,
						row.getString(it));
				basicdataList.add(basicData);
			}
		}
		executeSQLS(basicdataList);
		executeSum( taskid);
		return "1";
	}
	   private void executeSum(String taskid) throws Exception{
		   String deleteSql="DELETE FROM senddata_data WHERE index_item IN(SELECT id FROM"
		   		+ " senddata_itemname WHERE  pro_name IN(1,2,3,4,5,6,7))";
		   jdbcTemplate.update(deleteSql);
		   String itemtypesql="(SELECT id FROM senddata_itemname ";
		   StringBuffer buffer=new StringBuffer("");
		   buffer.append("INSERT INTO senddata_data (yr,VALUE,index_item)");
		   buffer.append(" SELECT ");
		   buffer.append(" yr,");
		   buffer.append(" SUM(a.`sdshl`*b.value/100),");
		   buffer.append(itemtypesql);
		   buffer.append("  WHERE pro_name=3) index_item");
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
		   buffer.append(" FROM  senddata_itemname WHERE  pid IS NOT NULL");
		   buffer.append(" )");
		   buffer.append(" ) b ON a.`id`=b.index_item GROUP  BY b.yr");
		   buffer.append(" UNION ALL");
		   buffer.append(" SELECT ");
		   buffer.append(" yr,");
		   buffer.append(" SUM(b.value)-SUM(a.`sdshl`*b.value/100),");
		   buffer.append(itemtypesql);
		   buffer.append("  WHERE pro_name=1) index_item");

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
		   buffer.append(" FROM  senddata_itemname WHERE  pid IS NOT NULL");
		   buffer.append(" )");
		   buffer.append(" ) b ON a.`id`=b.index_item GROUP  BY b.yr");
		   buffer.append(" UNION ALL");
		   buffer.append(" SELECT ");
		   buffer.append("  b.yr,");
		   buffer.append("  SUM(b.value*a.wgwstdlyxss/10000),");
		   buffer.append(itemtypesql);
		   buffer.append("  WHERE pro_name=4) index_item");
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
		   buffer.append("   FROM  senddata_itemname WHERE  pid IS NOT NULL");
		   buffer.append("    )");
		   buffer.append("    ) b ON a.`id`=b.index_item GROUP  BY b.yr");
		   buffer.append("  UNION ALL");
		   buffer.append("  SELECT yr,");
		   buffer.append("      SUM(VALUE) VALUE,");
		   buffer.append(itemtypesql);
		   buffer.append("  WHERE pro_name=2) index_item");
		          
		   buffer.append("   FROM");
		   buffer.append("     senddata_data tb ");
		   buffer.append("  WHERE index_item IN ");
		   buffer.append(" (SELECT ");
		   buffer.append("     id ");
		   buffer.append("    FROM  senddata_itemname WHERE  pid IS NOT NULL");
		   buffer.append("    )   GROUP BY yr");
		   buffer.append("  UNION ALL");
		   buffer.append("  SELECT  tb.yr,");
		   buffer.append(" 	SUM(tb.value) VALUE,");
		   buffer.append(itemtypesql);
		   buffer.append("  WHERE pro_name=5) index_item");
		   buffer.append("  FROM");
		   buffer.append("    senddata_data tb ");
		   buffer.append("  WHERE tb.index_item IN ");
		   buffer.append("   (SELECT ");
		   buffer.append("     id ");
		   buffer.append("   FROM  senddata_itemname WHERE  pid =5");
		   buffer.append("    )   GROUP BY yr");
		   buffer.append("    UNION ALL");
		   buffer.append("  SELECT ");
		   buffer.append(" 	tb.yr,");
		   buffer.append(" 	SUM(tb.value),");
		   buffer.append(itemtypesql);
		   buffer.append("  WHERE pro_name=6) index_item");
		   buffer.append("  FROM");
		   buffer.append("   senddata_data tb ");
		   buffer.append("  WHERE index_item IN ");
		   buffer.append("    (SELECT ");
		   buffer.append("     id ");
		   buffer.append("  FROM  senddata_itemname WHERE pid =6");
		   buffer.append("   )   GROUP BY yr");
		   buffer.append("    UNION ALL");
		   buffer.append("    SELECT ");
		   buffer.append("     tb.yr,");
		   buffer.append("  SUM(tb.value),");
		   buffer.append(itemtypesql);
		   buffer.append("  WHERE pro_name=7) index_item");
		   buffer.append("  FROM");
		   buffer.append("  senddata_data tb ");
		   buffer.append("  WHERE index_item IN ");
		   buffer.append("   (SELECT ");
		   buffer.append("    id ");
		   buffer.append("    FROM  senddata_itemname WHERE  pro_name =7");
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
	private SendItemName createItemName(String id,String sdshl,String wgwstdlyxss)
			throws Exception {
		SendItemName itemname = new SendItemName();
		itemname.setId(id);
		itemname.setSdshl(sdshl);
		itemname.setWgwstdlyxss(wgwstdlyxss);
		return itemname;
	}
	private void executeSQLS(final List<BasicData> basicDatas)
			throws Exception {

		String deletesql = "delete from senddata_data"
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

		String insertsql = "insert  senddata_data"
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
	private void executeSQLItem(final List<SendItemName> itemnames)
			throws Exception {
		String updateSql = "update  senddata_itemname set sdshl=?,wgwstdlyxss=?"
				+ " where id=?";
		BatchPreparedStatementSetter setupdate = new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i)
					throws SQLException {
				// TODO Auto-generated method stub
				SendItemName basicdata = itemnames.get(i);
				ps.setString(1, basicdata.getSdshl());
				ps.setString(2, basicdata.getWgwstdlyxss());
				ps.setString(3, basicdata.getId());
			}

			@Override
			public int getBatchSize() {
				// TODO Auto-generated method stub
				return itemnames.size();
			}
		};
		jdbcTemplate.batchUpdate(updateSql, setupdate);

	}
	@Override
	public String addProData(final SendItemName sendItemName) throws Exception {
		String insertsql = "insert  senddata_itemname" 
				+ "(pro_name,pid) VALUES(?,?)";
		PreparedStatementSetter setinsert = new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				// TODO Auto-generated method stub
				ps.setString(1, sendItemName.getPro_name());
				ps.setString(2, sendItemName.getPid());
			}

		};
		jdbcTemplate.update(insertsql, setinsert);
		return "1";
	}

	@Override
	public String deleteProData(String[] delectArr,String taskid) throws Exception {
		// TODO Auto-generated method stub
		String deleteSql="DELETE FROM senddata_data WHERE index_item IN(SELECT id FROM"
			   		+ " senddata_itemname WHERE  pro_name IN(1,2,3,4,5,6,7))";
		jdbcTemplate.update(deleteSql);
		StringBuffer  buffer=new StringBuffer("delete from senddata_itemname where id in(");
		String InSql = "";
		for (int i = 0; i < delectArr.length; i++) {
			InSql = InSql + "?,";
		}
		buffer.append(InSql.substring(0, InSql.length() - 1));
		buffer.append(")");
		jdbcTemplate.update(buffer.toString(),delectArr);
		
		StringBuffer  buf=new StringBuffer("delete from senddata_data where index_item in(");
		String InSq = "";
		for (int i = 0; i < delectArr.length; i++) {
			InSq = InSq + "?,";
		}
		buf.append(InSq.substring(0, InSq.length() - 1));
		buf.append(")");
		jdbcTemplate.update(buf.toString(),delectArr);
		executeSum( taskid);
		
		return "1";
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
