package com.github.totalquantity.prepareData.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.totalquantity.prepareData.service.PrepareDataService;
import com.github.totalquantity.totaldata.service.TotalDataService;

@Controller
@RequestMapping(value ="/prepareData")
public class PrepareDataController {
	 @Autowired
	 private PrepareDataService prepareDataService ;
	 
	@RequestMapping(value ="/index")
	public String index(long pid,HttpServletRequest request, HttpServletResponse response){
		 //return "basicData/nationalEconomy/nationalEconomyMain";
		request.setAttribute("p_id", pid);
		 return "totalQuantity/prepareData/prepareDataMain";
	}
	
	@RequestMapping(value ="/imageData")
	public String imageData(String index_type ,HttpServletRequest request, HttpServletResponse response){
		 //return "basicData/nationalEconomy/nationalEconomyMain";
		request.setAttribute("index_type", index_type);
		 return "totalQuantity/prepareData/prepareDataPic";
	}
	
	@RequestMapping(value ="/showData")
	public String showData(String index_type ,HttpServletRequest request, HttpServletResponse response){
		 //return "basicData/nationalEconomy/nationalEconomyMain";
		request.setAttribute("index_type", index_type);
		 return "totalQuantity/prepareData/prepareData";
	}


	@RequestMapping(value = "/queryData",produces= "text/plain;charset=UTF-8")
	public @ResponseBody String queryData(HttpServletRequest request,
			HttpServletResponse response) {
		 String taskid = request.getParameter("taskid");
		 String planyear = request.getParameter("planyear")==null?"": request.getParameter("planyear");

		 String index_type = request.getParameter("index_type")==null?"": request.getParameter("index_type");

		JSONObject obj = new JSONObject();
		obj.put("year", planyear);
		obj.put("index_type", index_type);
		//String algorithm = param.getString("algorithm") ;
		//String taskid = request.getSession().getAttribute("taskid").toString();
		obj.put("taskid",taskid);
		return prepareDataService.queryData(obj);
	
	}
	@RequestMapping(value = "/saveData")
	public @ResponseBody
	String saveData(HttpServletRequest request) {
		try {
			String editObj = request.getParameter("editObj");
			String taskid = request.getParameter("taskid");
			JSONObject jsonobj = new JSONObject();
			jsonobj.put("editObj", editObj);
			jsonobj.put("taskid", taskid);
			prepareDataService.saveData(jsonobj);
			return "1";
		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		}
	}
}
