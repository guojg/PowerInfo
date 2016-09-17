package com.github.regionalanalysis.preparedata.constantcostarg.controller;



import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.balance.parparedata.powerquotient.service.PowerQuotientService;
import com.github.balance.parparedata.senddata.model.Domain;
import com.github.regionalanalysis.preparedata.constantcostarg.service.ConstantCostArgService;
import com.github.totalquantity.totaldata.service.TotalDataService;

@Controller
@RequestMapping(value ="/constantCostArgController")
public class ConstantCostArgController {

	 @Autowired
	 private ConstantCostArgService constantCostArgService ;
	 
	@RequestMapping(value ="/index")
	public String index(HttpServletRequest request, HttpServletResponse response){
		 return "regionalanalysis/preparedata/constantcostarg";
	}
	
	@RequestMapping(value ="/saveData")
	public  @ResponseBody String saveData(HttpServletRequest request, HttpServletResponse response){
		Map map= request.getParameterMap();

		constantCostArgService.saveData(map);
		 return "1";
	}
	 
	 @RequestMapping(value = "/initData",produces="application/json;charset=UTF-8")
		public @ResponseBody String initData(HttpServletRequest request) {
			String id = request.getParameter("id")!=null?request.getParameter("id"):"";

		 	return constantCostArgService.initData(id);
			
		}
	 
		@RequestMapping(value = "/getPlant", produces="application/json;charset=UTF-8")
		public  @ResponseBody String getPlant(HttpServletRequest request,
				HttpServletResponse response) {
	       
			String a= constantCostArgService.getPlant() ;
			return a;
			
		}

}
