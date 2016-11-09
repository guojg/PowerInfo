package com.github.regionalanalysis.fx.electricitycontrastyear.dao;


import java.util.List;
import java.util.Map;
import net.sf.json.JSONObject;


public interface ElectricityContrastFxYearDao {
	
	/**
	 * 查询业务表数据
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> queryData(JSONObject param);
	
}
