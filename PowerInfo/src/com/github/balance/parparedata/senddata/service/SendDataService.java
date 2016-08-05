package com.github.balance.parparedata.senddata.service;

import net.sf.json.JSONObject;
public interface SendDataService {

	public String queryData(JSONObject param) throws Exception;
	public String saveData(JSONObject param) throws Exception;
	public String deleteData(String[] ids) throws Exception;

}
