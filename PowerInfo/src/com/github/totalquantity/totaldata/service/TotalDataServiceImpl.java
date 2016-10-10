package com.github.totalquantity.totaldata.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.sf.json.JSONObject;

import com.github.basicData.model.BasicYear;
import com.github.common.export.ExcelParams;
import com.github.common.export.ExcelUtils;
import com.github.common.export.rules.CellEqualMergeRules;
import com.github.common.export.rules.MergeRules;
import com.github.common.util.JsonUtils;
import com.github.totalquantity.calculatePlan.dao.CalculatePlanDao;
import com.github.totalquantity.prepareData.dao.PrepareDataDao;
import com.github.totalquantity.totaldata.dao.TotalDataDao;
import com.github.totalquantity.totaldata.entity.TotalData;

@Service
public class TotalDataServiceImpl implements TotalDataService {

	@Autowired
	private TotalDataDao totalDataDao ;

	@Override
	public String queryData(JSONObject param) {
		List<Map<String, Object>> list = totalDataDao.queryData(param);
		return JsonUtils.listTranJsonByQuery(list);
	}
	@Override
	public List<BasicYear> getYears(JSONObject param) throws Exception {
		// TODO Auto-generated method stub

		return totalDataDao.getYears(param);
	}
	
	@Override
	public String queryData6(JSONObject param) {
		List<Map<String, Object>> list = totalDataDao.queryData6(param);
		return JsonUtils.listTranJsonByQuery(list);
	}
	public void ExportExcel6(JSONObject param, HttpServletResponse response)
			throws Exception {

		String[] excelTitle = new String[] { "" };
		String years[]=param.getString("year").split(",");
		List<Map<String, Object>> list = totalDataDao.queryData6(param);
		String[] colTitle = null;
		String[] colName = null;

		colTitle = new String[ years.length];
		colName = new String[ years.length];

		for (int i = 0; i < years.length; i++) {
			colTitle[i] = years[i] + "年";
			colName[i] = years[i];
		}
		String fileName = "建议值";
		
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
