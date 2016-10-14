package com.github.balance.parparedata.generator.dao;


import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;

import com.github.balance.parparedata.electricpowerplant.model.PowerPlant;
import com.github.balance.parparedata.generator.model.Generator;
import com.github.basicData.model.BasicData;
@Repository
public class GeneratorDaoImpl implements GeneratorDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Map<String, Object>> queryData(JSONObject param)
			throws Exception {
		int psize =0;
		int pNum=0;
		String indexs[]=null;
		List params = new ArrayList();
		if(param.get("pageSize")!=null){
			psize= Integer.parseInt(param.getString("pageSize"));
		}
		if(param.get("pageNum")!=null){
		    pNum = Integer.parseInt(param.getString("pageNum"));
		}
		if(param.get("indexs")!=null){
			 indexs=param.getString("indexs").split(",");
		}
		String name=param.getString("name");
		String gene_name=param.getString("gene_name");
		int  startNum = psize*(pNum-1);
		int  endNum = psize*pNum;
		StringBuffer buffer=new StringBuffer("SELECT g.id, e.plant_name,g.plant_id,g.gene_name,g.gene_capacity,(select value from sys_dict_table where domain_id=12 and code=g.index_item) index_itemname,g.index_item,date_format(g.start_date,'%Y-%m-%d') start_date,");
		buffer.append(" date_format(g.end_date,'%Y-%m-%d') end_date from shiro.generator_data g join electricpowerplant_data e on e.id=g.plant_id");
		if(!"".equals(name)){
			buffer.append(" and e.plant_name like ?");
			params.add("%"+name+"%");
		}
		if(!"".equals(gene_name)){
			buffer.append(" and g.gene_name like ?");
			params.add("%"+gene_name+"%");
		}
		if(indexs.length>0){
			buffer.append(" and g.index_item in (");
			String InSql = "";
			for (int i = 0; i < indexs.length; i++) {
				InSql = InSql + "?,";
				params.add(indexs[i]);
			}
			buffer.append(InSql.substring(0, InSql.length() - 1));
			buffer.append(")");
		}
		if(psize!=0){
			buffer.append(" limit ?,?");
			params.add(startNum);
			params.add(endNum);
			return jdbcTemplate.queryForList(buffer.toString(),params.toArray());
		}else{
			return jdbcTemplate.queryForList(buffer.toString(),params.toArray());
		}	
			
		
	}
	
	

	@Override
	public String addRecord(final Generator generator) throws Exception {
		// TODO Auto-generated method stub
		String insertsql = "insert  generator_data" 
				+ "(gene_name,gene_capacity,start_date,end_date,index_item,plant_id) VALUES(?,?,?,?,?,?)";
		PreparedStatementSetter setinsert = new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				// TODO Auto-generated method stub
				ps.setString(1, generator.getGeneName());
				ps.setDouble(2, generator.getGeneCapacity());
				ps.setString(3, generator.getStartDate());
				ps.setString(4, generator.getEndDate());
				ps.setString(5, generator.getIndexItem());
				ps.setString(6, generator.getPlantId());

			}

		};
		jdbcTemplate.update(insertsql, setinsert);
		return "1";
	}

	@Override
	public String updateRecord(final Generator generator) throws Exception {
		// TODO Auto-generated method stub
		String insertsql = "update  generator_data" 
				+ " set  gene_name=?,gene_capacity=?,start_date=?,end_date=?,index_item=?,plant_id=? where id=?";
		PreparedStatementSetter setupdate = new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				// TODO Auto-generated method stub
				ps.setString(1, generator.getGeneName());
				ps.setDouble(2, generator.getGeneCapacity());
				ps.setString(3, generator.getStartDate());
				ps.setString(4, generator.getEndDate());
				ps.setString(5, generator.getIndexItem());
				ps.setString(6, generator.getPlantId());
				ps.setString(7, generator.getId());
			}

		};
		jdbcTemplate.update(insertsql, setupdate);
		return "1";
	}

	@Override
	public String deleteRecord(String[] delectArr) throws Exception {
		// TODO Auto-generated method stub
		StringBuffer  buffer=new StringBuffer("delete from generator_data where id in(");
		String InSql = "";
		for (int i = 0; i < delectArr.length; i++) {
			InSql = InSql + "?,";
		}
		buffer.append(InSql.substring(0, InSql.length() - 1));
		buffer.append(")");
		jdbcTemplate.update(buffer.toString(),delectArr);
		return "1";
	}

	@Override
	public String importRecord(List<Generator> list) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public int getTotalCount() {
		// TODO Auto-generated method stub
		
		String Sql="select count(1) from shiro.generator_data";
		
		return jdbcTemplate.queryForInt(Sql);
	}



	@Override
	public List<Map<String, Object>> queryPlant() {
		String sql ="select id code ,plant_name value from electricpowerplant_data  order by id desc";
		List<Map<String, Object>> list =  this.jdbcTemplate.queryForList(sql);
		return list;
	}
}
