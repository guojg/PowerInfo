package com.github.regionalanalysis.db.generatorcontrast.service;


import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
public interface GeneratorContrastDbService {

	public String queryData(JSONObject param) ;
	public void  ExportExcel(JSONObject param,HttpServletResponse response) throws Exception;

}
