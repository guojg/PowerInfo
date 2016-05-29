package com.github.basicData.nationalEconomy.service;

import org.springframework.stereotype.Service;

import net.sf.json.JSONObject;

public interface NationalEconomyService {

	public String queryData(JSONObject param);
	public String queryMenu(JSONObject param);
}
