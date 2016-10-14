package com.github.balance.parparedata.generator.model;

public class Generator {
	
	
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
	private String geneName;
	private Double geneCapacity;
	private String startDate;
	private String endDate;
	private String id;
	private String indexItem;
	private String plantId;
	
	public String getPlantId() {
		return plantId;
	}
	public void setPlantId(String plantId) {
		this.plantId = plantId;
	}
	public String getIndexItem() {
		return indexItem;
	}
	public void setIndexItem(String indexItem) {
		this.indexItem = indexItem;
	}
	public String getGeneName() {
		return geneName;
	}
	public void setGeneName(String geneName) {
		this.geneName = geneName;
	}
	public Double getGeneCapacity() {
		return geneCapacity;
	}
	public void setGeneCapacity(Double geneCapacity) {
		this.geneCapacity = geneCapacity;
	}
	

}
