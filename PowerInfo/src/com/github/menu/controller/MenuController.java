package com.github.menu.controller;

import java.io.IOException;
import java.io.PrintWriter;

import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.menu.service.MenuService;


@Controller
@RequestMapping(value ="/menu")
public class MenuController {
	 @Autowired
	 private MenuService menuService ;
	 

	
	@RequestMapping(value ="/queryAccordion")
	public void queryAccordion(HttpServletRequest request, HttpServletResponse response){
		response.setCharacterEncoding("UTF-8");
		//String jsonParam	= request.getParameter("jsonParam");
		JSONObject obj = new JSONObject();


		String	resultJson = menuService.queryAccordion(obj);
		try {
			PrintWriter pw = response.getWriter();
			pw.write(resultJson);
			pw.flush();
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	@RequestMapping(value ="/queryMenu")
	public void queryMenu(HttpServletRequest request, HttpServletResponse response){
		response.setCharacterEncoding("UTF-8");
		//String jsonParam	= request.getParameter("jsonParam");
		JSONObject obj = new JSONObject();


		String	resultJson = menuService.queryMenu(obj);
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
