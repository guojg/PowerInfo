package com.github.menu.dao;

import java.util.List;
import java.util.Map;

public interface MenuDao {
	
	public List<Map<String, Object>> queryAccordion(List<String> years);

	public List<Map<String, Object>> queryMenu(List<String> years);
}
