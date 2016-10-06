package com.github.regionalanalysis.fx.electricpowerplant.dao;

import java.util.List;
import java.util.Map;

import com.github.balance.parparedata.electricpowerplant.model.PowerPlant;
import com.github.basicData.model.BasicIndex;
import com.github.basicData.model.BasicYear;
import com.github.regionalanalysis.preparedata.electricpowerplant.model.PlantAnalysis;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public interface PlantAnalysisFxDao {
	public List<Map<String, Object>> queryData(JSONObject param) throws Exception;
	public String updateRecord(final PlantAnalysis powerPlant) throws Exception;	
	public String deleteRecord(String delectArr[],String task_id) throws Exception;
	public String importRecord(List<PowerPlant> list) throws Exception;
	public List<Map<String, Object>> getPlantById(String id,String task_id) throws Exception;
	
	public List<Map<String, Object>> getFdjByDc(String id,String task_id) throws Exception;

	public int getTotalCount(String task_id);
	
}
