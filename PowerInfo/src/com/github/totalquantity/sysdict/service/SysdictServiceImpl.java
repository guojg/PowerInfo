package com.github.totalquantity.sysdict.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.github.common.util.JsonUtils;
import com.github.totalquantity.sysdict.dao.SysdictDao;
import com.github.totalquantity.sysdict.entity.Sysdict;
@Service
public class SysdictServiceImpl implements SysdictService {
	@Autowired
	private SysdictDao sysdictDao;
	@Override
	public String queryData(JSONObject obj) {
		// TODO Auto-generated method stub
		List<Sysdict>  list =  sysdictDao.queryData(obj);
		return JsonUtils.sysDictListTranJson(list);
	}
	public String getDataByCodeValue (JSONObject obj) {
		// TODO Auto-generated method stub
		List<Map<String, Object>>   list =  sysdictDao.queryDataByMap(obj);
		return JsonUtils.transformListToJson(list);
	}
	@Override
	public String getBalanceYears(JSONObject obj) {
		// TODO Auto-generated method stub
		String year = obj.getString("year");
		String[] years = year.split(",");
		JSONArray jsonArray = new JSONArray();
		for(String yr : years){
			JSONObject objyr = new JSONObject();
			objyr.put("code", yr);
			objyr.put("value", yr+"年");
			jsonArray.add(objyr);
		}
		return jsonArray.toString();
	}
	@Override
	public String queryDataNotCondition(JSONObject obj) {
		List<Sysdict>  list =  sysdictDao.queryDataNotCondition(obj);
		return JsonUtils.sysDictListTranJson(list);
	}
	@Override
	public String queryCompany(JSONObject obj) {
		List<Map<String, Object>>  list =  sysdictDao.queryCompany(obj);
		return JsonUtils.transformListToJson(list);
	}
	@Override
	public String getBalanceYearExtend() {
		List<Map<String, Object>>   list =  sysdictDao.getBalanceYearExtend();
		String startyear = list.get(0).get("START_YEAR").toString();
		String stopyear = list.get(0).get("STOP_YEAR").toString();
		JSONArray jsonArray = new JSONArray();

		for(int i=Integer.parseInt(startyear);i<=Integer.parseInt(stopyear);++i){
			JSONObject objyr = new JSONObject();
			objyr.put("code",i);
			objyr.put("value", i+"年");
			jsonArray.add(objyr);
		}
		return jsonArray.toString();
	}
	

}
