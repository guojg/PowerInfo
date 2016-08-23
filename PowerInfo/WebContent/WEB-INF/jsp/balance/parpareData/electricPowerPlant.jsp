<%@ page language="java" pageEncoding="UTF-8"%>
<html>
<head>
<title>电厂</title>
<meta http-equiv="content-type" content="text/html;charset=UTF-8" />
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">

<!--引入此文件包含jquery_easyui的css样式与公用js以及登录用户信息-->
<%@include file="../../common/commonInclude.jsp"%>
<%@include file="../../common/commonDefineBtn.jsp"%>
<%
	String pid = request.getAttribute("pid") == null ? "" : request.getAttribute("pid").toString();
%>
<script type="text/javascript">
	var cols = [ [ {
		field : 'id',
		width : 20,
		align : 'center',
		checkbox : true
	}, {
		field : 'plant_name',
		title : '电厂名称',
		width : 200,
		align : 'center'
	}, {
		field : 'plant_capacity',
		title : '装机容量',
		width : 100,
		align : 'center'
	}, {
		field : 'index_itemname',
		title : '电源类型',
		width : 100,
		align : 'center'
	}, {
		field : 'start_date',
		title : '投产日期',
		width : 100,
		align : 'center'
	}, {
		field : 'end_date',
		title : '退役日期',
		width : 100,
		align : 'center'
	} ] ];
	$(function() {
		 comboBoxInit({
				id : "index_item",
				url : path + '/sysdict/getDataByCodeValue?domain_id=12',
				textkey : "value",
				valuekey : "code",
				multiple : true

		 });
		$("#tool_add").bind("click", function() {
			addRecord();
		});
		$("#tool_query").bind("click", function() {
			queryData();
		});
		$("#tool_update").bind("click", function() {
			updateRecord();
		});
		$("#tool_delete").bind("click", function() {
			deleteRecords();
		});
		$("#tool_export").bind("click", function() {
			ExportExcel();
		});
		queryData();
	});
	function ExportExcel() {//导出Excel文件
		var indexs = $("#index_item").combo("getValues");
		//指标
		var index_s;
		if (indexs != "") {
			index_s = indexs + "";
		} else {
			index_s = "";
		}
		if (index_s == "") {
			$.messager.alert("提示", "请选择电源类型！");
			return;
		}
		var plant_name=$("#plant_name").val();
		//用ajax发动到动态页动态写入xls文件中
		var f = $('<form action="'+path+'/electricPowerPlant/exportData" method="post" id="fm1"></form>');
        var i = $('<input type="hidden" id="indexs" name="indexs" />');  
        var l=$('<input type="hidden" id="name" name="name" />');  
    	i.val(index_s);  
    	i.appendTo(f);  
    	l.val(plant_name);  
    	l.appendTo(f);  
    	f.appendTo(document.body).submit();  
    	document.body.removeChild(f);  
	}
	//查询方法调用的函数
	function queryData() {
		var indexs = $("#index_item").combo("getValues");
		//指标
		var index_s;
		if (indexs != "") {
			index_s = indexs + "";
		} else {
			index_s = "";
		}
		if (index_s == "") {
			$.messager.alert("提示", "请选择电源类型！");
			return;
		}
		var plant_name=$("#plant_name").val();
		var queryParams = {
			indexs : index_s,
			name :plant_name
			
		};
		var url = path + '/electricPowerPlant/queryData';
		var Height_Page = $("html").height();
		var datagrid_title_height = $("#datagrid_div").position().top;
		var height = Height_Page - datagrid_title_height - 5;
		$('#datagrid').datagrid({
			width : 'auto',
			height : height,
			autoRowHeight : false,
			collapsible : true,
			url : url,
			queryParams : queryParams,
			remoteSort : false,
			columns : cols,
			rownumbers : true,
			pagination:true
		});
	}
	function addRecord() {
		commonHelper.toAdd({
			title : '新增',
			width : 500,
			height : 300,
			url : "openAddRecord"
		});
	}
	function updateRecord() {
		commonHelper.toAdd({
			title : '修改',
			width : 500,
			height : 300,
			url : "openUploadRecord"
		});
	}
	function deleteRecords() {
		$.messager.confirm('提示', '确认删除?', function(r) {
			if (r) {
				var rows = $('#datagrid').datagrid('getChecked');
				var ids = "";
				for (rowindex in rows) {
					if (parseInt(rowindex) + 1 == rows.length) {
						ids = ids + rows[rowindex]["id"];
					} else {
						ids = ids + rows[rowindex]["id"] + ",";
					}
				}
				$.post('deleteRecord', {
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
		<a id="tool_query"> <img src='<%=path%>/static/images/query.gif'
			align='top' border='0' title='查询' />
		</a> <a id="tool_add"> <img src='<%=path%>/static/images/new.gif'
			align='top' border='0' title='新增' />
		</a> <a id="tool_update"> <img
			src='<%=path%>/static/images/xiugai.gif' align='top' border='0'
			title='修改' />
		</a> <a id="tool_delete"> <img
			src='<%=path%>/static/images/delete.png' align='top' border='0'
			title='删除' />
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
				<td class="tdlft">电厂名称：</td>
				<td class="tdrgt"><input id="plant_name"  name="plant_name"/></td>
				<td class="tdlft">电源类型：</td>
				<td class="tdrgt"><input id="index_item" name="index_item"/></td>
			</tr>
		</table>
	</fieldset>
	<div id="datagrid_div">
		<table id="datagrid"></table>
	</div>

</body>
</html>