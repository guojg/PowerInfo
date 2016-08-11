package com.github.balance.parparedata.electricpowerplant.service;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.balance.parparedata.electricpowerplant.dao.ElectricPowerPlantDao;
import com.github.balance.parparedata.electricpowerplant.model.PowerPlant;
import com.github.balance.parparedata.hinderedidleCapacity.dao.HinderedIdleCapacityDao;
import com.github.common.util.JsonUtils;

import net.sf.json.JSONObject;

@Service
public class ElectricPowerPlantServiceImpl implements  ElectricPowerPlantService{

	@Autowired
	private ElectricPowerPlantDao electricPowerPlantDao;

	@Override
	public String queryData(JSONObject param) throws Exception {
		List<Map<String, Object>> list = electricPowerPlantDao.queryData(param);
		return JsonUtils.listTranJsonByPage(list,10);
	}

	@Override
	public String addRecord(JSONObject obj) throws Exception {
		// TODO Auto-generated method stub
		return electricPowerPlantDao.addRecord(createModle(obj));
	}

	@Override
	public String updateRecord(JSONObject obj) throws Exception {
		// TODO Auto-generated method stub
		return electricPowerPlantDao.updateRecord(createModle(obj));
	}

	@Override
	public String deleteRecord(JSONObject obj) throws Exception {
		// TODO Auto-generated method stub
		String delectArr[] = obj.get("deleteids") == null ? null : obj
				.get("deleteids").toString().split(",");
		return electricPowerPlantDao.deleteRecord(delectArr);
	}
	
	private PowerPlant createModle(JSONObject obj){
		PowerPlant p=new PowerPlant();
		JSONObject data=null;
		if(obj!=null){
			 data=JSONObject.fromObject(obj.get("editObj"));
		}
		p.setEndDate(data.getString("end_date"));
		p.setPlantCapacity(data.getString("plant_capacity"));
		p.setPlantName(data.getString("plant_name"));
		p.setStartDate(data.getString("start_date"));
		p.setIndexItem(data.getString("index_item"));
		if(data.get("id")!=null){
			p.setId(data.getString("id"));
		}
		return p;
	}

}
