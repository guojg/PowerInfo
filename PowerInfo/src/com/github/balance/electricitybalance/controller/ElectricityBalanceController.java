package com.github.balance.electricitybalance.controller;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.balance.electricitybalance.service.ElectricityBalanceService;
import com.github.balance.electricitybalance.service.ElectricityBalanceServiceImpl;
import com.github.balance.powerbalance.service.PowerBalanceService;

@Controller
@RequestMapping(value ="/electricitybalance")
public class ElectricityBalanceController {
	@Autowired
	private ElectricityBalanceService electricityBalanceService;
	@RequestMapping(value ="/index")
	public String index(HttpServletRequest request, HttpServletResponse response){
		 return "balance/electricity/electricitybalance";
	}
	
	@RequestMapping(value = "/queryData" ,produces="text/plain;charset=UTF-8")
	public @ResponseBody String queryData(HttpServletRequest request,
			HttpServletResponse response) {
		 String taskid = request.getParameter("taskid");
		 String year = request.getParameter("year")==null?"": request.getParameter("year");
		JSONObject obj = new JSONObject();
		obj.put("year", year);
		obj.put("taskid",taskid);
		String resultJson = electricityBalanceService.queryData(obj);
	System.out.println(resultJson);
		return resultJson;
	}
	
}
