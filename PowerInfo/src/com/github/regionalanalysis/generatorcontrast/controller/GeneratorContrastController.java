package com.github.regionalanalysis.generatorcontrast.controller;


import java.io.PrintWriter;

import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.regionalanalysis.generatorcontrast.service.GeneratorContrastService;

@Controller
@RequestMapping(value = "/generatorContrastController")
public class GeneratorContrastController {
	@Autowired
	private GeneratorContrastService generatorContrastService;

	@RequestMapping(value = "/queryData",produces= "text/plain;charset=UTF-8")
	public @ResponseBody String queryData(HttpServletRequest request, HttpServletResponse response){

		response.setCharacterEncoding("UTF-8");
		String indexs = request.getParameter("index_xs");
		String indeys = request.getParameter("index_ys");
		Object object=  request.getSession().getAttribute("maparea");
		String area_id = "";
		if(object != null) area_id = object.toString();
		String id = request.getParameter("id");

		JSONObject obj = new JSONObject();
		obj.put("index_xs", indexs);
		obj.put("index_ys", indeys);
		obj.put("id", id);
		obj.put("area_id", area_id);
		return generatorContrastService.queryData(obj);
		
	}

	
	@RequestMapping(value = "/index")
	public String index(Long pid, HttpServletRequest request,
			HttpServletResponse re) {
		return "regionalanalysis/generatorContrast";
	}
	
	
	@RequestMapping(value = "/image")
	public String image(Long pid, HttpServletRequest request,
			HttpServletResponse re) {
		return "regionalanalysis/generatorContrastImage";
	}
	
	@RequestMapping(value = "/main")
	public String main(Long pid, HttpServletRequest request,
			HttpServletResponse re) {
		return "regionalanalysis/generatorContrastMain";
	}

	@RequestMapping("/exportData")
	public void exportData(HttpServletRequest request,
			HttpServletResponse response) {
			response.setCharacterEncoding("UTF-8");
			String indexs = request.getParameter("index_xs");
			String index_text=request.getParameter("index_text");
			String index_ys=request.getParameter("index_ys");
			String id=request.getParameter("id");
			Object object=  request.getSession().getAttribute("maparea");
			String area_id = "";
			if(object != null) area_id = object.toString();
			JSONObject obj = new JSONObject();
			obj.put("index_xs", indexs);
			obj.put("index_text", index_text);
			obj.put("index_ys", index_ys);
			obj.put("id", id);
			obj.put("area_id", area_id);

			try {
				generatorContrastService.ExportExcel(obj, response);
			}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 

	}

}
