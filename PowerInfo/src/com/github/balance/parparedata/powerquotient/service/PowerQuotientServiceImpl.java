package com.github.balance.parparedata.powerquotient.service;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.github.balance.parparedata.powerquotient.dao.PowerQuotientDao;
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
	@Override
	public String saveData(JSONObject param) {
		JSONArray rows = null;
		if (param.get("editObj") != null) {
			rows = JSONArray.fromObject(param.get("editObj"));
		}
		JSONObject obj = new JSONObject();
		obj.put("taskid", param.get("taskid")==null?"":param.get("taskid").toString());
		return powerQuotientDao.saveData(rows,obj);
	}

}
