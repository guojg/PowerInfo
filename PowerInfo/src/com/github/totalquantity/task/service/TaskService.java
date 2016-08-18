package com.github.totalquantity.task.service;


import java.util.List;

import com.github.basicData.model.BasicYear;
import com.github.totalquantity.task.entity.TotalTask;
import com.github.totalquantity.task.entity.TotalYear;

import net.sf.json.JSONObject;

public interface TaskService {

	public void saveData(TotalTask task);
	/**
	 * 查询
	 * @param param
	 * @return
	 */
	
	public String queryData(JSONObject param);
	public List<TotalYear> getBaseYears() throws Exception;

	public List<TotalYear> getPlanYears() throws Exception;
	public String initData(String id);
	public String deleteRecord(JSONObject obj) throws Exception;


}
