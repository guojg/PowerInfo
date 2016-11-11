package com.github.regionalanalysis.db.constantcostarg.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.common.util.JsonUtils;
import com.github.common.util.NewSnUtil;
import com.github.regionalanalysis.common.dao.TotalDataAnalysisDao;
import com.github.regionalanalysis.db.constantcostarg.dao.ConstantCostDbArgDao;
import com.github.regionalanalysis.db.constantcostarg.entity.ConstantCostArg;

import net.sf.json.JSONObject;

@Service
public class ConstantCostDbArgServiceImpl implements ConstantCostDbArgService {

	@Autowired
	private ConstantCostDbArgDao constantCostDbArgDao;
	@Autowired
	private TotalDataAnalysisDao totalDataAnalysisDao;
	
	@Override
	public String  saveData(Map m,String organ,String taskid) {
		// TODO Auto-generated method stub
		List<ConstantCostArg> list = this.mapToList(m,organ,taskid);
		Long jz_id = Long.parseLong(list.get(0).getJz_id()) ;
		String result = constantCostDbArgDao.save(list);
		Integer area_id=Integer.parseInt(organ);
		Long task_id=Long.parseLong(taskid);

		try {
			//totalDataAnalysisDao.totalDatadCompare(jz_id,area_id,task_id);
			//totalDataAnalysisDao.totalDataPlantCompare(constantCostDbArgDao.getPlantByJz(jz_id.toString(),task_id.toString()),area_id,task_id);
			totalDataAnalysisDao.fdcSaveTotalDb(constantCostDbArgDao.getPlantByJz(jz_id.toString(),taskid), area_id,task_id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	private List<ConstantCostArg> mapToList(Map map,String organ,String taskid){
		List<ConstantCostArg> list = new ArrayList<ConstantCostArg>();
		JSONObject obj = new JSONObject();
		 Iterator entries = map.entrySet().iterator();
		 Map.Entry entry;
		 String name = "";
		 String value = "";
		 String jz_id = NewSnUtil.getID();
		 while (entries.hasNext()) {
		  entry = (Map.Entry) entries.next();
		   name = (String) entry.getKey();
		  Object valueObj = entry.getValue();
		  if(null == valueObj){
		   value = "";
		  }else if(valueObj instanceof String[]){
			   String[] values = (String[])valueObj;
			   for(int i=0;i<values.length;i++){
			    value = values[i] + ",";
			   }
			   value = value.substring(0, value.length()-1);
		  }else{
			   value = valueObj.toString();
		  }
		  ConstantCostArg c = new ConstantCostArg();
		  c.setIndex_type(name);
		  c.setIndex_value(value);
		  if("11".equals(name)&& !"".equals(value)){
			  jz_id = value ;
		  }
		 // c.setJz_id(jz_id);
		  list.add(c);	  
		 }
		 for(ConstantCostArg c : list){
			 c.setJz_id(jz_id);
			 if("11".equals(c.getIndex_type())){
				 c.setIndex_value(jz_id);
			 }
			 c.setArea_id(organ);
			 c.setTask_id(taskid);
		 }
		 return list ;
	}

	@Override
	public String initData(String id,String task_id) {
		List<ConstantCostArg> list = constantCostDbArgDao.getDataById(id,task_id);
		JSONObject obj = new JSONObject();
		for (ConstantCostArg cp : list){
			obj.put(cp.getIndex_type(), cp.getIndex_value()) ;
		}
		return obj .toString();
	}

	@Override
	public String getPlant(String area_id,String task_id) {
		List<Map<String, Object>>   list =  constantCostDbArgDao.queryPlant(area_id,task_id);
		return JsonUtils.transformListToJson(list);
	}
	
	


}
