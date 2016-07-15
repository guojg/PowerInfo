package com.github.totalquantity.totaldata.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
	

}
