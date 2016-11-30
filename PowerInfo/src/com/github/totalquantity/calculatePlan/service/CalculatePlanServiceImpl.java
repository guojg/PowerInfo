package com.github.totalquantity.calculatePlan.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.common.util.MyMath;
import com.github.totalquantity.basedata.dao.BaseDao;
import com.github.totalquantity.basedata.entity.QuoteBase;
import com.github.totalquantity.calculateAlgorithm.service.CalculateAlgorithmService;
import com.github.totalquantity.calculatePlan.dao.CalculatePlanDao;
import com.github.totalquantity.calculatePlan.entity.CalculatePlan;
import com.github.totalquantity.common.Containts;
import com.github.totalquantity.prepareData.dao.PrepareDataDao;
import com.github.totalquantity.prepareData.entity.PrepareData;
import com.github.totalquantity.task.dao.TaskDao;
import com.github.totalquantity.task.entity.TotalTask;
import com.github.totalquantity.totaldata.dao.TotalDataDao;
import com.github.totalquantity.totaldata.entity.TotalData;
/**
 * 算法输入参数保存业务层
 * @author guo
 *
 */
@Service
public class CalculatePlanServiceImpl implements CalculatePlanService {
	@Autowired
	private CalculatePlanDao calculatePlanDao;
	@Autowired
	private PrepareDataDao prepareDataDao;
	@Autowired
	private TotalDataDao totalDataDao;
	@Autowired
	private BaseDao baseDao;
	@Autowired
	private TaskDao taskDao;
	
	
	@Autowired
	private CalculateAlgorithmService calculateAlgorithmService;
	/**
	 * 保存
	 */
	@Override
	public void saveData(JSONArray array,TotalTask task ) {
		
		List<CalculatePlan> list =  this.getCalculatePlanList(array);
		calculatePlanDao.saveData(list);
		taskDao.updateData(task);
	}
	
	/**
	 * 将前台的参数数组组装成“参数（即CalculatePlan）”对象集合
	 * @param array
	 * @return
	 */
	private List<CalculatePlan>  getCalculatePlanList(JSONArray array){
		List<CalculatePlan> list = new ArrayList<CalculatePlan>();
		String taskid="";	//任务号
		for(int i=0 ; i< array.size() ;++i){
			JSONObject obj = (JSONObject)array.get(i);//每个算法的相关参数值
			 Iterator it = obj.keys(); 
			String algorithm="";   //算法代号
			  while (it.hasNext()) { 
				  CalculatePlan cp = new CalculatePlan();
				  String key = it.next().toString(); 
				  String value = obj.getString(key);
				  if(key.startsWith("algorithm")){					 
					  algorithm= value;
					  continue;
				  }
				  if(key.startsWith("taskid")){					 
					  taskid= value;
					  continue;
				  }
				  cp.setTaskid(taskid); 
				  cp.setAlgorithm(algorithm);
				  cp.setIndex_type(key);   //输入key
				  cp.setIndex_value("".equals(value)?null:Double.parseDouble(value));

				 list.add(cp) ;
			  } 
		}
		return list;
	}

	@Override
	public void startCalculate(JSONObject obj) {
		
		List<CalculatePlan> list = calculatePlanDao.getDataBytask(obj.getString("taskid"));
		/*List<CalculatePlan> list1 = new ArrayList<CalculatePlan>();//算法一
		List<CalculatePlan> list2 = new ArrayList<CalculatePlan>();//算法2
		List<CalculatePlan> list3 = new ArrayList<CalculatePlan>();//算法3
		List<CalculatePlan> list4 = new ArrayList<CalculatePlan>();//算法4
		List<CalculatePlan> list5 = new ArrayList<CalculatePlan>();//算法5
		List<CalculatePlan> list6 = new ArrayList<CalculatePlan>();//算法6
		
		for(CalculatePlan c : list){
			switch(c.getAlgorithm()){
				case "1":
					list1.add(c);
					break;
				case "2":
					list2.add(c);
					break;
				case "3":
					list3.add(c);
					break;
				case "4":
					list4.add(c);
					break;
				case "5":
					list5.add(c);
					break;
				case "6":
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
		m.put(Containts.calculate6, list6);*/
		 List<TotalData> resultList = calculateCondition(list,obj) ;
		 totalDataDao.saveData(resultList);
	}
	/**
	 * 计算情况
	 * @param m
	 */
	/*public void calculateCondition(Map<String,List<CalculatePlan>> m){
		double d1=0.0;
		double d2=0.0;
		double d3=0.0;
		double d4=0.0;
		double d5=0.0;
		double d6=0.0;
		List<Map<String,Double>> list = new ArrayList<Map<String,Double>>();
		for (String key : m.keySet()) {
			switch(key){
			case "1":
				d1=calculateAlgorithmService.averageGrowthRate(m.get(key));
				Map<String,Double> m1 = new HashMap<String,Double>();
				m1.put(key, d1);
				break;
			case "2":
				d2 = calculateAlgorithmService.productionValuePerUnitConsumption(m.get(key));
				Map<String,Double> m2 = new HashMap<String,Double>();
				m2.put(key, d2);
				break;
			case "3":
				d3 =calculateAlgorithmService.elasticityCoefficient(m.get(key));
				Map<String,Double> m3 = new HashMap<String,Double>();
				m3.put(key, d3);
				break;
			case "4":
				d4 =calculateAlgorithmService.avgElectricityConsumption(m.get(key));
				Map<String,Double> m4 = new HashMap<String,Double>();
				m4.put(key, d4);
				break;
			case "5":
				d5 =calculateAlgorithmService.avgValue(list);
				break;
			case "6":
				d6 =calculateAlgorithmService.optimalWeight(list, m.get(key));
				break;
		}	 
		}
		
		
	}*/
	/**
	 * 计算
	 * 		平均增长率法：averageGrowthRate
	 * 		产值单耗法:productionValuePerUnitConsumption
	 * 		弹性系数法：elasticityCoefficient
	 * 		人均用电量法：avgElectricityConsumption
	 * 		平均值法:avgValue
	 * 		最优权重法：optimalWeight
	 * @param m
	 */
	public List<TotalData> calculateCondition(List<CalculatePlan>m,JSONObject obj){
		List<PrepareData> prepareData = prepareDataDao.getAllPrepareData(obj) ;//准备数据
		String algorithm = obj.getString("algorithm");
		String[] algorithmArray = algorithm.split(",");
		Arrays.sort(algorithmArray);
		JSONObject baseparam = new JSONObject();
		//92用电量；122,123,124 一、二、三产单耗；128人均用电量
		baseparam.put("indexs", "107,122,123,124,128,113");
		baseparam.put("year", obj.getString("baseyear"));
		List<QuoteBase> quoteBase = baseDao.queryBaseData(baseparam);//基准年数据
		List<TotalData> resultList = new ArrayList<TotalData>();//插入数据库的集合
		Map<String,Map<Integer,Double>> mm = new HashMap<String,Map<Integer,Double>>();
		List<Map<String,Map<Integer,Double>>> l = new ArrayList<Map<String,Map<Integer,Double>>>();
		List<Map<String,Double>> list = new ArrayList<Map<String,Double>>();
		for(String str : algorithmArray){
			switch(str){
			case "1":
			/*
			 * 平均增长率法
			 */
			double	d1=calculateAlgorithmService.averageGrowthRate(quoteBase,obj,m);
			mm=this.averageCalculate(d1,obj,"1");
			l.add(mm);
			Map<String,Double> map = new HashMap<String,Double>();

			map.put("1", d1);
			list.add(map);
			break;
			case "2":
			/*
			 * 产值单耗法
			 */
			double	d2 = calculateAlgorithmService.productionValuePerUnitConsumption(quoteBase,prepareData,obj,m);
			mm=this.averageCalculate(d2,obj,"2");
			l.add(mm);
			
			Map<String,Double> map1 = new HashMap<String,Double>();

			map1.put("2", d2);
			list.add(map1);
			break;
			case "3":
			/*
			 * 弹性系数法
			 */
			double d3 =calculateAlgorithmService.elasticityCoefficient(quoteBase,obj,m);
			mm=this.averageCalculate(d3,obj,"3");
			l.add(mm);
			
			Map<String,Double> map2 = new HashMap<String,Double>();

			map2.put("3", d3);
			list.add(map2);
			break;
			case "4":
			/*
			 * 人均用电量法	
			 */
			double d4 =calculateAlgorithmService.avgElectricityConsumption(quoteBase,prepareData,obj,m);
			mm=this.averageCalculate(d4,obj,"4");
			l.add(mm);
			Map<String,Double> map3 = new HashMap<String,Double>();

			map3.put("4", d4);
			list.add(map3);
			break;
			case "5":
			/*
			 * 平均值法
			 */
			double d5 =calculateAlgorithmService.avgValue(list);
			mm=this.averageCalculate(d5,obj,"5");
			l.add(mm);
			Map<String,Double> map4 = new HashMap<String,Double>();

			map4.put("5", d5);
			break;
			case "6":
			/*
			 * 最优权重法
			 */
			double d6 =calculateAlgorithmService.optimalWeight(list, m);
			mm=this.averageCalculate(d6,obj,"6");
			l.add(mm);
			Map<String,Double> map5 = new HashMap<String,Double>();

			map5.put("6", d6);
			break;
			}
		}
	
	
		int planyear=obj.getInt("planyear");	//预测年
		String taskid=obj.getString("taskid");	//预测年
		/*for (String key : map.keySet()) {
			TotalData td = new TotalData();
			td.setAlgorithm(key);
			td.setValue(map.get(key));
			td.setTask_id(taskid);
			td.setYear(planyear);
			resultList.add(td) ;
		}*/
		for(Map<String,Map<Integer,Double>> mmm : l){
			for (String key : mmm.keySet()) {
				Map<Integer,Double> imap = mmm.get(key);
				for (Integer ikey : imap.keySet()) {
					TotalData td = new TotalData();
					td.setAlgorithm(key);
					td.setValue(imap.get(ikey));
					td.setTask_id(taskid);
					td.setYear(ikey);
					resultList.add(td) ;
				}
				
			}
		}
		return resultList;

	}

	private Map<String,Map<Integer,Double>> averageCalculate(double d1, JSONObject obj,String flag) {
		Map<Integer,Double> map = new HashMap<Integer,Double>();
		List<Integer> years = this.getYear(obj) ;
		int baseyear=years.get(0);	//基准年
		int planyear=years.get(years.size()-1);
		//double d = d1/(planyear-baseyear);
		double sub = d1 -calculatePlanDao.getDataByBaseyear(baseyear);
		double d = MyMath.div(sub, planyear-baseyear, Containts.PRECISION);//每年增加的值
		map.put(planyear, d1);
		for(int i=years.size()-2; i >=1 ;--i){
			
			d1= MyMath.round(MyMath.sub(d1, d),Containts.PRECISION);
			map.put(years.get(i), d1);

			//d1=d1-d;
		}
		Map<String,Map<Integer,Double>> resultMap = new HashMap<String,Map<Integer,Double>>();
		 resultMap.put(flag, map);
		 return resultMap;
		
	}
	private List<Integer> getYear( JSONObject obj){
		List<Integer> years = new ArrayList<Integer>();
		int baseyear=obj.getInt("baseyear");	//基准年
		int planyear=obj.getInt("planyear");	//预测年
		for(int i=baseyear ; i<=planyear;++i){
			years.add(i);
		}
		return years;
	}

	@Override
	public String initData(String taskid) {
		List<CalculatePlan> list = calculatePlanDao. getDataBytask(taskid);
		JSONObject obj = new JSONObject();
		for (CalculatePlan cp : list){
			obj.put(cp.getIndex_type(), cp.getIndex_value()) ;
		}
		return obj .toString();
	}
}
