package com.github.basicData.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.basicData.dao.BasicDataDao;
import com.github.basicData.model.BasicIndex;
import com.github.basicData.model.BasicYear;
import com.github.common.export.ExcelParams;
import com.github.common.export.ExcelUtils;
import com.github.common.export.rules.CellEqualMergeRules;
import com.github.common.export.rules.MergeRules;
import com.github.common.util.JsonUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class BasicDataServiceImpl implements BasicDataService {

	@Autowired
	private BasicDataDao basicDataDao;

	@Override
	public String queryData(JSONObject param) throws Exception {
		List<Map<String, Object>> list = basicDataDao.queryData(param);
		return JsonUtils.listTranJsonByQuery(list);
	}

	@Override
	@Transactional
	public String saveData(JSONObject param) throws Exception {
		// TODO Auto-generated method stub
		JSONArray rows = null;
		if (param.get("editObj") != null) {
			rows = JSONArray.fromObject(param.get("editObj"));
		}
		return basicDataDao.saveData(rows);
	}

	@Override
	@Transactional
	public List<Map<String, Object>> addLeaf(JSONObject param) throws Exception {
		JSONObject obj = null;
		if (param.get("data") != null) {
			obj = JSONObject.fromObject(param.get("data"));
		}
		return basicDataDao.addLeaf(obj);
	}

	@Override
	public String updatLeaf(JSONObject param) throws Exception {
		JSONObject obj = null;
		if (param.get("data") != null) {
			obj = JSONObject.fromObject(param.get("data"));
		}
		;
		return basicDataDao.updatLeaf(obj);
	}

	@Override
	public String deleteLeaf(String id) throws Exception {
		// TODO Auto-generated method stub
		return basicDataDao.deleteLeaf(id);
	}

	@Override
	public String addYear(JSONObject param) throws Exception {
		// TODO Auto-generated method stub
		JSONObject obj = null;
		if (param.get("data") != null) {
			obj = JSONObject.fromObject(param.get("data"));
		}
		return basicDataDao.addYear(obj);
	}

	@Override
	public List<BasicYear> getYears() throws Exception {
		// TODO Auto-generated method stub

		return basicDataDao.getYears();
	}

	@Override
	public List<BasicIndex> getIndexs(String pid) throws Exception {
		// TODO Auto-generated method stub

		return basicDataDao.getIndexs(pid);
	}

	public void ExportExcel(JSONObject param, HttpServletResponse response)
			throws Exception {

		String[] excelTitle = new String[] { "" };
		String years[]=param.getString("years").split(",");
		List<Map<String, Object>> list = basicDataDao.queryData(param);
		String[] colTitle = null;
		String[] colName = null;

		colTitle = new String[1 + years.length];
		colTitle[0] = "指标";
		colName = new String[1 + years.length];
		colName[0] = "index_name";
		for (int i = 0; i < years.length; i++) {
			colTitle[1+i] = years[i] + "年";
			colName[1+i] = years[i];
		}
		String fileName = "指标表";
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
	public String isOnly(String name) throws Exception{
		// TODO Auto-generated method stub
		return basicDataDao.isOnly(name);
	}

	@Override
	public String getUnits(String pid) throws Exception {
		// TODO Auto-generated method stub
		List<Map<String,String>> units=basicDataDao.getUnits(pid);
		String Units="单位：";
		if(units!=null&&units.size()>0){
			for(int i=0;i<units.size();i++){
				String addstr= units.get(i).get("value").toString();
				if(Units.indexOf(addstr)==-1){
					Units+=addstr+"、";
				}
			}
			Units=Units.substring(0, Units.length()-1);
		}else{
			Units="";
		}
		
		return Units;
	}

	@Override
	public String updatUnit(JSONObject param) throws Exception {
		// TODO Auto-generated method stub
		JSONObject obj = null;
		if (param.get("data") != null) {
			obj = JSONObject.fromObject(param.get("data"));
		}
		;
		return basicDataDao.updateUnit(obj);
	}

	@Override
	public String addUnit(JSONObject param) throws Exception {
		// TODO Auto-generated method stub
		JSONObject obj = null;
		if (param.get("data") != null) {
			obj = JSONObject.fromObject(param.get("data"));
		}
		return basicDataDao.addUnit(obj);
	}

	@Override
	public String queryUnits() throws Exception {
		List<Map<String, Object>> list = basicDataDao.queryUnits();
		return JsonUtils.listTranJsonByQuery(list);
	}

}
