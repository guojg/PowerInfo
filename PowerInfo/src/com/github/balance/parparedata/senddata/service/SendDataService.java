package com.github.balance.parparedata.senddata.service;

import java.util.List;

import com.github.balance.parparedata.senddata.model.Domain;

import net.sf.json.JSONObject;
public interface SendDataService {

	public String queryData(JSONObject param) throws Exception;
	public String saveData(JSONObject param) throws Exception;
	public String deleteData(String[] ids) throws Exception;
	public String addProData(JSONObject param)throws Exception;
	public List<Domain> getTypes() throws Exception;

}
