package com.github.regionalanalysis.preparedata.constantcostarg.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.balance.powerbalance.dao.PowerBalanceDao;
import com.github.common.export.ExcelParams;
import com.github.common.export.ExcelUtils;
import com.github.common.export.rules.CellEqualMergeRules;
import com.github.common.export.rules.MergeRules;
import com.github.common.util.JsonUtils;
import com.github.common.util.NewSnUtil;
import com.github.regionalanalysis.preparedata.coalcost.dao.CoalCostDao;
import com.github.regionalanalysis.preparedata.constantcostarg.dao.ConstantCostArgDao;
import com.github.regionalanalysis.preparedata.constantcostarg.entity.ConstantCostArg;
import com.github.totalquantity.calculatePlan.entity.CalculatePlan;

import net.sf.json.JSONObject;

@Service
public class ConstantCostArgServiceImpl implements ConstantCostArgService {

	@Autowired
	private ConstantCostArgDao constantCostArgDao;
	@Autowired
	private CoalCostDao coalCostDao;
	
	@Override
	public String  saveData(Map m) {
		// TODO Auto-generated method stub
		List<ConstantCostArg> list = this.mapToList(m);
		Long jz_id = Long.parseLong(list.get(0).getJz_id()) ;
		String result = constantCostArgDao.save(list);
		try {
			coalCostDao.totalData(jz_id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	private List<ConstantCostArg> mapToList(Map map){
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
		 }
		 return list ;
	}

	@Override
	public String initData(String id) {
		List<ConstantCostArg> list = constantCostArgDao.getDataById(id);
		JSONObject obj = new JSONObject();
		for (ConstantCostArg cp : list){
			obj.put(cp.getIndex_type(), cp.getIndex_value()) ;
		}
		return obj .toString();
	}

	@Override
	public String getPlant() {
		List<Map<String, Object>>   list =  constantCostArgDao.queryPlant();
		return JsonUtils.transformListToJson(list);
	}		
	

}
