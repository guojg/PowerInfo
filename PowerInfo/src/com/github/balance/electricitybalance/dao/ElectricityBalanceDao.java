package com.github.balance.electricitybalance.dao;


import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public interface ElectricityBalanceDao {
	public List<Map<String, Object>> queryData(JSONObject param);
	public List<Map<String, Object>> exportData(JSONObject param);
	/**
	 * 抽取数据
	 * @param obj
	 * @return
	 */
	public int extractData(JSONObject obj);
	
	/**
	 * 保存业务数据
	 * @return
	 */
	public String saveData(JSONArray array,JSONObject obj);
}
