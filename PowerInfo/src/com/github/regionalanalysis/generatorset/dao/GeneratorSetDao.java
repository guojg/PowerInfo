package com.github.regionalanalysis.generatorset.dao;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;




public interface GeneratorSetDao {
	
	
	public List<Map<String, Object>> queryData(JSONObject param);
	
	public int queryDataCount(JSONObject param);
	
	public List<Map<String, Object>> queryAllData(JSONObject param);
 
}
