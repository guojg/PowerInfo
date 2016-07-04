package com.github.totalquantity.sysdict.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.sf.json.JSONObject;

import com.github.common.util.JsonUtils;
import com.github.totalquantity.sysdict.dao.SysdictDao;
import com.github.totalquantity.sysdict.entity.Sysdict;
@Service
public class SysdictServiceImpl implements SysdictService {
	@Autowired
	private SysdictDao sysdictDao;
	@Override
	public String queryData(JSONObject obj) {
		// TODO Auto-generated method stub
		List<Sysdict>  list =  sysdictDao.queryData(obj);
		return JsonUtils.sysDictListTranJson(list);
	}

}
