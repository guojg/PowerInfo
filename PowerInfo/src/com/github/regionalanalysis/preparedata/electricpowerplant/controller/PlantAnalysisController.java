package com.github.regionalanalysis.preparedata.electricpowerplant.controller;


import java.io.PrintWriter;
import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.regionalanalysis.preparedata.electricpowerplant.service.PlantAnalysisService;


@Controller
@RequestMapping(value = "/plantAnalysis")
public class PlantAnalysisController {
	@Autowired
	private PlantAnalysisService plantAnalysisService;

	@RequestMapping(value = "/queryData")
	public void queryData(HttpServletRequest request,
			HttpServletResponse response) {
		response.setCharacterEncoding("UTF-8");
		String pageNum = request.getParameter("page");
		String pageSize = request.getParameter("rows");
		String name=request.getParameter("name");
		if (pageNum == null  || "".equals(pageNum))
			pageNum = "1";
		if (pageSize == null || "".equals(pageSize))
			pageSize = "10";
		JSONObject obj = new JSONObject();
		obj.put("pageNum", pageNum);
		obj.put("pageSize", pageSize);
		obj.put("name", name);
		try {
			String resultJson = plantAnalysisService.queryData(obj);
			PrintWriter pw = response.getWriter();
			pw.write(resultJson);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/addRecord")
	public @ResponseBody
	String addRecord(HttpServletRequest request) {
		try {
			String editObj = request.getParameter("editObj");
			String area_id =request.getParameter("area_id");
			JSONObject jsonobj = new JSONObject();
			jsonobj.put("editObj", editObj);
			jsonobj.put("area_id", area_id);
			plantAnalysisService.addRecord(jsonobj);
			return "1";
		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		}
	}
	@RequestMapping(value = "/updateRecord")
	public @ResponseBody
	String updateRecord(HttpServletRequest request) {
		try {
			String editObj = request.getParameter("editObj");
			String area_id =request.getParameter("area_id");
			
			JSONObject jsonobj = new JSONObject();
			jsonobj.put("area_id", area_id);

			jsonobj.put("editObj", editObj);
			plantAnalysisService.updateRecord(jsonobj);
			return "1";
		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		}
	}
	@RequestMapping(value = "/deleteRecord")
	public @ResponseBody
	String deleteRecord(HttpServletRequest request) {
		try {
			String deleteids = request.getParameter("ids");
			JSONObject jsonobj = new JSONObject();
			jsonobj.put("deleteids",deleteids );
			plantAnalysisService.deleteRecord(jsonobj);
			return "1";
		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		}
	}
	@RequestMapping(value = "/main")
	public String index(Long pid, HttpServletRequest request,
			HttpServletResponse re) {
		return "regionalanalysis/preparedata/electricPowerPlant";
	}
	@RequestMapping(value = "/openAddRecord")
	public String openAddRecord(Long pid, HttpServletRequest request,
			HttpServletResponse re) {
		return "regionalanalysis/preparedata/addPlant";
	}
	@RequestMapping(value = "/openUploadRecord")
	public String openUploadRecord(Long pid, HttpServletRequest request,
			HttpServletResponse re) {
		return "regionalanalysis/preparedata/updatePlant";
	}
	@RequestMapping("/exportData")
	public void exportData(HttpServletRequest request,
			HttpServletResponse response) {
			response.setCharacterEncoding("UTF-8");
			String indexs=request.getParameter("indexs");
			String name=request.getParameter("name");
			JSONObject obj = new JSONObject();
			obj.put("indexs", indexs);
			obj.put("name", name);
			try {
				plantAnalysisService.ExportExcel(obj, response);
			}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 

	}

}
