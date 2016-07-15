package com.github.totalquantity.calculatePlan.entity;

/**
 * 算法参数对象
 * @author guo
 *
 */
public class CalculatePlan {
	private String taskid ;
	private String algorithm;
	private String index_type ;
	private Double index_value ;
	
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getAlgorithm() {
		return algorithm;
	}
	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}
	public String getIndex_type() {
		return index_type;
	}
	public void setIndex_type(String index_type) {
		this.index_type = index_type;
	}
	public Double getIndex_value() {
		return index_value;
	}
	public void setIndex_value(Double index_value) {
		this.index_value = index_value;
	}
	
	
}
