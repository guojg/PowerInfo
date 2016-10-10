package com.github.regionalanalysis.preparedata.map.service;



import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.totalquantity.sysdict.dao.SysdictDao;


@Service
public class MapServiceImpl implements  MapService{
	@Autowired
	private SysdictDao sysdictDao;

	@Override
	public String queryCompanyByCode(String organCode) {
		// TODO Auto-generated method stub
		String result="";
		 List<Map<String, Object>> list=sysdictDao.queryCompanyByCode(organCode);
		if(list.size()>0){
			result = list.get(0).get("VALUE").toString();
		}
		return result;
	}
	
	

}
