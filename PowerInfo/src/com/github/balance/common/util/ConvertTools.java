package com.github.balance.common.util;

public class ConvertTools {
	/**
	 * 计算增长率用到的：将原始年份数组转换成目标年份，为了拼sql
	 * @param years
	 * @return
	 */
	public  static String[] convertIncreaseRate (String[] sourceArr){
		//if(years==null || "".equals(years)){
		//	return null;
	//	}
	   //  String[] sourceArr= years.split(",");
	     String[] destinationArr = new String[sourceArr.length];
	        for(int i=0; i<destinationArr.length;++i){
	            if(i==0){
	                destinationArr[i]=sourceArr[i];
	            }else{
	                destinationArr[i]=sourceArr[i-1];
	            }
	        }

		return destinationArr;
	}
}
