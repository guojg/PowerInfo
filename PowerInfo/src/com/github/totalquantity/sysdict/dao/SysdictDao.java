package com.github.totalquantity.sysdict.dao;

import java.util.List;
import java.util.Map;





import com.github.totalquantity.sysdict.entity.Sysdict;

import net.sf.json.JSONObject;

public interface SysdictDao {

	public List<Sysdict>  queryData(JSONObject obj );
	public List<Map<String, Object>> queryDataByMap(JSONObject obj);
	public List<Sysdict> queryDataNotCondition(JSONObject obj);
}
