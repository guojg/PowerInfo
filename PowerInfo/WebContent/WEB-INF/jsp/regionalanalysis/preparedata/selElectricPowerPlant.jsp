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
%>
<script type="text/javascript">
	var area_id='<%=organCode%>';
	var cols = [ [ {
		field : 'id',
		width : 20,
		align : 'center',
		checkbox : true
	}, {
		field : 'plant_name',
		title : '电厂名称',
		width : 200,
		align : 'center'
	}, {
		field : 'plant_capacity',
		title : '装机容量',
		width : 100,
		align : 'center'
	}, {
		field : 'index_itemname',
		title : '电源类型',
		width : 100,
		align : 'center'
	}, {
		field : 'start_date',
		title : '投产日期',
		width : 100,
		align : 'center'
	}, {
		field : 'end_date',
		title : '退役日期',
		width : 100,
		align : 'center'
	} ] ];
	$(function() {
		 comboBoxInit({
				id : "index_item",
				url : path + '/sysdict/getDataByCodeValue?domain_id=12',
				textkey : "value",
				valuekey : "code",
				multiple : true

		 });
		$("#tool_query").bind("click", function() {
			queryData();
		});
		$("#tool_sel").bind("click", function() {
			selRecords();
		});
		queryData();
	});
	//取消
	function cancel(){
		//关闭窗口
		window.parent.$('#win_div').window('close');
	}
	function selRecords() {
		$.messager.confirm('提示', '确认抽取所选电厂及其关联的机组?', function(r) {
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
				var url =path+'/electricPowerPlant/selRecordToAnalysis';
				$.post(url, {"ids" : ids}, function(data) {
					var data = $.parseJSON(data);
					if (data== '1') {
						$.messager.alert('提示', '抽取成功！', 'info', function() {
							queryData();
							window.parent.queryData();

						});
					}
				});
			}
		});

	}

	//查询方法调用的函数
	function queryData() {
		var indexs = $("#index_item").combo("getValues");
		//指标
		var index_s;
		if (indexs != "") {
			index_s = indexs + "";
		} else {
			index_s = "";
		}
		if (index_s == "") {
			$.messager.alert("提示", "请选择电源类型！");
			return;
		}
		var plant_name=$("#plant_name").val();
		var queryParams = {
			indexs : index_s,
			name :plant_name,
			area_id:area_id,
			flag:1
		};
		var url = path + '/electricPowerPlant/queryData';
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

</script>
</head>
<body>
	<!-- 引入自定义按钮页面 -->
	<div id="btn_div">
		<a id="tool_query"> <img src='<%=path%>/static/images/query.gif'
			align='top' border='0' title='查询' />
		</a>
 		<a id="tool_sel"> <img src='<%=path%>/static/images/cqsj.gif'
			align='top' border='0' title='抽取数据' />
		</a> 
		<a id="btn_cancel" href="javascript:cancel();" >
		<img src="/PowerInfo/static/images/quxiao.gif" border="0" style="vertical-align: middle">
		</a>
	</div>
	<fieldset id="field">
		<legend>查询条件</legend>
		<table id="search_tbl">
			<tr>
				<td class="tdlft">电厂名称：</td>
				<td class="tdrgt"><input id="plant_name"  name="plant_name"/></td>
				<td class="tdlft">电源类型：</td>
				<td class="tdrgt"><input id="index_item" name="index_item"/></td>
			</tr>
		</table>
	</fieldset>
			<div id="title" style="padding-right: 5px;text-align: right"><b>单位：万千瓦</b></div>
	
	<div id="datagrid_div">
		<table id="datagrid"></table>
	</div>

</body>
</html>