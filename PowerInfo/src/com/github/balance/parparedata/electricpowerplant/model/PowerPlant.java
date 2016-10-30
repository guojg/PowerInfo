package com.github.balance.parparedata.electricpowerplant.model;

public class PowerPlant {
	
	public String getPlantName() {
		return plantName;
	}
	public void setPlantName(String plantName) {
		this.plantName = plantName;
	}
	public String getPlantCapacity() {
		return plantCapacity;
	}
	public void setPlantCapacity(String plantCapacity) {
		this.plantCapacity = plantCapacity;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	private String plantName;
	private String plantCapacity;
	private String startDate;
	private String endDate;
	private String id;
	private String indexItem;
	private String areaId;
	public String getAreaId() {
		return areaId;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	public String getIndexItem() {
		return indexItem;
	}
	public void setIndexItem(String indexItem) {
		this.indexItem = indexItem;
	}

}
