package com.github.balance.parparedata.electricpowerplant.dao;


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
import com.github.basicData.model.BasicData;
import com.github.common.util.NewSnUtil;
@Repository
public class ElectricPowerPlantDaoImpl implements ElectricPowerPlantDao {
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
		String area_id=param.get("area_id")==null?null:param.get("area_id").toString();
		String flag =param.get("flag")==null?null:param.get("flag").toString();
		int  startNum = psize*(pNum-1);
		int  endNum = psize*pNum;
		StringBuffer buffer=new StringBuffer("SELECT id,plant_name,plant_capacity,(select value from sys_dict_table where domain_id=12 and code=index_item) index_itemname,index_item,");
		buffer.append("area_id,(SELECT value FROM bn_code_company WHERE CODE=area_id) area_name,organ,(SELECT value FROM  shiro.sys_dict_table where domain_id=302 and code=cooling_type ) cooling_name,cooling_type  from shiro.electricpowerplant_data where 1=1");
		if(!"".equals(name)){
			buffer.append(" and plant_name like ?");
			params.add("%"+name+"%");
		}
		if(flag!=null&&!"".equals(flag)){
			buffer.append(" and flag is null");
		}
		if(area_id!=null&&!"".equals(area_id)){
			buffer.append(" and area_id = ?");
			params.add(area_id);
		}
		if(indexs.length>0){
			buffer.append(" and index_item in (");
			String InSql = "";
			for (int i = 0; i < indexs.length; i++) {
				InSql = InSql + "?,";
				params.add(indexs[i]);
			}
			buffer.append(InSql.substring(0, InSql.length() - 1));
			buffer.append(")");
		}
		buffer.append(" ORDER BY CONVERT(plant_name USING gbk) ASC");
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
	public String addRecord(final PowerPlant powerPlant) throws Exception {
		// TODO Auto-generated method stub
		String insertsql = "insert  electricPowerPlant_data" 
				+ "(plant_name,plant_capacity,start_date,end_date,index_item,area_id,organ,cooling_type) VALUES(?,?,null,null,?,?,?,?)";
		PreparedStatementSetter setinsert = new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				// TODO Auto-generated method stub
				ps.setString(1, powerPlant.getPlantName());
				ps.setString(2, powerPlant.getPlantCapacity());
				ps.setString(3, powerPlant.getIndexItem());
				ps.setString(4, powerPlant.getAreaId());
				ps.setString(5, powerPlant.getOrgan());
				ps.setString(6, powerPlant.getCoolingType());
			}

		};
		jdbcTemplate.update(insertsql, setinsert);
		return "1";
	}

	@Override
	public String updateRecord(final PowerPlant powerPlant) throws Exception {
		// TODO Auto-generated method stub
		String insertsql = "update  electricPowerPlant_data" 
				+ " set  plant_name=?,plant_capacity=?,start_date=null,end_date=null,index_item=?,area_id=? ,organ=?,cooling_type=? where id=?";
		PreparedStatementSetter setupdate = new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				// TODO Auto-generated method stub
				ps.setString(1, powerPlant.getPlantName());
				ps.setString(2, powerPlant.getPlantCapacity());
				ps.setString(3, powerPlant.getIndexItem());
				ps.setString(4, powerPlant.getAreaId());
				ps.setString(5, powerPlant.getOrgan());
				ps.setString(6, powerPlant.getCoolingType());
				ps.setString(7, powerPlant.getId());
			}

		};
		jdbcTemplate.update(insertsql, setupdate);
		return "1";
	}

	@Override
	public String deleteRecord(String[] delectArr) throws Exception {
		// TODO Auto-generated method stub
		String InSql = "";
		for (int i = 0; i < delectArr.length; i++) {
			InSql = InSql + "?,";
		}
		//删除应电厂下的机组
		StringBuffer  buffergenerator=new StringBuffer("delete from generator_data where plant_id in(");
		buffergenerator.append(InSql.substring(0, InSql.length() - 1));
		buffergenerator.append(")");
		jdbcTemplate.update(buffergenerator.toString(),delectArr);
		//删除电厂
		StringBuffer  buffer=new StringBuffer("delete from electricPowerPlant_data where id in(");

		buffer.append(InSql.substring(0, InSql.length() - 1));
		buffer.append(")");
		jdbcTemplate.update(buffer.toString(),delectArr);
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
		String indexs[]=null;
		List params = new ArrayList();
		if(param.get("indexs")!=null){
			 indexs=param.getString("indexs").split(",");
		}
		String name=param.getString("name");
		String area_id=param.get("area_id")==null?null:param.get("area_id").toString();
		String flag =param.get("flag")==null?null:param.get("flag").toString();
		StringBuffer buffer=new StringBuffer("select count(1) from shiro.electricpowerplant_data where 1=1");
		if(!"".equals(name)){
			buffer.append(" and plant_name like ?");
			params.add("%"+name+"%");
		}
		if(flag!=null&&!"".equals(flag)){
			buffer.append(" and flag is null");
		}
		if(area_id!=null&&!"".equals(area_id)){
			buffer.append(" and area_id = ?");
			params.add(area_id);
		}
		if(indexs.length>0){
			buffer.append(" and index_item in (");
			String InSql = "";
			for (int i = 0; i < indexs.length; i++) {
				InSql = InSql + "?,";
				params.add(indexs[i]);
			}
			buffer.append(InSql.substring(0, InSql.length() - 1));
			buffer.append(")");
		}
		
		return jdbcTemplate.queryForInt(buffer.toString(),params.toArray());
	}



	@Override
	public String selRecordToAnalysis(String[] idArr) throws Exception {
		// TODO Auto-generated method stub
		String InSql = getInSql(idArr);
		//根据ids查询店里电量平衡表中的纪录
		List<Map<String,Object>> balancePlants=this.getPlantOfBalance(InSql, idArr);
		for(Map<String,Object> balancePlant:balancePlants){
			insertPlantAndGenerator(balancePlant);
		}
		updateFlag(InSql,idArr);
		return "1";
	}
	
	private String getInSql(String[] idArr) throws Exception{
		String InSql = "";
		for (int i = 0; i < idArr.length; i++) {
			InSql = InSql + "?,";
		}
		return InSql;
	}
	private List<Map<String,Object>> getPlantOfBalance(String InSql,String[] idArr) throws Exception{
		StringBuffer  buffer=new StringBuffer();
		buffer.append("SELECT id,plant_name,plant_capacity,area_id,index_item,cooling_type FROM electricpowerplant_data WHERE id in(");
		buffer.append(InSql.substring(0, InSql.length() - 1));
		buffer.append(")");
		return jdbcTemplate.queryForList(buffer.toString(),idArr);
	}
	private void insertPlantAndGenerator(Map<String,Object> plant) throws Exception{
		int id=getMax();
		insertPlant(plant,id);
		List<Map<String,Object>> balancePlants=this.getGeneratorofBalance(plant.get("id").toString());
		if(balancePlants.size()>0){
			insertGenerator(balancePlants,id+1,plant.get("area_id"));
		}
	}
	private void insertPlant(Map<String,Object> plant,int id)throws Exception{
		StringBuffer  buffer=new StringBuffer();
		buffer.append("INSERT INTO shiro.`electricpowerplant_analysis_data`(id,plant_name,plant_capacity,area_id,power_type,Cooling_type,consumption_rate)");
		buffer.append("values(?,?,?,?,?,?,?);");
		String index_item=plant.get("index_item")==null?null:plant.get("index_item").toString();
		String cooling_type=plant.get("Cooling_type")==null?null:plant.get("Cooling_type").toString();
		
		String consumption_rate=null;
		if("3".equals(index_item)){
			consumption_rate=queryTemplateData(index_item+cooling_type).get(0).get("value").toString();
		}else{
			consumption_rate=queryTemplateData(index_item).get(0).get("value").toString();
		}
	    jdbcTemplate.update(buffer.toString(),new Object[]{id+1,plant.get("plant_name"),plant.get("plant_capacity"),plant.get("area_id"),plant.get("index_item"),plant.get("Cooling_type"),consumption_rate});
	}
	private void insertGenerator(List<Map<String,Object>> plangenerator,int plant_id,Object area_id)throws Exception{
		 //获取模板值行业期望收益率（%）、运行寿命（年）、煤耗率（克标煤/千瓦时）
		 List<Map<String, Object>>   items= queryTemplateData("9,10,11");
		 //行业期望收益率（%）
		 String expectedrate=items.get(0).get("value").toString();
		 //运行寿命（年）
		 String operatinglife =items.get(1).get("value").toString();
		 //煤耗率（克标煤/千瓦时）
		 String coalrate=items.get(2).get("value").toString();

		for(Map<String,Object> generator:plangenerator){
			
			String[] sql=new String[8];
			StringBuffer  sql1=new StringBuffer();
			StringBuffer  sql2=new StringBuffer();
			StringBuffer  sql3=new StringBuffer();
			StringBuffer  sql4=new StringBuffer();
			StringBuffer  sql5=new StringBuffer();
			StringBuffer  sql6=new StringBuffer();
			StringBuffer  sql7=new StringBuffer();
//			StringBuffer  sql8=new StringBuffer();
			String jz_id=NewSnUtil.getID();
			sql1.append("INSERT INTO constant_cost_arg(index_type,index_value,jz_id,area_id)");
			sql1.append("values(100,'"+generator.get("gene_name")+"',"+jz_id+","+area_id+")");
			sql2.append("INSERT INTO constant_cost_arg(index_type,index_value,jz_id,area_id)");
			sql2.append("values(300,'"+generator.get("gene_capacity")+"',"+jz_id+","+area_id+")");
			sql3.append("INSERT INTO constant_cost_arg(index_type,index_value,jz_id,area_id)");
			sql3.append("values(200,'"+plant_id+"',"+jz_id+","+area_id+")");
			sql4.append("INSERT INTO constant_cost_arg(index_type,index_value,jz_id,area_id)");
			sql4.append("values(11,'"+jz_id+"',"+jz_id+","+area_id+")");
			sql5.append("INSERT INTO constant_cost_arg(index_type,index_value,jz_id,area_id)");
			sql5.append("values(700,'"+expectedrate+"',"+jz_id+","+area_id+")");
			sql6.append("INSERT INTO constant_cost_arg(index_type,index_value,jz_id,area_id)");
			sql6.append("values(800,'"+operatinglife+"',"+jz_id+","+area_id+")");
			sql7.append("INSERT INTO constant_cost_arg(index_type,index_value,jz_id,area_id)");
			sql7.append("values(18001,'"+coalrate+"',"+jz_id+","+area_id+")");
//			sql8.append("INSERT INTO constant_cost_arg(index_type,index_value,jz_id,area_id)");
//			sql8.append("values(18001,'"+generator.get("index_item")+"',"+jz_id+","+area_id+")");
			sql[0]=sql1.toString();
			sql[1]=sql2.toString();
			sql[2]=sql3.toString();
			sql[3]=sql4.toString();
			sql[4]=sql5.toString();
			sql[5]=sql6.toString();
			sql[6]=sql7.toString();
//			sql[7]=sql8.toString();
			jdbcTemplate.batchUpdate(sql);
		}
	}
	private List<Map<String,Object>> getGeneratorofBalance(String  plantid) throws Exception{
		StringBuffer  buffer=new StringBuffer();
		buffer.append("SELECT gene_name,gene_capacity,index_item FROM generator_data WHERE plant_id=?");
	    return jdbcTemplate.queryForList(buffer.toString(),new Object[]{plantid});
	}
	private int getMax() throws Exception{
		StringBuffer  buffer=new StringBuffer();
		buffer.append("SELECT max(id) from  electricpowerplant_analysis_data");
	    return jdbcTemplate.queryForInt(buffer.toString());
	}
	/**
	 * 修改状态flag表示已经抽取过，不再抽取
	 */
	private void  updateFlag(String InSql,String[] idArr) throws Exception{
		StringBuffer  buffer=new StringBuffer();
		buffer.append("update electricpowerplant_data set flag=1 WHERE id in(");
		buffer.append(InSql.substring(0, InSql.length() - 1));
		buffer.append(")");
		jdbcTemplate.update(buffer.toString(),idArr);
	}
	private List<Map<String, Object>> queryTemplateData(String id) throws Exception {
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
	
	
}
