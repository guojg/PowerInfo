package com.github.regionalanalysis.db.generatorcontrastyear.service;


import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
public interface GeneratorContrastDbYearService {

	public String queryData(JSONObject param) ;
	public void  ExportExcel(JSONObject param,HttpServletResponse response) throws Exception;

}
