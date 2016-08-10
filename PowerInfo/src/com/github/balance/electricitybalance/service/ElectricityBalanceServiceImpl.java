package com.github.balance.electricitybalance.service;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.balance.electricitybalance.dao.ElectricityBalanceDao;
import com.github.common.util.JsonUtils;

import net.sf.json.JSONObject;

@Service
public class ElectricityBalanceServiceImpl implements ElectricityBalanceService {

	@Autowired
	private ElectricityBalanceDao electricityBalanceDao;
	@Override
	public String queryData(JSONObject param) {
		List<Map<String, Object>> list = electricityBalanceDao.queryData(param);
		return JsonUtils.listTranJsonByQuery(list);
	}
	@Override
	public String extractData(JSONObject obj) {
		int  result =  electricityBalanceDao.extractData(obj);
		return null;
	}
}
