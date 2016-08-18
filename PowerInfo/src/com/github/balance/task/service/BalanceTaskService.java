package com.github.balance.task.service;




import java.util.List;

import com.github.balance.task.entity.BalanceTask;
import com.github.balance.task.entity.BalanceYear;
import com.github.basicData.model.BasicYear;

import net.sf.json.JSONObject;

public interface BalanceTaskService {

	public void saveData(BalanceTask task);
	/**
	 * 查询
	 * @param param
	 * @return
	 */
	
	public String queryData(JSONObject param);
	
	public List<BalanceYear> getYears() throws Exception;
	public String initData(String id);
	public String deleteRecord(JSONObject obj) throws Exception;


}
