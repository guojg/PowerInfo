package com.github.common.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;



import java.util.Set;

import com.github.common.tree.AttributeMsg;
import com.github.common.tree.NodeMsg;
import com.github.totalquantity.sysdict.entity.Sysdict;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JsonUtils {
	/**
	 * @param list	需转换的集合List<Map>
	 * @return 转换后的json字符串
	 */
	public static String listTranJson(List<Map<String, Object>> list){
		JSONObject rows = new JSONObject();
		for(int j=0 ;j <list.size(); j++){
			Set<String> set = list.get(j).keySet() ;
			Iterator<String> iter = set.iterator();
			while(iter.hasNext()){
				String k = iter.next() ;
				rows.put(k, list.get(j).get(k));
			}
			
		}
		return rows.toString() ;
	}
	
	/**
	 * @param list	需转换的集合List<Map>
	 * @return 转换后的json字符串
	 */
	public static String sysDictListTranJson(List<Sysdict> list){
		JSONObject rows = new JSONObject();
		for(int j=0 ;j <list.size(); j++){
			Sysdict sd = list.get(j) ;	
			rows.put(sd.getCode(),sd.getValue());			
		}
		return rows.toString() ;
	}
	
	/**
	 * @param list	需转换的集合List<Map>
	 * @return 转换后的json字符串
	 */
	public static String listTranJsonByQuery(List<Map<String, Object>> list){
		JSONObject rows = new JSONObject();
		JSONArray jsonArray  = new JSONArray();
		for(int j=0 ;j <list.size(); j++){
			Set<String> set = list.get(j).keySet() ;
			Iterator<String> iter = set.iterator();
			JSONObject row = new JSONObject();

			while(iter.hasNext()){
				String k = iter.next() ;
				row.put(k, list.get(j).get(k));
			}
			jsonArray.add(row);
			
		}
		rows.put("rows", jsonArray) ;
		return rows.toString() ;
	}
	/**
	 * @param list	需转换的集合List<Map>
	 * @return 转换后的json字符串
	 */
	public static String listTranJsonByPage(List<Map<String, Object>> list,int total){
		JSONObject rows = new JSONObject();
		JSONArray jsonArray  = new JSONArray();
		for(int j=0 ;j <list.size(); j++){
			Set<String> set = list.get(j).keySet() ;
			Iterator<String> iter = set.iterator();
			JSONObject row = new JSONObject();

			while(iter.hasNext()){
				String k = iter.next() ;
				row.put(k, list.get(j).get(k));
			}
			jsonArray.add(row);
			
		}
		rows.put("total", total);
		rows.put("rows", jsonArray) ;
		return rows.toString() ;
	}
	/**
	 * @param list	需转换的集合List<Map>
	 * @return 转换后的json字符串
	 */
	public static String listTranJsonByMenu(List<Map<String, Object>> list){
		JSONObject rows = new JSONObject();
		JSONArray jsonArray  = new JSONArray();
		String str="";
		for(int j=0 ;j <list.size(); j++){
			Set<String> set = list.get(j).keySet() ;
			Iterator<String> iter = set.iterator();
			JSONObject row = new JSONObject();

			while(iter.hasNext()){
				String k = iter.next() ;
				row.put(k, list.get(j).get(k));
				if("icon".equals(k)){
					str=list.get(j).get(k).toString();
				}
			}
			jsonArray.add(row);
			
		}
		rows.put(str, jsonArray) ;
		return rows.toString() ;
	}
	/**
	 * @desc 将list 集合转换为 json对象
	 * @author liubb
	 * @param list : list 集合数据
	 * @reuturn 符合 json 类型的集合数据
	 * @date 2014-12-23
	 */
	public static String transformListToJson(List list){
		JSONArray jsonArray  = JSONArray.fromObject(list);
		return jsonArray.toString();
	}
	

	/**
	 * @desc 将list 集合转换为 tree 集合
	 * @author liubb
	 * @param list : list 集合数据
	 * @reuturn 符合 tree 类型的集合数据
	 * @date 2014-12-23
	 */
	public static List<NodeMsg> transformBoToTree(List<Map<String, Object>> list){
		List<NodeMsg> resultList = new ArrayList<NodeMsg>();
		
		for(int i=0;i<list.size();i++){
			Map hashMap = (Map) list.get(i);
			String url=hashMap.get("url")==null?"":hashMap.get("url").toString();
			String tablename=hashMap.get("tablename")==null?"":hashMap.get("tablename").toString();
			NodeMsg node = new NodeMsg(hashMap.get("id")==null?"":hashMap.get("id").toString() , 
										hashMap.get("text")==null?"":hashMap.get("text").toString() , 
										hashMap.get("parent_id")==null?"":hashMap.get("parent_id").toString(),
										hashMap.get("isleaf")==null?"":hashMap.get("isleaf").toString(),
												new AttributeMsg(url,tablename));
			
			resultList.add(node);
		}
		return resultList ;
	}
}
