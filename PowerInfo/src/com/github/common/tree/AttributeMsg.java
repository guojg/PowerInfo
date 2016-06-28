package com.github.common.tree;

public class AttributeMsg {
	
	public AttributeMsg(String url,String tablename){
		this.url=url;
		this.tableName=tablename;
	}
	private String url;
	private String tableName;

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
