package com.github.balance.parparedata.senddata.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.github.balance.parparedata.senddata.model.Domain;

import net.sf.json.JSONObject;
public interface SendDataService {

	public String queryData(JSONObject param) throws Exception;
	public String saveData(JSONObject param) throws Exception;
	public String deleteData(JSONObject obj) throws Exception;
	public String addProData(JSONObject param)throws Exception;
	public List<Domain> getTypes() throws Exception;
	public void  ExportExcel(JSONObject param,HttpServletResponse response) throws Exception;


}
