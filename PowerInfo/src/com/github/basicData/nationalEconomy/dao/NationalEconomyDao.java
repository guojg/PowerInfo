package com.github.basicData.nationalEconomy.dao;

import java.util.List;
import java.util.Map;

public interface NationalEconomyDao {
	
	public List<Map<String, Object>> queryData(List<String> years);
}
