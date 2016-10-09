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
	
	@RequestMapping(value = "/getDataByNotCondition", produces="application/json;charset=UTF-8")
	public  @ResponseBody String getDataNotCondition(HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject obj = new JSONObject();
        String domain_id = request.getParameter("domain_id")!=null?request.getParameter("domain_id"):"";
        obj.put("domain_id", domain_id);
        String condition = request.getParameter("condition")!=null?request.getParameter("condition"):"20000";
        obj.put("condition", condition);
		String a= sysdictService.queryDataNotCondition(obj) ;
		return a;
	
	
	}
	
	@RequestMapping(value = "/getDataByCodeValue", produces="application/json;charset=UTF-8")
	public  @ResponseBody String getDataByCodeValue(HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject obj = new JSONObject();
        String domain_id = request.getParameter("domain_id")!=null?request.getParameter("domain_id"):"";
        obj.put("domain_id", domain_id);
        String condition = request.getParameter("condition")!=null?request.getParameter("condition"):"";
        obj.put("condition", condition);
		String a= sysdictService.getDataByCodeValue(obj) ;
		return a;
		
	}
	
	@RequestMapping(value = "/getBalanceYears", produces="application/json;charset=UTF-8")
	public  @ResponseBody String getBalanceYears(HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject obj = new JSONObject();
        String year = request.getParameter("year")!=null?request.getParameter("year"):"";
        obj.put("year", year);
       
		String a= sysdictService.getBalanceYears(obj) ;
		return a;
		
	}
	
	/**
	 * 电网企业
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getCompany", produces="application/json;charset=UTF-8")
	public  @ResponseBody String getCompany(HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject obj = new JSONObject();
        String area_id = request.getParameter("area_id")!=null?request.getParameter("area_id"):"";
        obj.put("area_id", area_id);
       
		String a= sysdictService.queryCompany(obj) ;
		return a;
	
	
	}
	
}
