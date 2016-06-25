package com.github.totalquantity.calculatePlan.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.totalquantity.calculateAlgorithm.CalculateAlgorithm;
import com.github.totalquantity.calculatePlan.dao.CalculatePlanDao;
import com.github.totalquantity.calculatePlan.entity.CalculatePlan;
import com.github.totalquantity.common.Containts;
/**
 * 算法输入参数保存业务层
 * @author guo
 *
 */
@Service
public class CalculatePlanServiceImpl implements CalculatePlanService {
	@Autowired
	private CalculatePlanDao calculatePlanDao;
	/**
	 * 保存
	 */
	@Override
	public void saveData(JSONArray array) {
		
		List<CalculatePlan> list =  this.getCalculatePlanList(array);
		calculatePlanDao.saveData(list);
	}
	
	/**
	 * 将前台的参数数组组装成“参数（即CalculatePlan）”对象集合
	 * @param array
	 * @return
	 */
	private List<CalculatePlan>  getCalculatePlanList(JSONArray array){
		List<CalculatePlan> list = new ArrayList<CalculatePlan>();
		for(int i=0 ; i< array.size() ;++i){
			JSONObject obj = (JSONObject)array.get(i);//每个算法的相关参数值
			 Iterator it = obj.keys(); 
			String algorithm="";   //算法代号
			String taskid="111";	//任务号
			  while (it.hasNext()) { 
				  CalculatePlan cp = new CalculatePlan();
				  String key = it.next().toString(); 
				  String value = obj.getString(key);
				  if(key.startsWith("algorithm")){					 
					  algorithm= value;
					  continue;
				  }
				  cp.setTaskid(taskid); 
				  cp.setAlgorithm(algorithm);
				  cp.setIndex_type(key);   //输入key
				  cp.setIndex_value(value);//输入值
				 list.add(cp) ;
			  } 
		}
		return list;
	}

	@Override
	public void startCalculate() {
		
		List<CalculatePlan> list = calculatePlanDao.getDataBytask("111");
		List<CalculatePlan> list1 = new ArrayList<CalculatePlan>();//算法一
		List<CalculatePlan> list2 = new ArrayList<CalculatePlan>();//算法2
		List<CalculatePlan> list3 = new ArrayList<CalculatePlan>();//算法3
		List<CalculatePlan> list4 = new ArrayList<CalculatePlan>();//算法4
		List<CalculatePlan> list5 = new ArrayList<CalculatePlan>();//算法5
		List<CalculatePlan> list6 = new ArrayList<CalculatePlan>();//算法6
		
		for(CalculatePlan c : list){
			switch(c.getAlgorithm()){
				case Containts.calculate1:
					list1.add(c);
					break;
				case Containts.calculate2:
					list2.add(c);
					break;
				case Containts.calculate3:
					list3.add(c);
					break;
				case Containts.calculate4:
					list4.add(c);
					break;
				case Containts.calculate5:
					list5.add(c);
					break;
				case Containts.calculate6:
					list6.add(c);
					break;
			}	 
		}
		Map<String,List<CalculatePlan>> m = new HashMap<String,List<CalculatePlan>>();
		m.put(Containts.calculate1, list1);
		m.put(Containts.calculate2, list2);
		m.put(Containts.calculate3, list3);
		m.put(Containts.calculate4, list4);
		m.put(Containts.calculate5, list5);
		m.put(Containts.calculate6, list6);
		calculateCondition(m) ;
		
	}
	/**
	 * 计算情况
	 * @param m
	 */
	public void calculateCondition(Map<String,List<CalculatePlan>> m){
		for (String key : m.keySet()) {
			switch(key){
			case Containts.calculate1:
				CalculateAlgorithm.averageGrowthRate(m.get(key));
				break;
			case Containts.calculate2:
				break;
			case Containts.calculate3:
				break;
			case Containts.calculate4:
				break;
			case Containts.calculate5:
				break;
			case Containts.calculate6:
				break;
		}	 
		}
		
		
	}
}
