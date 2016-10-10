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
import com.github.regionalanalysis.preparedata.electricpowerplant.service.PlantAnalysisService;
import com.github.regionalanalysis.preparedata.map.service.MapService;

@Controller
@RequestMapping(value = "/mapController")
public class MapController {
	@Autowired
	private MapService mapService;

	
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
		request.getSession().removeAttribute("organName");
		  String organCode = request.getParameter("organCode")!=null?request.getParameter("organCode"):"";
		  String organName = mapService.queryCompanyByCode(organCode);
		request.getSession().setAttribute("maparea", organCode);
		request.getSession().setAttribute("organName", organName);
		return "regionalanalysis/preparedata/maparea";
	}
	
	
	
	

}
