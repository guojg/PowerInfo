package com.github.basicData.nationalEconomy.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;





import org.springframework.stereotype.Service;

import com.github.basicData.nationalEconomy.dao.NationalEconomyDao;
import com.github.common.util.JsonUtils;

import net.sf.json.JSONObject;
@Service
public class NationalEconomyServiceImpl implements NationalEconomyService{

	@Autowired
	private NationalEconomyDao nationalEconomyDao;
	@Override
	public String queryData(JSONObject param) {
		List<String> years = new ArrayList<String>();
		years.add("2005");
		years.add("2006");
		List<Map<String, Object>> list = nationalEconomyDao.queryData(years);
		return JsonUtils.listTranJsonByQuery(list);
	}
	@Override
	public String queryMenu(JSONObject param) {
		// TODO Auto-generated method stub
		return null;
	}

}
