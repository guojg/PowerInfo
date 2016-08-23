package com.github.totalquantity.prepareData.service;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

public interface PrepareDataService {
	
	/**
	 * 查询
	 * @param param
	 * @return
	 */
	
	public String queryData(JSONObject param);

	public void saveData(JSONObject jsonobj);
	public void  ExportExcel(JSONObject param,HttpServletResponse response) throws Exception;

}
