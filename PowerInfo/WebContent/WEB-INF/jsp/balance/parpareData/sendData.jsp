<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.github.balance.task.entity.BalanceTask"%>
 <!DOCTYPE html>
<html>
<head>
<title>外购外送</title>
<meta http-equiv="content-type" content="text/html;charset=UTF-8" />
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">

<!--引入此文件包含jquery_easyui的css样式与公用js以及登录用户信息-->
<%@include file="../../common/commonInclude.jsp"%>
<%@include file="../../common/commonDefineBtn.jsp"%>

<script type="text/javascript">
	var cols;
	var years='2015,2016,2017';

	var savEvtTime = 0;
	var dcAt = 0;
	var dcTime = 250;
	var savTO = null;
	var frozenCols = [ [ {field:'ck',title:'',width:30,align:'center',checkbox:true},
	    {
		field : 'pro_name',
		title : '指标名称',
		width : 200,
		align : 'center',formatter:function(value,row,index){
			if(row['pid']==""||typeof(row['pid'])=='undefined'){
				return '<font size=6>'+value+'</font>';
			}
			else{
				return value;
			}
		 }
	} ] ];
	$(function() {


		$("#tool_save").bind("click", function() {
			save();
		});
		$("#tool_query").bind("click", function() {
			queryData();
		});
		$("#tool_add").bind("click", function() {
			addProName();
		});
		$("#tool_del").bind("click", function() {
			delProName();
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
	function addProName() {
		commonHelper.toAdd({
			title : '新增',
			width : 300,
			height : 250,
			url : path + '/sendData/openAddProData'
		});
	}
	function delProName(){
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
				$.post(path + '/sendData/deleteProData', {
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
		//用ajax发动到动态页动态写入xls文件中
		var f = $('<form action="'+path+'/sendData/exportData" method="post" id="fm1"></form>');  
        var i = $('<input type="hidden" id="years" name="years" />');  

    	i.val(yrs_s);  
    	i.appendTo(f);  
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
		//非冰冻列
		cols = createCols(yrs_s);
		//查询条件暂时放外面
		var queryParams = {
			years : yrs_s
		};

		var url = path + '/sendData/queryData';
		var Height_Page = $(document).height();
		var datagrid_title_height = $("#datagrid_div").position().top;
		var height = Height_Page - datagrid_title_height;
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
				var rows=$('#datagrid').datagrid('getRows');  
				if(field!="pro_name"&&!(rows[rowIndex]['pid']==""||typeof(rows[rowIndex]['pid'])=='undefined')){
					clickEvent(rowIndex, field, value);
				}
			},
	        onLoadSuccess:function(data){
	        	$(".datagrid-header-row input[type='checkbox']").hide();	
	            for(var i=0;i<data.rows.length;i++){
	            	var row=data.rows[i];
	            	var pid= row['pid'];
	                if(pid==""||typeof(pid)=='undefined'){
	                	$(".datagrid-row[datagrid-row-index="+i+"] input[type='checkbox']").hide();	       
	                }
	                if(row["pro_name"]==""){
	                	$(".datagrid-row[datagrid-row-index="+i+"]").hide();	       
	                }
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
		cols.push({
			'field' : "wgwstdlyxss",
			'title' : "利用小时数",
			'align' : 'center',
			'width' : 120,
			'editor' : 'text'

		});
		cols.push({
			'field' : "sdshl",
			'title' : "输电损耗率",
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

	/**
	 * ‘保存’按钮功能
	 */
	function save() {
		endEdit();
		var updates = $('#datagrid').datagrid('getChanges');
		if (updates.length <= 0) {
			return;
		}
		if (!validate($('#datagrid'), updates, [ 'pro_name' ], 13, 2)) {
			return;
		}
		var param = JSONH.stringify(updates);
		var data = {
			editObj : param
			
		};
		$.ajax({
			type : 'POST',
			async : false,
			url : path + '/sendData/saveData',
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
		<a id="tool_add"> <img
			src='<%=path%>/static/images/new.gif' align='top' border='0'
			title='新增' />
		</a>
		<a id="tool_del"> <img
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
				<td class="tdlft">年份：</td>
				<td class="tdrgt"><input id="years" class="comboboxComponent" /></td>
			</tr>
		</table>
	</fieldset>
	<div id="title" style="padding-right: 5px;text-align: right"><b>单位：万千瓦、亿千瓦时、小时、%</b></div>
	<div id="datagrid_div">
		<table id="datagrid"></table>
	</div>

</body>
</html>