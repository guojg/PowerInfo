package com.github.totalquantity.calculatePlan.dao;

import java.util.List;

import com.github.totalquantity.calculatePlan.entity.CalculatePlan;
/**
 * 参数dao层，主要是增、删、改、查
 * @author guo
 *
 */
public interface CalculatePlanDao {
	/**
	 * 保存算法参数
	 * @param list
	 */
	public void saveData(List<CalculatePlan> list);
	
	/**
	 * 根据任务号获取输入参数值
	 * @param taskid 任务号
	 * @return 算法参数对象的数据集
	 */
	public List<CalculatePlan> getDataBytask(String taskid);
}
