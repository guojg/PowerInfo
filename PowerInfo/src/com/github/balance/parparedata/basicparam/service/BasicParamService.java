package com.github.balance.parparedata.basicparam.service;

import net.sf.json.JSONObject;



public interface BasicParamService {
	
	/**
	 * 查询
	 * @param param
	 * @return
	 */
	
	public String queryData();

	public String saveData(JSONObject jsonobj);

	public String initData();
	public int countData();

}
