package com.github.regionalanalysis.fx.generatorcontrast.controller;


import java.io.PrintWriter;

import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.regionalanalysis.db.generatorcontrast.service.GeneratorContrastDbService;
import com.github.regionalanalysis.db.task.entity.DbTask;
import com.github.regionalanalysis.fx.generatorcontrast.service.GeneratorContrastFxService;


@Controller
@RequestMapping(value = "/generatorContrastFxController")
public class GeneratorContrastFxController {
	@Autowired
	private GeneratorContrastFxService generatorContrastFxService;

	@RequestMapping(value = "/queryData",produces= "text/plain;charset=UTF-8")
	public @ResponseBody String queryData(HttpServletRequest request, HttpServletResponse response){

		response.setCharacterEncoding("UTF-8");
		String indexs = request.getParameter("index_xs");
		String indeys = request.getParameter("index_ys");
		
		String id = request.getParameter("id");
		DbTask tt=  (DbTask)request.getSession().getAttribute("fxtask");
		String task_id = tt.getId();

		JSONObject obj = new JSONObject();
		obj.put("index_xs", indexs);
		obj.put("index_ys", indeys);
		obj.put("id", id);
		obj.put("task_id", task_id);

		return generatorContrastFxService.queryData(obj);
		
	}
	@RequestMapping(value = "/queryDataPie",produces= "text/plain;charset=UTF-8")
	public @ResponseBody String queryDataPie(HttpServletRequest request, HttpServletResponse response){

		response.setCharacterEncoding("UTF-8");


		return generatorContrastFxService.queryDataPie();
		
	}

	
	@RequestMapping(value = "/index")
	public String index(Long pid, HttpServletRequest request,
			HttpServletResponse re) {
		return "regionalanalysis/fx/generatorContrast";
	}
	
	
	@RequestMapping(value = "/image")
	public String image(Long pid, HttpServletRequest request,
			HttpServletResponse re) {
		return "regionalanalysis/fx/generatorContrastImage";
	}
	
	@RequestMapping(value = "/main")
	public String main(Long pid, HttpServletRequest request,
			HttpServletResponse re) {
		return "regionalanalysis/fx/generatorContrastMain";
	}

	@RequestMapping("/exportData")
	public void exportData(HttpServletRequest request,
			HttpServletResponse response) {
			response.setCharacterEncoding("UTF-8");
			String indexs = request.getParameter("index_xs");
			String index_text=request.getParameter("index_text");
			String index_ys=request.getParameter("index_ys");
			String id=request.getParameter("id");
			DbTask tt=  (DbTask)request.getSession().getAttribute("fxtask");
			String task_id = tt.getId();
			JSONObject obj = new JSONObject();
			obj.put("index_xs", indexs);
			obj.put("index_text", index_text);
			obj.put("index_ys", index_ys);
			obj.put("id", id);
			obj.put("task_id", task_id);

			try {
				generatorContrastFxService.ExportExcel(obj, response);
			}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 

	}

}
