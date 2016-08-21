package com.github.balance.powerbalance.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.balance.powerbalance.dao.PowerBalanceDao;
import com.github.common.export.ExcelParams;
import com.github.common.export.ExcelUtils;
import com.github.common.export.rules.CellEqualMergeRules;
import com.github.common.export.rules.MergeRules;
import com.github.common.util.JsonUtils;

import net.sf.json.JSONObject;

@Service
public class PowerBalanceServiceImpl implements PowerBalanceService {

	@Autowired
	private PowerBalanceDao powerBalanceDao;
	@Override
	public String queryData(JSONObject param) {
		List<Map<String, Object>> list = powerBalanceDao.queryData(param);
		return JsonUtils.listTranJsonByQuery(list);
	}
	@Override
	public String extractData(JSONObject obj) {
		int  result =  powerBalanceDao.extractData(obj);
		return null;
	}
	public void ExportExcel(JSONObject param, HttpServletResponse response)
			throws Exception {

		String[] excelTitle = new String[] { "" };
		String years[]=param.getString("year").split(",");
		List<Map<String, Object>> list = powerBalanceDao.queryData(param);
		String[] colTitle = null;
		String[] colName = null;

		colTitle = new String[1+ years.length];
		colTitle[0] = "指标";
		colName = new String[1 + years.length];
		colName[0] = "code_name";
		for (int i = 0; i < years.length; i++) {
			colTitle[1+i] = years[i] + "年";
			colName[1+i] = years[i];
		}
		String fileName = "电力平衡";
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
