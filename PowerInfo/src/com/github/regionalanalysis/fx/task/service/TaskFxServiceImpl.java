package com.github.regionalanalysis.fx.task.service;



import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;





import org.springframework.stereotype.Service;











import com.github.common.util.JsonUtils;
import com.github.regionalanalysis.db.task.entity.DbTask;
import com.github.regionalanalysis.fx.task.dao.TaskFxDao;

@Service
public class TaskFxServiceImpl implements TaskFxService{

	@Autowired
	private TaskFxDao taskFxDao;

	@Override
	public void saveData(DbTask task) {
		if("".equals(task.getId())){
			taskFxDao.saveData(task);
		}else{
			taskFxDao.updateData(task);
		}
		
		
	}

	@Override
	public String queryData(JSONObject param) {
		List<Map<String, Object>> list = taskFxDao.queryData(param);
		int count = taskFxDao.queryDataCount(param);
		return JsonUtils.listTranJsonByPage(list,count);
	}

	

	@Override
	public String initData(String id) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> list = taskFxDao.initData(id);
		return JsonUtils.listTranJson(list);
	}

	@Override
	public String deleteRecord(JSONObject obj) throws Exception {
		// TODO Auto-generated method stub
		String delectArr[] = obj.get("deleteids") == null ? null : obj
				.get("deleteids").toString().split(",");
		return taskFxDao.deleteRecord(delectArr);
	}
	

}
