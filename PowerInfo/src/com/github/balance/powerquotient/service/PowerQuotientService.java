package com.github.balance.powerquotient.service;


import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.github.common.util.JsonUtils;

public interface PowerQuotientService {
	
	/**
	 * 查询
	 * @param param
	 * @return
	 */
	
	public String queryData(JSONObject param);
}
