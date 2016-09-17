<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>机组</title>
<meta http-equiv="content-type" content="text/html;charset=UTF-8" />
<!--引入此文件包含jquery_easyui的css样式与公用js以及登录用户信息-->
<%@include file="../common/commonInclude.jsp"%>
<%@include file="../common/commonDefineBtn.jsp" %>

<script type="text/javascript">
	var frozenCols = [ [
	    {field:'id',align:'center',checkbox:true},
	    {
		field : '100',
		title : '机组名',
		width : 100,
		align : 'center'
	} ] ];
	
	var cols ='';
	$(function() {
	
	
		
		 cols = [ [
		          	 {
		          		field : '200',
		          		title : '所属发电厂',
		          		width : 100,
		          		align : 'center'
		          	}, {
		          		field : '300',
		          		title : '额定容量',
		          		width : 100,
		          		align : 'center'
		          	}, {
		          		field : '400',
		          		title : '投运日期',
		          		width : 400,
		          		align : 'center'
		          	}] ];
		
		queryData();
	});

	//查询方法调用的函数
	function queryData() {
	
		//查询条件暂时放外面
		var queryParams = {};
debugger;
		var url = path+'/generatorSetController/queryData';
		var Height_Page = $(document).height();
		var datagrid_title_height = $("#datagrid_div").position().top;
		var height = Height_Page - datagrid_title_height;
		$('#datagrid').datagrid({
			width : 'auto',
			height : height,
			autoRowHeight : false,
			collapsible : true,
			url : url,
			remoteSort : false,
			frozenColumns : frozenCols,
			columns : cols,
			checkOnSelect:true,
			singleSelect:true,
			rownumbers : true,
			pagination : false,
			queryParams : queryParams,
			pagination: true
		});
	}
	
</script>
</head>
<body>
	<!-- 引入自定义按钮页面 -->
	<div id="btn_div">
		
	</div>

	<div id="datagrid_div">
		<table id="datagrid"></table>
	</div>

</body>
</html>