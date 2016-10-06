package com.github.regionalanalysis.db.constantcostarg.service;


import java.util.Map;



public interface ConstantCostDbArgService {
	
	/**
	 * 保存
	 * @param m
	 * @param area_id 
	 */
	public String saveData(Map m, String area_id) ;
	/**
	 * 初始化
	 * @param id
	 * @return
	 */
	public String initData(String id);
	/**
	 * 获得电厂和电厂id
	 * @param area_id 
	 * @return
	 */
	public String getPlant(String area_id);
}
