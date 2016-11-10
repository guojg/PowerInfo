<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>机组</title>
<meta http-equiv="content-type" content="text/html;charset=UTF-8" />
<!--引入此文件包含jquery_easyui的css样式与公用js以及登录用户信息-->
<%@include file="../../common/commonInclude.jsp"%>
<%@include file="../../common/commonDefineBtn.jsp" %>
<% 
String id = request.getParameter("id")==null ?"" : request.getParameter("id"); 
%>
<script type="text/javascript">
var id='<%=id%>' ;

var cols ='';
cols = [ [  {
	field : 'plant_name',
	title : '电厂名称',
	width : 150,
	align : 'center'
}, {
	field : 'jz_id',
	title : '机组名称',
	width : 150,
	align : 'center',
	hidden:true
} , {
	field : 'jz_name',
	title : '机组名称',
	width : 150,
	align : 'center'
}, {
	field : 'index_y_name',
	title : '指标名称',
	width : 100,
	align : 'center'
} , {
	field : 'unit_name',
	title : '单位',
	width : 100,
	align : 'center'
} , {
	field : 'value',
	title : '成本',
	width : 100,
	align : 'center'
}]];
	$(function() {
		 comboBoxInit({
				id : "index_y",
				url : path + '/sysdict/getDataByCodeValue?domain_id=303',
				textkey : "value",
				valuekey : "code",
				multiple : true
			});
	
		queryData();
		$("#tool_query").bind("click", function() {
			queryData();
		});
		$("#tool_export").bind("click", function() {
			ExportExcel();
		});
	});

	//查询方法调用的函数
	function queryData() {
		
		
		var indexy = $("#index_y").combo("getValues");
		var index_y;
		if (indexy != "") {
			index_y = indexy + "";
		} else {
			index_y = "";
		}
		if (index_y == "") {
			$.messager.alert("提示", "请选择指标！");
			return;
		}
		//查询条件暂时放外面
		var queryParams = {
				"index_ys" :index_y,
				"id":id
		};
		var url = path+'/generatorContrastDbYearController/queryData';
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
			onLoadSuccess: function(data) {
				$('#datagrid').datagrid('mergeCellsByField','plant_name');

				$('#datagrid').datagrid('mergeCellsByField','jz_name');
				
			}
		});
	}
	function ExportExcel() {//导出Excel文件
		debugger;
		var indexy = $("#index_y").combo("getValues");
		var index_y;
		if (indexy != "") {
			index_y = indexy + "";
		} else {
			index_y = "";
		}
		if (index_y == "") {
			$.messager.alert("提示", "请选择指标！");
			return;
		}
	
		//用ajax发动到动态页动态写入xls文件中
		var f = $('<form action="'+path+'/generatorContrastDbYearController/exportData" method="post" id="fm1"></form>');  
	    var l = $('<input type="hidden" id="index_ys" name="index_ys" />');
	    var n = $('<input type="hidden" id="id" name="id" />');  
		l.val(index_y);  
		l.appendTo(f);  
		n.val(id);  
		n.appendTo(f);  
		f.appendTo(document.body).submit();  
		document.body.removeChild(f);  
	}
	
</script>
</head>
<body>
	<!-- 引入自定义按钮页面 -->
		<!-- 引入自定义按钮页面 -->
	<div id="btn_div">
		<a id="tool_query"> <img src='<%=path%>/static/images/query.gif'
			align='top' border='0' title='查询' />
		</a>
		<a id="tool_export"> <img
			src='<%=path%>/static/images/daochu.gif' align='top' border='0'
			title='导出' />
		</a>
	</div>
	<fieldset id="field">
		<legend>查询条件</legend>
		<table id="search_tbl">
			<tr>
				<td class="tdlft">指标：</td>
				<td class="tdrgt"><input id="index_y" class="comboboxComponent" /></td>
			</tr>
			<tr style="display:none">
			<td>	<input id="index_x" class="comboboxComponent" type="hidden"/>
		   </td>
			</tr>
		</table>
	</fieldset>
	<div id="datagrid_div">
		<table id="datagrid"></table>
	</div>

</body>
</html>