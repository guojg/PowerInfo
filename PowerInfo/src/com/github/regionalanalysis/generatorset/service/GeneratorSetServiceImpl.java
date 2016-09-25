package com.github.regionalanalysis.generatorset.service;


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
import com.github.regionalanalysis.generatorset.dao.GeneratorSetDao;




import net.sf.json.JSONObject;

@Service
public class GeneratorSetServiceImpl implements GeneratorSetService {

	@Autowired
	private GeneratorSetDao generatorSetDao;

	
	@Override
	public String queryData(JSONObject param) {
		List<Map<String, Object>> list = generatorSetDao.queryData(param);
		int count = generatorSetDao.queryDataCount(param);
		return JsonUtils.listTranJsonByPage(list,count);
	}
	
	public void ExportExcel(JSONObject param, HttpServletResponse response)
			throws Exception {

		String[] excelTitle = new String[] { "" };
		
		List<Map<String, Object>> list = generatorSetDao.queryAllData(param);
		String[] colTitle = null;
		String[] colName = null;

		colTitle = new String[8];
		colTitle[0] = "机组名";
		colTitle[1] = "所属发电厂";
		colTitle[2] = "额定容量";
		colTitle[3] = "投运日期";
		colTitle[4] = "建设投资";
		colTitle[5] = "行业折现率（%）";
		colTitle[6] = "运营期";
		colTitle[7] = "固定资产折旧（年值）";
		colName = new String[8];
		colName[0] = "100";
		colName[1] = "plant_name";
		colName[2] = "300";
		colName[3] = "400";
		colName[4] = "600";
		colName[5] = "700";
		colName[6] = "800";
		colName[7] = "900";

		String fileName = "机组";
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
	public void deleteData(JSONObject obj) {
		String delectArr[] = obj.get("deleteids") == null ? null : obj
				.get("deleteids").toString().split(",");
		 generatorSetDao.deleteData(delectArr);
		
	}

}
