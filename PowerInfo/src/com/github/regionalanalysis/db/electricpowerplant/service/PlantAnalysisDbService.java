package com.github.regionalanalysis.db.electricpowerplant.service;


import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
public interface PlantAnalysisDbService {

	public String queryData(JSONObject param) throws Exception;
	public String updateRecord(JSONObject obj) throws Exception;	
	public String deleteRecord(JSONObject obj) throws Exception;
	public String getPlantById(String id,String task_id) throws Exception;
	public String getFdjByDc(String id,String task_id) throws Exception;
	public void  ExportExcel(JSONObject param,HttpServletResponse response) throws Exception;

}
