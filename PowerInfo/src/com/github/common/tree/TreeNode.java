package com.github.common.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class TreeNode {
    
    // 每个节点的详细信息
    private NodeMsg nodeMsg;
    
    // 父节点
    private TreeNode parent;
    
    // 子节点信息
    private List<TreeNode> leafs = new ArrayList<TreeNode>();
    
    // 全局变量对应关系，增加节点需要适时更新
    private Map<NodeMsg, TreeNode> locate = new HashMap<NodeMsg, TreeNode>();
    
    // 全局变量对应关系，节点信息的id同节点信息的对应关系，在类建立的时候必须将全部信息保存进dic中
    private Map<String, NodeMsg> dic = new HashMap<String, NodeMsg>();
    
    /**
     * 生成一个TreeNode
     * @param nodeMsg
     */
    public TreeNode(NodeMsg nodeMsg) {
        this.nodeMsg = nodeMsg;
        this.locate.put(nodeMsg, this);
    }
    
    /**
     * 生成一个TreeNode，同时传入几点信息的字典对应关系
     * @param nodeMsg
     * @param dic
     */
    public TreeNode(NodeMsg nodeMsg, Map<String, NodeMsg> dic) {
        this(nodeMsg);
        this.dic = dic;
        if (dic.size() == 0) {
            throw new Error("没有足够的字典信息，不能创建树形结构");
        }
    }
    
    /**
     * 新增一个节点，需要一个节点信息作为参数
     * 新增节点做为根节点的子节点或某节点的子节点
     * @param nodeMsg
     */
    public TreeNode addLeaf(NodeMsg nodeMsg) {
        TreeNode node = new TreeNode(nodeMsg);
        this.leafs.add(node);
        node.parent = this;
        node.locate = this.locate;
        node.dic = this.dic;
        // 更新locate， 有新的节点加入，需要更新全局信息
        node.locate.put(nodeMsg, node);
        return node;
    }
    
    /**
     * 在一个节点下新增一个子节点，如果parentNode不存在，则先在根节点下新增parentNode节点
     * @param parentNode
     * @param curNode
     */
    public void addLeaf(NodeMsg parentNode, NodeMsg curNode) {
        if (this.locate.containsKey(parentNode)) {
            this.locate.get(parentNode).addLeaf(curNode);
        } else {
            addLeaf(parentNode).addLeaf(curNode);
        }
    }
    
    /**
     * 重载方法，传入的是节点
     * @param parentId
     * @param curId
     */
    public void addLeaf(String parentId, String curId) {
        if (this.dic.containsKey(parentId)) {
            this.addLeaf(this.dic.get(parentId), this.dic.get(curId));
        } else {
            this.addLeaf(this.dic.get(curId));
        }
    }
    
    /**
     * 直接在当前节点下新增一个节点
     * @param node
     */
    public void addNode(TreeNode node) {
        this.leafs.add(node);
        node.parent = this;
        this.locate.putAll(node.locate);
    }
    
    /**
     * 给当前节点设置父节点
     * @param node
     * @return
     */
    public TreeNode setAsParent(NodeMsg nodeMsg) {
        TreeNode node = new TreeNode(nodeMsg);
        this.parent = node;
        node.leafs.add(this);
        node.locate = this.locate;
        node.locate.put(nodeMsg, node);
        return node;
    }
    
    /**
     * 拿到节点信息
     * @return
     */
    public NodeMsg getNodeMsg() {
        return this.nodeMsg;
    }
    
    /**
     * 根据节点信息拿到节点
     * @param node
     * @return
     */
    public TreeNode getTreeNode(NodeMsg node) {
        return this.locate.get(node);
    }
    
    /**
     * 拿到父节点
     * @return
     */
    public TreeNode getParentNode() {
        return this.parent;
    }
    
    /**
     * 拿到子节点
     * @return
     */
    public List<TreeNode> getChildren() {
        return this.leafs;
    }
    
    /**
     * 传入根节点输出树结构json串，对于任何树结构都需要生成一个虚拟根节点，用虚拟的根节点来生成树结构
     * @param root
     * @return
     */
    public String createTreeJson(NodeMsg root) {
        String json = "";
        List<TreeNode> list = this.locate.get(root).getChildren();
        if (list != null && list.size() > 0) {
            JSONArray tree = new JSONArray();
            for (TreeNode node : list) {
                JSONObject jsonNode = new JSONObject();
                JSONObject subNode = new JSONObject();
                subNode.put("url", node.getNodeMsg().getAttributes().getUrl()) ;
                subNode.put("tablename", node.getNodeMsg().getAttributes().getTableName());
                subNode.put("unit_code", node.getNodeMsg().getAttributes().getUnit_code());
                jsonNode.put("id", node.getNodeMsg().getId());
                jsonNode.put("text", node.getNodeMsg().getText());
                jsonNode.put("attributes",subNode);
                if (node.getChildren() != null && node.getChildren().size() > 0) {
                    jsonNode.put("state", "closed");
                    jsonNode.put("children", createTreeJson(node.getNodeMsg()));
                }
                tree.add(jsonNode);
            }
            json = tree.toString();
        }
        return json;
    }
    
    /**
     * 重载方法，传入为跟节点
     * @param node
     * @return
     */
    public String createTreeJson(TreeNode node) {
        return this.createTreeJson(node.getNodeMsg());
    }
    
    /**
     * 以当前节点为虚拟节点生成树结构
     * @return
     */
    public String createTreeJson() {
        return this.createTreeJson(this);
    }
    
    /**
     * 虚列一个根节点并生成树结构
     * @param args
     */
    public String createTreeJsonProx() {
        return this.createTreeJson(this.setAsParent(new NodeMsg()));
    }
    
  

}
