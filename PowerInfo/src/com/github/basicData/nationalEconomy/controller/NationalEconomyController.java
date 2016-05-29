package com.github.basicData.nationalEconomy.controller;

import java.io.IOException;
import java.io.PrintWriter;
import net.sf.json.JSONObject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.basicData.nationalEconomy.service.NationalEconomyService;

@Controller
@RequestMapping(value ="/nationalEconomy")
public class NationalEconomyController {
	 @Autowired
	 private NationalEconomyService nationalEconomyService ;
	 
	@RequestMapping(value ="/queryData")
	public void queryData(HttpServletRequest request, HttpServletResponse response){
		response.setCharacterEncoding("UTF-8");
		//String jsonParam	= request.getParameter("jsonParam");
		JSONObject obj = new JSONObject();
		String	resultJson = nationalEconomyService.queryData(obj);
		try {
			PrintWriter pw = response.getWriter();
			pw.write(resultJson);
			pw.flush();
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@RequestMapping(value ="/saveData")
	public void saveData(HttpServletRequest request, HttpServletResponse response){
		
	}
	@RequestMapping("/exportData")
	public void exportData(HttpServletRequest request, HttpServletResponse response){
		
	}
	
	
	@RequestMapping(value ="/commonServlet")
	public void queryMenu(HttpServletRequest request, HttpServletResponse response){
		response.setCharacterEncoding("UTF-8");
		//String jsonParam	= request.getParameter("jsonParam");
		JSONObject obj = new JSONObject();


		String	resultJson = nationalEconomyService.queryData(obj);
		try {
			PrintWriter pw = response.getWriter();
			pw.write(resultJson);
			pw.flush();
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
