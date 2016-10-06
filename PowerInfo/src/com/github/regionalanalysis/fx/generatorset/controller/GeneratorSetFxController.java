package com.github.regionalanalysis.fx.generatorset.controller;





import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.regionalanalysis.db.generatorset.service.GeneratorSetDbService;
import com.github.regionalanalysis.fx.generatorset.service.GeneratorSetFxService;

@Controller
@RequestMapping(value ="/generatorSetFxController")
public class GeneratorSetFxController {

	 @Autowired
	 private GeneratorSetFxService generatorSetFxService ;
	 
	@RequestMapping(value ="/index")
	public String index(HttpServletRequest request, HttpServletResponse response){
		return "regionalanalysis/fx/generatorset";
	}
	@RequestMapping(value ="/main")
	public String main(HttpServletRequest request, HttpServletResponse response){
		return "regionalanalysis/fx/generatorsetmain";
	}
	
	
	@RequestMapping(value ="/detail")
	public String detail(HttpServletRequest request, HttpServletResponse response){
		return "regionalanalysis/fx/generatorsetdetailmain";
	}
	
		@RequestMapping(value ="/queryData")
		public @ResponseBody String queryData(HttpServletRequest request, HttpServletResponse response){
			response.setCharacterEncoding("UTF-8");
			String pageNum = request.getParameter("page");
			String pageSize = request.getParameter("rows");
			String gene_name = request.getParameter("gene_name")==null?"":request.getParameter("gene_name");

			String elec_name = request.getParameter("elec_name")==null?"":request.getParameter("elec_name");
			String task_id = request.getParameter("task_id")==null?"":request.getParameter("task_id");

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
			obj.put("task_id", task_id);
			return generatorSetFxService.queryData(obj);
			
		}
		

		@RequestMapping("/exportData")
		public void exportData(HttpServletRequest request,
				HttpServletResponse response) {
				response.setCharacterEncoding("UTF-8");
				String gene_name = request.getParameter("gene_name")==null?"":request.getParameter("gene_name");

				String elec_name = request.getParameter("elec_name")==null?"":request.getParameter("elec_name");
				String task_id = request.getParameter("task_id")==null?"":request.getParameter("task_id");

				
				JSONObject obj = new JSONObject();
				obj.put("gene_name", gene_name);
				obj.put("elec_name", elec_name);
				obj.put("task_id", task_id);

				try {
					generatorSetFxService.ExportExcel(obj, response);
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
				generatorSetFxService.deleteData(jsonobj);
				return "1";
			} catch (Exception e) {
				e.printStackTrace();
				return "0";
			}
		}

}
