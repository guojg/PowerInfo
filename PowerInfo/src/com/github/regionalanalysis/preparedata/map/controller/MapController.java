package com.github.regionalanalysis.preparedata.map.controller;


import java.io.PrintWriter;

import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.regionalanalysis.generatorcontrast.service.GeneratorContrastService;

@Controller
@RequestMapping(value = "/mapController")
public class MapController {
	

	
	@RequestMapping(value = "/index")
	public String index(Long pid, HttpServletRequest request,
			HttpServletResponse re) {
		//return "regionalanalysis/generatorContrast";
		return "regionalanalysis/preparedata/maparea";
	}
	
	@RequestMapping(value = "/setSession")
	public String setSession(Long pid, HttpServletRequest request,
			HttpServletResponse re) {
		//return "regionalanalysis/generatorContrast";
		request.getSession().removeAttribute("maparea");
		  String organCode = request.getParameter("organCode")!=null?request.getParameter("organCode"):"";

		request.getSession().setAttribute("maparea", organCode);
		return "regionalanalysis/preparedata/maparea";
	}
	
	
	
	

}
