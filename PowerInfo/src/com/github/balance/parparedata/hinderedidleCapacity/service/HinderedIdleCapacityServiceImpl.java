package com.github.balance.parparedata.hinderedidleCapacity.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.balance.parparedata.hinderedidleCapacity.dao.HinderedIdleCapacityDao;
import com.github.balance.parparedata.loadelectricquantity.dao.LoadElectricQuantityDao;
import com.github.common.export.ExcelParams;
import com.github.common.export.ExcelUtils;
import com.github.common.export.rules.CellEqualMergeRules;
import com.github.common.export.rules.MergeRules;
import com.github.common.util.JsonUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class HinderedIdleCapacityServiceImpl implements  HinderedIdleCapacityService{

	@Autowired
	private HinderedIdleCapacityDao hinderedIdleCapacityDao;

	@Override
	public String queryData(JSONObject param) throws Exception {
		List<Map<String, Object>> list = hinderedIdleCapacityDao.queryData(param);
		return JsonUtils.listTranJsonByQuery(list);
	}

	@Override
	@Transactional
	public String saveData(JSONObject param) throws Exception {
		// TODO Auto-generated method stub
		return hinderedIdleCapacityDao.saveData(param);
	}
	public void ExportExcel(JSONObject param, HttpServletResponse response)
			throws Exception {

		String[] excelTitle = new String[] { "" };
		String years[]=param.getString("years").split(",");
		List<Map<String, Object>> list = hinderedIdleCapacityDao.queryData(param);
		String[] colTitle = null;
		String[] colName = null;

		colTitle = new String[1 + years.length];
		colTitle[0] = "指标名称";
		colName = new String[1 + years.length];
		colName[0] = "index_name";
		for (int i = 0; i < years.length; i++) {
			colTitle[1+i] = years[i] + "年";
			colName[1+i] = years[i];
		}
		String fileName = "受阻及空闲容量";
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
