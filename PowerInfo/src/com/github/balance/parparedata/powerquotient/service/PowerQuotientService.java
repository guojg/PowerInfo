package com.github.balance.parparedata.powerquotient.service;


import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.github.common.util.JsonUtils;

public interface PowerQuotientService {
	
	/**
	 * 查询
	 * @param param
	 * @return
	 */
	
	public String queryData(JSONObject param);

	public String saveData(JSONObject jsonobj);
	public void  ExportExcel(JSONObject param,HttpServletResponse response) throws Exception;

}
