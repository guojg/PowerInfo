package com.github.regionalanalysis.db.electricitycontrastyear.service;


import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
public interface ElectricityContrastDbYearService {

	public String queryData(JSONObject param) ;
	public void  ExportExcel(JSONObject param,HttpServletResponse response) throws Exception;

}
