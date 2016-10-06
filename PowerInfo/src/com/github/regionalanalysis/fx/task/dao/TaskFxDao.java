package com.github.regionalanalysis.fx.task.dao;


import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.github.regionalanalysis.db.task.entity.DbTask;

public interface TaskFxDao {

	public void saveData(DbTask task);

	public List<Map<String, Object>> queryData(JSONObject param);
	public int queryDataCount(JSONObject param);
	public void updateData(DbTask task);

	public List<Map<String, Object>> initData(String id);
	public String deleteRecord(String delectArr[]) throws Exception;


	
}
