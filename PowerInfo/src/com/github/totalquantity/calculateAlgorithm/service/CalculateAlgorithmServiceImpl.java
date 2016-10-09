package com.github.totalquantity.calculateAlgorithm.service;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.common.util.MyMath;
import com.github.totalquantity.basedata.entity.QuoteBase;
import com.github.totalquantity.calculatePlan.entity.CalculatePlan;
import com.github.totalquantity.common.Containts;
import com.github.totalquantity.prepareData.dao.PrepareDataDao;
import com.github.totalquantity.prepareData.entity.PrepareData;

/**
 * 算法类
 * 主要算法有五种：
 * 				平均增长率法：averageGrowthRate
 * 				产值单耗法:productionValuePerUnitConsumption
 * 				弹性系数法：elasticityCoefficient
 * 				人均用电量法：avgElectricityConsumption
 * 				平均值法:avgValue
 * 				最优权重法：optimalWeight
 * //107用电量；122,123,124 一、二、三产单耗；128人均用电量
 * @author guo
 *
 */
@Service
public class CalculateAlgorithmServiceImpl implements  CalculateAlgorithmService{
	@Autowired
	private PrepareDataDao prepareDataDao;
	/**
	 *平均增长率法
	 *     公式：基准年电量*（1+i）^(2020-2015)，其中i=(a+4m+b)/6（主观概率计算）
	 * @param list
	 * @return
	 */
	public  double  averageGrowthRate(List<QuoteBase> quoteBase,JSONObject obj,List<CalculatePlan> list){
		//基准年电量*（1+i）^(2020-2015)
		double d = 0;	//基准年电量>>>
		double i = subjectiveConcept(list);  //主观概率计算
		int baseyear=obj.getInt("baseyear");	//基准年
		int planyear=obj.getInt("planyear");	//预测年
		String taskid=obj.getString("taskid");	//预测年
		/*
		 * 组装用电量查询信息
		
		JSONObject pdObj = new JSONObject();
		pdObj.put("planyear", planyear);
		pdObj.put("taskid", taskid);
		pdObj.put("index_type", "");//用电量代号 */
		for(QuoteBase qb :quoteBase){
			switch(qb.getIndexItem()){
			case "107" :
				d = qb.getValue() ;
				break;
			
			}
		}
		/*
		 *获取用电量数据 
		 */
		/*List<PrepareData> pdList =getPrepareDataByIndexType(pdObj) ;
		if(pdList.size()>0){
			String valueStr = pdList.get(0).getValue()==null?"0":pdList.get(0).getValue().toString() ;
			d= Double.parseDouble(valueStr) ;
		}*/
		//double result = d* Math.pow(1+i, planyear-baseyear) ;
		double result =MyMath.round(MyMath.mul(d, Math.pow(1+i, planyear-baseyear)),Containts.PRECISION);
		return result ;
	}
	
	/**
	 * 主观概率计算i=(a+4m+b)/6
	 * 其中：最大值　ａ
　	 *       最小值　ｂ
　　  *　          最可能值 ｍ
	 */
	
	public  double subjectiveConcept(List<CalculatePlan> list){
				double a=0d;
				double m=0d;
				double b=0d;
				for(int j= 0 ; j<list.size() ; ++j){
					String key = list.get(j).getIndex_type() ;
					//Double value = list.get(j).getIndex_value();
					Double value = 0.0;
					if(list.get(j).getIndex_value()!=null){
						value = list.get(j).getIndex_value() ;
					}
					switch(key){
					case "maxRate": //最大值
						a = value/100.0;
						break;
					case "minRate"://最小值
						b = value/100.0 ;
						break;
					case "possibleRate"://最可能值
						m = value/100.0 ;
						break;
					}	
				}
				//double i= (a+4*m+b)/6;
				double i=MyMath.div(MyMath.add(MyMath.add(a, MyMath.mul(4, m)), b), 6, Containts.PRECISION);
		return i ;
	}
	
	/**
	 * 人均用电量法：主观概率计算i=(a+4m+b)/6
	 * 其中：最大值　ａ
　	 *       最小值　ｂ
　　  *　          最可能值 ｍ
	 */
	
	public  double avgSubjectiveConcept(List<CalculatePlan> list){
				double a=0d;
				double m=0d;
				double b=0d;
				for(int j= 0 ; j<list.size() ; ++j){
					String key = list.get(j).getIndex_type() ;
					//Double value = list.get(j).getIndex_value();
					Double value = 0.0;
					if(list.get(j).getIndex_value()!=null){
						value = list.get(j).getIndex_value() ;
					}
					switch(key){
					case "avgMaxRate": //最大值
						a = value/100.0 ;
						break;
					case "avgMinRate"://最小值
						b = value/100.0 ;
						break;
					case "avgPossibleRate"://最可能值
						m = value/100.0 ;
						break;
					}	
				}
				//double i= (a+4*m+b)/6;
				double i=MyMath.div(MyMath.add(MyMath.add(a, MyMath.mul(4, m)), b), 6, Containts.PRECISION);

		return i ;
	}
	/**
	 * 弹性系数法
	 *  	规划期末期用电量=规划期初期(即基准年)用电量*
	 *  （1+电力弹性系数*国内生产总值平均年增长速度）的预测年限（即预测年减去基准年）次方；
	 * 
	 * @param list
	 * @return
	 */
	public double elasticityCoefficient(List<QuoteBase> quoteBase,JSONObject obj,List<CalculatePlan> list){
		double result = 0.0 ;
		double baseyearElectricity=0;//规划期初期(即基准年)用电量>>>
		double coefficient=0.0; //电力弹性系数
		double incrementSpeed=0.0;//国内生产总值平均年增长速度
		int baseyear=obj.getInt("baseyear");	//基准年
		int planyear=obj.getInt("planyear");	//预测年
		for(QuoteBase qb :quoteBase){
			switch(qb.getIndexItem()){
			case "107" :
				baseyearElectricity = qb.getValue() ;
				break;
			
			}
		}
		for(int j= 0 ; j<list.size() ; ++j){
			String key = list.get(j).getIndex_type() ;
			//Double value = list.get(j).getIndex_value();
			Double value = 0.0;
			if(list.get(j).getIndex_value()!=null){
				value = list.get(j).getIndex_value() ;
			}
			switch(key){
			case "coefficient": //电力弹性系数
				coefficient = value ;
				break;
			case "incrementSpeed"://国内生产总值平均年增长速度
				incrementSpeed = value/100.0 ;
				break;
			}
		}
		//result=baseyearElectricity*Math.pow(1+coefficient*incrementSpeed, planyear-baseyear) ;
		result=MyMath.round(MyMath.mul(baseyearElectricity, Math.pow(1+coefficient*incrementSpeed, planyear-baseyear)), Containts.PRECISION);
		return result ;
	}
	/**
	 * 人均用电量法
	 *    公式:基准年人均用电量* （1+i）^(2020-2015)*预测年人口数
	 * @param list
	 * @return
	 */
	public  double avgElectricityConsumption(List<QuoteBase> quoteBase,List<PrepareData>prepareData,JSONObject obj,List<CalculatePlan> list){
		double result=0.0 ;
		double planPeople=0;//预测年人口数---
		for(PrepareData pd :prepareData){
			if("2".equals(pd.getIndex_type())){
				planPeople = pd.getValue() ;
				break;
			}
		}
		int baseyear=obj.getInt("baseyear");	//基准年
		int planyear=obj.getInt("planyear");	//预测年
		double  avgElectricityConsumption=0;//基准年人均用电量>>>
		for(QuoteBase qb :quoteBase){
			switch(qb.getIndexItem()){
			case "128" :
				avgElectricityConsumption = qb.getValue() ;
				break;
			
			}
		}
		double i= avgSubjectiveConcept(list);
		//result = avgElectricityConsumption*Math.pow(1+i, planyear-baseyear)*planPeople;
		result=MyMath.round(MyMath.mul(MyMath.mul(avgElectricityConsumption,Math.pow(1+i, planyear-baseyear)),planPeople) , Containts.PRECISION);

		return result;
	}
	/**
	 * 	产值单耗法
	 * 		公式：预测年一产GDP*预测年一产单耗（即：基准年一产单耗*一产单耗增长率）+
	 * 			预测年二产GDP*预测年二产单耗（即：基准年二产单耗*二产单耗增长率）+
	 *          预测年三产GDP*预测年三产单耗（即：基准年三产单耗*三产单耗增长率）+
	 *          预测年人均居民生活用电量（即基准年人均居民生活用电量*人均居民生活用电量增长率）*预测年人口
	 * @param list
	 * @return
	 */
	public double productionValuePerUnitConsumption(List<QuoteBase> quoteBase,List<PrepareData>prepareData,JSONObject obj,List<CalculatePlan> list){
		double result=0.0 ;
		double planPeople=0;//预测年人口数---
		double oneGDP=0;//预测年一产GDP---
		double twoGDP=0;//预测年二产GDP---
		double threeGDP=0;//预测年三产GDP---
		for(PrepareData pd :prepareData){
			switch(pd.getIndex_type()){
			case "2" :
				planPeople = pd.getValue() ;
				break;
			case "3" :
				oneGDP = pd.getValue() ;
				break;
			case "4" :
				twoGDP = pd.getValue() ;
				break;
			case "5" :
				threeGDP = pd.getValue() ;
				break;
			}
		}
		double onePerUnit=0;//基准年一产单耗>>>
		double twoPerUnit=0;//基准年二产单耗>>>
		double threePerUnit=0;//基准年三产单耗>>>
		double avgElectricityConsumption=0;//基准年人均居民生活用电量>>>
		
		for(QuoteBase qb :quoteBase){
			switch(qb.getIndexItem()){
			case "122" :
				onePerUnit = qb.getValue() ;
				break;
			case "123" :
				twoPerUnit = qb.getValue() ;
				break;
			case "124" :
				threePerUnit = qb.getValue() ;
				break;
			case "128" :
				avgElectricityConsumption = qb.getValue() ;
				break;
			}
		}
		double onePerUnitRate=0.0;//一产单耗增长率
		double twoPerUnitRate=0.0;//二产单耗增长率
		double threePerUnitRate=0.0;//三产单耗增长率
		double avgElectricityRate=0.0;//人均居民生活用电量增长率

		for(int j= 0 ; j<list.size() ; ++j){
			String key = list.get(j).getIndex_type() ;
			Double value = 0.0;
			if(list.get(j).getIndex_value()!=null){
				value = list.get(j).getIndex_value() ;
			}
			switch(key){
			case "oneProductionRate": //一产单耗增长率
				onePerUnitRate =value/100.0;
				break;
			case "twoProductionRate": //二产单耗增长率
				twoPerUnitRate = value/100.0 ;
				break;
			case "threeProductionRate": //三产单耗增长率
				threePerUnitRate = value/100.0 ;
				break;
			case "avgElectricityRate": //人均居民生活用电量增长率
				avgElectricityRate =value/100.0;
				break;
			}
		}
		result = oneGDP*onePerUnit*(1+onePerUnitRate)+twoGDP*twoPerUnit*(1+twoPerUnitRate)
				+threeGDP*threePerUnit*(1+threePerUnitRate)+
				avgElectricityConsumption*(1+avgElectricityRate)*planPeople;
		result = MyMath.round(result, Containts.PRECISION);
		return result;
	}
	/**
	 * 平均值法
	 *     平均值法：(方法一预测值+方法二预测值+方法三预测值+方法四预测值)/4
	 *     即选择的算法除以算法数
	 * @return
	 */
	public double avgValue(List<Map<String,Double>> list){
		double result=0.0 ;
		int count=0;
		for(int i=0 ; i<list.size() ; ++i){
			for(Map<String,Double> m : list){
				for (String key : m.keySet()) {
					//result += m.get(key).doubleValue();
					result = MyMath.add(result, m.get(key).doubleValue());
					++count;
				}
			}
		}
		//result = result/count;
		if(count !=0){
			result=MyMath.div(result, count, Containts.PRECISION);
		}

		return result;
	}
	
	/**
	 * 最优权重法
	 * 方法一预测值*权重1+
	 * 方法二预测值*权重2+方法三预测值*权重3+方法四预测值*权重4   ，其中四个权重值之和为1
	 * 
	 * 即 选择的算法*对应的权重   其中选择的权重值和为1
	 * @return
	 */
	public double optimalWeight(List<Map<String,Double>> list ,List<CalculatePlan> weightlist){
		double result=0.0 ;
		/*for(Map<String,Double> m : list){
			for (String key : m.keySet()) {
				
				for(CalculatePlan cp : weightlist){
					if(("weight"+key).equals(cp.getIndex_type())){
						reslut += m.get(key).doubleValue()*Double.parseDouble(cp.getIndex_value());
					}
				}
			}
		}*/
		for(CalculatePlan cp : weightlist){
			String index_type = cp.getIndex_type() ;
			Double index_value = cp.getIndex_value()/100.0 ;
			for(Map<String,Double> m : list){
				for (String key : m.keySet()) {
					if(index_type.equals("weight"+key) && index_value!=null && !"".equals(index_value)){
						//result += m.get(key).doubleValue()*index_value;
						result = MyMath.add(result,MyMath.mul(m.get(key).doubleValue(), index_value));
					}
				}
			}
		}
		result = MyMath.round(result, Containts.PRECISION);
		return result;
	}
	
	
	public List<PrepareData> getPrepareDataByIndexType(JSONObject obj){
		List<PrepareData> list =  prepareDataDao.getPrepareDataByIndexType(obj);
		return  list;
	}


	
}
