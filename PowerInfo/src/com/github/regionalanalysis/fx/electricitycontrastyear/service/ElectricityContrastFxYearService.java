package com.github.regionalanalysis.fx.electricitycontrastyear.service;


import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
public interface ElectricityContrastFxYearService {

	public String queryData(JSONObject param) ;
	public void  ExportExcel(JSONObject param,HttpServletResponse response) throws Exception;

}
