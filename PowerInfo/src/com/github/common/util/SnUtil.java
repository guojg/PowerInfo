package com.github.common.util;

import java.util.Date;



public class SnUtil {
	
	private static int sn = 0;
	
	private static int newSn = 0;
	
	//一次生成1000个数据
	public synchronized static String[] newGetNewID() {
		if (9999-newSn < 1000) {
			newSn = 0;
		}
		String returnPre = new java.text.SimpleDateFormat("yyyyMMddHHmmS").format(new java.util.Date()); 
		String returnMin = "" + newSn;
		newSn = newSn + 999;
		String returnMax = "" + newSn;
		String[] returnStrs = {returnPre, returnMin, returnMax};
		return returnStrs;
	}
	
	public synchronized static String getNewID() {
		if(sn>=9999) {
			sn = 0;
		}
		// 年4月2日2时2分2秒2毫秒3序列4共21位
		return new java.text.SimpleDateFormat("yyyyMMddHHmmssSSS").format(new java.util.Date()) 
				+ new java.text.DecimalFormat("0000").format( sn++ );
	}
	//前缀为自定义
	public synchronized static String getNewID(String prefix) {
		if(sn>=9999) {
			sn = 0;
		}
		// 年4月2日2时2分2秒2毫秒3序列4共21位
		return prefix.concat(new java.text.SimpleDateFormat("yyyyMMddHHmmssSSS").format(new java.util.Date()) 
				+ new java.text.DecimalFormat("0000").format( sn++ ));
	}
		
	public synchronized static String getNewID(int p_len) {
		if(sn>=999) {
			sn = 0;
		}
		// 年4月2日2时2分2秒2毫秒3序列3共20位
		return new java.text.SimpleDateFormat("yyyyMMddHHmmssSSS").format(new java.util.Date()) 
				+ new java.text.DecimalFormat("000").format( sn++ );
	}
	

}
