package com.github.regionalanalysis.db.electricpowerplant.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.common.export.ExcelParams;
import com.github.common.export.ExcelUtils;
import com.github.common.export.rules.CellEqualMergeRules;
import com.github.common.export.rules.MergeRules;
import com.github.common.util.JsonUtils;
import com.github.regionalanalysis.common.dao.TotalDataAnalysisDao;
import com.github.regionalanalysis.db.electricpowerplant.dao.PlantAnalysisDbDao;
import com.github.regionalanalysis.preparedata.electricpowerplant.model.PlantAnalysis;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class PlantAnalysisDbServiceImpl implements  PlantAnalysisDbService{

	@Autowired
	private PlantAnalysisDbDao plantAnalysisDbDao;
	
	@Autowired
	private TotalDataAnalysisDao totalDataAnalysisDao;

	@Override
	public String queryData(JSONObject param) throws Exception {
		List<Map<String, Object>> list = plantAnalysisDbDao.queryData(param);
		int totalcount=plantAnalysisDbDao.getTotalCount(param.getString("task_id"));
		return JsonUtils.listTranJsonByPage(list,totalcount);
	}

	
	@Override
	public String updateRecord(JSONObject obj) throws Exception {
		// TODO Auto-generated method stub
		String result=plantAnalysisDbDao.updateRecord(createModle(obj));
		Integer fdc_id=null;
		Integer area_id=null;
		Long task_id=null;

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
		if (obj.get("area_id") != null) {
			task_id =Long.parseLong( obj.get("task_id").toString());
		}
		totalDataAnalysisDao.fdcSaveTotalDb(fdc_id, area_id,task_id);
		return result;
	}

	@Override
	public String deleteRecord(JSONObject obj) throws Exception {
		// TODO Auto-generated method stub
		String delectArr[] = obj.get("deleteids") == null ? null : obj
				.get("deleteids").toString().split(",");
		return plantAnalysisDbDao.deleteRecord(delectArr,obj.getString("task_id"));
	}
	
	private PlantAnalysis createModle(JSONObject obj){
		PlantAnalysis p=new PlantAnalysis();
		JSONObject data=null;
		if(obj!=null){
			 data=JSONObject.fromObject(obj.get("editObj"));
		}
		p.setPlantCapacity(getJsonValue(data.get("plant_capacity")));
		p.setPlantName(getJsonValue(data.get("plant_name")));
		p.setEconomicalLife(getJsonValue(data.get("economical_life")));
		p.setEquiredReturn(getJsonValue(data.get("equired_return")));
		p.setFinancialCost(getJsonValue(data.get("financial_cost")));
		p.setGeneratingCapatity(getJsonValue(data.get("generating_capatity")));
		p.setGenerationCoal(getJsonValue(data.get("generation_coal")));
		p.setOperationCost(getJsonValue(data.get("operation_cost")));
		p.setOperationRate(getJsonValue(data.get("operation_rate")));
		p.setPlantLoss(getJsonValue(data.get("plant_loss")));
		p.setProductYear(getJsonValue(data.get("product_year")));
		p.setStartOutlay(getJsonValue(data.get("start_outlay")));
		p.setUnitCost(getJsonValue(data.get("unit_cost")));
		p.setMaterialsCost(getJsonValue(data.get("materials_cost")));
		p.setSalary(getJsonValue(data.get("salary")));
		p.setRepairsCost(getJsonValue(data.get("repairs_cost")));
		p.setOtherCost(getJsonValue(data.get("other_cost")));
		p.setTaskId(obj.getString("task_id"));

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
		List<Map<String, Object>> list = plantAnalysisDbDao.queryData(param);
		String[] colTitle = {"电厂名称","装机容量","发电量","电厂损耗 ","初始投资 ","投产年","经济运行寿命 ","期望收益率 ","年财务成本 ","发电煤耗 ","运行维护费率 ","运行维护成本 ","燃料单位成本 ","电厂材料费（元/年） ","工资、奖金及福利费（元/年） ","修理费（元/年）","其他费用（元/年）"};
		String[] colName = {"plant_name","plant_capacity","generating_capatity","plant_loss","start_outlay","product_year","economical_life","equired_return","financial_cost","generation_coal","operation_rate","operation_cost","unit_cost","materials_cost","salary","repairs_cost","","other_cost"};
		
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
	public String getPlantById(String id,String task_id) throws Exception {
		// TODO Auto-generated method stub
		
		List<Map<String, Object>> list = plantAnalysisDbDao.getPlantById(id,task_id);
		
		JSONArray obj=JSONArray.fromObject(list);
		return obj.toString();
	}

	@Override
	public String getFdjByDc(String id,String task_id) throws Exception {
		// TODO Auto-generated method stub

		List<Map<String, Object>> list = plantAnalysisDbDao.getFdjByDc(id,task_id);
		
		JSONArray obj=JSONArray.fromObject(list);
		return obj.toString();
	}

}
