package com.github.totalquantity.prepareData.service;

import net.sf.json.JSONObject;

public interface PrepareDataService {
	
	/**
	 * 查询
	 * @param param
	 * @return
	 */
	
	public String queryData(JSONObject param);

	public void saveData(JSONObject jsonobj);
}
