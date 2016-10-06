package com.github.regionalanalysis.db.electricitycontrast.dao;


import java.util.List;
import java.util.Map;
import net.sf.json.JSONObject;


public interface ElectricityContrastDbDao {
	
	/**
	 * 查询业务表数据
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> queryData(JSONObject param);
	
}
