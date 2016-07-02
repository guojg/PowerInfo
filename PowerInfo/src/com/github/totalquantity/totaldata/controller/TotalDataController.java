package com.github.totalquantity.totaldata.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.basicData.service.BasicDataService;
import com.github.totalquantity.totaldata.service.TotalDataService;

@Controller
@RequestMapping(value ="/totalData")
public class TotalDataController {

	 @Autowired
	 private TotalDataService totalDataService ;
	 
	@RequestMapping(value ="/index")
	public String index(HttpServletRequest request, HttpServletResponse response){
		 //return "basicData/nationalEconomy/nationalEconomyMain";
		 return "totalQuantity/totaldata/totaldataMain";
	}
	
	@RequestMapping(value ="/imageData")
	public String imageData(HttpServletRequest request, HttpServletResponse response){
		 //return "basicData/nationalEconomy/nationalEconomyMain";
		 return "totalQuantity/totaldata/totaldataPic";
	}
	
	@RequestMapping(value ="/showData")
	public String showData(HttpServletRequest request, HttpServletResponse response){
		 //return "basicData/nationalEconomy/nationalEconomyMain";
		 return "totalQuantity/totaldata/totaldata";
	}


	@RequestMapping(value = "/queryData")
	public void queryData(HttpServletRequest request,
			HttpServletResponse response) {
		response.setCharacterEncoding("UTF-8");
		// String jsonParam = request.getParameter("jsonParam");
		JSONObject obj = new JSONObject();
		obj.put("year", "2020,2021");
		obj.put("algorithm", "1,2,3,4,5,6");
		//String algorithm = param.getString("algorithm") ;
		//String taskid = request.getSession().getAttribute("taskid").toString();
		obj.put("taskid", "111");
		String resultJson = totalDataService.queryData(obj);
		try {
			PrintWriter pw = response.getWriter();
			pw.write(resultJson);
			pw.flush();
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

}
