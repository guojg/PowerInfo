package com.github.regionalanalysis.db.task.service;



import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;





import org.springframework.stereotype.Service;










import com.github.common.util.JsonUtils;
import com.github.regionalanalysis.db.task.dao.TaskDbDao;
import com.github.regionalanalysis.db.task.entity.DbTask;

@Service
public class TaskDbServiceImpl implements TaskDbService{

	@Autowired
	private TaskDbDao taskDbDao;

	@Override
	public void saveData(DbTask task) {
		if("".equals(task.getId())){
			taskDbDao.saveData(task);
		}else{
			taskDbDao.updateData(task);
		}
		
		
	}

	@Override
	public String queryData(JSONObject param) {
		List<Map<String, Object>> list = taskDbDao.queryData(param);
		int count = taskDbDao.queryDataCount(param);
		return JsonUtils.listTranJsonByPage(list,count);
	}

	

	@Override
	public String initData(String id) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> list = taskDbDao.initData(id);
		return JsonUtils.listTranJson(list);
	}

	@Override
	public String deleteRecord(JSONObject obj) throws Exception {
		// TODO Auto-generated method stub
		String delectArr[] = obj.get("deleteids") == null ? null : obj
				.get("deleteids").toString().split(",");
		return taskDbDao.deleteRecord(delectArr);
	}
	

}
