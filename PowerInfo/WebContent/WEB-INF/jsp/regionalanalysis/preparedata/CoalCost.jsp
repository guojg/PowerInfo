<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.github.balance.task.entity.BalanceTask"%>

<html>
<head>
<title>燃煤成本数据</title>
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
	var gkarray=null;
	var frozenCols = [ [ {
		field : 'index_y_name',
		title : '指标名称',
		width : 100,
		align : 'center',
		rowspan:2
	} , {
		field : 'unit_name',
		title : '单位',
		width : 100,
		align : 'center',
		rowspan:2
	}],[]];
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
				id : "index_x",
				url : path + '/sysdict/getDataByCodeValue?domain_id=31',
				textkey : "value",
				valuekey : "code",
				multiple : true
			});
		 debugger;
		 gkarray=$("#index_x").combobox("getData");
		queryData();
	});
	function ExportExcel() {//导出Excel文件
		var indexs = $("#index_x").combo("getValues");
		//水平年份
		var index_s;
		if (indexs != "") {
			index_s = indexs + "";
		} else {
			index_s = "";
		}
		if (index_s == "") {
			$.messager.alert("提示", "请选择工况！");
			return;
		}
		var index_text=$("#index_x").combobox("getText");
		//用ajax发动到动态页动态写入xls文件中
		var f = $('<form action="'+path+'/coalCost/exportData" method="post" id="fm1"></form>');  
        var l = $('<input type="hidden" id="index_xs" name="index_xs" />'); 
        var i= $('<input type="hidden" id="index_text" name="index_text" />'); 
    	l.val(index_s);  
    	l.appendTo(f);  
    	i.val(index_text);  
    	i.appendTo(f);  
    	f.appendTo(document.body).submit();  
    	document.body.removeChild(f);  
	}
	//查询方法调用的函数
	function queryData() {

		var indexs = $("#index_x").combo("getValues");
		var index_s;
		if (indexs != "") {
			index_s = indexs + "";
		} else {
			index_s = "";
		}
		if (index_s == "") {
			$.messager.alert("提示", "请选择工况！");
			return;
		}
		
		//非冰冻列
		cols = createCols(index_s);
		//查询条件暂时放外面
		debugger;
		var queryParams = {
				index_xs : index_s
		};

		var url = path + '/coalCost/queryData';
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
			singleSelect:true,
			onClickCell : function(rowIndex, field, value) {
				if(!(field=="index_y_name"||field=="unit_name"||rowIndex==4||rowIndex==5)){
					clickEvent(rowIndex, field, value);
				}
			}
		});
	}

	//动态生成列
	function createCols(indexs) {
		var rows=new Array();
		var row1=[];
		var row2 = [];
		var tmp = [];
		tmp = indexs.split(",");
		var collength=tmp.length;
		row1.push({
			'field' : 'gk',
			'title' : '工况',
			'align' : 'center',
			'width' : 120,
			'colspan' : collength
		});
		for (var i = 0; i < collength; i++) {
			row2.push({
				'field' : gkarray[1+i]["code"] + "",
				'title' : "" + gkarray[1+i]["value"],
				'align' : 'center',
				'width' : 120,
				'editor' : 'text'
			});
		}
		rows.push(row1);
		rows.push(row2);
		return rows;
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
		if (!validate($('#datagrid'), updates, [ 'index_y_name','unit_name' ], 13, 2)) {
			return;
		}
		var param = JSONH.stringify(updates);
		var data = {
			editObj : param
		};
		$.ajax({
			type : 'POST',
			async : false,
			url : path + '/coalCost/saveData',
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
	/**
	 * 数字验证
	 **/
	function validate(o, upts, arr, a, b) {
		var opts = o.datagrid('options');
		var flag = true;
		var regExp = /^-?[1-9]+(\.\d+)?$|^-?0(\.\d+)?$|^-?[1-9]+[0-9]*(\.\d+)?$/; //数字验证
		var regExp2 = '/^-?(?:\\d{1,' + a + '})(?:\\.\\d{1,' + b + '})?$/';
		regExp2 = eval(regExp2);
		for (var i = 0; i < upts.length; i++) {
			var rowIndex = -1;
			rowIndex = o.datagrid('getRowIndex', upts[i]);
			if (rowIndex < 0)
				return;
			$.each(upts[i], function(oi, j) {
				if (oi == opts.idField || j == '') {
					return;
				}
				var rflag = false;
				$.each(arr, function(ii, n) {
					if (oi == n) {
						rflag = true;
						return false;
					}
				});
				if (rflag) {
					return;
				}
				var $cell = $('tr[datagrid-row-index=' + rowIndex + ']').find(
						'td[field=' + oi + ']');
				if ($.trim(j) == '') {
					$cell.css('background', 'red');
					$cell.showTipp({
						flagInfo : '数据不能为空格！',
						flagWidth : '100'
					});
					flag = false;
					return;
				}
				if (!regExp.test(j)) {
					$cell.css('background', 'red');
					$cell.showTipp({
						flagInfo : '输入字符必须为数字！',
						flagWidth : '150'
					});
					flag = false;
					return;
				}
				if (!regExp2.test(j)) {
					$cell.css('background', 'red');
					$cell.showTipp({
						flagInfo : '输入字符精度为[' + a + ',' + b + ']！',
						flagWidth : '110'
					});
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
	<div id="btn_div">
		<a id="tool_query"> <img src='<%=path%>/static/images/query.gif'
			align='top' border='0' title='查询' />
		</a> <a id="tool_save"> <img src='<%=path%>/static/images/save.gif'
			align='top' border='0' title='保存' />
		</a> 
 		<a id="tool_total"> <img src='<%=path%>/static/images/huizong.gif'
			align='top' border='0' title='汇总' />
		</a> 
		 <a id="tool_export"> <img
			src='<%=path%>/static/images/daochu.gif' align='top' border='0'
			title='保存' />
		</a>
	</div>
	<fieldset id="field">
		<legend>查询条件</legend>
		<table id="search_tbl">
			<tr>
				<td class="tdlft">工况：</td>
				<td class="tdrgt"><input id="index_x" class="comboboxComponent" /></td>
			</tr>
		</table>
	</fieldset>
	<div id="datagrid_div">
		<table id="datagrid"></table>
	</div>

</body>
</html>