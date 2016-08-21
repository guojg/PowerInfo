package com.github.balance.parparedata.senddata.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.balance.parparedata.electricpowerplant.model.PowerPlant;
import com.github.balance.parparedata.senddata.dao.SendDataDao;
import com.github.balance.parparedata.senddata.model.Domain;
import com.github.balance.parparedata.senddata.model.SendItemName;
import com.github.common.export.ExcelParams;
import com.github.common.export.ExcelUtils;
import com.github.common.export.rules.CellEqualMergeRules;
import com.github.common.export.rules.MergeRules;
import com.github.common.util.JsonUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class SendDataServiceImpl implements  SendDataService{

	@Autowired
	private SendDataDao sendData;

	@Override
	public String queryData(JSONObject param) throws Exception {
		List<Map<String, Object>> list = sendData.queryData(param);
		return JsonUtils.listTranJsonByQuery(list);
	}

	@Override
	@Transactional
	public String saveData(JSONObject param) throws Exception {
		// TODO Auto-generated method stub

		return sendData.saveData(param);
	}
	
	

	@Override
	public String deleteData(JSONObject obj) throws Exception {
		String delectArr[] = obj.get("deleteids") == null ? null : obj
				.get("deleteids").toString().split(",");
		//String taskid=obj.getString("taskid");
		return sendData.deleteProData(delectArr);
	}

	@Override
	public String addProData(JSONObject obj) throws Exception {
		// TODO Auto-generated method stub
		return sendData.addProData(createModle(obj));
	}
	private SendItemName createModle(JSONObject obj){
		SendItemName p=new SendItemName();
		JSONObject data=null;
		if(obj!=null){
			 data=JSONObject.fromObject(obj.get("data"));
		}
		p.setTask_id(data.getString("task_id"));
		p.setPid(data.getString("pid"));
		p.setPro_name(data.getString("pro_name"));
		return p;
	}

	@Override
	public List<Domain> getTypes() throws Exception {
		// TODO Auto-generated method stub
		return sendData.getTypes();
	}
	public void ExportExcel(JSONObject param, HttpServletResponse response)
			throws Exception {

		String[] excelTitle = new String[] { "" };
		String years[]=param.getString("years").split(",");
		List<Map<String, Object>> list = sendData.queryData(param);
		String[] colTitle = null;
		String[] colName = null;

		colTitle = new String[3 + years.length];
		colTitle[0] = "指标名称";
		colTitle[years.length+1]="利用小时数";
		colTitle[years.length+2]="输电损耗率";	
		colName = new String[3 + years.length];
		colName[0] = "pro_name";
		colName[years.length+1] = "wgwstdlyxss";
		colName[years.length+2] = "sdshl";
		for (int i = 0; i < years.length; i++) {
			colTitle[1+i] = years[i] + "年";
			colName[1+i] = years[i];
		}
		String fileName = "外购外送";
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


}
