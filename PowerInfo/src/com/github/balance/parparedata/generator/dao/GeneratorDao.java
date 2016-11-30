package com.github.balance.parparedata.generator.dao;

import java.util.List;
import java.util.Map;

import com.github.balance.parparedata.generator.model.Generator;

import net.sf.json.JSONObject;


public interface GeneratorDao {
	

	public List<Map<String, Object>> queryData(JSONObject param) throws Exception;
	public String addRecord(final Generator generator) throws Exception;
	public String updateRecord(Generator generator) throws Exception;	
	public String deleteRecord(String delectArr[]) throws Exception;
	public String importRecord(List<Generator> list) throws Exception;
	
	public int getTotalCount();
	public void sumPlantCap(String id);
	public List<Map<String, Object>> queryPlant();
	public List<Map<String, Object>> getDylxByPlantId(String  plant_id) throws Exception;	
}
