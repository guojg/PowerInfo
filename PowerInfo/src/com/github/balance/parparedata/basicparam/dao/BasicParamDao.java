package com.github.balance.parparedata.basicparam.dao;


import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.github.totalquantity.totaldata.entity.TotalData;
/**
 * 结果与数据库相关操作
 * @author guo
 *
 */
public interface BasicParamDao {
	
	
	/**
	 * 查询数据
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> queryData();

	public void save(JSONArray rows);

	public void saveYr(String string, String string2);

	public void saveByl(String string);

	public List<Map<String, Object>> initData();
	public int countData();
	
}
