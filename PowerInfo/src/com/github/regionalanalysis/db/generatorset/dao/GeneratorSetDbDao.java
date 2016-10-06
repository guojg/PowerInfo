package com.github.regionalanalysis.db.generatorset.dao;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;




public interface GeneratorSetDbDao {
	
	
	public List<Map<String, Object>> queryData(JSONObject param);
	
	public int queryDataCount(JSONObject param);
	
	public List<Map<String, Object>> queryAllData(JSONObject param);

	public Object deleteData(String[] delectArr);
 
}
