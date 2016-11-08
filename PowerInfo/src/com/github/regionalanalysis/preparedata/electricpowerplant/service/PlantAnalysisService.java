package com.github.regionalanalysis.preparedata.electricpowerplant.service;


import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
public interface PlantAnalysisService {

	public String queryData(JSONObject param) throws Exception;
	public String queryTemplateData(String id) throws Exception;
	public String saveTemplateData(JSONObject param) throws Exception;
	public String addRecord(JSONObject obj) throws Exception;
	public String updateRecord(JSONObject obj) throws Exception;	
	public String deleteRecord(JSONObject obj) throws Exception;
	public String getPlantById(String id) throws Exception;
	public String getFdjByDc(String id) throws Exception;
	public void  ExportExcel(JSONObject param,HttpServletResponse response) throws Exception;

}
