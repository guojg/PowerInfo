package com.github.basicData.dao;

import java.util.List;
import java.util.Map;

import com.github.basicData.model.BasicIndex;
import com.github.basicData.model.BasicYear;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public interface BasicDataDao {
	
	/**
	 * 查询业务表数据
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> queryData(JSONObject param) throws Exception;
	/**
	 * 保存业务数据
	 * @return
	 */
	public String saveData(JSONArray array) throws Exception;
	public List<Map<String, Object>> addLeaf(JSONObject param)throws Exception;
	public String updatLeaf(JSONObject param)throws Exception;
	public String deleteLeaf(String id)throws Exception;	
	public String addYear(JSONObject param) throws Exception;
	public List<BasicYear> getYears() throws Exception;
	public List<BasicIndex> getIndexs(String pid) throws Exception;
	public String isOnly(String name) throws Exception;
	public List getUnits(String pid) throws Exception;
	public String updateUnit(JSONObject param)throws Exception;
	public String addUnit(JSONObject param)throws Exception;
	public List<Map<String, Object>> queryUnits() throws Exception;
	public List<BasicYear> getYearsBycondition(int year) throws Exception;


}
