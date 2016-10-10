package com.github.totalquantity.task.service;



import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;





import org.springframework.stereotype.Service;









import com.github.common.util.JsonUtils;
import com.github.totalquantity.calculatePlan.dao.CalculatePlanDao;
import com.github.totalquantity.prepareData.dao.PrepareDataDao;
import com.github.totalquantity.task.dao.TaskDao;
import com.github.totalquantity.task.entity.TotalTask;
import com.github.totalquantity.task.entity.TotalYear;
import com.github.totalquantity.totaldata.dao.TotalDataDao;

@Service
public class TaskServiceImpl implements TaskService{

	@Autowired
	private TaskDao taskDao;

	@Autowired
	private TotalDataDao totalDataDao ;
	@Autowired
	private PrepareDataDao prepareDataDao;
	@Autowired
	private CalculatePlanDao calculatePlanDao;

	@Override
	public void saveData(TotalTask task) {
		if("".equals(task.getId())){
			taskDao.saveData(task);
		}else{
			taskDao.updateData(task);
		}
		
		
	}

	@Override
	public String queryData(JSONObject param) {
		List<Map<String, Object>> list = taskDao.queryData(param);
		int count = taskDao.queryDataCount(param);
		return JsonUtils.listTranJsonByPage(list,count);
	}

	@Override
	public List<TotalYear> getBaseYears() throws Exception {
		// TODO Auto-generated method stub
		return taskDao.getBaseYears();
	}

	@Override
	public List<TotalYear> getPlanYears() throws Exception {
		// TODO Auto-generated method stub
		return taskDao.getPlanYears();
	}

	@Override
	public String initData(String id) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> list = taskDao.initData(id);
		return JsonUtils.listTranJson(list);
	}

	@Override
	public String deleteRecord(JSONObject obj) throws Exception {
		// TODO Auto-generated method stub
		String delectArr[] = obj.get("deleteids") == null ? null : obj
				.get("deleteids").toString().split(",");
		 taskDao.deleteRecord(delectArr);
		 totalDataDao.deleteRecord(delectArr);
		 calculatePlanDao.deleteRecord(delectArr);
		 prepareDataDao.deleteRecord(delectArr);
		 return "1";
	}
	

}
