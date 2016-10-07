package com.github.regionalanalysis.fx.task.service;


import com.github.regionalanalysis.db.task.entity.DbTask;


import net.sf.json.JSONObject;

public interface TaskFxService {

	public void saveData(DbTask task);
	/**
	 * 查询
	 * @param param
	 * @return
	 */
	
	public String queryData(JSONObject param);

	public String initData(String id);
	public String deleteRecord(JSONObject obj) throws Exception;


}
