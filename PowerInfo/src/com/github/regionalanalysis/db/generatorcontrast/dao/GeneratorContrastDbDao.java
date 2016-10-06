package com.github.regionalanalysis.db.generatorcontrast.dao;


import java.util.List;
import java.util.Map;
import net.sf.json.JSONObject;


public interface GeneratorContrastDbDao {
	
	/**
	 * 查询业务表数据
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> queryData(JSONObject param);
	
}
