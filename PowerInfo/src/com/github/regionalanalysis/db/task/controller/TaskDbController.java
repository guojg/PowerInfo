package com.github.regionalanalysis.db.task.controller;



import java.util.List;

import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.regionalanalysis.db.task.entity.DbTask;
import com.github.regionalanalysis.db.task.service.TaskDbService;


@Controller
@RequestMapping(value ="/taskdb")
public class TaskDbController {
	 @Autowired
	 private TaskDbService taskDbService ;
	 
	@RequestMapping(value ="/queryData")
	public @ResponseBody String queryData(HttpServletRequest request, HttpServletResponse response){
		response.setCharacterEncoding("UTF-8");
		String pageNum = request.getParameter("page");
		String pageSize = request.getParameter("rows");
        String area_id = request.getParameter("area_id")!=null?request.getParameter("area_id"):"";

		if (pageNum == null || "null".equals(pageNum) || "".equals(pageNum))
			pageNum = "1";
		if (pageSize == null || "null".equals(pageSize)
				|| "".equals(pageSize))
			pageSize = "10";
		JSONObject obj = new JSONObject();
		obj.put("pageNum", pageNum);
		obj.put("pageSize", pageSize);
		obj.put("area_id", area_id) ;
		return taskDbService.queryData(obj);
		
	}
	@RequestMapping(value ="/saveData")
	public void saveData(HttpServletRequest request, HttpServletResponse response){
		  
        String task_name = request.getParameter("task_name")!=null?request.getParameter("task_name"):"";
        String area_id = request.getParameter("area_id")!=null?request.getParameter("area_id"):"";
        String id = request.getParameter("id")!=null?request.getParameter("id"):"";
        DbTask task = new DbTask();
        task.setTask_name(task_name);
        task.setArea_id(area_id);
        task.setId(id);
        taskDbService.saveData(task);

	}
	@RequestMapping("/exportData")
	public void exportData(HttpServletRequest request, HttpServletResponse response){
		
	}
	@RequestMapping(value ="/index")
	public String index(HttpServletRequest request, HttpServletResponse response){
		 //return "basicData/nationalEconomy/nationalEconomyMain";
		 return "regionalanalysis/db/task/task";
	}
	
	@RequestMapping(value ="/taskAdd")
	public String taskAdd(HttpServletRequest request, HttpServletResponse response){
	
		return "regionalanalysis/db/task/taskAdd";
	}
	@RequestMapping(value ="/initData",produces="application/json;charset=UTF-8")
	public @ResponseBody String initData(HttpServletRequest request, HttpServletResponse response){
		String id = request.getParameter("id")==null?"":request.getParameter("id");
		return  taskDbService.initData(id);
	}
	@RequestMapping(value ="/taskDetail")
	public @ResponseBody void taskDetail(HttpServletRequest request, HttpServletResponse response){
		  String task_name = request.getParameter("task_name")!=null?request.getParameter("task_name"):"";
	        String id = request.getParameter("id")!=null?request.getParameter("id"):"";
	        DbTask task = new DbTask();
	        task.setTask_name(task_name);
	        task.setId(id);
	        taskDbService.saveData(task);
			request.getSession().removeAttribute("dbtask");
			
			request.getSession().setAttribute("dbtask", task);
	}

	@RequestMapping(value = "/deleteRecord")
	public @ResponseBody
	String deleteRecord(HttpServletRequest request) {
		try {
			String deleteids = request.getParameter("ids");
			JSONObject jsonobj = new JSONObject();
			jsonobj.put("deleteids",deleteids );
			taskDbService.deleteRecord(jsonobj);
			return "1";
		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		}
	}
	
}
