package com.github.balance.parparedata.loadelectricquantity.service;

import net.sf.json.JSONObject;
public interface LoadElectricQuantityService {

	public String queryData(JSONObject param) throws Exception;
	public String saveData(JSONObject param) throws Exception;

}
