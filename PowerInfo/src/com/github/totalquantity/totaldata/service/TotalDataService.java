package com.github.totalquantity.totaldata.service;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.github.basicData.model.BasicYear;
import com.github.common.util.JsonUtils;

public interface TotalDataService {
	
	/**
	 * 查询
	 * @param param
	 * @return
	 */
	
	public String queryData(JSONObject param);
	public List<BasicYear> getYears(JSONObject param) throws Exception;
	/**
	 * 查询建议值和现状数据
	 * @param param
	 * @return
	 */
	
	public String queryData6(JSONObject param);
}
