package com.github.common.tree;

public class AttributeMsg {
	
	public AttributeMsg(String url,String tablename,String unit_code,String useful){
		this.url=url;
		this.tableName=tablename;
		this.unit_code=unit_code;
		this.useful=useful;
	}
	private String url;
	private String tableName;
	private String unit_code;
	private String useful;

	public String getUseful() {
		return useful;
	}

	public void setUseful(String useful) {
		this.useful = useful;
	}

	public String getUnit_code() {
		return unit_code;
	}

	public void setUnit_code(String unit_code) {
		this.unit_code = unit_code;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	

}
