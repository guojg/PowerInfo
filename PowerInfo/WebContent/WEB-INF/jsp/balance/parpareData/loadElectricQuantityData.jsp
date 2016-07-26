<%@ page language="java" pageEncoding="UTF-8"%>
<html>
<head>
<title>负荷电量数据</title>
<meta http-equiv="content-type" content="text/html;charset=UTF-8" />
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">

<!--引入此文件包含jquery_easyui的css样式与公用js以及登录用户信息-->
<%@include file="../../common/commonInclude.jsp"%>
<%
	String pid = request.getAttribute("pid") == null ? "" : request
			.getAttribute("pid").toString();
%>
<script type="text/javascript">
	var pid='<%=pid%>';
	var cols;
	var savEvtTime = 0;
	var dcAt = 0;
	var dcTime = 250;
	var savTO = null;
	var frozenCols = [ [ {
		field : 'index_name',
		title : '指标名称',
		width : 100,
		align : 'center'
	} ] ];
	$(function() {

		$("#tool_save").bind("click", function() {
			save();
		});
		$("#tool_query").bind("click", function() {
			queryData();
		});
		$("#tool_export").bind("click", function() {
			ExportExcel();
		});
		comboBoxInit({
			id : "years",
			url : path + '/basicData/getyears',
			textkey : "yearName",
			valuekey : "year",
			multiple : true
		});
		comboBoxInit({
			id : "indexs",
			url : path + '/basicData/getindexs?pid=200',
			textkey : "indexName",
			valuekey : "indexItem",
			multiple : true
		});
		queryData();
	});
	function ExportExcel() {//导出Excel文件
		var years = $("#years").combo("getValues");
		//水平年份
		var yrs_s;
		if (years != "") {
			yrs_s = years + "";
		} else {
			yrs_s = "";
		}
		if (yrs_s == "") {
			$.messager.alert("提示", "请选择年份！");
			return;
		}
		var indexs = $("#indexs").combo("getValues");
		//水平年份
		var index_s;
		if (years != "") {
			index_s = indexs + "";
		} else {
			index_s = "";
		}
		if (index_s == "") {
			$.messager.alert("提示", "请选择指标！");
			return;
		}
		//用ajax发动到动态页动态写入xls文件中
		var f = $('<form action="'+path+'/basicData/exportData" method="post" id="fm1"></form>');  
        var i = $('<input type="hidden" id="years" name="years" />');  
        var l = $('<input type="hidden" id="indexs" name="indexs" />');  
    	i.val(yrs_s);  
    	i.appendTo(f);  
    	l.val(index_s);  
    	l.appendTo(f);  
    	f.appendTo(document.body).submit();  
    	document.body.removeChild(f);  
	}
	//查询方法调用的函数
	function queryData() {
		var years = $("#years").combo("getValues");
		//水平年份
		var yrs_s;
		if (years != "") {
			yrs_s = years + "";
		} else {
			yrs_s = "";
		}
		if (yrs_s == "") {
			$.messager.alert("提示", "请选择年份！");
			return;
		}
		var indexs = $("#indexs").combo("getValues");
		//水平年份
		var index_s;
		if (years != "") {
			index_s = indexs + "";
		} else {
			index_s = "";
		}
		if (index_s == "") {
			$.messager.alert("提示", "请选择指标！");
			return;
		}
		//非冰冻列
		cols = createCols(yrs_s);
		//查询条件暂时放外面
		var queryParams = {
			years : yrs_s,
			indexs : index_s
		};

		var url = path + '/loadElectricQuantity/queryData';
		var Height_Page = $("html").height();
		var datagrid_title_height = $("#datagrid_div").position().top;
		var height = Height_Page - datagrid_title_height - 5;
		$('#datagrid').datagrid({
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
				if(field!="index_name"&&rowIndex==2){
					clickEvent(rowIndex, field, value);
				}
			}
		});
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
			savTO = setTimeout(function() {
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
		var param = JSONH.stringify(updates);
		var data = {
			editObj : param
		};
		$.ajax({
			type : 'POST',
			async : false,
			url : path + '/loadElectricQuantity/saveData',
			data : data,
			success : function(data) {
				if (data == "1") {
					$.messager.alert("提示", "保存成功！");
					$('#datagrid').datagrid('reload');
				} else {
					$.messager.alert("提示", "保存失败！");
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
		</a> <a id="tool_save"> <img src='<%=path%>/static/images/save.gif'
			align='top' border='0' title='保存' />
		</a> <a id="tool_export"> <img
			src='<%=path%>/static/images/daochu.gif' align='top' border='0'
			title='保存' />
		</a>
	</div>
	<fieldset id="field">
		<legend>查询条件</legend>
		<table id="search_tbl">
			<tr>
				<td class="tdlft">年份：</td>
				<td class="tdrgt"><input id="years" class="comboboxComponent" /></td>
				<td class="tdlft">指标：</td>
				<td class="tdrgt"><input id="indexs" class="comboboxComponent" /></td>
			</tr>
		</table>
	</fieldset>
	<div id="datagrid_div">
		<table id="datagrid"></table>
	</div>

</body>
</html>