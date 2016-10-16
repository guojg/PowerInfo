package com.github.basicData.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.github.basicData.model.BasicIndex;
import com.github.basicData.model.BasicYear;

import net.sf.json.JSONObject;

public interface BasicDataService {

	public String queryData(JSONObject param) throws Exception;
	public String queryUnits() throws Exception;

	public String saveData(JSONObject param) throws Exception;

	public List<Map<String, Object>> addLeaf(JSONObject param) throws Exception;

	public String updatLeaf(JSONObject param) throws Exception;

	public String deleteLeaf(String id) throws Exception;
	
	public String addYear(JSONObject param) throws Exception;
	
	public List<BasicYear> getYears() throws Exception;
	
	public List<BasicIndex> getIndexs(String pid) throws Exception;
	
	public void  ExportExcel(JSONObject param,HttpServletResponse response) throws Exception;
	
	public String isOnly(String name) throws Exception;
	public String getUnits(String pid) throws Exception;
	public String updatUnit(JSONObject param) throws Exception;
	
	public String addUnit(JSONObject param) throws Exception;
	
}
