package com.github.totalquantity.calculateAlgorithm.service;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.github.totalquantity.basedata.entity.QuoteBase;
import com.github.totalquantity.calculatePlan.entity.CalculatePlan;
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
 * @author guo
 *
 */
public interface CalculateAlgorithmService {
	/**
	 *平均增长率法
	 *     公式：基准年电量*（1+i）^(2020-2015)，其中i=(a+4m+b)/6（主观概率计算）
	 * @param list  输入参数值集合
	 * @param obj   常规参数（基准年、任务号、规划年之类的）
	 * @return
	 */
	public  double  averageGrowthRate(List<QuoteBase> quoteBase,JSONObject obj,List<CalculatePlan> list);
	/**
	 * 弹性系数法
	 *  	规划期末期用电量=规划期初期(即基准年)用电量*
	 *  （1+电力弹性系数*国内生产总值平均年增长速度）的预测年限（即预测年减去基准年）次方；
	 * @param list  输入参数值集合
	 * @param obj   常规参数（基准年、任务号、规划年之类的）
	 * @return
	 */
	public double elasticityCoefficient(List<QuoteBase> quoteBase,JSONObject obj,List<CalculatePlan> list);
	/**
	 * 人均用电量法
	 *    公式:基准年人均用电量* （1+i）^(2020-2015)*预测年人口数
	 * @param list  输入参数值集合
	 * @param obj   常规参数（基准年、任务号、规划年之类的）
	 * @param prepareData   预测年数据
	 * @return
	 */
	public  double avgElectricityConsumption(List<QuoteBase> quoteBase,List<PrepareData>prepareData,JSONObject obj,List<CalculatePlan> list);
	/**
	 * 	产值单耗法
	 * 		公式：预测年一产GDP*预测年一产单耗（即：基准年一产单耗*一产单耗增长率）+
	 * 			预测年二产GDP*预测年二产单耗（即：基准年二产单耗*二产单耗增长率）+
	 *          预测年三产GDP*预测年三产单耗（即：基准年三产单耗*三产单耗增长率）+
	 *          预测年人均居民生活用电量（即基准年人均居民生活用电量*人均居民生活用电量增长率）*预测年人口
	 * @param list  输入参数值集合
	 * @param obj   常规参数（基准年、任务号、规划年之类的）
	 * @param prepareData   预测年数据
	 * @return
	 */
	public double productionValuePerUnitConsumption(List<QuoteBase> quoteBase,List<PrepareData>prepareData,JSONObject obj,List<CalculatePlan> list);
	/**
	 * 平均值法
	 *     平均值法：(方法一预测值+方法二预测值+方法三预测值+方法四预测值)/4
	 *     即选择的算法除以算法数
	 *   @param list  其他算法的结果集合
	 * @return
	 */
	public double avgValue(List<Map<String,Double>> list);
	/**
	 * 最优权重法
	 * 方法一预测值*权重1+
	 * 方法二预测值*权重2+方法三预测值*权重3+方法四预测值*权重4   ，其中四个权重值之和为1
	 * 
	 * 即 选择的算法*对应的权重   其中选择的权重值和为1
	 * @param weightlist  输入参数值集合
	 * @param list  其他算法的结果集合
	 * @return
	 */
	public double optimalWeight(List<Map<String,Double>> list ,List<CalculatePlan> weightlist);
}
