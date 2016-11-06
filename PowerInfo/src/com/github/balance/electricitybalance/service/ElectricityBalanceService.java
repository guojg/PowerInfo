package com.github.balance.electricitybalance.service;



import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.github.common.util.JsonUtils;

public interface ElectricityBalanceService {
	
	/**
	 * 查询
	 * @param param
	 * @return
	 */
	
	public String queryData(JSONObject param);
	public String queryCoalHourData(JSONObject param);
	/**
	 * 抽取
	 * @param obj
	 * @return
	 */
	public String extractData(JSONObject obj);
	public void  ExportExcel(JSONObject param,HttpServletResponse response) throws Exception;
	public String saveData(JSONObject param);

}
