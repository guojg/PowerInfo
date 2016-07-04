package com.github.totalquantity.totaldata.dao;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.github.totalquantity.totaldata.entity.TotalData;
/**
 * 结果与数据库相关操作
 * @author guo
 *
 */
public interface TotalDataDao {
	/**
	 * 保存算法数据值
	 * @param data
	 */
	public void saveData( List<TotalData> data) ;
	
	/**
	 * 查询数据
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> queryData(JSONObject param);
}
