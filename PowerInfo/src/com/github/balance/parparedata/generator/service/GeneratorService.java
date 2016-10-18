package com.github.balance.parparedata.generator.service;


import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
public interface GeneratorService {

	public String queryData(JSONObject param) throws Exception;
	public String addRecord(JSONObject obj) throws Exception;
	public String updateRecord(JSONObject obj) throws Exception;	
	public String deleteRecord(JSONObject obj) throws Exception;
	public void  ExportExcel(JSONObject param,HttpServletResponse response) throws Exception;
	public String getPlant();
	public String getDylxByPlantId(JSONObject obj) throws Exception;	

}
