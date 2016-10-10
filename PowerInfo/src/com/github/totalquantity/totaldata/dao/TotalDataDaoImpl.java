package com.github.totalquantity.totaldata.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.github.basicData.model.BasicYear;
import com.github.totalquantity.totaldata.entity.TotalData;
@Repository
public class TotalDataDaoImpl implements TotalDataDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public void saveData(final List<TotalData> list) {
		// TODO Auto-generated method stub
		String deletesql = "delete from total_data where task_id=?";
		this.jdbcTemplate.update(deletesql, new Object[]{list.get(0).getTask_id()}) ;
		String sql = "insert into total_data(task_id,algorithm,year,value) value (?,?,?,?)";
		this.jdbcTemplate.batchUpdate(sql,
		      new BatchPreparedStatementSetter() {
		        public void setValues(PreparedStatement ps, int i) throws SQLException {
		            ps.setString(1, list.get(i).getTask_id());//任务号
		            ps.setString(2, list.get(i).getAlgorithm());//算法代号
		            ps.setInt(3, list.get(i).getYear());//年
		            ps.setDouble(4, list.get(i).getValue());//值
		          }
		          public int getBatchSize() {
		            return list.size();
		          }

		        });
	}

	@Override
	public List<Map<String, Object>> queryData(JSONObject param) {
		String algorithm = param.getString("algorithm") ;
		String year = param.getString("year") ;
		String taskid = param.getString("taskid") ;
		StringBuffer sb = new StringBuffer();
		String sql ="select task_id,algorithm,t.value algorithm_name ";
		sb.append(sql) ;
		for (String yearStr : param.get("year").toString().split(",")) {
			sb.append(",SUM(CASE year WHEN ");
			sb.append(yearStr);
			sb.append(" THEN d.value END) '");
			sb.append(yearStr);
			sb.append("'");
		}
		sb.append(" from total_data d join sys_dict_table t on d.algorithm=t.code and t.domain_id=10 where task_id=? ") ;
		List<String> params = new ArrayList<String>();
		params.add(taskid) ;
		if(!"".equals(algorithm) && algorithm!=null){
			sb.append(" and instr(?,algorithm)>0") ;
			params.add(algorithm) ;
		}
		sb.append(" group by task_id,algorithm,t.value");
	
		/*if(!"".equals(year) && year!=null){
			sb.append(" and instr(?,year)>0") ;
			params.add(year) ;

		}*/
		List<Map<String, Object>>  list = this.jdbcTemplate.queryForList(sb.toString(),params.toArray());
		return list;
	}
	
	public  List<BasicYear> getYears(JSONObject param)  throws Exception {
		String baseyear = param.getString("baseyear") ;
		String planyear = param.getString("planyear") ;
		int baseyearInt = Integer.parseInt(baseyear);
		int planyearInt = Integer.parseInt(planyear);
		List<BasicYear> list = this.getYearCurents(baseyearInt);
		for(int i= baseyearInt+1 ; i<=planyearInt;++i){
			BasicYear by = new BasicYear();
			by.setYear(i+"");
			by.setYearName(i+"年");
			list.add(by) ;
		}
		return list;
		

	}
	private List<BasicYear> getYearCurents(int baseyear) throws Exception {
		// TODO Auto-generated method stub

		String sql = "select year,year_name from base_year where year<=?";
		List<BasicYear> list = jdbcTemplate.query(sql,new Object[]{baseyear},
				new BeanPropertyRowMapper(BasicYear.class));
		return list;
	}
	
	@Override
	public List<Map<String, Object>> queryData6(JSONObject param) {
		int baseyearInt = param.getInt("baseyearInt") ;
		String year = param.getString("year") ;
		String taskid = param.getString("taskid") ;
		String algorithm = param.getString("algorithm") ;
		String[] years = year.split(",");
		//List<Integer> aList = new ArrayList<Integer>();
		//List<Integer> bList = new ArrayList<Integer>();
		List<Object> list = new ArrayList<Object>();
		StringBuffer ayear = new StringBuffer();
		StringBuffer byear = new StringBuffer();
		for(String str : years){
			Integer a = Integer.parseInt(str);
			if(a<=baseyearInt){
				//aList.add(a);
				ayear.append("?,");
				
			}else{
				//bList.add(a);
				byear.append("?,") ;
			}
			list.add(str);
		}
		list.add(algorithm);
		list.add(taskid);
		StringBuffer sb = new StringBuffer();
		String sql ="select m.yr ";
		sb.append(sql) ;
		for (String yearStr :years) {
			sb.append(",SUM(CASE m.yr WHEN ");
			sb.append(yearStr);
			sb.append(" THEN m.value END) '");
			sb.append(yearStr);
			sb.append("'");
		}
		sb.append(" from (SELECT tb.yr,tb.value FROM electricalsource_data tb WHERE ")
		  .append( " index_item=107 ");
		  if(ayear.toString().length()>=1){
			  sb.append(" and tb.yr IN (")  
			  .append(ayear.toString().substring(0, ayear.toString().length()-1))
			  .append(") ") ;
		  }
		  sb.append(" union all")
		  .append(" SELECT t.year,t.value FROM  total_data t WHERE 1=1   ");
		  if(byear.toString().length()>=1){
			  sb.append(" and YEAR IN (")  
			   .append(byear.toString().substring(0, byear.toString().length()-1))
			  .append(") ") ;
		  }
		  sb.append("  and t.algorithm=? AND t.task_id=? ) m");


		List<Map<String, Object>>  resultList = this.jdbcTemplate.queryForList(sb.toString(),list.toArray());
		return resultList;
	}

	@Override
	public String deleteRecord(String[] delectArr) {
		StringBuffer  buffer=new StringBuffer("delete from total_data where task_id in(");
		String InSql = "";
		for (int i = 0; i < delectArr.length; i++) {
			InSql = InSql + "?,";
		}
		buffer.append(InSql.substring(0, InSql.length() - 1));
		buffer.append(")");
		jdbcTemplate.update(buffer.toString(),delectArr);
		return "1";
	}
	

}
