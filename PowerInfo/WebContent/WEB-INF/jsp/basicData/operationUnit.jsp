<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="content-type" content="text/html;charset=UTF-8" />
<title>Insert title here</title>
</head>
<%@include file="../common/commonInclude.jsp"%>
<%@include file="../common/commonDefineBtn.jsp" %>
<script type="text/javascript">
$(function() {

	$("#tool_add").bind("click", function() {
		addUnit();
	});
	$("#tool_update").bind("click", function() {
		updateUnit();
		
	});
	$("#btn_cancel").bind("click", function() {
		cancel();
	});
	queryData();
});
function queryData() {
	var cols = [ [ {
		field : 'id',
		width : 20,
		align : 'center',
		checkbox : true
	},{
		field : 'unit_value',
		title : '单位名称',
		width : 100,
		align : 'center'
	} ] ];
		var url = path + '/basicData/queryUnits';
		var Height_Page = $(document).height();
		var datagrid_title_height = $("#datagrid_div").position().top;
		var height = Height_Page - datagrid_title_height;
		$('#datagrid').datagrid({
			width : 'auto',
			height : height,
			autoRowHeight : false,
			collapsible : true,
			url : url,
			singSelect:true,
			remoteSort : false,
			columns : cols,
			rownumbers : true,
			pagination : false
		});
}
//取消
function cancel(){
	//关闭窗口
	window.parent.$('#win_div').window('close');
}
/**
 * 基础数据新增单位
 */
function addUnit() {
	commonHelper.toAdd({
		title : '增加单位',
		width : 320,
		height : 220,
		url : "openAddUnit"
	});

}
/**
 * 基础数据修改单位
 */
function updateUnit() {
	var row=$('#datagrid').datagrid('getChecked');
	if(row.length<=0){
		$.messager.alert('提示','必须选择一条记录','info');
	}else{
		commonHelper.toAdd({
			title : '修改单位',
			width : 320,
			height : 220,
			url : "openUpdateUnit"
		});
	}

}
</script>
<body>
	<div id="btn_div">
		<a id="tool_add"> <img src='<%=path%>/static/images/new.gif'
			align='top' border='0' title='新增' />
		</a> <a id="tool_update"> <img
			src='<%=path%>/static/images/xiugai.gif' align='top' border='0'
			title='修改' />
		</a> <a id="btn_cancel" > <img
			src="/PowerInfo/static/images/tc.png" border="0"
			style="vertical-align: middle" title='退出'></a>
	</div>
	<div id="datagrid_div">
		<table id="datagrid"></table>
	</div>
</body>
</html>