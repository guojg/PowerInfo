package com.github.totalquantity.task.dao;


import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.github.totalquantity.task.entity.TotalTask;
import com.github.totalquantity.task.entity.TotalYear;

public interface TaskDao {

	public void saveData(TotalTask task);

	public List<Map<String, Object>> queryData(JSONObject param);
	public int queryDataCount(JSONObject param);
	public void updateData(TotalTask task);
	public List<TotalYear> getBaseYears() throws Exception;
	public List<TotalYear> getPlanYears() throws Exception;

	public List<Map<String, Object>> initData(String id);


	
}
