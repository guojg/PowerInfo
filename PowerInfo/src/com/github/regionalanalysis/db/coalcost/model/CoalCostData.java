package com.github.regionalanalysis.db.coalcost.model;

public class CoalCostData {
	
	private String indexX;
	private String indexY;
	private String value;
	private String unit;
	private String fdjId;
	private String areaId;

	public String getAreaId() {
		return areaId;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	public String getFdjId() {
		return fdjId;
	}
	public void setFdjId(String fdjId) {
		this.fdjId = fdjId;
	}
	public String getIndexX() {
		return indexX;
	}
	public void setIndexX(String indexX) {
		this.indexX = indexX;
	}
	public String getIndexY() {
		return indexY;
	}
	public void setIndexY(String indexY) {
		this.indexY = indexY;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	

}
