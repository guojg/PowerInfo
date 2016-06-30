package com.github.totalquantity.totaldata.dao;

import java.util.List;

import com.github.totalquantity.totaldata.entity.TotalData;
/**
 * 结果与数据库相关操作
 * @author guo
 *
 */
public interface TotalDataDao {
	/**
	 * 保存算法数据值
	 * @param data
	 */
	public void saveData( List<TotalData> data) ;
}
