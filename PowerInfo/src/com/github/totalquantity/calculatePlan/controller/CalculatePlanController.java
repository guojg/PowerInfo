package com.github.totalquantity.calculatePlan.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.totalquantity.calculatePlan.service.CalculatePlanService;
import com.github.totalquantity.task.service.TaskService;

@Controller
@RequestMapping(value ="/calculatePlan")
public class CalculatePlanController {
	
	 @Autowired
	 private CalculatePlanService calculatePlanService ;
	 @RequestMapping(value ="/saveData")
	 public void saveData(HttpServletRequest request, HttpServletResponse response){
			
			String param = request.getParameter("param")!=null?request.getParameter("param"):"";
			JSONArray  array=JSONArray.fromObject(param);
			calculatePlanService.saveData(array);
			
		}
	 
	 @RequestMapping(value ="/index")
		public String index(HttpServletRequest request, HttpServletResponse response){
			 //return "basicData/nationalEconomy/nationalEconomyMain";
			 return "totalQuantity/calculatePlan/calculatePlan";
		}
	 
	 @RequestMapping(value ="/startCalculate")
	 public void startCalculate(HttpServletRequest request, HttpServletResponse response){

			calculatePlanService.startCalculate();
			
		}
	 
}
