package com.github.balance.parparedata.basicparam.controller;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;




import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.balance.parparedata.basicparam.service.BasicParamService;

@Controller
@RequestMapping(value ="/basicparam")
public class BasicParamController {

	 @Autowired
	 private BasicParamService basicParamService ;
	 
	@RequestMapping(value ="/index")
	public String index(HttpServletRequest request, HttpServletResponse response){
		 return "balance/parpareData/basicparam";
	}



	@RequestMapping(value = "/queryData" ,produces="text/plain;charset=UTF-8")
	public @ResponseBody String queryData(HttpServletRequest request,
			HttpServletResponse response) {

		String resultJson = basicParamService.queryData();
		
		return resultJson;
	}
	
	@RequestMapping(value = "/saveData")
	public @ResponseBody
	String saveData(HttpServletRequest request){
		String editObj = request.getParameter("editObj");
		String stopyear = request.getParameter("stopyear");
		String startyear = request.getParameter("startyear");
		String byl = request.getParameter("byl");

		JSONObject jsonobj = new JSONObject();
		jsonobj.put("editObj", editObj);
		jsonobj.put("startyear", startyear);
		jsonobj.put("stopyear", stopyear);
		jsonobj.put("byl", byl);
		return basicParamService.saveData(jsonobj);
		
	}
	@RequestMapping(value ="/countData")
	public @ResponseBody String countData(){
		return  basicParamService.countData()+"";

	}
	@RequestMapping(value ="/initData",produces="application/json;charset=UTF-8")
	public @ResponseBody String initData(HttpServletRequest request, HttpServletResponse response){
		return  basicParamService.initData();
	}
	
}
