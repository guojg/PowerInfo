package com.github.regionalanalysis.db.generatorcontrastyear.controller;



import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.regionalanalysis.db.generatorcontrastyear.service.GeneratorContrastDbYearService;
import com.github.regionalanalysis.db.task.entity.DbTask;


@Controller
@RequestMapping(value = "/generatorContrastDbYearController")
public class GeneratorContrastDbYearController {
	@Autowired
	private GeneratorContrastDbYearService generatorContrastDbYearService;

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

		return generatorContrastDbYearService.queryData(obj);
		
	}

	
	@RequestMapping(value = "/index")
	public String index(Long pid, HttpServletRequest request,
			HttpServletResponse re) {
		return "regionalanalysis/db/generatorContrastYear";
	}
	
	
	@RequestMapping(value = "/image")
	public String image(Long pid, HttpServletRequest request,
			HttpServletResponse re) {
		return "regionalanalysis/db/generatorContrastImageYear";
	}
	
	@RequestMapping(value = "/main")
	public String main(Long pid, HttpServletRequest request,
			HttpServletResponse re) {
		return "regionalanalysis/db/generatorContrastMainYear";
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
				generatorContrastDbYearService.ExportExcel(obj, response);
			}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 

	}

}
