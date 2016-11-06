package com.github.balance.electricitybalance.controller;



import java.util.List;

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
import com.github.basicData.model.BasicYear;
import com.github.basicData.service.BasicDataService;

@Controller
@RequestMapping(value ="/electricitybalance")
public class ElectricityBalanceController {
	@Autowired
	private ElectricityBalanceService electricityBalanceService;
	
	@Autowired
	private BasicDataService basicDataService;
	
	@RequestMapping(value ="/index")
	public String index(HttpServletRequest request, HttpServletResponse response){
		 return "balance/electricity/electricitybalance";
	}
	@RequestMapping(value ="/electricitybalanceImage")
	public String electricitybalanceImage(HttpServletRequest request, HttpServletResponse response){
		 return "balance/electricity/electricitybalanceImage";
	}
	
	@RequestMapping(value = "/queryData" ,produces="text/plain;charset=UTF-8")
	public @ResponseBody String queryData(HttpServletRequest request,
			HttpServletResponse response) {
		 String taskid = request.getParameter("taskid");
		 String year = request.getParameter("year")==null?"": request.getParameter("year");
		JSONObject obj = new JSONObject();
		obj.put("year", year);
		obj.put("task_id",taskid);
		String resultJson = electricityBalanceService.queryData(obj);
		return resultJson;
	}
	
	@RequestMapping(value = "/queryCoalHourData" ,produces="text/plain;charset=UTF-8")
	public @ResponseBody String queryCoalHourData(HttpServletRequest request,
			HttpServletResponse response) {
		 String taskid = request.getParameter("taskid");
		 String year = request.getParameter("year")==null?"": request.getParameter("year");
		JSONObject obj = new JSONObject();
		obj.put("year", year);
		obj.put("task_id",taskid);
		String resultJson = electricityBalanceService.queryCoalHourData(obj);
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
		String resultJson = electricityBalanceService.extractData(obj);
		return resultJson;
	}
	@RequestMapping("/exportData")
	public void exportData(HttpServletRequest request,
			HttpServletResponse response) {
			response.setCharacterEncoding("UTF-8");
			 String taskid = request.getParameter("taskid");
			 String year = request.getParameter("year")==null?"": request.getParameter("year");
			JSONObject obj = new JSONObject();
			obj.put("year", year);
			obj.put("task_id",taskid);
			try {
				electricityBalanceService.ExportExcel(obj, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
			 

	}
	
	@RequestMapping(value = "/saveData")
	public @ResponseBody
	String saveData(HttpServletRequest request){
		String editObj = request.getParameter("editObj");
		 String taskid = request.getParameter("taskid")==null?"":request.getParameter("taskid");

		JSONObject jsonobj = new JSONObject();
		jsonobj.put("editObj", editObj);
		jsonobj.put("taskid", taskid);
		return electricityBalanceService.saveData(jsonobj);
		
	}
	
	@RequestMapping(value = "/getyears",produces="application/json;charset=UTF-8")
	public @ResponseBody List<BasicYear> getYears(HttpServletRequest request) {
		 String year = request.getParameter("year")==null?"": request.getParameter("year");
			List<BasicYear> bys = null ;
		try {
			if(!"".equals(year)){
				bys = basicDataService.getYearsBycondition(Integer.parseInt(year.split(",")[0]));

			}else{
				bys = basicDataService.getYears();

			}
			for(String yr : year.split(",")){
				BasicYear by = new BasicYear();
				by.setYear(yr);
				by.setYearName(yr+"年");
				bys.add(by);
			}
			return bys;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
