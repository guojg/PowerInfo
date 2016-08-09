<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.github.totalquantity.task.entity.TotalTask"%>
 <!DOCTYPE html>
<html>
<head>
<title>电力平衡</title>
<meta http-equiv="content-type" content="text/html;charset=UTF-8" />
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">

<!--引入此文件包含jquery_easyui的css样式与公用js以及登录用户信息-->
<%@include file="../../common/commonInclude.jsp"%>

<script type="text/javascript">
var cols;
$(function() {
	//非冰冻列
	cols = createCols("2016,2017");
$('#datagrid').treegrid({    
   url:path+'/electricitybalance/queryData',   
   //url:path+'/js/basicData/treegrid_data3.json',
    idField:'id',    
    treeField:'code_name',    
    frozenColumns:[[    
        {title:'id',field:'id',width:180,hidden:true},    
        {field:'parentid',title:'_parentId',width:180,align:'right',hidden:true},  
        {field:'code_name',title:'指标',width:180},  
    ]] ,
	columns : cols
}); 
});

//动态生成列
function createCols(years) {
	var cols = [];
	var tmp = [];

	tmp = years.split(",");
	for (var i = 0; i < tmp.length; i++) {
		cols.push({
			'field' : tmp[i] + "",
			'title' : "" + tmp[i] + "年",
			'align' : 'center',
			'width' : 120

		});
	}
	cols.push({
		'field' : "hour_num",
		'title' : "机组利用小时数",
		'align' : 'center',
		'width' : 120

	});
	return new Array(cols);
}
</script>
</head>
<body>
	<!-- 引入自定义按钮页面 -->
	<div id="btn_div">
		<a id="tool_query"> <img src='<%=path%>/static/images/query.gif'
			align='top' border='0' title='查询' />
		</a> <a id="tool_save"> <img src='<%=path%>/static/images/save.gif'
			align='top' border='0' title='计算' />
		</a>
	</div>
	<fieldset id="field">
		<legend>查询条件</legend>
		<table id="search_tbl">
		</table>
	</fieldset>
	<div id="datagrid_div">
		<table id="datagrid" ></table>
	</div>

</body>
</html>