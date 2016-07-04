package com.github.totalquantity.sysdict.controller;

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

import com.github.totalquantity.sysdict.entity.Sysdict;
import com.github.totalquantity.sysdict.service.SysdictService;


@Controller
@RequestMapping(value ="/sysdict")
public class SysdictController {
	@Autowired
	private SysdictService  sysdictService ;
	@RequestMapping(value = "/getData")
	public  void getData(HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject obj = new JSONObject();
        String domain_id = request.getParameter("domain_id")!=null?request.getParameter("domain_id"):"";
        obj.put("domain_id", domain_id);
        try {
			PrintWriter pw = response.getWriter();
			String resultJson= sysdictService.queryData(obj) ;
			pw.write(resultJson);
			pw.flush();
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
}
