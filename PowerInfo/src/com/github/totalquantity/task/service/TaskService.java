package com.github.totalquantity.task.service;


import com.github.totalquantity.task.entity.TotalTask;

import net.sf.json.JSONObject;

public interface TaskService {

	public void saveData(TotalTask task);
	/**
	 * 查询
	 * @param param
	 * @return
	 */
	
	public String queryData(JSONObject param);

}