package com.github.menu.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;





import org.springframework.stereotype.Service;

import com.github.common.tree.TreeUtil;
import com.github.common.util.JsonUtils;
import com.github.menu.dao.MenuDao;

import net.sf.json.JSONObject;
@Service
public class MenuServiceImpl implements MenuService{

	@Autowired
	private MenuDao menuDao;

	@Override
	public String queryAccordion(JSONObject param) {
		List<Map<String, Object>> list = menuDao.queryAccordion(param);
		return JsonUtils.listTranJsonByMenu(list);
	}
	
	
	@Override
	public String queryMenu(JSONObject param) {
		List<Map<String, Object>> list = menuDao.queryMenu(param);
		String aa=TreeUtil.createTreeJson(JsonUtils.transformBoToTree(list)) ;
		return aa;
	}


}
