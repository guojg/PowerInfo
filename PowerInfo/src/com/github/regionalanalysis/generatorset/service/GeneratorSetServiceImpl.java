package com.github.regionalanalysis.generatorset.service;


import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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

}
