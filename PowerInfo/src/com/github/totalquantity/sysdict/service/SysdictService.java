package com.github.totalquantity.sysdict.service;



import net.sf.json.JSONObject;


public interface SysdictService {
	public String queryData(JSONObject obj );
	public String getDataByCodeValue(JSONObject obj );
	public String getBalanceYears(JSONObject obj);
}
