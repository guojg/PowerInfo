package com.github.regionalanalysis.generatorset.controller;



import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.regionalanalysis.generatorset.service.GeneratorSetService;

@Controller
@RequestMapping(value ="/generatorSetController")
public class GeneratorSetController {

	 @Autowired
	 private GeneratorSetService generatorSetService ;
	 
	@RequestMapping(value ="/index")
	public String index(HttpServletRequest request, HttpServletResponse response){
		return "regionalanalysis/generatorset";
	}
	@RequestMapping(value ="/main")
	public String main(HttpServletRequest request, HttpServletResponse response){
		return "regionalanalysis/generatorsetmain";
	}
	
	
		@RequestMapping(value ="/queryData")
		public @ResponseBody String queryData(HttpServletRequest request, HttpServletResponse response){
			response.setCharacterEncoding("UTF-8");
			String pageNum = request.getParameter("page");
			String pageSize = request.getParameter("rows");
			String gene_name = request.getParameter("gene_name")==null?"":request.getParameter("gene_name");

			String elec_name = request.getParameter("elec_name")==null?"":request.getParameter("elec_name");

			
			if (pageNum == null || "null".equals(pageNum) || "".equals(pageNum))
				pageNum = "1";
			if (pageSize == null || "null".equals(pageSize)
					|| "".equals(pageSize))
				pageSize = "10";
			JSONObject obj = new JSONObject();
			obj.put("pageNum", pageNum);
			obj.put("pageSize", pageSize);
			obj.put("gene_name", gene_name);
			obj.put("elec_name", elec_name);
			return generatorSetService.queryData(obj);
			
		}
		

		@RequestMapping("/exportData")
		public void exportData(HttpServletRequest request,
				HttpServletResponse response) {
				response.setCharacterEncoding("UTF-8");
				String gene_name = request.getParameter("gene_name")==null?"":request.getParameter("gene_name");

				String elec_name = request.getParameter("elec_name")==null?"":request.getParameter("elec_name");

				
				JSONObject obj = new JSONObject();
				obj.put("gene_name", gene_name);
				obj.put("elec_name", elec_name);
				try {
					generatorSetService.ExportExcel(obj, response);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 

		}

}
