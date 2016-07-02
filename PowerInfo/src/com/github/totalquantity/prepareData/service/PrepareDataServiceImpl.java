package com.github.totalquantity.prepareData.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.common.util.JsonUtils;
import com.github.totalquantity.prepareData.dao.PrepareDataDao;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@Service
public class PrepareDataServiceImpl implements PrepareDataService {
	@Autowired
	private PrepareDataDao prepareDataDao;
	@Override
	public String queryData(JSONObject param) {
		List<Map<String, Object>> list = prepareDataDao.queryData(param);
		return JsonUtils.listTranJsonByQuery(list);
	}
	@Override
	public void saveData(JSONObject jsonobj) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		JSONArray rows = null;
		if (jsonobj.get("editObj") != null) {
			rows = JSONArray.fromObject(jsonobj.get("editObj"));
		}
		 prepareDataDao.saveData(rows,jsonobj.getString("taskid"));
		
	}

}
