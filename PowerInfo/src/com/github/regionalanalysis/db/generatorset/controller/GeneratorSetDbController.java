package com.github.regionalanalysis.db.generatorset.controller;





import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.regionalanalysis.db.generatorset.service.GeneratorSetDbService;

@Controller
@RequestMapping(value ="/generatorSetDbController")
public class GeneratorSetDbController {

	 @Autowired
	 private GeneratorSetDbService generatorSetDbService ;
	 
	@RequestMapping(value ="/index")
	public String index(HttpServletRequest request, HttpServletResponse response){
		return "regionalanalysis/generatorset";
	}
	@RequestMapping(value ="/main")
	public String main(HttpServletRequest request, HttpServletResponse response){
		return "regionalanalysis/generatorsetmain";
	}
	
	
	@RequestMapping(value ="/detail")
	public String detail(HttpServletRequest request, HttpServletResponse response){
		return "regionalanalysis/generatorsetdetailmain";
	}
	
		@RequestMapping(value ="/queryData")
		public @ResponseBody String queryData(HttpServletRequest request, HttpServletResponse response){
			response.setCharacterEncoding("UTF-8");
			String pageNum = request.getParameter("page");
			String pageSize = request.getParameter("rows");
			String gene_name = request.getParameter("gene_name")==null?"":request.getParameter("gene_name");

			String elec_name = request.getParameter("elec_name")==null?"":request.getParameter("elec_name");
			Object object=  request.getSession().getAttribute("maparea");
			String area_id = "";
			if(object != null) area_id = object.toString();
			
			if (pageNum == null || "null".equals(pageNum) || "".equals(pageNum))
				pageNum = "1";
			if (pageSize == null || "null".equals(pageSize)
					|| "".equals(pageSize))
				pageSize = "10";
			JSONObject obj = new JSONObject();
			obj.put("pageNum", pageNum);
			obj.put("pageSize", pageSize);
			obj.put("gene_name", gene_name);
			obj.put("elec_name", elec_name);
			obj.put("area_id", area_id);
			return generatorSetDbService.queryData(obj);
			
		}
		

		@RequestMapping("/exportData")
		public void exportData(HttpServletRequest request,
				HttpServletResponse response) {
				response.setCharacterEncoding("UTF-8");
				String gene_name = request.getParameter("gene_name")==null?"":request.getParameter("gene_name");

				String elec_name = request.getParameter("elec_name")==null?"":request.getParameter("elec_name");
				Object object=  request.getSession().getAttribute("maparea");
				String area_id = "";
				if(object != null) area_id = object.toString();
				
				JSONObject obj = new JSONObject();
				obj.put("gene_name", gene_name);
				obj.put("elec_name", elec_name);
				obj.put("area_id", area_id);

				try {
					generatorSetDbService.ExportExcel(obj, response);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 

		}
		
		@RequestMapping(value = "/deleteData")
		public @ResponseBody
		String deleteData(HttpServletRequest request) {
			try {
				String deleteids = request.getParameter("ids");
				JSONObject jsonobj = new JSONObject();
				jsonobj.put("deleteids",deleteids );
				generatorSetDbService.deleteData(jsonobj);
				return "1";
			} catch (Exception e) {
				e.printStackTrace();
				return "0";
			}
		}

}
