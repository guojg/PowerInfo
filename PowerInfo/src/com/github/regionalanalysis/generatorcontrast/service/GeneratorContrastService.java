package com.github.regionalanalysis.generatorcontrast.service;


import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
public interface GeneratorContrastService {

	public String queryData(JSONObject param) ;
	public void  ExportExcel(JSONObject param,HttpServletResponse response) throws Exception;

}
