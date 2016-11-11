<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>任务</title>
<meta http-equiv="content-type" content="text/html;charset=UTF-8" />
<!--引入此文件包含jquery_easyui的css样式与公用js以及登录用户信息-->
<%@include file="../../../common/commonInclude.jsp"%>
<%@include file="../../../common/commonDefineBtn.jsp" %>
<%
Object obj=  request.getSession().getAttribute("maparea");
String organCode="";
if(obj != null) {
	organCode = obj.toString() ; 
}

Object objName=  request.getSession().getAttribute("organName");
String organName="";
if(objName != null) {
	organName = objName.toString() ; 
}

%>
<script type="text/javascript">
var organCode='<%=organCode%>';
var organ_name='<%=organName%>';

	var cols = [ [
	    {field:'id',align:'center',checkbox:true},
	    {
			field : 'area_name',
			title : '电网企业',
			width : 100,
			align : 'center'
		} ,
	    {
		field : 'task_name',
		title : '任务名',
		width : 250,
		align : 'center',
		formatter: function(value,row,index){

    		return '<a href="#" onclick="detail(\''+row.task_name+"\',"+row.id+')">'+value+'</a> ';
		}
	} ] ];
	

	$(function() {
	
		if(organCode==""){
			$.messager.alert('提示', '请选择从地图中区域！', 'info');
			return ;
		}
		$('#organ_name').html('<b>'+organ_name+'</b>');

		$("#tool_xjrw").bind("click", function() {
			xjrw();
		});
		$("#tool_xgrw").bind("click", function() {
			xgrw();
		});
		$("#tool_scrw").bind("click", function() {
			scrw();
		});
		queryData();
	});

	//查询方法调用的函数
	function queryData() {
	
		//查询条件暂时放外面
		var queryParams = {"area_id":organCode};

		var url = path + '/taskfx/queryData';
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
			columns : cols,
			checkOnSelect:true,
			singleSelect:true,
			rownumbers : true,
			pagination : false,
			queryParams : queryParams,
			pagination: true
		});
	}
	function xjrw(){
		commonHelper.toAdd({
			title : '新建任务',
			width : 500,
			height : 320,
			url : path + "/taskfx/taskAdd"
		});
	}
	
	function xgrw(){
		var rows = $('#datagrid').datagrid('getChecked');
		commonHelper.toAdd({
			title : '修改任务',
			width : 500,
			height : 320,
			url : path + "/taskfx/taskAdd?id="+rows[0].id
		});
	}
	function detail(task_name,id){
		var param={
				"id":id,
				"task_name":task_name
		};
		$.ajax({
			 type : 'POST',
			 url : path+'/taskfx/taskDetail',
			 dataType: 'text',
			 data: param,
			 async:false,
			 success:function(msg){
					//页面跳转
					window.parent.addNav(190);
		 	 }
		});
	
	}
	
	function scrw(){
		var rows = $('#datagrid').datagrid('getChecked');
		if(rows.length<1){
			$.messager.alert('提示', '请选择需要删除的任务！', 'info');
			return ;
		}
		$.messager.confirm('提示', '确认删除?', function(r) {
			if (r) {
				var ids = "";
				for (rowindex in rows) {
					if (parseInt(rowindex) + 1 == rows.length) {
						ids = ids + rows[rowindex]["id"];
					} else {
						ids = ids + rows[rowindex]["id"] + ",";
					}
				}
				$.post(path+'/taskfx/deleteRecord', {
					"ids" : ids
				}, function(data) {
					var data = $.parseJSON(data);
					if (data== '1') {
						$.messager.alert('提示', '删除成功！', 'info', function() {
							queryData();

						});
					}
				});
			}
		});
	}
	
</script>
</head>
<body>
	<!-- 引入自定义按钮页面 -->
	<div id="btn_div">
		<a id="tool_xjrw"> <img src='<%=path%>/static/images/xjrw.gif'
			align='top' border='0' title='新建任务' />
		</a>
		<a id="tool_xgrw"> <img src='<%=path%>/static/images/xiugai.gif'
			align='top' border='0' title='修改' />
		</a> <a id="tool_scrw"> <img
			src='<%=path%>/static/images/delete.png' align='top' border='0'
			title='删除' />
		</a>
	</div>
<fieldset id="field">
		<legend></legend>
		<table id="search_tbl">
			<tr>
			<td class="tdlft">区域：</td>
				<td class="tdrgt"><span id="organ_name"></span></td>
		
			</tr>
		</table>
	</fieldset>
	<div id="datagrid_div">
		<table id="datagrid"></table>
	</div>

</body>
</html>