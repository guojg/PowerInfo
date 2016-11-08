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
	/**
	 * 装机盈余图
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value ="/powerbalanceImage1")
	public String powerbalanceImage1(HttpServletRequest request, HttpServletResponse response){
		 return "balance/power/powerbalanceImage1";
	}
	/**
	 * 年末装机容量图
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value ="/powerbalanceImage2")
	public String powerbalanceImage2(HttpServletRequest request, HttpServletResponse response){
		 return "balance/power/powerbalanceImage2";
	}
	@RequestMapping(value ="/powerbalanceImageMain")
	public String powerbalanceImageMain(HttpServletRequest request, HttpServletResponse response){
		 return "balance/power/powerbalanceImageMain";
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
				powerBalanceService.ExportExcel(obj, response);
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
		return powerBalanceService.saveData(jsonobj);
		
	}
	
}
