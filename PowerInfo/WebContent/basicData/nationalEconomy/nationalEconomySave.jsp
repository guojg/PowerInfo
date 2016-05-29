<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>国民经济发展概况-填报</title>
<meta http-equiv="content-type" content="text/html;charset=UTF-8" /> 
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">

<!--引入此文件包含jquery_easyui的css样式与公用js以及登录用户信息-->	
<%@include file="../../common/commonInclude.jsp" %>	
<script type="text/javascript"> 

var cols;
var frozenCols = [[
                   {field : 'index_name',title : '指标',width : 100,align : 'center'
					}
              ]];

$(function(){ 

	//***************初始化按钮开始****************
    
  
   
    //***************初始化查询条件开始*****************
	
    //***************初始化查询条件结束*****************
	//初始化列表
    queryData("","");
});

//查询方法调用的函数
function queryData(years,zhibiaoArr) {
	

	//非冰冻列
	cols = createCols("");
	//查询条件暂时放外面
	var queryParams = {};
	
    var url = path + '/nationalEconomy/queryData';

    var Height_Page = $("html").height();
	var datagrid_title_height = $("#datagrid_div").position().top;
	var datagrid_note_height = $("#datagrid_note").height();
	var height = Height_Page-datagrid_title_height-datagrid_note_height-5;
    
    $('#datagrid').datagrid( {
		width : 'auto',
		height : height,
		autoRowHeight : false,
		collapsible : true,
		url : url,
		remoteSort : false,
		frozenColumns : frozenCols,
		columns : cols,
		rownumbers : true,
		pagination : false,
		queryParams : queryParams,
		
		onClickCell : function(rowIndex, field, value) {
			
				clickEvent(rowIndex, field, value);
			
		}
	});
}

//动态生成列
function createCols(years) {
	var cols = [];
	var tmp = [];
	var years="2005,2006,2007";
	tmp = years.split(",");
	for ( var i = 0; i <tmp.length; i++) {
		cols.push( {
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
	var type = window.event.type;
	switch (type) {
	case "click":
		var d = new Date();
		savEvtTime = d.getTime();
		savTO = setTimeout( function() {
			clickonetime(rowIndex, field, value);
		}, dcTime);
		break;
	}
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
	 var editor1={type:'text'};
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
			$(editor.target).parent().find('input:visible').focus().blur(function() {
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
	for ( var i = 0; i < rows.length; i++) {
		$('#datagrid').datagrid('endEdit', i);
	}

}

/**
* ‘保存’按钮功能
*/
function save() {
	
}


/**
* 数字验证
*/
function validate(o, upts, arr, a, b){
	var opts = o.datagrid('options');
	var flag = true;
	var regExp = /^-?[1-9]+(\.\d+)?$|^-?0(\.\d+)?$|^-?[1-9]+[0-9]*(\.\d+)?$/;	//数字验证
	var regExp2 = '/^-?(?:\\d{1,'+a+'})(?:\\.\\d{1,'+b+'})?$/';
	regExp2 = eval(regExp2);
	for(var i = 0; i < upts.length; i++) {
		var rowIndex = -1;
		rowIndex = o.datagrid('getRowIndex', upts[i]);
		if(rowIndex < 0) return;
		$.each(upts[i], function(oi, j){
			if(oi == opts.idField || j == '') {
				return;
			}
			var rflag = false;
			$.each(arr, function(ii, n){
				if(oi == n) {
					rflag = true;
					return false;
				}
			});
			if(rflag) {
				return;
			}
			var $cell = $('tr[datagrid-row-index='+ rowIndex +']').find('td[field=' + oi + ']');
			if($.trim(j) == ''){
				$cell.css('background', 'red');
				$cell.showTipp({flagInfo : '数据不能为空格！', flagWidth : '100'});
				flag = false;
				return;
			}
			if(!regExp.test(j)) {
				$cell.css('background', 'red');
				$cell.showTipp({flagInfo : '输入字符必须为数字！', flagWidth : '150'});
				flag = false;
				return;
			}
			if(!regExp2.test(j)) {
				$cell.css('background', 'red');
				$cell.showTipp({flagInfo : '输入字符精度为['+a+','+b+']！', flagWidth : '110'});
				flag = false;
			}
		});
	}
	return flag;
}
</script>
</head>
<body>
<!-- 引入自定义按钮页面 -->
<%@include file="../../common/commonDefineBtn.jsp" %>

<div id="datagrid_title">国民经济发展概况(单位：平方公里、万人、亿元、元/人)</div>

<div id="datagrid_div">
	<table id="datagrid"></table>
</div>

<div id="datagrid_note">
	<ul style="text-align: center;">
		<li id="more_note" onclick="hide_show_note('less_note','datagrid')">更多</li>
		<li id="less_note" onclick="hide_show_note('more_note','datagrid')">收起</li>
	</ul>
	<ul style="display: none;">
		<li>填表说明：1、GDP取自政府统计年鉴，填写2005年可比价。</li>
		<li>2、供电面积：由国家电网公司提供供电服务的经营区面积（不含地方电力）。</li>
		<li>3、导出文件的后缀名必须为.xls。</li>
	</ul>
</div>

</body>  
</html>