package com.github.balance.task.dao;



import java.util.List;
import java.util.Map;

import com.github.balance.task.entity.BalanceTask;
import com.github.balance.task.entity.BalanceYear;

import net.sf.json.JSONObject;


public interface BalanceTaskDao {

	public void saveData(BalanceTask task);

	public List<Map<String, Object>> queryData(JSONObject param);
	public int queryDataCount(JSONObject param);
	public void updateData(BalanceTask task);

	public List<BalanceYear> getYears();

	public List<Map<String, Object>> initData(String id);
	public String deleteRecord(String delectArr[]) throws Exception;
	public String deleteOtherData(String delectArr[]) ;

}
