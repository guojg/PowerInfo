package com.github.balance.powerquotient.service;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.sf.json.JSONObject;

import com.github.balance.powerquotient.dao.PowerQuotientDao;
import com.github.common.util.JsonUtils;

@Service
public class PowerQuotientServiceImpl implements PowerQuotientService {

	@Autowired
	private PowerQuotientDao powerQuotientDao ;
	@Override
	public String queryData(JSONObject param) {
		List<Map<String, Object>> list = powerQuotientDao.queryData(param);
		return JsonUtils.listTranJsonByQuery(list);
	}

}
