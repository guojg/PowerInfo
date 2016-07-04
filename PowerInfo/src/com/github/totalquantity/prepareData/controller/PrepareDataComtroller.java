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
public class PrepareDataComtroller {
	 @Autowired
	 private PrepareDataService prepareDataService ;
	 
	@RequestMapping(value ="/index")
	public String index(HttpServletRequest request, HttpServletResponse response){
		 //return "basicData/nationalEconomy/nationalEconomyMain";
		 return "totalQuantity/prepareData/prepareDataMain";
	}
	
	@RequestMapping(value ="/imageData")
	public String imageData(HttpServletRequest request, HttpServletResponse response){
		 //return "basicData/nationalEconomy/nationalEconomyMain";
		 return "totalQuantity/prepareData/prepareDataPic";
	}
	
	@RequestMapping(value ="/showData")
	public String showData(HttpServletRequest request, HttpServletResponse response){
		 //return "basicData/nationalEconomy/nationalEconomyMain";
		 return "totalQuantity/prepareData/prepareData";
	}


	@RequestMapping(value = "/queryData")
	public void queryData(HttpServletRequest request,
			HttpServletResponse response) {
		response.setCharacterEncoding("UTF-8");
		 String taskid = request.getParameter("taskid");
		 String planyear = request.getParameter("planyear")==null?"": request.getParameter("planyear");

		 String index_type = request.getParameter("index_type")==null?"": request.getParameter("index_type");

		JSONObject obj = new JSONObject();
		obj.put("year", planyear);
		obj.put("index_type", index_type);
		//String algorithm = param.getString("algorithm") ;
		//String taskid = request.getSession().getAttribute("taskid").toString();
		obj.put("taskid",taskid);
		String resultJson = prepareDataService.queryData(obj);
		try {
			PrintWriter pw = response.getWriter();
			pw.write(resultJson);
			pw.flush();
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
