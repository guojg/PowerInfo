package com.github.regionalanalysis.preparedata.constantcostarg.service;


import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.github.common.util.JsonUtils;
import com.github.totalquantity.task.entity.TotalTask;

public interface ConstantCostArgService {
	
	/**
	 * 保存
	 * @param m
	 */
	public void saveData(Map m) ;
	/**
	 * 初始化
	 * @param id
	 * @return
	 */
	public String initData(String id);
	/**
	 * 获得电厂和电厂id
	 * @return
	 */
	public String getPlant();
}
