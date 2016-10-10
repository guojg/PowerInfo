package com.github.totalquantity.prepareData.dao;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.github.totalquantity.prepareData.entity.PrepareData;
/**
 * 数据准备与数据库的操作
 * @author guo
 *
 */
public interface PrepareDataDao {
	/**
	 * 通过指标查询准备数据
	 * @param obj
	 * @return
	 */
	public List<PrepareData> getPrepareDataByIndexType(JSONObject obj);
	/**
	 * 查询所有指标准备数据
	 * @param obj
	 * @return
	 */
	public List<PrepareData> getAllPrepareData(JSONObject obj);
	/**
	 * 展示页面的数据
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> queryData(JSONObject param);
	public void saveData(JSONArray rows, String object);
	public String deleteRecord(String delectArr[]) ;


}
