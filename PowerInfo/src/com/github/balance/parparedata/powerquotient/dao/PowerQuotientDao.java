package com.github.balance.parparedata.powerquotient.dao;


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
public interface PowerQuotientDao {
	/**
	 * 保存业务数据
	 * @return
	 */
	public String saveData(JSONArray array,JSONObject obj);
	
	/**
	 * 查询数据
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> queryData(JSONObject param);
}
