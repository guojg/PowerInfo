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

	@RequestMapping(value = "/queryData")
	public @ResponseBody String queryData(HttpServletRequest request, HttpServletResponse response){

		response.setCharacterEncoding("UTF-8");
		String indexs = request.getParameter("index_xs");
		String gene_id = request.getParameter("gene_id");

		JSONObject obj = new JSONObject();
		obj.put("index_xs", indexs);
		obj.put("gene_id", gene_id);

		return generatorContrastService.queryData(obj);
		
	}

	
	@RequestMapping(value = "/index")
	public String index(Long pid, HttpServletRequest request,
			HttpServletResponse re) {
		return "regionalanalysis/generatorContrast";
	}

	@RequestMapping("/exportData")
	public void exportData(HttpServletRequest request,
			HttpServletResponse response) {
			response.setCharacterEncoding("UTF-8");
			String indexs = request.getParameter("index_xs");
			String index_text=request.getParameter("index_text");
			JSONObject obj = new JSONObject();
			obj.put("index_xs", indexs);
			obj.put("index_text", index_text);
			try {
				generatorContrastService.ExportExcel(obj, response);
			}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 

	}

}
