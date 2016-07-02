package com.github.basicData.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.basicData.model.BasicYear;
import com.github.basicData.service.BasicDataService;
import com.github.common.tree.TreeUtil;
import com.github.common.util.JsonUtils;

@Controller
@RequestMapping(value = "/basicData")
public class BasicDataController {
	@Autowired
	private BasicDataService basicDataService;

	@RequestMapping(value = "/queryData")
	public void queryData(HttpServletRequest request,
			HttpServletResponse response) {
		response.setCharacterEncoding("UTF-8");
		String years = request.getParameter("years");
		
		JSONObject obj = new JSONObject();
		obj.put("years", years);
		obj.put("pid", request.getSession().getAttribute("pid"));
		String resultJson = basicDataService.queryData(obj);
		try {
			PrintWriter pw = response.getWriter();
			pw.write(resultJson);
			pw.flush();
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/saveData")
	public @ResponseBody
	String saveData(HttpServletRequest request) {
		try {
			String editObj = request.getParameter("editObj");
			JSONObject jsonobj = new JSONObject();
			jsonobj.put("editObj", editObj);
			basicDataService.saveData(jsonobj);
			return "1";
		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		}
	}

	@RequestMapping(value = "/addleaf")
	public @ResponseBody String addLeaf(HttpServletRequest request) {
		try {
			String data = request.getParameter("data");
			JSONObject jsonobj = new JSONObject();
			jsonobj.put("data", data);
			JSONObject returnobj=new JSONObject();
			String leafinfo=TreeUtil.createTreeJson(JsonUtils.transformBoToTree(basicDataService.addLeaf(jsonobj))) ;
			returnobj.put("leaf", leafinfo);
			returnobj.put("flag", "1");
			return returnobj.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		}
	}
	@RequestMapping(value = "/addyear")
	public @ResponseBody String addYear(HttpServletRequest request) {
		try {
			String data = request.getParameter("data");
			JSONObject jsonobj = new JSONObject();
			jsonobj.put("data", data);
			JSONObject returnobj=new JSONObject();
			String returnflag=basicDataService.addYear(jsonobj) ;
			returnobj.put("flag", returnflag);
			return returnobj.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		}
	}
	@RequestMapping(value = "/getyears",produces="application/json;charset=UTF-8")
	public @ResponseBody List<BasicYear> getYears(HttpServletRequest request) {
		try {
			return basicDataService.getYears();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	@RequestMapping(value = "/updateleaf")
	public @ResponseBody String updateLeaf(HttpServletRequest request) {
		try {
			String data = request.getParameter("data");
			JSONObject jsonobj = new JSONObject();
			jsonobj.put("data", data);
			JSONObject returnobj=new JSONObject();
			String returnFlag=basicDataService.updatLeaf(jsonobj) ;
			returnobj.put("flag", returnFlag);
			return returnobj.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		}
	}
	@RequestMapping(value = "/removeleaf")
	public @ResponseBody String removeLeaf(HttpServletRequest request) {
		try {
			String id = request.getParameter("id");
			String returnFlag=basicDataService.deleteLeaf(id) ;
			JSONObject returnobj=new JSONObject();
			returnobj.put("flag", returnFlag);
			return returnobj.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		}
	}
	@RequestMapping(value = "/openAddLeaf")
	public String append(HttpServletRequest request,
			HttpServletResponse response) {
		return "basicData/addLeaf";
	}
	
	@RequestMapping(value = "/openUpdateLeaf")
	public String openUpdateLeaf(HttpServletRequest request,
			HttpServletResponse response) {
		return "basicData/updateLeaf";
	}
	@RequestMapping(value = "/openAddYear")
	public String openAddYear(HttpServletRequest request,
			HttpServletResponse response) {
		return "basicData/addYear";
	}

	@RequestMapping("/exportData")
	public void exportData(HttpServletRequest request,
			HttpServletResponse response) {

	}

	@RequestMapping(value = "/index")
	public String index(Long pid, HttpServletRequest request,
			HttpServletResponse re) {
		request.getSession().setAttribute("pid", pid);
		return "basicData/basicDataMain";
	}
	@RequestMapping(value = "/table")
	public String table(HttpServletRequest request, HttpServletResponse response) {
		return "basicData/basicDataSave";
	}

	@RequestMapping(value = "/image")
	public String image(HttpServletRequest request, HttpServletResponse response) {
		return "basicData/basicDataPic";
	}

}
