package com.github.totalquantity.basedata.dao;

import java.util.List;

import com.github.totalquantity.basedata.entity.QuoteBase;

import net.sf.json.JSONObject;

public interface BaseDao {

	public List<QuoteBase> queryBaseData(JSONObject param);
}
