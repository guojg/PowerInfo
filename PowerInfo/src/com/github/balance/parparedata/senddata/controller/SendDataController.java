package com.github.balance.parparedata.senddata.controller;


import java.io.PrintWriter;

import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.balance.parparedata.senddata.service.SendDataService;

@Controller
@RequestMapping(value = "/sendData")
public class SendDataController {
	@Autowired
	private SendDataService sendDataService;

	@RequestMapping(value = "/queryData")
	public void queryData(HttpServletRequest request,
			HttpServletResponse response) {
		response.setCharacterEncoding("UTF-8");
		String years = "2015,2016";	
		JSONObject obj = new JSONObject();
		obj.put("years", years);
		try {
			String resultJson = sendDataService.queryData(obj);
			PrintWriter pw = response.getWriter();
			pw.write(resultJson);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/saveData")
	public @ResponseBody
	String saveData(HttpServletRequest request) {
		try {
			String editObj = request.getParameter("editObj");
			JSONObject jsonobj = new JSONObject();
			jsonobj.put("editObj", editObj);
			sendDataService.saveData(jsonobj);
			return "1";
		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		}
	}
	@RequestMapping(value = "/main")
	public String index(Long pid, HttpServletRequest request,
			HttpServletResponse re) {
		return "balance/parpareData/hinderedIdleCapacity";
	}

//	@RequestMapping("/exportData")
//	public void exportData(HttpServletRequest request,
//			HttpServletResponse response) {
//			response.setCharacterEncoding("UTF-8");
//			String years = request.getParameter("years");
//			String indexs = request.getParameter("indexs");
//			
//			JSONObject obj = new JSONObject();
//			obj.put("years", years);
//			obj.put("indexs", indexs);
//			try {
//				loadElectricQuantityService.ExportExcel(obj, response);
//			}catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			 
//
//	}

}
