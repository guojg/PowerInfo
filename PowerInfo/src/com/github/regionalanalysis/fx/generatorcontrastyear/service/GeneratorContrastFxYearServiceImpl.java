package com.github.regionalanalysis.fx.generatorcontrastyear.service;


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
import com.github.regionalanalysis.fx.generatorcontrastyear.dao.GeneratorContrastFxYearDao;

import net.sf.json.JSONObject;

@Service
public class GeneratorContrastFxYearServiceImpl implements  GeneratorContrastFxYearService{

	@Autowired
	private GeneratorContrastFxYearDao generatorContrastFxYearDao;

	@Override
	public String queryData(JSONObject param) {
		List<Map<String, Object>> list = generatorContrastFxYearDao.queryData(param);
		return JsonUtils.listTranJsonByQuery(list);
	}

	
	public void ExportExcel(JSONObject param, HttpServletResponse response)
			throws Exception {

		String[] excelTitle = new String[] { "" };
		List<Map<String, Object>> list = generatorContrastFxYearDao.queryData(param);
		String[] colTitle = null;
		String[] colName = null;

		colTitle = new String[5];
		colTitle[0] = "电厂名称";
		colTitle[1] = "机组名称";
		
		colTitle[2] = "指标名称";
		colTitle[3] = "单位";
		colTitle[4] = "成本";
		colName = new String[5];
		colName[0] = "plant_name";
		colName[1] = "jz_name";
		colName[2] = "index_y_name";
		colName[3] = "unit_name";
		colName[4] = "value";
		String fileName = "机组成本对比";
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

		int[] i = new int[] {0,1,2};
		// 第二个参数rules为导出规则的list集合，第三个参数为第二个参数所需的参数，一个rule参数为一个int数组
		ex.exportExcel(response, rules, new int[][] { i });

	}


	@Override
	public String queryDataPie() {
		List<Map<String, Object>> list = generatorContrastFxYearDao.queryDataPie();
		return JsonUtils.listTranJsonByQuery(list);
	}
}
