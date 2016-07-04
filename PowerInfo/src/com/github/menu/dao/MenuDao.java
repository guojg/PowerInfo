package com.github.menu.dao;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

public interface MenuDao {
	
	public List<Map<String, Object>> queryAccordion(JSONObject param);

	public List<Map<String, Object>> queryMenu(JSONObject param);
}
