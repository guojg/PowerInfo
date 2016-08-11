package com.github.balance.parparedata.hinderedidleCapacity.service;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.balance.parparedata.hinderedidleCapacity.dao.HinderedIdleCapacityDao;
import com.github.balance.parparedata.loadelectricquantity.dao.LoadElectricQuantityDao;
import com.github.common.util.JsonUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class HinderedIdleCapacityServiceImpl implements  HinderedIdleCapacityService{

	@Autowired
	private HinderedIdleCapacityDao hinderedIdleCapacityDao;

	@Override
	public String queryData(JSONObject param) throws Exception {
		List<Map<String, Object>> list = hinderedIdleCapacityDao.queryData(param);
		return JsonUtils.listTranJsonByQuery(list);
	}

	@Override
	@Transactional
	public String saveData(JSONObject param) throws Exception {
		// TODO Auto-generated method stub
		return hinderedIdleCapacityDao.saveData(param);
	}

}
