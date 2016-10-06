package com.github.regionalanalysis.db.electricitycontrast.service;


import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
public interface ElectricityContrastDbService {

	public String queryData(JSONObject param) ;
	public void  ExportExcel(JSONObject param,HttpServletResponse response) throws Exception;

}
