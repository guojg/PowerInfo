package com.github.common.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@RequestMapping(value ="/mondelSel")
public class MondelSel {
	
 	@RequestMapping(value ="/sel")
	public String modelSel(HttpServletRequest request, HttpServletResponse response){
		
		return "modelsel";
	}
 	
 	@RequestMapping(value ="/MainIndex")
	public String mainIndex(HttpServletRequest request, HttpServletResponse response){
		
		return "index2";
	}
 	
}
