package com.github.regionalanalysis.fx.constantcostarg.dao;

import java.util.List;
import java.util.Map;

import com.github.regionalanalysis.db.constantcostarg.entity.ConstantCostArg;




public interface ConstantCostFxArgDao {
	/**
	 * 保存
	 * @param list
	 */
	public String save(List<ConstantCostArg> list);
	/**
	 * 通过机组id获得数据
	 * @param id
	 * @param task_id 
	 * @return
	 */
	public List<ConstantCostArg> getDataById(String id, String task_id);
	/**
	 * 获得所有的电厂
	 * @param area_id 
	 * @return
	 */
	public List<Map<String, Object>> queryPlant(String area_id, String task_id);
	public Integer getPlantByJz(String jz_id, String task_id);
}
