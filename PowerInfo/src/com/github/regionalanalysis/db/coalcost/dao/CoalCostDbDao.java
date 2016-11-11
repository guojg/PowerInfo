package com.github.regionalanalysis.db.coalcost.dao;

import java.util.List;
import java.util.Map;


import net.sf.json.JSONObject;


public interface CoalCostDbDao {
	
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
	
	public Integer getDcByFdj(String fdj_id,String task_id) throws Exception;
	
	public void sumData(String fdj_id,String task_id) throws Exception ;
	
}
