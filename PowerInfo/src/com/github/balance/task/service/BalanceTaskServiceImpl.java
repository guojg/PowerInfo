package com.github.balance.task.service;




import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;





import org.springframework.stereotype.Service;












import com.github.balance.task.dao.BalanceTaskDao;
import com.github.balance.task.entity.BalanceTask;
import com.github.balance.task.entity.BalanceYear;
import com.github.basicData.model.BasicYear;
import com.github.common.util.JsonUtils;

@Service
public class BalanceTaskServiceImpl implements BalanceTaskService{

	@Autowired
	private BalanceTaskDao balanceTaskDao;

	@Override
	public void saveData(BalanceTask task) {
		if( "".equals(task.getId())){
			balanceTaskDao.saveData(task);
		}else{
			balanceTaskDao.updateData(task);
		}
		
		
	}

	@Override
	public String queryData(JSONObject param) {
		List<Map<String, Object>> list = balanceTaskDao.queryData(param);
		int count = balanceTaskDao.queryDataCount(param);
		return JsonUtils.listTranJsonByPage(list,count);
	}

	@Override
	public List<BalanceYear> getYears() throws Exception {
		// TODO Auto-generated method stub
		return balanceTaskDao.getYears();
	}

	@Override
	public String initData(String id) {
		List<Map<String, Object>> list = balanceTaskDao.initData(id);
		return JsonUtils.listTranJson(list);
	}

	@Override
	public String deleteRecord(JSONObject obj) throws Exception {
		// TODO Auto-generated method stub
		String delectArr[] = obj.get("deleteids") == null ? null : obj
				.get("deleteids").toString().split(",");
		return balanceTaskDao.deleteRecord(delectArr);
	}
	

}
