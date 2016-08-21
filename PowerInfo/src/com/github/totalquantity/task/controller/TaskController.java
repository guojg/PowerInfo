package com.github.totalquantity.task.controller;



import java.util.List;

import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.totalquantity.task.entity.TotalTask;
import com.github.totalquantity.task.entity.TotalYear;
import com.github.totalquantity.task.service.TaskService;

@Controller
@RequestMapping(value ="/task")
public class TaskController {
	 @Autowired
	 private TaskService taskService ;
	 
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
		return taskService.queryData(obj);
		
	}
	@RequestMapping(value ="/saveData")
	public void saveData(HttpServletRequest request, HttpServletResponse response){
		  
        String task_name = request.getParameter("task_name")!=null?request.getParameter("task_name"):"";
        String baseyear = request.getParameter("baseyear")!=null?request.getParameter("baseyear"):"";
        String planyear = request.getParameter("planyear")!=null?request.getParameter("planyear"):"";
        String algorithm = request.getParameter("algorithm")!=null?request.getParameter("algorithm"):"";
        String id = request.getParameter("id")!=null?request.getParameter("id"):"";
        TotalTask task = new TotalTask();
        task.setTask_name(task_name);
        task.setBaseyear(baseyear);
        task.setPlanyear(planyear);
        task.setAlgorithm(algorithm);
        task.setId(id);
        taskService.saveData(task);

	}
	@RequestMapping("/exportData")
	public void exportData(HttpServletRequest request, HttpServletResponse response){
		
	}
	@RequestMapping(value ="/index")
	public String index(HttpServletRequest request, HttpServletResponse response){
		 //return "basicData/nationalEconomy/nationalEconomyMain";
		 return "totalQuantity/task/task";
	}
	
	@RequestMapping(value ="/taskAdd")
	public String taskAdd(HttpServletRequest request, HttpServletResponse response){
	
		return "totalQuantity/task/taskAdd";
	}
	@RequestMapping(value ="/initData",produces="application/json;charset=UTF-8")
	public @ResponseBody String initData(HttpServletRequest request, HttpServletResponse response){
		String id = request.getParameter("id")==null?"":request.getParameter("id");
		return  taskService.initData(id);
	}
	@RequestMapping(value ="/taskDetail")
	public @ResponseBody void taskDetail(HttpServletRequest request, HttpServletResponse response){
		 //return "basicData/nationalEconomy/nationalEconomyMain";
			String baseyear = request.getParameter("baseyear")==null?"":request.getParameter("baseyear");
			String task_name = request.getParameter("task_name")==null?"":request.getParameter("task_name");
			String planyear = request.getParameter("planyear")==null?"":request.getParameter("planyear");
			String algorithm = request.getParameter("algorithm")==null?"":request.getParameter("algorithm");
			String taskid = request.getParameter("taskid")==null?"":request.getParameter("taskid");
			String algorithmRadio = request.getParameter("algorithmRadio")==null?"":request.getParameter("algorithmRadio");
			request.getSession().removeAttribute("totaltask");
			TotalTask tt = new TotalTask();
			tt.setBaseyear(baseyear);
			tt.setTask_name(task_name);
			tt.setId(taskid);
			tt.setPlanyear(planyear);
			tt.setAlgorithm(algorithm);
			tt.setAlgorithmRadio(algorithmRadio);
			request.getSession().setAttribute("totaltask", tt);
	}
	@RequestMapping(value = "/getBaseYears",produces="application/json;charset=UTF-8")
	public @ResponseBody List<TotalYear> getBaseYears(HttpServletRequest request) {
		try {
			return taskService.getBaseYears();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	@RequestMapping(value = "/getPlanYears",produces="application/json;charset=UTF-8")
	public @ResponseBody List<TotalYear> getPlanYears(HttpServletRequest request) {
		try {
			return taskService.getPlanYears();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	@RequestMapping(value = "/deleteRecord")
	public @ResponseBody
	String deleteRecord(HttpServletRequest request) {
		try {
			String deleteids = request.getParameter("ids");
			JSONObject jsonobj = new JSONObject();
			jsonobj.put("deleteids",deleteids );
			taskService.deleteRecord(jsonobj);
			return "1";
		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		}
	}
	
}
