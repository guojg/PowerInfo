package com.github.balance.parparedata.basicparam.service;


import java.util.List;
import java.util.Map;






import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;







import com.github.balance.parparedata.basicparam.dao.BasicParamDao;
import com.github.common.util.JsonUtils;

@Service
public class BasicParamServiceImpl implements BasicParamService {

	@Autowired
	private BasicParamDao basicParamDao ;
	@Override
	public String queryData() {
		List<Map<String, Object>> list = basicParamDao.queryData();
		return JsonUtils.listTranJsonByQuery(list);
	}
	@Override
	public String saveData(JSONObject jsonobj) {
		JSONArray rows = null;
		if (jsonobj.get("editObj") != null) {
			rows = JSONArray.fromObject(jsonobj.get("editObj"));
			basicParamDao.save(rows);
		}
		if(jsonobj.get("startyear") != null &&jsonobj.get("stopyear") != null){
			basicParamDao.saveYr(jsonobj.get("startyear").toString(),jsonobj.get("stopyear").toString());
		}
		if(jsonobj.get("byl") != null){
			basicParamDao.saveByl(jsonobj.get("byl").toString());
		}
		return "1";
	}
	@Override
	public String initData() {
		List<Map<String, Object>> list = basicParamDao.initData();
		return JsonUtils.listTranJson(list);
	}
	@Override
	public int countData(){
		return basicParamDao.countData();
	}

}
