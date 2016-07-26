package com.github.totalquantity.sysdict.controller;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.totalquantity.sysdict.service.SysdictService;


@Controller
@RequestMapping(value ="/sysdict")
public class SysdictController {
	@Autowired
	private SysdictService  sysdictService ;
	@RequestMapping(value = "/getData", produces="application/json;charset=UTF-8")
	public  @ResponseBody String getData(HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject obj = new JSONObject();
        String domain_id = request.getParameter("domain_id")!=null?request.getParameter("domain_id"):"";
        obj.put("domain_id", domain_id);
       
		String a= sysdictService.queryData(obj) ;
		return a;
	
	
	}
}
