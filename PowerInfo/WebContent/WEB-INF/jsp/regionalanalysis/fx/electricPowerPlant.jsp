<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.github.regionalanalysis.db.task.entity.DbTask"%>

<!DOCTYPE html>
<html>
<head>
<title>单一电厂</title>
<meta http-equiv="content-type" content="text/html;charset=UTF-8" />
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">

<!--引入此文件包含jquery_easyui的css样式与公用js以及登录用户信息-->
<%@include file="../../common/commonInclude.jsp"%>
<%@include file="../../common/commonDefineBtn.jsp"%>
<%
	String pid = request.getAttribute("pid") == null ? "" : request.getAttribute("pid").toString();
DbTask tt=  (DbTask)request.getSession().getAttribute("fxtask");

String taskid = "" ;
String task_name = "";
if(tt != null){
	 taskid = tt.getId();
	 task_name = tt.getTask_name();
}
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
var task_id='<%=taskid%>';
var task_name='<%=task_name%>';
var area_id='<%=organCode%>';
var organ_name='<%=organName%>';


var cols = [ [ {
	field : 'id',
	width : 20,
	align : 'center',
	checkbox : true
}, {
	field : 'plant_name',
	title : '电厂名称',
	width : 200,
	align : 'center',
	formatter: function(value,row,index){

		return '<a href="#" onclick="detail('+row.id+')">'+value+'</a> ';
	}
}, {
	field : 'plant_capacity',
	title : '装机总容量（WM）',
	width : 100,
	align : 'center'
}, {
	field : 'product_year',
	title : '开工年',
	width : 100,
	align : 'center'
}, {
	field : 'build_year',
	title : '建成年',
	width : 100,
	align : 'center'
}, {
	field : 'start_outlay',
	title : '静态投资（万元）',
	width : 100,
	align : 'center'
}, {
	field : 'consumption_rate',
	title : '厂用电率',
	width : 100,
	align : 'center'
}, {
	field : 'electricity_consumption',
	title : '厂用电量（千瓦时）',
	width : 100,
	align : 'center'
}, {
	field : 'power_type_name',
	title : '电源类型',
	width : 100,
	align : 'center'
}, {
	field : 'cooling_type_name',
	title : '冷却类型',
	width : 100,
	align : 'center'
}, {
	field : 'materials_cost',
	title : '电厂材料费（元/年）',
	width : 100,
	align : 'center'
}, {
	field : 'salary',
	title : '工资、奖金及福利费（元/年）',
	width : 100,
	align : 'center'
}, {
	field : 'repairs_cost',
	title : '修理费（元/年）',
	width : 100,
	align : 'center'
}, {
	field : 'other_cost',
	title : '其他费用（元/年）',
	width : 100,
	align : 'center'
}] ];
	$(function() {	
		
		$('#organ_name').html('<b>'+organ_name+'</b>');

		$('#task_name').html('<b>'+task_name+'</b>');
		$("#tool_query").bind("click", function() {
			queryData();
		});
		$("#tool_update").bind("click", function() {
			updateRecord();
		});
		/*$("#tool_delete").bind("click", function() {
			deleteRecords();
		});*/

		$("#tool_db").bind("click", function() {
			duibi();
		});
		$("#tool_export").bind("click", function() {
			ExportExcel();
		});
		if(task_id==""){
			var Height_Page = $(document).height();
			var datagrid_title_height = $("#datagrid_div").position().top;
			var height = Height_Page - datagrid_title_height;
			$('#datagrid').datagrid({
				width : 'auto',
				height : height,
				autoRowHeight : false,
				collapsible : true,
				url : "",
				remoteSort : false,
				columns : cols,
				rownumbers : true,
				pagination:true
			});
			$.messager.alert('提示', '请先选择任务！', 'info');
			return ;
		}
		queryData();
	});
	
	function detail(id) {
		 window.parent.closeSingleExtent('电厂详情');
		 window.parent.addTab('电厂详情', path+'/plantAnalysisfx/detail?id='+id+"&task_id="+task_id, '');
	}
	function ExportExcel() {//导出Excel文件
		var plant_name=$("#plant_name").val();
		//用ajax发动到动态页动态写入xls文件中
		var f = $('<form action="'+path+'/plantAnalysisfx/exportData" method="post" id="fm1"></form>');
        var l=$('<input type="hidden" id="name" name="name" />');  
        var m=$('<input type="hidden" id="task_id" name="task_id" />');  
    	l.val(plant_name);  
    	l.appendTo(f);  
    	m.val(task_id);  
    	m.appendTo(f);  
    	f.appendTo(document.body).submit();  
    	document.body.removeChild(f);  
	}
	//查询方法调用的函数
	function queryData() {
		var plant_name=$("#plant_name").val();
		var queryParams = {
			name :plant_name,
			"task_id":task_id,
			"area_id":area_id
			
		};
		var url = path + '/plantAnalysisfx/queryData';
		var Height_Page = $(document).height();
		var datagrid_title_height = $("#datagrid_div").position().top;
		var height = Height_Page - datagrid_title_height;
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

	function updateRecord() {
		var rows = $('#datagrid').datagrid('getChecked');
		if(rows.length !=1){
			$.messager.alert('提示', '请选择一个电厂！', 'info');
			return ;
		}
		 window.parent.closeSingleExtent('电厂修改');
		 window.parent.addTab('电厂修改', path+'/plantAnalysisfx/openUploadRecord?id='+rows[0]["id"], '');
	}
	function duibi() {
		var rows = $('#datagrid').datagrid('getChecked');
		if(rows.length !=1){
			$.messager.alert('提示', '请选择一个需要分析的电厂！', 'info');
			return ;
		}
		var ids = "";
		for (rowindex in rows) {
			if (parseInt(rowindex) + 1 == rows.length) {
				ids = ids + rows[rowindex]["id"];
			} else {
				ids = ids + rows[rowindex]["id"] + ",";
			}
		}
		window.parent.closeSingleExtent('电厂典型日成本分析展示');
		 window.parent.addTab('电厂典型日成本分析展示', path+'/electricityContrastFxController/main?id='+ids+'&task_id='+task_id, '');
		
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
					"ids" : ids,
					"task_id" :task_id
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
		</a><a id="tool_update"> <img
			src='<%=path%>/static/images/xiugai.gif' align='top' border='0'
			title='修改' />
		</a> 
		 <a id="tool_db"> <img
			src='<%=path%>/static/images/cbfx.jpg' align='top' border='0'
			title='成本分析' />
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
				<td class="tdlft">区域：</td>
				<td ><span id="organ_name"></span></td>
			<td class="tdlft">任务：</td>
				<td ><span id="task_name"></span></td>
				<td class="tdlft">电厂名称：</td>
				<td class="tdrgt"><input id="plant_name"  name="plant_name"/></td>
			</tr>
		</table>
	</fieldset>
			<div id="title" style="padding-right: 5px;text-align: right"><b>单位：万千瓦，吨，年，%，元，元/年</b></div>
	
	<div id="datagrid_div">
		<table id="datagrid"></table>
	</div>

</body>
</html>