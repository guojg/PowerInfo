package com.github.regionalanalysis.preparedata.coalcost.service;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
public interface CoalCostService {

	public String queryData(JSONObject param) throws Exception;
	public String saveData(JSONObject param) throws Exception;
	public void  ExportExcel(JSONObject param,HttpServletResponse response) throws Exception;

}
