package com.github.totalquantity.calculateAlgorithm;

import java.util.List;

import com.github.totalquantity.calculatePlan.entity.CalculatePlan;

public class CalculateAlgorithm {

	public static double  averageGrowthRate(List<CalculatePlan> list){
		//基准年电量*（1+i）^(2020-2015)
		double d = 1.34;	//基准年电量
		double i = subjectiveConcept(list);  //主观概率计算
		int baseyear=2015;	//基准年
		int planyear=2020;	//预测年
		double result = d* Math.pow(1+i, planyear-baseyear) ;
		return result ;
	}
	
	/**
	 * 主观概率计算i=(a+4m+b)/6
	 * 其中：最大值　ａ
　	 *       最小值　ｂ
　　  *　          最可能值 ｍ
	 */
	
	public static double subjectiveConcept(List<CalculatePlan> list){
				double a=0d;
				double m=0d;
				double b=0d;
				for(int j= 0 ; j<list.size() ; ++j){
					String key = list.get(j).getIndex_type() ;
					String value = list.get(j).getIndex_value();
					switch(key){
					case "maxRate": //最大值
						a = Double.parseDouble(value) ;
						break;
					case "minRate"://最小值
						b = Double.parseDouble(value) ;
						break;
					case "possibleRate"://最可能值
						m = Double.parseDouble(value) ;
						break;
					}	
				}
				double i= (a+4*m+b)/6;
		return i ;
	}
}
