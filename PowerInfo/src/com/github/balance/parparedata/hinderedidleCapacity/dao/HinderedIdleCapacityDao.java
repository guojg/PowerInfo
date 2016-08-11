package com.github.balance.parparedata.hinderedidleCapacity.dao;

import java.util.List;
import java.util.Map;
import net.sf.json.JSONObject;


public interface HinderedIdleCapacityDao {
	
	/**
	 * 查询业务表数据
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> queryData(JSONObject param) throws Exception;
	/**
	 * 保存业务数据
	 * @return
	 */
	public String saveData(JSONObject obj) throws Exception;
}
