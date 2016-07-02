package com.github.totalquantity.calculatePlan.controller;

import java.io.IOException;
import java.io.PrintWriter;

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
			String	resultJson ="";
			try {
				PrintWriter pw = response.getWriter();
				calculatePlanService.saveData(array);
				resultJson ="1";
				pw.write(resultJson);
				pw.flush();
				pw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	 
	 @RequestMapping(value ="/initData")
	 public void initData(HttpServletRequest request, HttpServletResponse response){
			
			String taskid = request.getParameter("taskid")!=null?request.getParameter("taskid"):"";
	
		
			response.setCharacterEncoding("UTF-8");
			//String jsonParam	= request.getParameter("jsonParam");
			JSONObject obj = new JSONObject();
			String	resultJson ="";
			try {
				PrintWriter pw = response.getWriter();
				resultJson = calculatePlanService.initData(taskid);
				pw.write(resultJson);
				pw.flush();
				pw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	 
	 @RequestMapping(value ="/index")
		public String index(HttpServletRequest request, HttpServletResponse response){
			 //return "basicData/nationalEconomy/nationalEconomyMain";
			 return "totalQuantity/calculatePlan/calculatePlan3";
		}
	 
	 @RequestMapping(value ="/startCalculate")
	 public void startCalculate(HttpServletRequest request, HttpServletResponse response){
		 	Object baseyearObj = request.getSession().getAttribute("plan_baseyear") ;
		 	Object planyearObj = request.getSession().getAttribute("plan_planyear");
		 	int baseyear = baseyearObj==null?0:Integer.parseInt(baseyearObj.toString());
		 	int planyear = planyearObj==null?0:Integer.parseInt(planyearObj.toString());

		 	JSONObject obj = new JSONObject();
		 	obj.put("baseyear", baseyear) ;
		 	obj.put("planyear", planyear) ;
			calculatePlanService.startCalculate(obj);
			
		}
	 
	 @RequestMapping(value ="/startIndex")
		public String startCalculateIndex(HttpServletRequest request, HttpServletResponse response){
			 //return "basicData/nationalEconomy/nationalEconomyMain";
			 return "totalQuantity/startCalculate/startCalculate";
		}
	 
}
