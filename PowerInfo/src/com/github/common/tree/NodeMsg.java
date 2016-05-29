package com.github.common.tree;

public class NodeMsg {
    private String id;
    
    private String text;
    
    private String parentid;
    
    private String iconCls;
    
    private String state;
    
    private AttributeMsg attributes;
    
    private String isLeaf;

    public NodeMsg() {
        
    }
    
    public NodeMsg(String id, String text) {
        this.id = id;
        this.text = text;
    }
    
    public NodeMsg(String id, String text, String parentid) {
        this.id = id;
        this.text = text;
        this.parentid = parentid;
    }
    
    public NodeMsg(String id, String text, String parentid,String isLeaf) {
        this.id = id;
        this.text = text;
        this.parentid = parentid;
        this.isLeaf = isLeaf;
    }
    
    public NodeMsg(String id, String text, String parentid,String isLeaf,AttributeMsg attributes ) {
        this.id = id;
        this.text = text;
        this.parentid = parentid;
        this.isLeaf = isLeaf;
        this.attributes=attributes;
        
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }

    public String getIconCls() {
        return iconCls;
    }

    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

   

	public AttributeMsg getAttributes() {
		return attributes;
	}

	public void setAttributes(AttributeMsg attributes) {
		this.attributes = attributes;
	}

	public String getIsLeaf() {
		return isLeaf;
	}

	public void setIsLeaf(String isLeaf) {
		this.isLeaf = isLeaf;
	}
    
}
