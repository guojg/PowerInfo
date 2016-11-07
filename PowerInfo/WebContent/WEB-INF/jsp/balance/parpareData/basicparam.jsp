<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.github.balance.task.entity.BalanceTask"%>
 <!DOCTYPE html>
<html>
<head>
<title>基本参数</title>
<meta http-equiv="content-type" content="text/html;charset=UTF-8" />
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">

<!--引入此文件包含jquery_easyui的css样式与公用js以及登录用户信息-->
<%@include file="../../common/commonInclude.jsp"%>
<script type="text/javascript" src="<%=path%>/js/totalquantity/jsonutil.js"></script>
<script type="text/javascript" src="<%=path %>/js/totalquantity/common/validatedatagrid.js"></script>

<script type="text/javascript">
var savEvtTime = 0;
var dcAt = 0;
var dcTime = 250;
var savTO = null;
var cols=[[
	        {field:'hour_num',title:'机组利用小时数',width:120,align:'center',editor:'text',
				formatter: function(value,row,index){
					if(row['code']==3){
						return "-";
					}else{
						return value;
					}
				}
},{field:'quotient',title:'新增装机利用系数',width:120,editor:'text',align:'center'}]];
$(function() {
	
	var nowYear =new Date().getFullYear();
	$('#startyear').numberspinner({
		value:nowYear,
		onChange:function(){
			var startChange=$('#startyear').numberspinner('getValue');
			  $('#stopyear').numberspinner({min:parseInt(startChange)+1});	
		}
	});
	$('#stopyear').numberspinner({
		value:nowYear+4
	});
	queryData();
	$("#tool_save").bind("click", function() {
		save();
	});
	$.ajax({
		type : 'POST',
		async : false,
		url : path + '/basicparam/countData',
		success : function(data) {
			if (data == "0") {
				
			} else {
				init();
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
		}
	});
});
function init(){
	$.ajax({
		type : 'POST',
		async : false,
		url : path + '/basicparam/initData',
		success : function(data) {
    		$("#startyear").numberspinner('setValue',data["START_YEAR"]);
    		$("#stopyear").numberspinner('setValue',data["STOP_YEAR"]);
    		$("#byl").numberspinner('setValue',data["BYL"]);


		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
		}
	});
}
function queryData(){

	//非冰冻列
	var Height_Page = $(document).height();
	var datagrid_title_height = $("#datagrid_div").position().top;
	var height = Height_Page - datagrid_title_height-15;
	$('#datagrid').datagrid({    
		   url:path+'/basicparam/queryData',   
			width : 'auto',
			height : height,
			autoRowHeight : false,
			collapsible : true,
			remoteSort : false,
		    frozenColumns:[[    
		        {title:'code',field:'code',width:180,hidden:true},    
		        {field:'displayvalue',title:'电源类型',width:120,align:'center'}
		    ]] ,
		    columns : cols,
			rownumbers : true,
			singleSelect:true,
			pagination : false,
			queryParams : {},
			onClickCell : function(rowIndex, field, value) {
				if($('#datagrid').datagrid('getData').rows[rowIndex] .code==3 && field=="hour_num"){
					
				}else{
				clickEvent(rowIndex, field, value);
				}

			}
		}); 
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
	if (!validate($('#datagrid'), updates,[ 'displayvalue' ],10, 2)) {
		return;
	}
	var param = JSONH.stringify(updates);
	 var startYear = $('#startyear').numberspinner('getValue');
	 var stopYear = $('#stopyear').numberspinner('getValue');
	 var byl = $('#byl').numberspinner('getValue');

	var data = {
		"editObj" : param,
		'stopyear':stopYear,
		'startyear':startYear,
		"byl" : byl
	};
	$.ajax({
		type : 'POST',
		async : false,
		url : path + '/basicparam/saveData',
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
		<a id="tool_save"> <img src='<%=path%>/static/images/save.gif'
			align='top' border='0' title='保存' /></a>
	
	</div>
	<fieldset>
		<legend>规划年份</legend>
		<table style="margin-left: 5px">
			<tr >
				<td >起始年：</td>
				<td  class="tdrgt"><input id="startyear" name="startyear" class="easyui-numberspinner" min="2000" max="5000" data-options="required:true,suffix:'年'" type="text"  /></td>	
			</tr>
			<tr>
				<td class="tdlft">终止年：</td>
				<td  class="tdrgt"><input id="stopyear" name="stopyear" class="easyui-numberspinner" min="2000" max="5000" data-options="required:true,suffix:'年'" type="text"  /></td>	
			</tr>
		</table>
	</fieldset>
	<fieldset>
		<legend>备用率</legend>
		<table style="margin-left: 5px">
			<tr>
				<td class="tdlft">备用率(%)：</td>
				<td class="tdrgt"><input id="byl" name="byl" class="easyui-numberspinner" min="0" max="100" value="15" data-options="required:true,precision:2" type="text"  /></td>
			</tr>
		</table>
	</fieldset>
	<fieldset>
		<legend>机组利用小时数及新增装机利用系数</legend>
		<div id="title" style="padding-right: 5px;text-align: right"><b>单位：小时,%</b></div>
		<div id="datagrid_div">
			<table id="datagrid" ></table>
		</div>
	</fieldset>


</body>
</html>