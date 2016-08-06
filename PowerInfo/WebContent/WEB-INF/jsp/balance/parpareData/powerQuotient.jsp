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
var savEvtTime = 0;
var dcAt = 0;
var dcTime = 250;
var savTO = null;
$(function() {

	//非冰冻列
	cols = createCols("2014,2015,2016,2017");
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
	queryParams : {},
	onClickCell : function(rowIndex, field, value) {
		clickEvent(rowIndex, field, value);

	}  
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
			'width' : 120,
			'editor' : 'text'

		});
		
	}
	cols.push({
		'field' : "hour_num",
		'title' : "机组利用小时数",
		'align' : 'center',
		'width' : 120,
		'editor' : 'text'

	});
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
</script>
</head>
<body>
	<!-- 引入自定义按钮页面 -->
	<div id="btn_div">
		<a id="tool_query"> <img src='<%=path%>/static/images/query.gif'
			align='top' border='0' title='查询' />
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