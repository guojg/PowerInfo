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
import com.github.totalquantity.task.entity.TotalTask;
import com.github.totalquantity.task.service.TaskService;

@Controller
@RequestMapping(value ="/calculatePlan")
public class CalculatePlanController {
	
	 @Autowired
	 private CalculatePlanService calculatePlanService ;
	 @RequestMapping(value ="/saveData")
	 public void saveData(HttpServletRequest request, HttpServletResponse response){
			
			String param = request.getParameter("param")!=null?request.getParameter("param"):"";
			String algorithmRadio = request.getParameter("algorithmRadio")!=null?request.getParameter("algorithmRadio"):"";
			TotalTask tt=  (TotalTask)request.getSession().getAttribute("totaltask");
			tt.setAlgorithmRadio(algorithmRadio);
			
			JSONArray  array=JSONArray.fromObject(param);
			String	resultJson ="";
			try {
				PrintWriter pw = response.getWriter();
				calculatePlanService.saveData(array,tt);
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
		 TotalTask tt=  (TotalTask)request.getSession().getAttribute("totaltask");
		 	JSONObject obj = new JSONObject();
		 	obj.put("baseyear", tt.getBaseyear()==null?"0": tt.getBaseyear()) ;
		 	obj.put("planyear", tt.getPlanyear()==null?"0": tt.getPlanyear()) ;
		 	obj.put("taskid", tt.getId()==null?"0":tt.getId()) ;
		 	obj.put("algorithm", tt.getAlgorithm()==null?"1,2,3,4,5,6":tt.getAlgorithm());
			calculatePlanService.startCalculate(obj);
			
		}
	 
	 @RequestMapping(value ="/startIndex")
		public String startCalculateIndex(HttpServletRequest request, HttpServletResponse response){
			 //return "basicData/nationalEconomy/nationalEconomyMain";
			 return "totalQuantity/startCalculate/startCalculate";
		}
	 
}
