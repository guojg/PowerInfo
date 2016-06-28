package com.github.basicData.service;

import java.util.List;
import java.util.Map;

import com.github.basicData.model.BasicYear;

import net.sf.json.JSONObject;

public interface BasicDataService {

	public String queryData(JSONObject param);

	public String saveData(JSONObject param) throws Exception;

	public List<Map<String, Object>> addLeaf(JSONObject param) throws Exception;

	public String updatLeaf(JSONObject param) throws Exception;

	public String deleteLeaf(String id) throws Exception;
	
	public String addYear(JSONObject param) throws Exception;
	
	public List<BasicYear> getYears() throws Exception;
}
