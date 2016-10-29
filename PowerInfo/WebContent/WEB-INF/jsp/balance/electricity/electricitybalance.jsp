<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.github.balance.task.entity.BalanceTask"%>
 <!DOCTYPE html>
<html>
<head>
<title>电力平衡</title>
<meta http-equiv="content-type" content="text/html;charset=UTF-8" />
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">

<!--引入此文件包含jquery_easyui的css样式与公用js以及登录用户信息-->
<%@include file="../../common/commonInclude.jsp"%>

<% 
		BalanceTask tt=  (BalanceTask)request.getSession().getAttribute("balancetask");
		String taskid = tt.getId();
		String year = tt.getYear();
		String task_name = tt.getTask_name();
		%>
<script type="text/javascript">
var taskid='<%=taskid%>';
var years = '<%=year%>';
var task_name='<%=task_name%>';
var cols;
var savEvtTime = 0;
var dcAt = 0;
var dcTime = 250;
var savTO = null;
var editingId ;
$(function() {
	$('#task_name').html('<b>'+task_name+'</b>');

	$("#tool_query").bind("click", function() {
		queryData();
	});
	$("#tool_save").bind("click", function() {
		save();
	});
	$("#tool_extract").bind("click", function() {
		extractData();
	});
	$("#tool_export").bind("click", function() {
		ExportExcel();
	});
	 comboBoxInit({
			id : "years",
			url : path + '/sysdict/getBalanceYears?year='+years,
			textkey : "value",
			valuekey : "code",
			multiple : true
		});
	 queryData();

});
function queryData(){
	var yrs = $('#years').combo('getValues').join(",");

	//非冰冻列
	cols = createCols(yrs);

$('#datagrid').treegrid({    
	fitColumns:false,
   url:path+'/electricitybalance/queryData',   
   //url:path+'/js/basicData/treegrid_data3.json',
    idField:'id',    
    treeField:'code_name', 
    singleSelect:true,
	queryParams : {"year":yrs,"taskid":taskid},
    frozenColumns:[[    
        {title:'id',field:'id',width:180,hidden:true},    
        {field:'parentid',title:'_parentId',width:180,align:'right',hidden:true},  
        {field:'code_name',title:'指标',width:180},  
    ]] ,
	columns : cols,
	onClickCell : function(field,row) {
		clickEvent(field,row);


	},
	onAfterEdit:function(row ,changes){
		calculateSum();
	}
}); 
}
function ExportExcel() {//导出Excel文件
	var yrs = $('#years').combo('getValues').join(",");

	//用ajax发动到动态页动态写入xls文件中
	var f = $('<form action="'+path+'/electricitybalance/exportData" method="post" id="fm1"></form>');  
    var i = $('<input type="hidden" id="year" name="year" />');  
    var m = $('<input type="hidden" id="taskid" name="taskid" />');  
	i.val(yrs);  
	i.appendTo(f);  
	
	m.val(taskid);  
	m.appendTo(f); 
	f.appendTo(document.body).submit();  
	document.body.removeChild(f);  
}

function calculateSum(){
	var row100 = $('#datagrid').treegrid('find',100);  //全社会用电量
	var row200 = $('#datagrid').treegrid('find',200);	//外购(+)外送(-)
	var row300 = $('#datagrid').treegrid('find',300);	//需自发用电量
	var row400 = $('#datagrid').treegrid('find',400);	//煤电利用小时数
	var tmp = [];

	tmp = $('#years').combo('getValues').join(",").split(",");
	for (var i = 0; i < tmp.length; i++) {
		row300[tmp[i]] = row100[tmp[i]]+row200[tmp[i]];
	}
	 $('#datagrid').treegrid('refresh',300);
}
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
			'width' : 120,
			'editor' : 'numberspinner'

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

//点击事件
function clickEvent( field,row) {

		var d = new Date();
		savEvtTime = d.getTime();
		savTO = setTimeout(function() {
			clickonetime(field, row);
		}, dcTime);

}

/**
 * 单击事件
 */
function clickonetime(field, row) {
	if (savEvtTime - dcAt <= 0) {
		return false;
	}
	editCell(field, row);
}

/**
 * 解开一个格子的编辑状态
 */
function editCell( field, row) {
	debugger;
	if(editingId != undefined ){
		endEdit(editingId);
	}
	//if (rowIndex == noEditRow) return;
	/*for ( var i in cols[cols.length - 1]) {
		var columnOption = $('#datagrid').datagrid('getColumnOption',
				cols[cols.length - 1][i]['field']);
		if (cols[cols.length - 1][i]['field'] == field) {
			columnOption.editor = {type:cols[cols.length - 1][i].editor};
		} else {
			columnOption.editor = {};
		}
	}
	$('#datagrid').datagrid('beginEdit',row.id);
*/			
			if (row){
				editingId = row.id ;
				$('#datagrid').treegrid('beginEdit', editingId);
			}

}

/**
 * 结束表格编辑状态
 */
function endEdit(id) {
	$('#datagrid').treegrid('endEdit', id) ;

}
function extractData(){
	 var param = {
				'taskid':taskid,
				'year':years
			 };
	 $.ajax({
			type : 'POST',
			async : false,
			dataType: 'json',
			data: param,
			url :  path+'/electricitybalance/extractData',
			success : function(data) {
				
				$.messager.alert("提示", "计算成功！");
				queryData();
			}
		});
}

/**
 * ‘保存’按钮功能
 */
function save() {
	//endEdit();
	var updates = $('#datagrid').datagrid('getChanges');
	if (updates.length <= 0) {
		return;
	}
	var param = JSONH.stringify(updates);
	var data = {
		"taskid":taskid,
		"editObj" : param
	};
	$.ajax({
		type : 'POST',
		async : false,
		url : path + '/electricitybalance/saveData',
		data : data,
		success : function(data) {
			if (data == "undifined") {
				$.messager.alert("提示", "保存失败！");
				$('#datagrid').datagrid('reload');
			} else {
				$.messager.alert("提示", "保存成功！");
				$('#datagrid').datagrid('reload');
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
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
		</a> <a id="tool_extract"> <img src='<%=path%>/static/images/js.gif'
			align='top' border='0' title='计算' />
		</a>
		<a id="tool_save"> <img src='<%=path%>/static/images/save.gif'
			align='top' border='0' title='保存' />
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
					<td class="tdlft">任务：</td>
				<td class="tdrgt"><span id="task_name"></span></td>
		<td class="tdlft">年份：</td>
				<td class="tdrgt"><input id="years" class="comboboxComponent" /></td>
		</tr>
		</table>
	</fieldset>
	<div id="title" style="padding-right: 5px;text-align: right"><b>单位：万千瓦、亿千瓦时、小时</b></div>
	<div id="datagrid_div">
		<table id="datagrid" ></table>
	</div>

</body>
</html>