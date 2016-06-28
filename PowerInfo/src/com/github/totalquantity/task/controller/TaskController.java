package com.github.totalquantity.task.controller;


import java.io.IOException;
import java.io.PrintWriter;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.totalquantity.task.service.TaskService;

@Controller
@RequestMapping(value ="/task")
public class TaskController {
	 @Autowired
	 private TaskService taskService ;
	 
	@RequestMapping(value ="/queryData")
	public void queryData(HttpServletRequest request, HttpServletResponse response){
		response.setCharacterEncoding("UTF-8");
		//String jsonParam	= request.getParameter("jsonParam");
		JSONObject obj = new JSONObject();
		String	resultJson ="";
		try {
			PrintWriter pw = response.getWriter();
			pw.write(resultJson);
			pw.flush();
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@RequestMapping(value ="/saveData")
	public void saveData(HttpServletRequest request, HttpServletResponse response){
		  
        /*String task_name = request.getParameter("task_name")!=null?request.getParameter("task_name"):"";
        String baseyear = request.getParameter("baseyear")!=null?request.getParameter("baseyear"):"";
        String planyear = request.getParameter("planyear")!=null?request.getParameter("planyear"):"";
        String algorithm = request.getParameter("algorithm")!=null?request.getParameter("algorithm"):"";
        TotalTask task = new TotalTask();
        task.setTask_name(task_name);
        task.setBaseyear(baseyear);
        task.setPlanyear(planyear);
        task.setAlgorithm(algorithm);
        taskService.saveData(task);*/
		String param = request.getParameter("abc")!=null?request.getParameter("abc"):"";
		 // JSONObject jb=new JSONObject();
		  //  JSONArray array=(JSONArray)jb.fromObject(param).get("allMenu");
		JSONArray  array=JSONArray.fromObject(param);
		JSONObject obj = (JSONObject) array.get(0);
		//obj.get("1");
		System.out.println(obj.get("1"));
	}
	@RequestMapping("/exportData")
	public void exportData(HttpServletRequest request, HttpServletResponse response){
		
	}
	@RequestMapping(value ="/index")
	public String index(HttpServletRequest request, HttpServletResponse response){
		 //return "basicData/nationalEconomy/nationalEconomyMain";
		 return "totalQuantity/task/taskAdd";
	}
	
	
}
