package com.github.regionalanalysis.db.electricpowerplant.controller;


import java.io.PrintWriter;

import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.regionalanalysis.db.electricpowerplant.service.PlantAnalysisDbService;


@Controller
@RequestMapping(value = "/plantAnalysisdb")
public class PlantAnalysisDbController {
	@Autowired
	private PlantAnalysisDbService plantAnalysisDbService;

	@RequestMapping(value = "/queryData")
	public void queryData(HttpServletRequest request,
			HttpServletResponse response) {
		response.setCharacterEncoding("UTF-8");
		String pageNum = request.getParameter("page");
		String pageSize = request.getParameter("rows");
		String name=request.getParameter("name");
		String task_id=request.getParameter("task_id");
		if (pageNum == null  || "".equals(pageNum))
			pageNum = "1";
		if (pageSize == null || "".equals(pageSize))
			pageSize = "10";
		JSONObject obj = new JSONObject();
		obj.put("pageNum", pageNum);
		obj.put("pageSize", pageSize);
		obj.put("name", name);
		obj.put("task_id", task_id);
		try {
			String resultJson = plantAnalysisDbService.queryData(obj);
			PrintWriter pw = response.getWriter();
			pw.write(resultJson);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	@RequestMapping(value = "/updateRecord")
	public @ResponseBody
	String updateRecord(HttpServletRequest request) {
		try {
			String editObj = request.getParameter("editObj");
			String task_id =request.getParameter("task_id");
			String area_id =request.getParameter("area_id");

			JSONObject jsonobj = new JSONObject();
			jsonobj.put("task_id", task_id);
			jsonobj.put("area_id", area_id);

			jsonobj.put("editObj", editObj);
			plantAnalysisDbService.updateRecord(jsonobj);
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
			String task_id = request.getParameter("task_id");
			JSONObject jsonobj = new JSONObject();
			jsonobj.put("deleteids",deleteids );
			jsonobj.put("task_id",task_id );

			plantAnalysisDbService.deleteRecord(jsonobj);
			return "1";
		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		}
	}
	@RequestMapping(value = "/main")
	public String index(Long pid, HttpServletRequest request,
			HttpServletResponse re) {
		return "regionalanalysis/db/electricPowerPlant";
	}
	@RequestMapping(value = "/mainyear")
	public String indexyear(Long pid, HttpServletRequest request,
			HttpServletResponse re) {
		return "regionalanalysis/db/electricPowerPlantYear";
	}
	@RequestMapping(value = "/detailData")
	public void detailData(HttpServletRequest request,
			HttpServletResponse response) {
		String id = request.getParameter("id");
		String task_id = request.getParameter("task_id");

		try {
			String resultJson = plantAnalysisDbService.getPlantById(id,task_id);
			PrintWriter pw = response.getWriter();
			pw.write(resultJson);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@RequestMapping(value = "/getFdjByDc")
	public void getFdjByDc(HttpServletRequest request,
			HttpServletResponse response) {
		String id = request.getParameter("id");
		String task_id = request.getParameter("task_id");

		try {
			String resultJson = plantAnalysisDbService.getFdjByDc(id,task_id);
			PrintWriter pw = response.getWriter();
			pw.write(resultJson);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@RequestMapping(value = "/detail")
	public String detail(Long pid, HttpServletRequest request,
			HttpServletResponse re) {
		return "regionalanalysis/db/detailPlant";
	}

	@RequestMapping(value = "/openUploadRecord")
	public String openUploadRecord(Long pid, HttpServletRequest request,
			HttpServletResponse re) {
		return "regionalanalysis/db/updatePlant";
	}
	@RequestMapping("/exportData")
	public void exportData(HttpServletRequest request,
			HttpServletResponse response) {
			response.setCharacterEncoding("UTF-8");
			String indexs=request.getParameter("indexs");
			String name=request.getParameter("name");
			String task_id = request.getParameter("task_id");

			JSONObject obj = new JSONObject();
			obj.put("indexs", indexs);
			obj.put("name", name);
			obj.put("task_id", task_id);

			try {
				plantAnalysisDbService.ExportExcel(obj, response);
			}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 

	}

}
