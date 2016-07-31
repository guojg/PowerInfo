package com.github.balance.powerbalance.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.balance.powerbalance.dao.PowerBalanceDao;
import com.github.common.util.JsonUtils;

import net.sf.json.JSONObject;

@Service
public class PowerBalanceServiceImpl implements PowerBalanceService {

	@Autowired
	private PowerBalanceDao powerBalanceDao;
	@Override
	public String queryData(JSONObject param) {
		List<Map<String, Object>> list = powerBalanceDao.queryData(param);
		return JsonUtils.listTranJsonByQuery(list);
	}

}
