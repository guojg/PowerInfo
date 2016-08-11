package com.github.balance.parparedata.senddata.service;


import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.balance.parparedata.electricpowerplant.model.PowerPlant;
import com.github.balance.parparedata.senddata.dao.SendDataDao;
import com.github.balance.parparedata.senddata.model.Domain;
import com.github.balance.parparedata.senddata.model.SendItemName;
import com.github.common.util.JsonUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class SendDataServiceImpl implements  SendDataService{

	@Autowired
	private SendDataDao sendData;

	@Override
	public String queryData(JSONObject param) throws Exception {
		List<Map<String, Object>> list = sendData.queryData(param);
		return JsonUtils.listTranJsonByQuery(list);
	}

	@Override
	@Transactional
	public String saveData(JSONObject param) throws Exception {
		// TODO Auto-generated method stub

		return sendData.saveData(param);
	}
	
	

	@Override
	public String deleteData(String[] ids) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String addProData(JSONObject obj) throws Exception {
		// TODO Auto-generated method stub
		return sendData.addProData(createModle(obj));
	}
	private SendItemName createModle(JSONObject obj){
		SendItemName p=new SendItemName();
		JSONObject data=null;
		if(obj!=null){
			 data=JSONObject.fromObject(obj.get("data"));
		}
		p.setTask_id(data.getString("task_id"));
		p.setPid(data.getString("pid"));
		p.setPro_name(data.getString("pro_name"));
		return p;
	}

	@Override
	public List<Domain> getTypes() throws Exception {
		// TODO Auto-generated method stub
		return sendData.getTypes();
	}



}
