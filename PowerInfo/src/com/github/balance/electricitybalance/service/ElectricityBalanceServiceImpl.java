package com.github.balance.electricitybalance.service;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.balance.electricitybalance.dao.ElectricityBalanceDao;
import com.github.basicData.dao.BasicDataDao;
import com.github.basicData.model.BasicYear;
import com.github.common.export.ExcelParams;
import com.github.common.export.ExcelUtils;
import com.github.common.export.rules.CellEqualMergeRules;
import com.github.common.export.rules.MergeRules;
import com.github.common.util.JsonUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class ElectricityBalanceServiceImpl implements ElectricityBalanceService {

	@Autowired
	private ElectricityBalanceDao electricityBalanceDao;
	
	@Autowired
	private BasicDataDao basicDataDao;
	
	@Override
	public String queryData(JSONObject param) {
		List<Map<String, Object>> list = electricityBalanceDao.queryData(param);
		return JsonUtils.listTranJsonByQuery(list);
	}
	@Override
	public String queryCoalHourData(JSONObject param) {
		String yrs ="";
		try {
			List<BasicYear> baseYear = basicDataDao.getYears();
			if(baseYear !=null && baseYear.size()>0){
				for(BasicYear br:baseYear){
					yrs +=br.getYear()+"," ;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		param.put("baseyears", yrs);
		List<Map<String, Object>> list = electricityBalanceDao.queryCoalHourData(param);
		return JsonUtils.listTranJsonByQuery(list);
	}
	@Override
	public String extractData(JSONObject obj) {
		int  result =  electricityBalanceDao.extractData(obj);
		return null;
	}
	public void ExportExcel(JSONObject param, HttpServletResponse response)
			throws Exception {

		String[] excelTitle = new String[] { "" };
		String years[]=param.getString("year").split(",");
		List<Map<String, Object>> list = electricityBalanceDao.exportData(param);
		String[] colTitle = null;
		String[] colName = null;

		colTitle = new String[2+ years.length];
		colTitle[0] = "指标";
		colName = new String[2 + years.length];
		colName[0] = "code_name";
		for (int i = 0; i < years.length; i++) {
			colTitle[1+i] = years[i] + "年";
			colName[1+i] = years[i];
		}
		colName[1+years.length]="hour_num";
		colTitle[1+years.length]="机组利用小时数";
		String fileName = "电量平衡";
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
	public String saveData(JSONObject param) {
		JSONArray rows = null;
		if (param.get("editObj") != null) {
			rows = JSONArray.fromObject(param.get("editObj"));
		}
		JSONArray result =getArray(rows);
		JSONObject obj = new JSONObject();
		obj.put("taskid", param.get("taskid")==null?"":param.get("taskid").toString());
		return electricityBalanceDao.saveData(result,obj);
	}
	
	private JSONArray getArray(JSONArray rows){
		JSONArray result = new  JSONArray();
		for(int i=0 ; i<rows.size() ;++i){
			JSONObject obj = new JSONObject();
			JSONObject objRow =JSONObject.fromObject(rows.get(i));
			  Iterator it = objRow.keys();  		      
			    while (it.hasNext()) {          
			        String key = it.next().toString();  
			        if("children".equals(key)){
			        	JSONArray childArray = getArray(JSONArray.fromObject(objRow.get(key)) );
			        	if(childArray != null){
			    			for(int n=0 ; n< childArray.size() ;++n){
			    				 result.add(JSONObject.fromObject(childArray.get(n))) ;
			    			}
			    		}
			        }else if("index_item".equals(key)|| "state".equals(key)||"p_index_item".equals(key)||"id".equals(key)
			        		||"pcode_name".equals(key)||"code_name".equals(key)||"ORD".equals(key)||"ord_2".equals(key)
			        		||"task_id".equals(key)){
			        	
			        }else{
			        	obj.put(key,  objRow.getString(key)) ;
			        }
			    }
			    result.add(obj) ;
		}
		
		return result;
	}
}
