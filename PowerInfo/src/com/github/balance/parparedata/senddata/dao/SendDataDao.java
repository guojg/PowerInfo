package com.github.balance.parparedata.senddata.dao;

import java.util.List;
import java.util.Map;

import com.github.balance.parparedata.senddata.model.Domain;
import com.github.balance.parparedata.senddata.model.SendItemName;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public interface SendDataDao {
	
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
	public String saveData(JSONArray array) throws Exception;
	
	public String addProData(final SendItemName sendItemName)throws Exception;
	
	public String deleteProData(String ids)throws Exception;
	
	public List<Domain> getTypes()throws Exception;
}
