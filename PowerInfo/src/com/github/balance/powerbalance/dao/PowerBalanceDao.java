package com.github.balance.powerbalance.dao;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

public interface PowerBalanceDao {
	public List<Map<String, Object>> queryData(JSONObject param);
	/**
	 * 抽取数据
	 * @param obj
	 * @return
	 */
	public int extractData(JSONObject obj);
}
