package com.github.regionalanalysis.fx.coalcost.controller;


import java.io.PrintWriter;

import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.regionalanalysis.fx.coalcost.service.CoalCostFxService;

@Controller
@RequestMapping(value = "/coalCostfx")
public class CoalCostFxController {
	@Autowired
	private CoalCostFxService coalCostFxService;

	@RequestMapping(value = "/queryData")
	public void queryData(HttpServletRequest request,
			HttpServletResponse response) {
		response.setCharacterEncoding("UTF-8");
		String indexs = request.getParameter("index_xs");
		String fdj_id = request.getParameter("fdj_id");
		String task_id = request.getParameter("task_id");

		JSONObject obj = new JSONObject();
		obj.put("index_xs", indexs);
		obj.put("fdj_id", fdj_id);
		obj.put("task_id", task_id);
		try {
			String resultJson = coalCostFxService.queryData(obj);
			PrintWriter pw = response.getWriter();
			pw.write(resultJson);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/saveData")
	public @ResponseBody
	String saveData(HttpServletRequest request) {
		try {
			String editObj = request.getParameter("editObj");
			String fdj_id = request.getParameter("fdj_id");
			String area_id = request.getParameter("area_id");
			String task_id = request.getParameter("task_id");

			JSONObject jsonobj = new JSONObject();
			jsonobj.put("editObj", editObj);
			jsonobj.put("fdj_id", fdj_id);
			jsonobj.put("area_id", area_id);
			jsonobj.put("task_id", task_id);

			coalCostFxService.saveData(jsonobj);
			return "1";
		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		}
	}
	@RequestMapping(value = "/main")
	public String index(Long pid, HttpServletRequest request,
			HttpServletResponse re) {
		return "regionalanalysis/fx/CoalCost";
	}
	
	@RequestMapping(value = "/detail")
	public String detail(Long pid, HttpServletRequest request,
			HttpServletResponse re) {
		return "regionalanalysis/fx/CoalCostDetail";
	}

	@RequestMapping("/exportData")
	public void exportData(HttpServletRequest request,
			HttpServletResponse response) {
			response.setCharacterEncoding("UTF-8");
			String indexs = request.getParameter("index_xs");
			String index_text=request.getParameter("index_text");
			String fdj_id=request.getParameter("fdj_id");
			String task_id = request.getParameter("task_id");

			JSONObject obj = new JSONObject();
			obj.put("index_xs", indexs);
			obj.put("index_text", index_text);
			obj.put("fdj_id", fdj_id);
			obj.put("task_id", task_id);

			try {
				coalCostFxService.ExportExcel(obj, response);
			}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 

	}

}
