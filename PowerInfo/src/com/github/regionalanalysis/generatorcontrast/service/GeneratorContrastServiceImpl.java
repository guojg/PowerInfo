package com.github.regionalanalysis.generatorcontrast.service;


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
import com.github.regionalanalysis.generatorcontrast.dao.GeneratorContrastDao;

import net.sf.json.JSONObject;

@Service
public class GeneratorContrastServiceImpl implements  GeneratorContrastService{

	@Autowired
	private GeneratorContrastDao generatorContrastDao;

	@Override
	public String queryData(JSONObject param) {
		List<Map<String, Object>> list = generatorContrastDao.queryData(param);
		return JsonUtils.listTranJsonByQuery(list);
	}

	
	public void ExportExcel(JSONObject param, HttpServletResponse response)
			throws Exception {

		String[] excelTitle = new String[] { "" };
		String index_xs[]=param.getString("index_xs").split(",");
		String index_text[]=param.getString("index_text").split(",");
		List<Map<String, Object>> list = generatorContrastDao.queryData(param);
		String[] colTitle = null;
		String[] colName = null;

		colTitle = new String[2 + index_xs.length];
		colTitle[0] = "项目";
		colTitle[1] = "单位";
		colName = new String[2 + index_xs.length];
		colName[0] = "index_y_name";
		colName[1] = "unit_name";
		for (int i = 0; i < index_xs.length; i++) {
			colTitle[2+i] = index_text[i];
			colName[2+i] = index_xs[i];
		}
		String fileName = "燃煤成本数据";
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
