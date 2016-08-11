package com.github.balance.powerbalance.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.balance.powerbalance.service.PowerBalanceService;

@Controller
@RequestMapping(value ="/powerbalance")
public class PowerBalanceController {
	@Autowired
	private PowerBalanceService powerBalanceService;
	@RequestMapping(value ="/index")
	public String index(HttpServletRequest request, HttpServletResponse response){
		 return "balance/power/powerbalance";
	}
	
	@RequestMapping(value = "/queryData" ,produces="text/plain;charset=UTF-8")
	public @ResponseBody String queryData(HttpServletRequest request,
			HttpServletResponse response) {
		 String taskid = request.getParameter("taskid");
		 String year = request.getParameter("year")==null?"": request.getParameter("year");
		JSONObject obj = new JSONObject();
		obj.put("year", year);
		obj.put("task_id",taskid);
		String resultJson = powerBalanceService.queryData(obj);
		return resultJson;
	}
	
	@RequestMapping(value = "/extractData" ,produces="text/plain;charset=UTF-8")
	public @ResponseBody String extractData(HttpServletRequest request,
			HttpServletResponse response) {
		 String taskid = request.getParameter("taskid");
		 String year = request.getParameter("year")==null?"": request.getParameter("year");
		JSONObject obj = new JSONObject();
		obj.put("year", year);
		obj.put("task_id",taskid);
		String resultJson = powerBalanceService.extractData(obj);
		return resultJson;
	}
	
}
