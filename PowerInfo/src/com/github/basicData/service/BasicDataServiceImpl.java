package com.github.basicData.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.basicData.dao.BasicDataDao;
import com.github.basicData.model.BasicYear;
import com.github.common.util.JsonUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class BasicDataServiceImpl implements BasicDataService {

	@Autowired
	private BasicDataDao basicDataDao;

	@Override
	public String queryData(JSONObject param) {
		List<Map<String, Object>> list = basicDataDao.queryData(param);
		return JsonUtils.listTranJsonByQuery(list);
	}

	@Override
	@Transactional
	public String saveData(JSONObject param) throws Exception {
		// TODO Auto-generated method stub
		JSONArray rows = null;
		if (param.get("editObj") != null) {
			rows = JSONArray.fromObject(param.get("editObj"));
		}
		return basicDataDao.saveData(rows);
	}

	@Override
	@Transactional
	public List<Map<String, Object>> addLeaf(JSONObject param) throws Exception {
		JSONObject obj = null;
		if (param.get("data") != null) {
			obj = JSONObject.fromObject(param.get("data"));
		}
		return basicDataDao.addLeaf(obj);
	}

	@Override
	public String updatLeaf(JSONObject param) throws Exception {
		JSONObject obj = null;
		if (param.get("data") != null) {
			obj = JSONObject.fromObject(param.get("data"));
		}
		;
		return basicDataDao.updatLeaf(obj);
	}

	@Override
	public String deleteLeaf(String id) throws Exception {
		// TODO Auto-generated method stub
		return  basicDataDao.deleteLeaf(id);
	}

	@Override
	public String addYear(JSONObject param) throws Exception {
		// TODO Auto-generated method stub
		JSONObject obj = null;
		if (param.get("data") != null) {
			obj = JSONObject.fromObject(param.get("data"));
		}
		return basicDataDao.addYear(obj);
	}

	@Override
	public List<BasicYear> getYears() throws Exception{
		// TODO Auto-generated method stub
		
		return basicDataDao.getYears();
	}

}
