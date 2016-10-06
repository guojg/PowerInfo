package com.github.regionalanalysis.fx.electricitycontrast.service;


import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
public interface ElectricityContrastFxService {

	public String queryData(JSONObject param) ;
	public void  ExportExcel(JSONObject param,HttpServletResponse response) throws Exception;

}
