<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.github.balance.task.entity.BalanceTask"%>
<html>
<head>
<title>受阻及空闲容量</title>
<meta http-equiv="content-type" content="text/html;charset=UTF-8" />
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">

<!--引入此文件包含jquery_easyui的css样式与公用js以及登录用户信息-->
<%@include file="../../common/commonInclude.jsp"%>
<%
	String pid = request.getAttribute("pid") == null ? "" : request
			.getAttribute("pid").toString();
	BalanceTask tt=  (BalanceTask)request.getSession().getAttribute("balancetask");
	String taskid = tt.getId();
	String years=tt.getYear();
	String task_name = tt.getTask_name();

%>

<script type="text/javascript">
	var pid='<%=pid%>';
	var taskid='<%=taskid%>';
	var years='<%=years%>';
	var task_name='<%=task_name%>';

	var cols;
	var savEvtTime = 0;
	var dcAt = 0;
	var dcTime = 250;
	var savTO = null;
	var frozenCols = [ [ {
		field : 'index_name',
		title : '指标名称',
		width : 200,
		align : 'center'
	} ] ];
	$(function() {
		$('#task_name').val(task_name);

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
				url : path + '/sysdict/getBalanceYears?year='+years,
				textkey : "value",
				valuekey : "code",
				multiple : true
			});
		 comboBoxInit({
				id : "indexs",
				url : path + '/sysdict/getDataByCodeValue?domain_id=13',
				textkey : "value",
				valuekey : "code",
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
			indexs : index_s,
			taskid:taskid
			
		};

		var url = path + '/hinderedIdleCapacity/queryData';
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
				if(field!="index_name"&&rowIndex!=0){
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
		if (!validate($('#datagrid'), updates, [ 'index_name' ], 13, 2)) {
			return;
		}
		var param = JSONH.stringify(updates);
		var data = {
			editObj : param,
			taskid:taskid
		};
		$.ajax({
			type : 'POST',
			async : false,
			url : path + '/hinderedIdleCapacity/saveData',
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
		<!--  <a id="tool_export"> <img
			src='<%=path%>/static/images/daochu.gif' align='top' border='0'
			title='导出' />
		</a>--> 
	</div>
	<fieldset id="field">
		<legend>查询条件</legend>
		<table id="search_tbl">
			<tr>
			<td class="tdlft">任务：</td>
				<td class="tdrgt"><input id="task_name" name="task_name" type="text" disabled="disabled"/></td>
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