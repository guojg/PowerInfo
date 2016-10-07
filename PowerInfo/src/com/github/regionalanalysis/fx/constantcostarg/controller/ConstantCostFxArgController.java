package com.github.regionalanalysis.fx.constantcostarg.controller;




import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.regionalanalysis.db.constantcostarg.service.ConstantCostDbArgService;
import com.github.regionalanalysis.db.task.entity.DbTask;
import com.github.regionalanalysis.fx.constantcostarg.service.ConstantCostFxArgService;

@Controller
@RequestMapping(value ="/constantCostFxArgController")
public class ConstantCostFxArgController {

	 @Autowired
	 private ConstantCostFxArgService constantCostDbArgService ;
	 
	@RequestMapping(value ="/index")
	public String index(HttpServletRequest request, HttpServletResponse response){
		 return "regionalanalysis/fx/constantcostarg";
	}
	@RequestMapping(value ="/detail")
	public String detail(HttpServletRequest request, HttpServletResponse response){
		 return "regionalanalysis/fx/constantcostargdetail";
	}
	
	@RequestMapping(value ="/saveData")
	public  @ResponseBody String saveData(HttpServletRequest request, HttpServletResponse response){
		Map map= request.getParameterMap();
		Object obj=  request.getSession().getAttribute("maparea");
		String area_id = "";
		if(obj != null) area_id = obj.toString();
		DbTask tt=  (DbTask)request.getSession().getAttribute("fxtask");
		String task_id = tt.getId();
		return constantCostDbArgService.saveData(map,area_id,task_id);
	}
	 
	 @RequestMapping(value = "/initData",produces="application/json;charset=UTF-8")
		public @ResponseBody String initData(HttpServletRequest request) {
			String id = request.getParameter("id")!=null?request.getParameter("id"):"";
			DbTask tt=  (DbTask)request.getSession().getAttribute("fxtask");
			String task_id = tt.getId();
		 	return constantCostDbArgService.initData(id,task_id);
			
		}
	 
		@RequestMapping(value = "/getPlant", produces="application/json;charset=UTF-8")
		public  @ResponseBody String getPlant(HttpServletRequest request,
				HttpServletResponse response) {
			Object obj=  request.getSession().getAttribute("maparea");
			String area_id = "";
			if(obj != null) area_id = obj.toString();
			DbTask tt=  (DbTask)request.getSession().getAttribute("fxtask");
			String task_id = tt.getId();
			String a= constantCostDbArgService.getPlant(area_id,task_id) ;
			return a;
			
		}

}
