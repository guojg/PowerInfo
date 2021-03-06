<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>机组</title>
<meta http-equiv="content-type" content="text/html;charset=UTF-8" />
<!--引入此文件包含jquery_easyui的css样式与公用js以及登录用户信息-->
<%@include file="../common/commonInclude.jsp"%>
<%@include file="../common/commonDefineBtn.jsp" %>
<%
Object objName=  request.getSession().getAttribute("organName");
String organName="";
if(objName != null) {
	organName = objName.toString() ; 
}
%>
<script type="text/javascript">
var organ_name='<%=organName%>';

	var frozenCols = [ [
	    {field:'jz_id',align:'center',checkbox:true},
	    {
		field : '100',
		title : '机组名',
		width : 100,
		align : 'center',
		formatter: function(value,row,index){

    		return '<a href="#" onclick="detail('+row.jz_id+')">'+value+'</a> ';
		}
	} ] ];
	
	var cols ='';
	$(function() {
	
	
		$('#organ_name').html('<b>'+organ_name+'</b>');

		 cols = [ [
		          	 {
		          		field : 'plant_name',
		          		title : '所属发电厂',
		          		width : 100,
		          		align : 'center'
		          	}, {
		          		field : '300',
		          		title : '额定容量（万千瓦）',
		          		width : 120,
		          		align : 'center'
		          	}, {
		          		field : '400',
		          		title : '投运日期',
		          		width : 100,
		          		align : 'center'
		          	}, {
		          		field : '600',
		          		title : '建设投资（元）',
		          		width : 100,
		          		align : 'center'
		          	}, {
		          		field : '700',
		          		title : '行业期望收益率（%）',
		          		width : 140,
		          		align : 'center'
		          	}, {
		          		field : '800',
		          		title : '运行寿命（年）',
		          		width : 100,
		          		align : 'center'
		          	}, {
		          		field : '900',
		          		title : '固定资产折旧（年值）',
		          		width : 120,
		          		align : 'center'
		          	}] ];
		
		queryData();
		$("#tool_query").bind("click", function() {
			queryData();
		});
		$("#tool_export").bind("click", function() {
			ExportExcel();
		});
		$("#tool_add").bind("click", function() {
			addRecord();
		});
		$("#tool_update").bind("click", function() {
			updateRecord();
		});
		$("#tool_delete").bind("click", function() {
			deleteRecords();
		});
		/*$("#tool_db").bind("click", function() {
			duibi();
		});*/
	});

	//查询方法调用的函数
	function queryData() {
	
		//查询条件暂时放外面
		var elec_name = $('#elec_name').val();
		var gene_name = $('#gene_name').val();

		var queryParams = {"elec_name":elec_name,"gene_name":gene_name};
		var url = path+'/generatorSetController/queryData';
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
			pagination : false,
			queryParams : queryParams,
			pagination: true
		});
	}
	function addRecord() {
		commonHelper.toAdd({
			title : '新增',
			width : 800,
			height : 450,
			url : path + '/generatorSetController/main'
		});
		
	}
	function updateRecord() {
		var rows = $('#datagrid').datagrid('getChecked');
		if(rows.length!=1){
			$.messager.alert('提示', '请选择一条需要修改的机组！', 'info');
			return ;
		}
		commonHelper.toAdd({
			title : '修改',
			width : 800,
			height : 450,
			url : path + '/generatorSetController/main?id='+rows[0].jz_id
		});
	}
	
	function detail(jz_id) {
		
		commonHelper.toAdd({
			title : '修改',
			width : 800,
			height : 500,
			url : path + '/generatorSetController/detail?id='+jz_id
		});
	}
	function duibi() {
		var rows = $('#datagrid').datagrid('getChecked');
		if(rows.length<1){
			$.messager.alert('提示', '请选择需要对比的机组！', 'info');
			return ;
		}
		var ids = "";
		for (rowindex in rows) {
			if (parseInt(rowindex) + 1 == rows.length) {
				ids = ids + rows[rowindex]["jz_id"];
			} else {
				ids = ids + rows[rowindex]["jz_id"] + ",";
			}
		}
		window.parent.closeSingleExtent('对比');
		 window.parent.addTab('对比', path+'/generatorContrastController/main?id='+ids, '');
		/*commonHelper.toAdd({
			title : '对比',
			width : 900,
			height : 550,
			url : path + '/generatorContrastController/main?id='+ids
		});*/
	}
	function deleteRecords() {
		var rows = $('#datagrid').datagrid('getChecked');
		if(rows.length<1){
			$.messager.alert('提示', '请选需要删除的机组！', 'info');
			return ;
		}
		$.messager.confirm('提示', '确认删除?', function(r) {
			if (r) {
				var rows = $('#datagrid').datagrid('getChecked');
				var ids = "";
				for (rowindex in rows) {
					if (parseInt(rowindex) + 1 == rows.length) {
						ids = ids + rows[rowindex]["jz_id"];
					} else {
						ids = ids + rows[rowindex]["jz_id"] + ",";
					}
				}
				$.post(path+'/generatorSetController/deleteData', {
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
	 <a id="tool_add"> <img src='<%=path%>/static/images/new.gif'
			align='top' border='0' title='新增' />
		</a> <a id="tool_update"> <img
			src='<%=path%>/static/images/xiugai.gif' align='top' border='0'
			title='修改' />
		</a> <a id="tool_delete"> <img
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
					<td class="tdlft">区域：</td>
				<td ><span id="organ_name"></span></td>
			<td class="tdlft">电厂名称：</td>
				<td class="tdrgt"><input id="elec_name" name="elec_name" type="text"/></td>
				<td class="tdlft">机组名称：</td>
				<td class="tdrgt"><input id="gene_name" name="gene_name" type="text" /></td>
			</tr>
		</table>
	</fieldset>
	<div id="title" style="padding-right: 5px;text-align: right"><b>单位：万千瓦，%，年，元</b></div>
	
	<div id="datagrid_div">
		<table id="datagrid"></table>
	</div>

</body>
</html>