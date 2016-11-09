package com.github.regionalanalysis.fx.generatorcontrastyear.dao;


import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;


public interface GeneratorContrastFxYearDao {
	
	/**
	 * 查询业务表数据
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> queryData(JSONObject param);

	public List<Map<String, Object>> queryDataPie();
	
}
