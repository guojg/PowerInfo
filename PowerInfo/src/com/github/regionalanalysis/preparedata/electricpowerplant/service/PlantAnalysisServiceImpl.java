package com.github.regionalanalysis.preparedata.electricpowerplant.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.common.export.ExcelParams;
import com.github.common.export.ExcelUtils;
import com.github.common.export.rules.CellEqualMergeRules;
import com.github.common.export.rules.MergeRules;
import com.github.common.util.JsonUtils;
import com.github.regionalanalysis.common.dao.TotalDataAnalysisDao;
import com.github.regionalanalysis.preparedata.electricpowerplant.dao.PlantAnalysisDao;
import com.github.regionalanalysis.preparedata.electricpowerplant.model.PlantAnalysis;
import com.ibm.db2.jcc.t4.ob;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class PlantAnalysisServiceImpl implements  PlantAnalysisService{

	@Autowired
	private PlantAnalysisDao plantAnalysisDao;
	
	@Autowired
	private TotalDataAnalysisDao totalDataAnalysisDao;

	@Override
	public String queryData(JSONObject param) throws Exception {
		List<Map<String, Object>> list = plantAnalysisDao.queryData(param);
		int totalcount=plantAnalysisDao.getTotalCount(param);
		return JsonUtils.listTranJsonByPage(list,totalcount);
	}

	@Override
	public String addRecord(JSONObject obj) throws Exception {
		// TODO Auto-generated method stub
		return plantAnalysisDao.addRecord(createModle(obj));
	}

	@Override
	public String updateRecord(JSONObject obj) throws Exception {
		// TODO Auto-generated method stub
		String result=plantAnalysisDao.updateRecord(createModle(obj));
		Integer fdc_id=null;
		Integer area_id=null;
		JSONObject data=null;
		if(obj!=null){
			 data=JSONObject.fromObject(obj.get("editObj"));
		}
		if (data.get("id") != null) {
			fdc_id = Integer.parseInt(data.get("id").toString());
		}
		if (obj.get("area_id") != null) {
			area_id =Integer.parseInt( obj.get("area_id").toString());
		}
		totalDataAnalysisDao.fdcSaveTotal(fdc_id, area_id);
		return result;
	}

	@Override
	public String deleteRecord(JSONObject obj) throws Exception {
		// TODO Auto-generated method stub
		String delectArr[] = obj.get("deleteids") == null ? null : obj
				.get("deleteids").toString().split(",");
		return plantAnalysisDao.deleteRecord(delectArr);
	}
	
	private PlantAnalysis createModle(JSONObject obj){
		PlantAnalysis p=new PlantAnalysis();
		JSONObject data=null;
		if(obj!=null){
			 data=JSONObject.fromObject(obj.get("editObj"));
		}
		p.setPlantCapacity(getJsonValue(data.get("plant_capacity")));
		p.setPlantName(getJsonValue(data.get("plant_name")));
		p.setProductYear(getJsonValue(data.get("product_year")));
		p.setBuildYear(getJsonValue(data.get("build_year")));
		p.setStartOutlay(getJsonValue(data.get("start_outlay")));
		p.setConsumptionRate(getJsonValue(data.get("consumption_rate")));
		p.setElectricityConsumption(getJsonValue(data.get("electricity_consumption")));
		p.setPowerType(getJsonValue(data.get("power_type")));
		p.setCoolingType(getJsonValue(data.get("cooling_type")));
		p.setMaterialsCost(getJsonValue(data.get("materials_cost")));
		p.setSalary(getJsonValue(data.get("salary")));
		p.setRepairsCost(getJsonValue(data.get("repairs_cost")));
		p.setOtherCost(getJsonValue(data.get("other_cost")));
		p.setAreaId(obj.getString("area_id"));

		if(data.get("id")!=null){
			p.setId(data.getString("id"));
		}
		return p;
	}
	private String getJsonValue(Object obj){
	    String returnValue=null;
	    if(obj!=null&&!"".equals(obj.toString())){
	    	returnValue=obj.toString();
	    }
		
		return returnValue;
	}
	public void ExportExcel(JSONObject param, HttpServletResponse response)
			throws Exception {
		String[] excelTitle = new String[] { "" };
		List<Map<String, Object>> list = plantAnalysisDao.queryData(param);
		String[] colTitle = {"电厂名称","装机总容量（WM）","开工年","建成年 ","静态投资（万元） ","厂用电率","厂用电量（千瓦时） ","电源类型 ","冷却类型 ","电厂材料费（元/年） ","工资、奖金及福利费（元/年） ","修理费（元/年）","其他费用（元/年）"};
		String[] colName = {"plant_name","plant_capacity","product_year","build_year","start_outlay","consumption_rate","electricity_consumption","power_type","cooling_type","materials_cost","salary","repairs_cost","","other_cost"};
		
		String fileName = "区域区域电厂竞争力分析——电厂";
		ExcelParams params = new ExcelParams(fileName, excelTitle, null,
				colTitle, colName, list);

		// 导出excel文件的固定列，默认序号从1开始
		params.setColLock("1");

		// 生成ExcelUtils对象，引入params对象
		ExcelUtils ex = new ExcelUtils(params);

		// 导出规则定义
		MergeRules rule = new CellEqualMergeRules();

		List<MergeRules> rules = new ArrayList<MergeRules>();
		rules.add(rule);

		int[] i = new int[] {};
		// 第二个参数rules为导出规则的list集合，第三个参数为第二个参数所需的参数，一个rule参数为一个int数组
		ex.exportExcel(response, rules, new int[][] { i });

	}

	@Override
	public String getPlantById(String id) throws Exception {
		// TODO Auto-generated method stub
		
		List<Map<String, Object>> list = plantAnalysisDao.getPlantById(id);
		
		JSONArray obj=JSONArray.fromObject(list);
		return obj.toString();
	}

	@Override
	public String getFdjByDc(String id) throws Exception {
		// TODO Auto-generated method stub

		List<Map<String, Object>> list = plantAnalysisDao.getFdjByDc(id);
		
		JSONArray obj=JSONArray.fromObject(list);
		return obj.toString();
	}

	@Override
	public String queryTemplateData(String id) throws Exception {
		// TODO Auto-generated method stub
		List<Map<String, Object>> list = plantAnalysisDao.queryTemplateData(id);
		return JsonUtils.listTranJsonByQuery(list);
	}
	@Override
	@Transactional
	public String saveTemplateData(JSONObject param) throws Exception {
		// TODO Auto-generated method stub
		JSONArray rows = null;
		if (param.get("editObj") != null) {
			rows = JSONArray.fromObject(param.get("editObj"));
		}
		return plantAnalysisDao.saveTemplateData(rows);
	}


}
