package com.github.balance.task.controller;

import java.util.List;

import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.balance.task.entity.BalanceTask;
import com.github.balance.task.entity.BalanceYear;
import com.github.balance.task.service.BalanceTaskService;
import com.github.basicData.model.BasicYear;

@Controller
@RequestMapping(value ="/balancetask")
public class BalanceTaskController {
	 @Autowired
	 private BalanceTaskService balanceTaskService ;
	 
	@RequestMapping(value ="/queryData")
	public @ResponseBody String queryData(HttpServletRequest request, HttpServletResponse response){
		response.setCharacterEncoding("UTF-8");
		String pageNum = request.getParameter("page");
		String pageSize = request.getParameter("rows");
		if (pageNum == null || "null".equals(pageNum) || "".equals(pageNum))
			pageNum = "1";
		if (pageSize == null || "null".equals(pageSize)
				|| "".equals(pageSize))
			pageSize = "10";
		JSONObject obj = new JSONObject();
		obj.put("pageNum", pageNum);
		obj.put("pageSize", pageSize);
		return balanceTaskService.queryData(obj);
		
	}
	@RequestMapping(value ="/saveData")
	public void saveData(HttpServletRequest request, HttpServletResponse response){
		  
        String task_name = request.getParameter("task_name")!=null?request.getParameter("task_name"):"";
        String year = request.getParameter("year")!=null?request.getParameter("year"):"";
        String id = request.getParameter("id")!=null?request.getParameter("id"):"";
        String startyear = request.getParameter("startyear")!=null?request.getParameter("startyear"):"";
        String stopyear = request.getParameter("stopyear")!=null?request.getParameter("stopyear"):"";

    	BalanceTask bt = new BalanceTask();
		bt.setYear(year);
		bt.setTask_name(task_name);
		bt.setId(id);
		bt.setStartyear(startyear);
		bt.setStopyear(stopyear);
		balanceTaskService.saveData(bt);

	}
	@RequestMapping("/exportData")
	public void exportData(HttpServletRequest request, HttpServletResponse response){
		
	}
	@RequestMapping(value ="/index")
	public String index(HttpServletRequest request, HttpServletResponse response){
		 //return "basicData/nationalEconomy/nationalEconomyMain";
		 return "balance/task/task";
	}
	
	@RequestMapping(value ="/taskAdd")
	public String taskAdd(HttpServletRequest request, HttpServletResponse response){
	
		return "balance/task/taskAdd";
	}
	@RequestMapping(value ="/taskDetail")
	public @ResponseBody void taskDetail(HttpServletRequest request, HttpServletResponse response){
		 //return "basicData/nationalEconomy/nationalEconomyMain";
			String year = request.getParameter("year")==null?"":request.getParameter("year");
			String task_name = request.getParameter("task_name")==null?"":request.getParameter("task_name");
			String taskid = request.getParameter("taskid")==null?"":request.getParameter("taskid");
			request.getSession().removeAttribute("balancetask");
			BalanceTask bt = new BalanceTask();
			bt.setYear(year);
			bt.setTask_name(task_name);
			bt.setId(taskid);
			request.getSession().setAttribute("balancetask", bt);
	}
	
	@RequestMapping(value = "/getyears",produces="application/json;charset=UTF-8")
	public @ResponseBody List<BalanceYear> getYears(HttpServletRequest request) {
		try {
			return balanceTaskService.getYears();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	@RequestMapping(value ="/initData",produces="application/json;charset=UTF-8")
	public @ResponseBody String initData(HttpServletRequest request, HttpServletResponse response){
		String id = request.getParameter("id")==null?"":request.getParameter("id");
		return  balanceTaskService.initData(id);
	}
	
	@RequestMapping(value = "/deleteRecord")
	public @ResponseBody
	String deleteRecord(HttpServletRequest request) {
		try {
			String deleteids = request.getParameter("ids");
			JSONObject jsonobj = new JSONObject();
			jsonobj.put("deleteids",deleteids );
			balanceTaskService.deleteRecord(jsonobj);
			return "1";
		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		}
	}
	
}
