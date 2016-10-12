<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.github.balance.task.entity.BalanceTask"%>
 <!DOCTYPE html>
<html>
<head>
<title>当年新增装机利用系数</title>
<meta http-equiv="content-type" content="text/html;charset=UTF-8" />
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">

<!--引入此文件包含jquery_easyui的css样式与公用js以及登录用户信息-->
<%@include file="../../common/commonInclude.jsp"%>
<script type="text/javascript" src="<%=path%>/js/totalquantity/jsonutil.js"></script>
<script type="text/javascript" src="<%=path %>/js/totalquantity/common/validatedatagrid.js"></script>
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
$(function() {
	$('#task_name').html('<b>'+task_name+'</b>');
	 comboBoxInit({
			id : "dylxs",
			url : path + '/sysdict/getDataByCodeValue?domain_id=12',
			textkey : "value",
			valuekey : "code",
			multiple : true
		});
	 comboBoxInit({
			id : "years",
			url : path + '/sysdict/getBalanceYears?year='+years,
			textkey : "value",
			valuekey : "code",
			multiple : true
		});
	 queryData();

	$("#tool_save").bind("click", function() {
		save();
	});
	$("#tool_query").bind("click", function() {
		queryData();
	});
	$("#tool_export").bind("click", function() {
		ExportExcel();
	});

});
function queryData(){
	var index_type = $('#dylxs').combo('getValues').join(",");
	var yrs = $('#years').combo('getValues').join(",");

	//非冰冻列
	cols = createCols(yrs);
	var Height_Page = $(document).height();
	var datagrid_title_height = $("#datagrid_div").position().top;
	var height = Height_Page - datagrid_title_height;
	$('#datagrid').datagrid({    
		   url:path+'/powerQuotient/queryData',   
			width : 'auto',
			height : height,
			autoRowHeight : false,
			collapsible : true,
			remoteSort : false,
		    frozenColumns:[[    
		        {title:'code',field:'code',width:180,hidden:true},    
		        {field:'displayvalue',title:'电源类型',width:120}  
		    ]] ,
			columns : cols,
			rownumbers : true,
			singleSelect:true,
			pagination : false,
			queryParams : {"index_type":index_type,"year":yrs,"taskid":taskid},
			onClickCell : function(rowIndex, field, value) {
				if($('#datagrid').datagrid('getData').rows[rowIndex] .code==3 && field=="hour_num"){
					
				}else{
				clickEvent(rowIndex, field, value);
				}

			}
		}); 
}
function ExportExcel() {//导出Excel文件
	var index_type = $('#dylxs').combo('getValues').join(",");
	var yrs = $('#years').combo('getValues').join(",");

	//用ajax发动到动态页动态写入xls文件中
	var f = $('<form action="'+path+'/powerQuotient/exportData" method="post" id="fm1"></form>');  
    var i = $('<input type="hidden" id="year" name="year" />');  
    var l = $('<input type="hidden" id="index_type" name="index_type" />');
    var m = $('<input type="hidden" id="taskid" name="taskid" />');  
	i.val(yrs);  
	i.appendTo(f);  
	l.val(index_type);  
	l.appendTo(f);  
	m.val(taskid);  
	m.appendTo(f); 
	f.appendTo(document.body).submit();  
	document.body.removeChild(f);  
}
//动态生成列
function createCols(years) {
	var cols = [];
	var tmp = [];
	cols.push({
		'field' : "hour_num",
		'title' : "机组利用小时数",
		'align' : 'center',
		'width' : 120,
		'editor' : 'text',
		'formatter': function(value,row,index){
			if(row['code']==3){
				return "-";
			}else{
				return value;
			}
			}

	});
	tmp = years.split(",");
	for (var i = 0; i < tmp.length; i++) {
		cols.push({
			'field' : tmp[i] + "",
			'title' : "" + tmp[i] + "年",
			'align' : 'center',
			'width' : 120,
			'editor' : 'text'

		});
		
	}

	return new Array(cols);
}
//点击事件
function clickEvent(rowIndex, field, value) {

		var d = new Date();
		savEvtTime = d.getTime();
		savTO = setTimeout(function() {
			clickonetime(rowIndex, field, value);
		}, dcTime);

}

/**
 * 单击事件
 */
function clickonetime(rowIndex, field, value) {
	if (savEvtTime - dcAt <= 0) {
		return false;
	}
	editCell(rowIndex, field, value);
}

/**
 * 解开一个格子的编辑状态
 */
function editCell(rowIndex, field, value) {
	var editor1 = {
		type : 'text'
	};
	endEdit();
	//if (rowIndex == noEditRow) return;
	for ( var i in cols[cols.length - 1]) {
		var columnOption = $('#datagrid').datagrid('getColumnOption',
				cols[cols.length - 1][i]['field']);
		if (cols[cols.length - 1][i]['field'] == field) {
			columnOption.editor = editor1;
		} else {
			columnOption.editor = {};
		}
	}
	$('#datagrid').datagrid('beginEdit', rowIndex);
	var editors = $('#datagrid').datagrid('getEditors', rowIndex);
	// 获取当前行的当前单元格的编辑器
	$.each(editors, function(i, editor) {
		if (editor.field === field) {
			$(editor.target).parent().find('input:visible').focus().blur(
					function() {
						endEdit();
					});
		} else {
			$(editor.target).hide().closest('div').text(editor.oldHtml);
		}
	});
}

/**
 * 结束表格编辑状态
 */
function endEdit() {
	var rows = $('#datagrid').datagrid('getRows');
	for (var i = 0; i < rows.length; i++) {
		$('#datagrid').datagrid('endEdit', i);
	}

}
/**
 * ‘保存’按钮功能
 */
function save() {
	endEdit();
	var updates = $('#datagrid').datagrid('getChanges');
	if (updates.length <= 0) {
		return;
	}
	if (!validate($('#datagrid'), updates,[ 'displayvalue' ],10, 2)) {
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
		url : path + '/powerQuotient/saveData',
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
		</a><a id="tool_save"> <img src='<%=path%>/static/images/save.gif'
			align='top' border='0' title='保存' /></a>
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
				<td class="tdlft">电源类型：</td>
				<td class="tdrgt"><input id="dylxs" class="comboboxComponent" /></td>
				<td class="tdlft">年份：</td>
				<td class="tdrgt"><input id="years" class="comboboxComponent" /></td>
			</tr>
		</table>
	</fieldset>
	<div id="title" style="padding-right: 5px;text-align: right"><b>单位：%</b></div>
	<div id="datagrid_div">
		<table id="datagrid" ></table>
	</div>

</body>
</html>