package com.github.regionalanalysis.db.generatorset.service;


import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;



public interface GeneratorSetDbService {
	

	/**
	 * 查询
	 * @param param
	 * @return
	 */
	public String queryData(JSONObject param) ;
	public void  ExportExcel(JSONObject param,HttpServletResponse response) throws Exception;
	public void deleteData(JSONObject jsonobj);

}
