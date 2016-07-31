package com.github.balance.powerbalance.service;


import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.github.common.util.JsonUtils;

public interface PowerBalanceService {
	
	/**
	 * 查询
	 * @param param
	 * @return
	 */
	
	public String queryData(JSONObject param);
}
