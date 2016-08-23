package com.github.balance.parparedata.senddata.controller;


import java.io.PrintWriter;
import java.util.List;

import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.balance.parparedata.senddata.model.Domain;
import com.github.balance.parparedata.senddata.service.SendDataService;

@Controller
@RequestMapping(value = "/sendData")
public class SendDataController {
	@Autowired
	private SendDataService sendDataService;

	@RequestMapping(value = "/queryData")
	public void queryData(HttpServletRequest request,
			HttpServletResponse response) {
		response.setCharacterEncoding("UTF-8");
		String years = request.getParameter("years");
		String taskid=request.getParameter("taskid");
		JSONObject obj = new JSONObject();
		obj.put("years", years);
		obj.put("taskid", taskid);
		try {
			String resultJson = sendDataService.queryData(obj);
			PrintWriter pw = response.getWriter();
			pw.write(resultJson);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@RequestMapping(value = "/deleteProData")
	public @ResponseBody
	String deleteRecord(HttpServletRequest request) {
		try {
			String deleteids = request.getParameter("ids");
			//String taskid=request.getParameter("taskid");
			JSONObject jsonobj = new JSONObject();
			jsonobj.put("deleteids",deleteids );
			//jsonobj.put("taskid",taskid );
			sendDataService.deleteData(jsonobj);
			return "1";
		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		}
	}
	@RequestMapping(value = "/addProData")
	public @ResponseBody
	String addProData(HttpServletRequest request) {
		try {
			String editObj = request.getParameter("data");
			JSONObject jsonobj = new JSONObject();
			jsonobj.put("data", editObj);
			sendDataService.addProData(jsonobj);
			return "1";
		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		}
	}
	@RequestMapping(value = "/saveData")
	public @ResponseBody
	String saveData(HttpServletRequest request) {
		try {
			String editObj = request.getParameter("editObj");
			String taskid = request.getParameter("taskid");
			JSONObject jsonobj = new JSONObject();
			jsonobj.put("editObj", editObj);
			jsonobj.put("taskid", taskid);
			sendDataService.saveData(jsonobj);
			return "1";
		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		}
	}
	@RequestMapping(value = "/main")
	public String index(Long pid, HttpServletRequest request,
			HttpServletResponse re) {
		return "balance/parpareData/sendData";
	}
	@RequestMapping(value = "/gettypes",produces="application/json;charset=UTF-8")
	public @ResponseBody List<Domain> getTypes(HttpServletRequest request) {
		try {
			return sendDataService.getTypes();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	@RequestMapping(value = "/openAddProData")
	public String append(HttpServletRequest request,
			HttpServletResponse response) {
		return "balance/parpareData/addSendData";
	}
	@RequestMapping("/exportData")
	public void exportData(HttpServletRequest request,
			HttpServletResponse response) {
			response.setCharacterEncoding("UTF-8");
			String years = request.getParameter("years");
			String taskid =request.getParameter("taskid");
			
			JSONObject obj = new JSONObject();
			obj.put("years", years);
			obj.put("taskid", taskid);
			try {
				sendDataService.ExportExcel(obj, response);
			}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 

	}


}
