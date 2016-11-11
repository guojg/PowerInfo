package com.github.regionalanalysis.preparedata.constantcostarg.controller;




import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.regionalanalysis.preparedata.constantcostarg.service.ConstantCostArgService;

@Controller
@RequestMapping(value ="/constantCostArgController")
public class ConstantCostArgController {

	 @Autowired
	 private ConstantCostArgService constantCostArgService ;
	 
	@RequestMapping(value ="/index")
	public String index(HttpServletRequest request, HttpServletResponse response){
		 return "regionalanalysis/preparedata/constantcostarg";
	}
	@RequestMapping(value ="/detail")
	public String detail(HttpServletRequest request, HttpServletResponse response){
		 return "regionalanalysis/preparedata/constantcostargdetail";
	}
	
	@RequestMapping(value ="/saveData")
	public  @ResponseBody String saveData(HttpServletRequest request, HttpServletResponse response){
		Map map= request.getParameterMap();
		Object obj=  request.getSession().getAttribute("maparea");
		String area_id = "";
		if(obj != null) area_id = obj.toString();
		return constantCostArgService.saveData(map,area_id);
	}
	 
	 @RequestMapping(value = "/initData",produces="application/json;charset=UTF-8")
		public @ResponseBody String initData(HttpServletRequest request) {
			String id = request.getParameter("id")!=null?request.getParameter("id"):"";

		 	return constantCostArgService.initData(id);
			
		}
	 
		@RequestMapping(value = "/getPlant", produces="application/json;charset=UTF-8")
		public  @ResponseBody String getPlant(HttpServletRequest request,
				HttpServletResponse response) {
			Object obj=  request.getSession().getAttribute("maparea");
			String area_id = "";
			if(obj != null) area_id = obj.toString();
			String a= constantCostArgService.getPlant(area_id) ;
			return a;
			
		}

}
