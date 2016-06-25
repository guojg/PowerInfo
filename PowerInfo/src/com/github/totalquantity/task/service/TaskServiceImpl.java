package com.github.totalquantity.task.service;



import org.springframework.beans.factory.annotation.Autowired;





import org.springframework.stereotype.Service;



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
	

}
