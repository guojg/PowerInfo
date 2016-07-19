package com.github.totalquantity.task.service;



import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;





import org.springframework.stereotype.Service;





import com.github.common.util.JsonUtils;
import com.github.totalquantity.task.dao.TaskDao;
import com.github.totalquantity.task.entity.TotalTask;

@Service
public class TaskServiceImpl implements TaskService{

	@Autowired
	private TaskDao taskDao;

	@Override
	public void saveData(TotalTask task) {
		taskDao.saveData(task);
		
	}

	@Override
	public String queryData(JSONObject param) {
		List<Map<String, Object>> list = taskDao.queryData(param);
		int count = taskDao.queryDataCount(param);
		return JsonUtils.listTranJsonByPage(list,count);
	}


	

}
