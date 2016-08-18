package com.github.totalquantity.totaldata.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.basicData.model.BasicYear;
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
	@RequestMapping(value ="/showData1")
	public String showData1(HttpServletRequest request, HttpServletResponse response){
		 //return "basicData/nationalEconomy/nationalEconomyMain";
		 return "totalQuantity/totaldata/totaldata1";
	}
	@RequestMapping(value ="/showData2")
	public String showData2(HttpServletRequest request, HttpServletResponse response){
		 //return "basicData/nationalEconomy/nationalEconomyMain";
		 return "totalQuantity/totaldata/totaldata2";
	}
	@RequestMapping(value ="/showData3")
	public String showData3(HttpServletRequest request, HttpServletResponse response){
		 //return "basicData/nationalEconomy/nationalEconomyMain";
		 return "totalQuantity/totaldata/totaldata3";
	}
	@RequestMapping(value ="/showData4")
	public String showData4(HttpServletRequest request, HttpServletResponse response){
		 //return "basicData/nationalEconomy/nationalEconomyMain";
		 return "totalQuantity/totaldata/totaldata4";
	}

	@RequestMapping(value ="/showData5")
	public String showData5(HttpServletRequest request, HttpServletResponse response){
		 //return "basicData/nationalEconomy/nationalEconomyMain";
		 return "totalQuantity/totaldata/totaldata5";
	}
	@RequestMapping(value ="/showData6")
	public String showData6(HttpServletRequest request, HttpServletResponse response){
		 //return "basicData/nationalEconomy/nationalEconomyMain";
		 return "totalQuantity/totaldata/totaldata6";
	}
	


	@RequestMapping(value = "/queryData" ,produces="text/plain;charset=UTF-8")
	public @ResponseBody String queryData(HttpServletRequest request,
			HttpServletResponse response) {
		//response.setCharacterEncoding("UTF-8");
		// String jsonParam = request.getParameter("jsonParam");
		 String taskid = request.getParameter("taskid");
		 String year = request.getParameter("year")==null?"": request.getParameter("year");

		 String index_type = request.getParameter("index_type")==null?"": request.getParameter("index_type");

		JSONObject obj = new JSONObject();
		obj.put("year", year);
		obj.put("algorithm", index_type);
		obj.put("taskid",taskid);
		String resultJson = totalDataService.queryData(obj);
		/*try {
			PrintWriter pw = response.getWriter();
			pw.write(resultJson);
			pw.flush();
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		return resultJson;
	}
	
	@RequestMapping(value = "/getyears",produces="application/json;charset=UTF-8")
	public @ResponseBody List<BasicYear> getYears(HttpServletRequest request) {
		try {
			 String baseyear = request.getParameter("baseyear")==null?"2015": request.getParameter("baseyear");

			 String planyear = request.getParameter("planyear")==null?"2020": request.getParameter("planyear");

			JSONObject obj = new JSONObject();
			obj.put("baseyear", baseyear);
			obj.put("planyear", planyear);
			return totalDataService.getYears(obj);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	

	@RequestMapping(value = "/queryData6" ,produces="text/plain;charset=UTF-8")
	public @ResponseBody String queryData6(HttpServletRequest request,
			HttpServletResponse response) {
		//response.setCharacterEncoding("UTF-8");
		// String jsonParam = request.getParameter("jsonParam");
		 String taskid = request.getParameter("taskid");
		 String year = request.getParameter("year")==null?"": request.getParameter("year");

		 String baseyear = request.getParameter("baseyearInt")==null?"2015": request.getParameter("baseyearInt");
		 String algorithm = request.getParameter("algorithm")==null?"2015": request.getParameter("algorithm");

		JSONObject obj = new JSONObject();
		obj.put("year", year);
		obj.put("baseyearInt", Integer.parseInt(baseyear));
		obj.put("taskid",taskid);
		obj.put("algorithm",algorithm);
		/*obj.put("year", "2005,2006,2007,2015,2020");
		obj.put("baseyearInt", 2015);
		obj.put("taskid",11);
		obj.put("algorithm",5);*/
		String resultJson = totalDataService.queryData6(obj);
	
		return resultJson;
	}
}
