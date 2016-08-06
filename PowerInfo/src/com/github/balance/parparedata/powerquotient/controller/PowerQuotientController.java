package com.github.balance.parparedata.powerquotient.controller;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.balance.parparedata.powerquotient.service.PowerQuotientService;
import com.github.totalquantity.totaldata.service.TotalDataService;

@Controller
@RequestMapping(value ="/powerQuotient")
public class PowerQuotientController {

	 @Autowired
	 private PowerQuotientService powerQuotientService ;
	 
	@RequestMapping(value ="/index")
	public String index(HttpServletRequest request, HttpServletResponse response){
		 return "balance/parpareData/powerQuotient";
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
		obj.put("type", index_type);
		obj.put("taskid",taskid);
		String resultJson = powerQuotientService.queryData(obj);
		
		return resultJson;
	}
	@RequestMapping(value = "/saveData")
	public @ResponseBody
	String saveData(HttpServletRequest request){
		String editObj = request.getParameter("editObj");
		JSONObject jsonobj = new JSONObject();
		jsonobj.put("editObj", editObj);
		jsonobj.put("taskid", "1");
		return powerQuotientService.saveData(jsonobj);
		
	}

}
