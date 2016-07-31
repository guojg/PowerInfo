package com.github.balance.parparedata.electricpowerplant.dao;

import java.util.List;
import java.util.Map;

import com.github.balance.parparedata.electricpowerplant.model.PowerPlant;
import com.github.basicData.model.BasicIndex;
import com.github.basicData.model.BasicYear;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public interface ElectricPowerPlantDao {
	

	public List<Map<String, Object>> queryData(JSONObject param) throws Exception;
	public String addRecord(PowerPlant powerPlant) throws Exception;
	public String updateRecord(PowerPlant powerPlant) throws Exception;	
	public String deleteRecord(String delectArr[]) throws Exception;
	public String importRecord(List<PowerPlant> list) throws Exception;
	
}
