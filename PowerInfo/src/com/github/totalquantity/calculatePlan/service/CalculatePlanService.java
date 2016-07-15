package com.github.totalquantity.calculatePlan.service;

import com.github.totalquantity.task.entity.TotalTask;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public interface CalculatePlanService {
	/**
	 * 保存
	 */
	public void saveData(JSONArray array,TotalTask task) ;
	
	
	/**
	 * 启动计算
	 */
	public void startCalculate(JSONObject obj) ;


	public String initData(String taskid);
}
