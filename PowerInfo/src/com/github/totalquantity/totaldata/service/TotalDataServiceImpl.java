package com.github.totalquantity.totaldata.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.sf.json.JSONObject;

import com.github.common.util.JsonUtils;
import com.github.totalquantity.totaldata.dao.TotalDataDao;
import com.github.totalquantity.totaldata.entity.TotalData;

@Service
public class TotalDataServiceImpl implements TotalDataService {

	@Autowired
	private TotalDataDao totalDataDao ;
	@Override
	public String queryData(JSONObject param) {
		List<Map<String, Object>> list = totalDataDao.queryData(param);
		return JsonUtils.listTranJsonByQuery(list);
	}

}
