package com.github.common.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TreeUtil {
    /**
     * 根据NodeMsg集合生成字典，为拼装树结构时能根据字典通过id，parentid定位到具体的TreeNode
     * @param list
     * @return
     */
    private static Map<String, NodeMsg> createDic(List<NodeMsg> list) {
        HashMap<String, NodeMsg> map = new HashMap<String, NodeMsg>();
        if (list != null && list.size() > 0) {
            for (NodeMsg node : list) {
                map.put(node.getId(), node);
            }
            return map;
        } else {
            return null;
        }
    }
    
    /**
     * @param list
     * @return
     */
    public static String createTreeJson(List<NodeMsg> list) {
        Map<String, NodeMsg> map = createDic(list);
        
        TreeNode rootNode = new TreeNode(new NodeMsg(), map);
        
        for (NodeMsg node : list) {
            if (node.getParentid() == null || "".equals(node.getParentid())) {
                rootNode.addLeaf(node);
            } else {
                rootNode.addLeaf(node.getParentid(), node.getId());
            }
        }
        return rootNode.createTreeJson();
    }
    
    /**
     * 根据传入的TreeNode列表组装成json,异步加载
     */
    public static String createTreeAsynJson(List<NodeMsg> list) {
        String json = "[";
        for (NodeMsg node : list) {
            json += "{";
            String id = node.getId();
            String text = node.getText();
            String isleaf = node.getIsLeaf();
            String state = "0".equals(isleaf) ? "closed" : "open";
            json += "\"id\" : \"" + id + "\", \"text\" : \"" + text + "\", \"state\" : \"" + state + "\"";
            json += "},";
        }
        json = json.substring(0, json.length() - 1);
        json += "]";
        return json;
    }
    
   
    
}
