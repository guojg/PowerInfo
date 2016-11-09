package com.github.regionalanalysis.fx.generatorcontrastyear.service;


import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
public interface GeneratorContrastFxYearService {

	public String queryData(JSONObject param) ;
	public void  ExportExcel(JSONObject param,HttpServletResponse response) throws Exception;
	public String queryDataPie();

}
