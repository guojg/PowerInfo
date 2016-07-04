package com.github.menu.service;

import org.springframework.stereotype.Service;

import net.sf.json.JSONObject;

public interface MenuService {


	public String queryAccordion(JSONObject param);
	public String queryMenu(JSONObject param);
}
