package com.github.totalquantity.calculatePlan.service;

import net.sf.json.JSONArray;


public interface CalculatePlanService {
	/**
	 * 保存
	 */
	public void saveData(JSONArray array) ;
	
	
	/**
	 * 启动计算
	 */
	public void startCalculate() ;
}
