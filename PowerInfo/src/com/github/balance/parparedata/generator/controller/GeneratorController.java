package com.github.balance.parparedata.generator.controller;


import java.io.PrintWriter;

import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.balance.parparedata.generator.service.GeneratorService;

@Controller
@RequestMapping(value = "/generator")
public class GeneratorController {
	@Autowired
	private GeneratorService generatorService;

	@RequestMapping(value = "/queryData")
	public void queryData(HttpServletRequest request,
			HttpServletResponse response) {
		response.setCharacterEncoding("UTF-8");
		String pageNum = request.getParameter("page");
		String pageSize = request.getParameter("rows");
		String indexs=request.getParameter("indexs");
		String name=request.getParameter("name");
		String gene_name=request.getParameter("gene_name");
		String sort=request.getParameter("sort");
		String order=request.getParameter("order");

		if (pageNum == null  || "".equals(pageNum))
			pageNum = "1";
		if (pageSize == null || "".equals(pageSize))
			pageSize = "10";
		JSONObject obj = new JSONObject();
		obj.put("pageNum", pageNum);
		obj.put("pageSize", pageSize);
		obj.put("indexs", indexs);
		obj.put("name", name);
		obj.put("gene_name", gene_name);
		obj.put("sort", sort);
		obj.put("order", order);

		try {
			String resultJson = generatorService.queryData(obj);
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
			JSONObject jsonobj = new JSONObject();
			jsonobj.put("editObj", editObj);
			generatorService.addRecord(jsonobj);
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
			JSONObject jsonobj = new JSONObject();
			jsonobj.put("editObj", editObj);
			generatorService.updateRecord(jsonobj);
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
			String plantids = request.getParameter("plant_ids");
			JSONObject jsonobj = new JSONObject();
			jsonobj.put("deleteids",deleteids );
			jsonobj.put("plant_ids",plantids );
			generatorService.deleteRecord(jsonobj);
			return "1";
		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		}
	}
	@RequestMapping(value = "/main")
	public String index(Long pid, HttpServletRequest request,
			HttpServletResponse re) {
		return "balance/parpareData/generator";
	}
	@RequestMapping(value = "/openAddRecord")
	public String openAddRecord(Long pid, HttpServletRequest request,
			HttpServletResponse re) {
		return "balance/parpareData/addGenerator";
	}
	@RequestMapping(value = "/openUploadRecord")
	public String openUploadRecord(Long pid, HttpServletRequest request,
			HttpServletResponse re) {
		return "balance/parpareData/updateGenerator";
	}
	@RequestMapping("/exportData")
	public void exportData(HttpServletRequest request,
			HttpServletResponse response) {
			response.setCharacterEncoding("UTF-8");
			String indexs=request.getParameter("indexs");
			String name=request.getParameter("name");
			String gene_name=request.getParameter("gene_name");

			JSONObject obj = new JSONObject();
			obj.put("indexs", indexs);
			obj.put("name", name);
			obj.put("gene_name", gene_name);

			try {
				generatorService.ExportExcel(obj, response);
			}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 

	}
	
	@RequestMapping(value = "/getPlant", produces="application/json;charset=UTF-8")
	public  @ResponseBody String getPlant(HttpServletRequest request,
			HttpServletResponse response) {
		String a= generatorService.getPlant() ;
		return a;
		
	}
	@RequestMapping(value = "/getdylx")
	public @ResponseBody
	String getDylx(HttpServletRequest request) {
		try {
			String plant_id = request.getParameter("plant_id");
			JSONObject jsonobj = new JSONObject();
			jsonobj.put("plant_id",plant_id );
			
			return generatorService.getDylxByPlantId(jsonobj);
		} catch (Exception e) {
			e.printStackTrace();
			return "-1";
		}
	}

}
