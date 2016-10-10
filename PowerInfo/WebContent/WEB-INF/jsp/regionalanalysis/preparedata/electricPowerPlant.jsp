<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>电厂</title>
<meta http-equiv="content-type" content="text/html;charset=UTF-8" />
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">

<!--引入此文件包含jquery_easyui的css样式与公用js以及登录用户信息-->
<%@include file="../../common/commonInclude.jsp"%>
<%@include file="../../common/commonDefineBtn.jsp"%>
<%
	Object obj=request.getSession().getAttribute("maparea");
	String organCode="";
	if(obj!=null){
		organCode=obj.toString();
	}
	Object objName=  request.getSession().getAttribute("organName");
	String organName="";
	if(objName != null) {
		organName = objName.toString() ; 
	}
%>
<script type="text/javascript">
	var area_id='<%=organCode%>';
	var organ_name='<%=organName%>';

	var cols = [ [ {
		field : 'id',
		width : 20,
		align : 'center',
		checkbox : true
	}, {
		field : 'plant_name',
		title : '电厂名称',
		width : 200,
		align : 'center',
		formatter: function(value,row,index){

    		return '<a href="#" onclick="detail('+row.id+')">'+value+'</a> ';
		}
	}, {
		field : 'plant_capacity',
		title : '装机容量',
		width : 100,
		align : 'center'
	}, {
		field : 'generating_capatity',
		title : '发电量',
		width : 100,
		align : 'center'
	}, {
		field : 'plant_loss',
		title : '电厂损耗',
		width : 100,
		align : 'center'
	}, {
		field : 'start_outlay',
		title : '初始投资',
		width : 100,
		align : 'center'
	}, {
		field : 'product_year',
		title : '投产年',
		width : 100,
		align : 'center'
	}, {
		field : 'economical_life',
		title : '经济运行寿命',
		width : 100,
		align : 'center'
	}, {
		field : 'equired_return',
		title : '期望收益率',
		width : 100,
		align : 'center'
	}, {
		field : 'financial_cost',
		title : '年财务成本',
		width : 100,
		align : 'center'
	}, {
		field : 'generation_coal',
		title : '发电煤耗',
		width : 100,
		align : 'center'
	}, {
		field : 'operation_rate',
		title : '运行维护费率',
		width : 100,
		align : 'center'
	}, {
		field : 'operation_cost',
		title : '运行维护成本',
		width : 100,
		align : 'center'
	}, {
		field : 'unit_cost',
		title : '燃料单位成本',
		width : 100,
		align : 'center'
	}, {
		field : 'materials_cost',
		title : '电厂材料费（元/年）',
		width : 200,
		align : 'center'
	}, {
		field : 'salary',
		title : '工资、奖金及福利费（元/年）',
		width : 200,
		align : 'center'
	}, {
		field : 'repairs_cost',
		title : '修理费（元/年）',
		width : 200,
		align : 'center'
	}, {
		field : 'other_cost',
		title : '其他费用（元/年）',
		width : 200,
		align : 'center'
	}] ];
	$(function() {
		$('#organ_name').html('<b>'+organ_name+'</b>');

		$("#tool_add").bind("click", function() {
			addRecord();
		});
		$("#tool_query").bind("click", function() {
			queryData();
		});
		$("#tool_update").bind("click", function() {
			updateRecord();
		});
		$("#tool_delete").bind("click", function() {
			deleteRecords();
		});
		$("#tool_export").bind("click", function() {
			ExportExcel();
		});
		queryData();
	});
	
	function detail(id) {
		 window.parent.closeSingleExtent('电厂详情');
		 window.parent.addTab('电厂详情', path+'/plantAnalysis/detail?id='+id, '');
	}
	function ExportExcel() {//导出Excel文件
		var plant_name=$("#plant_name").val();
		//用ajax发动到动态页动态写入xls文件中
		var f = $('<form action="'+path+'/plantAnalysis/exportData" method="post" id="fm1"></form>');
        var l=$('<input type="hidden" id="name" name="name" />');
        var i=$('<input type="hidden" id="area_id" name="area_id" />');
    	l.val(plant_name);
    	l.appendTo(f);
    	i.val(area_id);
    	i.appendTo(f);
    	f.appendTo(document.body).submit();
    	document.body.removeChild(f);  
	}
	//查询方法调用的函数
	function queryData() {
		var plant_name=$("#plant_name").val();
		var queryParams = {
			name :plant_name,
			area_id:area_id
		};
		var url = path + '/plantAnalysis/queryData';
		var Height_Page = $(document).height();
		var datagrid_title_height = $("#datagrid_div").position().top;
		var height = Height_Page - datagrid_title_height;
		$('#datagrid').datagrid({
			width : 'auto',
			height : height,
			autoRowHeight : false,
			collapsible : true,
			url : url,
			queryParams : queryParams,
			remoteSort : false,
			columns : cols,
			rownumbers : true,
			pagination:true
		});
	}
	function addRecord() {
		commonHelper.toAdd({
			title : '新增',
			width : 700,
			height : 300,
			url : path + '/plantAnalysis/openAddRecord'
		});
	}
	function updateRecord() {
		commonHelper.toAdd({
			title : '修改',
			width : 700,
			height : 300,
			url : path + '/plantAnalysis/openUploadRecord'
		});
	}
	function deleteRecords() {
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
				$.post('deleteRecord', {
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
</script>
</head>
<body>
	<!-- 引入自定义按钮页面 -->
	<div id="btn_div">
		<a id="tool_query"> <img src='<%=path%>/static/images/query.gif'
			align='top' border='0' title='查询' />
		</a> <a id="tool_add"> <img src='<%=path%>/static/images/new.gif'
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
				<td class="tdrgt"><input id="plant_name"  name="plant_name" type="text" /></td>
			</tr>
		</table>
	</fieldset>
	<div id="datagrid_div">
		<table id="datagrid"></table>
	</div>

</body>
</html>