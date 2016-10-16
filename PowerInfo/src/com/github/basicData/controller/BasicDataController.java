package com.github.basicData.controller;


import java.io.PrintWriter;

import java.util.List;

import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.basicData.model.BasicIndex;
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
		String indexs = request.getParameter("indexs");
		
		JSONObject obj = new JSONObject();
		obj.put("years", years);
		obj.put("indexs", indexs);
		try {
			String resultJson = basicDataService.queryData(obj);
			PrintWriter pw = response.getWriter();
			pw.write(resultJson);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/queryUnits")
	public void queryUnits(HttpServletRequest request,
			HttpServletResponse response) {
		response.setCharacterEncoding("UTF-8");
		try {
			String resultJson = basicDataService.queryUnits();
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
			JSONObject jsonobj = new JSONObject();
			jsonobj.put("editObj", editObj);
			basicDataService.saveData(jsonobj);
			return "1";
		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		}
	}

	@RequestMapping(value = "/addleaf", produces="application/json;charset=UTF-8")
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
	@RequestMapping(value = "/isonly",produces="application/json;charset=UTF-8")
	public @ResponseBody String isOnly(HttpServletRequest request) {
		try {
			String index_name = request.getParameter("index_name");
			JSONObject returnobj=new JSONObject();
			String returnflag=basicDataService.isOnly(index_name) ;
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
	@RequestMapping(value = "/getindexs",produces="application/json;charset=UTF-8")
	public @ResponseBody List<BasicIndex> getIndexs( String pid,HttpServletRequest request) {
		try {
			
			return basicDataService.getIndexs(pid);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	@RequestMapping(value = "/updateUnit",produces="application/json;charset=UTF-8")
	public @ResponseBody String updateUnit(HttpServletRequest request) {
		try {
			String data = request.getParameter("data");
			JSONObject jsonobj = new JSONObject();
			jsonobj.put("data", data);
			JSONObject returnobj=new JSONObject();
			String returnFlag=basicDataService.updatUnit(jsonobj) ;
			returnobj.put("flag", returnFlag);
			return returnobj.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		}
	}
	@RequestMapping(value = "/addUnit",produces="application/json;charset=UTF-8")
	public @ResponseBody String addUnit(HttpServletRequest request) {
		try {
			String data = request.getParameter("data");
			JSONObject jsonobj = new JSONObject();
			jsonobj.put("data", data);
			JSONObject returnobj=new JSONObject();
			String returnFlag=basicDataService.addUnit(jsonobj) ;
			returnobj.put("flag", returnFlag);
			return returnobj.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		}
	}
	@RequestMapping(value = "/getUnits",produces="application/json;charset=UTF-8")
	public @ResponseBody String getUnits(HttpServletRequest request) {
		try {
			String pid = request.getParameter("pid");
			JSONObject returnobj=new JSONObject();
			String units=basicDataService.getUnits(pid) ;
			returnobj.put("unitstr", units);
			return returnobj.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		}
	}
	@RequestMapping(value = "/updateleaf",produces="application/json;charset=UTF-8")
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
			response.setCharacterEncoding("UTF-8");
			String years = request.getParameter("years");
			String indexs = request.getParameter("indexs");
			
			JSONObject obj = new JSONObject();
			obj.put("years", years);
			obj.put("indexs", indexs);
			try {
				basicDataService.ExportExcel(obj, response);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 

	}

	@RequestMapping(value = "/index")
	public String index(Long pid, HttpServletRequest request,
			HttpServletResponse re) {
		request.setAttribute("pid", pid);
		return "basicData/basicDataMain";
	}
	@RequestMapping(value = "/table")
	public String table(Long pid,HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute("pid", pid);
		return "basicData/basicDataSave";
	}

	@RequestMapping(value = "/image")
	public String image(Long pid,HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute("pid", pid);
		return "basicData/basicDataPic";
	}
	@RequestMapping(value = "/operationUnit")
	public String operationUnit(Long pid,HttpServletRequest request, HttpServletResponse response) {
		return "basicData/operationUnit";
	}
	@RequestMapping(value = "/openAddUnit")
	public String openAddUnit(Long pid,HttpServletRequest request, HttpServletResponse response) {
		return "basicData/addUnit";
	}
	@RequestMapping(value = "/openUpdateUnit")
	public String openUpdateUnit(Long pid,HttpServletRequest request, HttpServletResponse response) {
		return "basicData/updateUnit";
	}
	
}
