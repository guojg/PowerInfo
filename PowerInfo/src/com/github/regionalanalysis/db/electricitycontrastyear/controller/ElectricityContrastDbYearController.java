package com.github.regionalanalysis.db.electricitycontrastyear.controller;


import java.io.PrintWriter;

import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.regionalanalysis.db.electricitycontrastyear.service.ElectricityContrastDbYearService;
import com.github.regionalanalysis.db.task.entity.DbTask;


@Controller
@RequestMapping(value = "/electricityContrastDbYearController")
public class ElectricityContrastDbYearController {
	@Autowired
	private ElectricityContrastDbYearService electricityContrastDbYearService;

	@RequestMapping(value = "/queryData",produces= "text/plain;charset=UTF-8")
	public @ResponseBody String queryData(HttpServletRequest request, HttpServletResponse response){

		response.setCharacterEncoding("UTF-8");
		String indeys = request.getParameter("index_ys");
		
		String id = request.getParameter("id");
		DbTask tt=  (DbTask)request.getSession().getAttribute("dbtask");
		String task_id = tt.getId();

		JSONObject obj = new JSONObject();
		obj.put("index_ys", indeys);
		obj.put("id", id);
		obj.put("task_id", task_id);

		return electricityContrastDbYearService.queryData(obj);
		
	}

	
	@RequestMapping(value = "/index")
	public String index(Long pid, HttpServletRequest request,
			HttpServletResponse re) {
		return "regionalanalysis/db/electricityContrastYear";
	}
	
	
	@RequestMapping(value = "/image")
	public String image(Long pid, HttpServletRequest request,
			HttpServletResponse re) {
		return "regionalanalysis/db/electricityContrastImageYear";
	}
	
	@RequestMapping(value = "/main")
	public String main(Long pid, HttpServletRequest request,
			HttpServletResponse re) {
		return "regionalanalysis/db/electricityContrastMainYear";
	}

	@RequestMapping("/exportData")
	public void exportData(HttpServletRequest request,
			HttpServletResponse response) {
			response.setCharacterEncoding("UTF-8");
			String index_text=request.getParameter("index_text");
			String index_ys=request.getParameter("index_ys");
			String id=request.getParameter("id");
			DbTask tt=  (DbTask)request.getSession().getAttribute("dbtask");
			String task_id = tt.getId();
			JSONObject obj = new JSONObject();
			obj.put("index_text", index_text);
			obj.put("index_ys", index_ys);
			obj.put("id", id);
			obj.put("task_id", task_id);

			try {
				electricityContrastDbYearService.ExportExcel(obj, response);
			}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 

	}

}
