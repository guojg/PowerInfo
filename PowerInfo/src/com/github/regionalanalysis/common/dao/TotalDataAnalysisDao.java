package com.github.regionalanalysis.common.dao;



public interface TotalDataAnalysisDao {
	
	/**
	 * 机组总成本分析计算
	 * @param param
	 * @throws Exception
	 */
	public void totalData(final Long fdj_id,final Integer area_id) throws Exception;
	/**
	 * 机组总成本分析计算分析
	 * @param param
	 * @throws Exception
	 */
	public void totalDataAnalysis( Long fdj_id, Integer area_id,Long task_id) throws Exception;
	
	/**
	 * 机组总成本分析计算对比
	 * @param param
	 * @throws Exception
	 */
	public void totalDatadCompare( Long fdj_id, Integer area_id, Long task_id) throws Exception;
	
	/**
	 * 电厂总成本分析计算
	 * @param param
	 * @throws Exception
	 */
	public void totalDataPlant(Integer  dc_id, Integer area_id) throws Exception;
	
	/**
	 * 电厂总成本分析计算分析
	 * @param param
	 * @throws Exception
	 */
	public void totalDataPlantAnalysis( Integer dc_id, Integer area_id, Long task_id) throws Exception;
	
	/**
	 * 电厂总成本分析计算对比
	 * @param param
	 * @throws Exception
	 */
	public void totalDataPlantCompare( Integer dc_id, Integer area_id, Long task_id) throws Exception;
	
	/**
	 * 半小时计算
	 * @param param
	 * @throws Exception
	 */
	public void HalfFdjAssigned( Integer dc_id, Integer area_id) throws Exception;
	
	/**
	 * 半小时计算分析
	 * @param param
	 * @throws Exception
	 */
	public void HalfFdjAssignedFx( Integer dc_id, Integer area_id, Long task_id) throws Exception;
	
	/**
	 * 半小时计算分析
	 * @param param
	 * @throws Exception
	 */
	public void HalfFdjAssignedDb( Integer dc_id, Integer area_id, Long task_id) throws Exception;
	
	/**
	 * 全部重新计算
	 * @param param
	 * @throws Exception
	 */
	public void fdcSaveTotal( Integer fdc_id, Integer area_id) throws Exception;
	
	/**
	 * 全部重新计算分析
	 * @param param
	 * @throws Exception
	 */
	public void fdcSaveTotalFx( Integer fdc_id, Integer area_id, Long task_id) throws Exception;
	
	/**
	 * 全部重新计算对比
	 * @param param
	 * @throws Exception
	 */
	public void fdcSaveTotalDb( Integer fdc_id, Integer area_id, Long task_id) throws Exception;
}
