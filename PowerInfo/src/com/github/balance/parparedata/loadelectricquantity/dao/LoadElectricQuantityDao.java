package com.github.balance.parparedata.loadelectricquantity.dao;

import java.util.List;
import java.util.Map;

import com.github.basicData.model.BasicIndex;
import com.github.basicData.model.BasicYear;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public interface LoadElectricQuantityDao {
	
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
	public String saveData(JSONObject param) throws Exception;
	
	public String totalData(String taskid) throws Exception;
}
