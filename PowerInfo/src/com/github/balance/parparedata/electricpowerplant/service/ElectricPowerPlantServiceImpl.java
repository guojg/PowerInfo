package com.github.balance.parparedata.electricpowerplant.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.balance.parparedata.electricpowerplant.dao.ElectricPowerPlantDao;
import com.github.balance.parparedata.electricpowerplant.model.PowerPlant;
import com.github.balance.parparedata.hinderedidleCapacity.dao.HinderedIdleCapacityDao;
import com.github.common.export.ExcelParams;
import com.github.common.export.ExcelUtils;
import com.github.common.export.rules.CellEqualMergeRules;
import com.github.common.export.rules.MergeRules;
import com.github.common.util.JsonUtils;

import net.sf.json.JSONObject;

@Service
public class ElectricPowerPlantServiceImpl implements  ElectricPowerPlantService{

	@Autowired
	private ElectricPowerPlantDao electricPowerPlantDao;

	@Override
	public String queryData(JSONObject param) throws Exception {
		List<Map<String, Object>> list = electricPowerPlantDao.queryData(param);
		int totalcount=electricPowerPlantDao.getTotalCount();
		return JsonUtils.listTranJsonByPage(list,totalcount);
	}

	@Override
	public String addRecord(JSONObject obj) throws Exception {
		// TODO Auto-generated method stub
		return electricPowerPlantDao.addRecord(createModle(obj));
	}

	@Override
	public String updateRecord(JSONObject obj) throws Exception {
		// TODO Auto-generated method stub
		return electricPowerPlantDao.updateRecord(createModle(obj));
	}

	@Override
	public String deleteRecord(JSONObject obj) throws Exception {
		// TODO Auto-generated method stub
		String delectArr[] = obj.get("deleteids") == null ? null : obj
				.get("deleteids").toString().split(",");
		return electricPowerPlantDao.deleteRecord(delectArr);
	}
	
	private PowerPlant createModle(JSONObject obj){
		PowerPlant p=new PowerPlant();
		JSONObject data=null;
		if(obj!=null){
			 data=JSONObject.fromObject(obj.get("editObj"));
		}
//		if(!"".equals(data.getString("end_date"))){
//			p.setEndDate(data.getString("end_date"));
//		}else{
//			p.setEndDate(null);
//		}
		p.setPlantCapacity(data.getString("plant_capacity"));
		p.setPlantName(data.getString("plant_name"));
//		p.setStartDate(data.getString("start_date"));
		p.setIndexItem(data.getString("index_item"));
		p.setAreaId(data.getString("area_id"));
		if(data.get("id")!=null){
			p.setId(data.getString("id"));
		}
		return p;
	}
	public void ExportExcel(JSONObject param, HttpServletResponse response)
			throws Exception {
		String[] excelTitle = new String[] { "" };
		List<Map<String, Object>> list = electricPowerPlantDao.queryData(param);
		String[] colTitle = {"电厂名称","装机容量","电源类型","投产日期","退役日期"};
		String[] colName = {"plant_name","plant_capacity","index_itemname","start_date","end_date"};
		
		String fileName = "电厂";
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
	public String selRecordToAnalysis(JSONObject obj) throws Exception {
		String selArr[] = obj.get("selids") == null ? null : obj
				.get("selids").toString().split(",");
		return electricPowerPlantDao.selRecordToAnalysis(selArr);
	}

}
