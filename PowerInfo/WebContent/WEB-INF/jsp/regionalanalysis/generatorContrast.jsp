<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>机组</title>
<meta http-equiv="content-type" content="text/html;charset=UTF-8" />
<!--引入此文件包含jquery_easyui的css样式与公用js以及登录用户信息-->
<%@include file="../common/commonInclude.jsp"%>
<%@include file="../common/commonDefineBtn.jsp" %>

<script type="text/javascript">
var frozenCols = [ [  {
	field : 'jz_id',
	title : '机组名称',
	width : 150,
	align : 'center',
	rowspan:2
} , {
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
	
	var cols ='';
	var gkarray='';
	$(function() {
		 comboBoxInit({
				id : "index_y",
				url : path + '/sysdict/getDataByCodeValue?domain_id=36',
				textkey : "value",
				valuekey : "code",
				multiple : true
			});
		 comboBoxInit({
				id : "index_x",
				url : path + '/sysdict/getDataByCodeValue?domain_id=31',
				textkey : "value",
				valuekey : "code",
				multiple : true
			});
	
		 gkarray=$("#index_x").combobox("getData");
		
		queryData();
		$("#tool_query").bind("click", function() {
			queryData();
		});
		$("#tool_export").bind("click", function() {
			ExportExcel();
		});
	});

	//查询方法调用的函数
	function queryData() {
	
		//查询条件暂时放外面
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
				"index_xs" : index_s
		};
		var url = path+'/generatorContrastController/queryData';
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
			checkOnSelect:true,
			singleSelect:true,
			rownumbers : true,
			pagination : false,
			queryParams : queryParams,
			onLoadSuccess: function(data) {
				$('#datagrid').datagrid('mergeCellsByField','jz_id');
				
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
	function ExportExcel() {//导出Excel文件
		//查询条件暂时放外面
		var elec_name = $('#elec_name').val();
		var gene_name = $('#gene_name').val();

		//用ajax发动到动态页动态写入xls文件中
		var f = $('<form action="'+path+'/generatorSetController/exportData" method="post" id="fm1"></form>');  
	    var i = $('<input type="hidden" id="elec_name" name="elec_name" />');  
	    var l = $('<input type="hidden" id="gene_name" name="gene_name" />');
		i.val(elec_name);  
		i.appendTo(f);  
		l.val(gene_name);  
		l.appendTo(f);  
		f.appendTo(document.body).submit();  
		document.body.removeChild(f);  
	}
	
</script>
</head>
<body>
	<!-- 引入自定义按钮页面 -->
		<!-- 引入自定义按钮页面 -->
	<div id="btn_div">
		<a id="tool_query"> <img src='<%=path%>/static/images/query.gif'
			align='top' border='0' title='查询' />
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
				<td class="tdlft">指标：</td>
				<td class="tdrgt"><input id="index_y" class="comboboxComponent" /></td>
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