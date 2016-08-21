package com.github.balance.parparedata.loadelectricquantity.service;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
public interface LoadElectricQuantityService {

	public String queryData(JSONObject param) throws Exception;
	public String saveData(JSONObject param) throws Exception;
	public String totalData(JSONObject param) throws Exception;
	public void  ExportExcel(JSONObject param,HttpServletResponse response) throws Exception;

}
