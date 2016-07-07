<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<html>
<head>
<title>图表生成</title>
<%@include file="../common/commonInclude.jsp"%>
<%
String pid=request.getAttribute("pid")==null?"":request.getAttribute("pid").toString();
%>
<!-- 加载CSS -->
<link rel="stylesheet"
	href="<%=path%>/static/js/jquery-easyui-1.4/farbtastic/farbtastic.css" />
<link rel="stylesheet" type="text/css"
	href="<%=path%>/static/css//button.css">
<link rel="stylesheet" href="<%=path%>/static/css/chart.css" />
<link rel="stylesheet" href="<%=path%>/static/css/index.css" />
<script type="text/javascript">
var pid='<%=pid%>';
	$(function() {
		$("#tool_query").bind("click", function() {
			var years = $("#years").combo("getValues");
			//水平年份
			var yrs_s;
			if(years!=""){
				yrs_s=years+"";
			}else{
				yrs_s="";
			}
			if(yrs_s==""){
				$.messager.alert("提示", "请选择年份！");
				return;
			}
			var indexs = $("#indexs").combo("getValues");
			//水平年份
			var index_s;
			if(years!=""){
				index_s=indexs+"";
			}else{
				index_s="";
			}
			if(index_s==""){
				$.messager.alert("提示", "请选择指标！");
				return;
			}
			drawChart();
		});

		comboBoxInit({
			id : "years",
			url : path + '/basicData/getyears',
			textkey : "yearName",
			valuekey : "year",
			multiple : true
		});
		comboBoxInit({
			id : "pic_type",
			url : path + '/js/basicData/chartType.json',
			textkey : "text",
			valuekey : "id",
			defaultVal : "column"
		});
		comboBoxInit({
			id : "indexs",
			url : path + '/basicData/getindexs?pid='+pid,
			textkey : "indexName",
			valuekey : "indexItem",
			multiple : true
		});
		$("#pic_type").combobox({
			onSelect:function(){
				drawChart();
			}
		});
	});
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
			<tr>
				<td class="tdlft">年份：</td>
				<td class="tdrgt"><input id="years" class="comboboxComponent" /></td>
				<td class="tdlft">指标：</td>
				<td class="tdrgt"><input id="indexs" class="comboboxComponent" /></td>
				<td class="tdlft">图标类型：</td>
				<td class="tdrgt"><input id="pic_type"
					class="comboboxComponent" /></td>
			</tr>
		</table>
	</fieldset>
	<div id="container" style="width: 80%; height: 70%"></div>


	<script type="text/javascript"
		src="<%=path%>/static/js/Highcharts-4.0.1/raphael.js"></script>
	<script type="text/javascript"
		src="<%=path%>/static/js/Highcharts-4.0.1/js/highcharts.js"></script>
	<script type="text/javascript"
		src="<%=path%>/static/js/Highcharts-4.0.1/js/themes/custom.js"></script>
	<script type="text/javascript"
		src="<%=path%>/static/js/Highcharts-4.0.1/js/modules/exporting.js"></script>
	<script type="text/javascript"
		src="<%=path%>/static/js/jquery-easyui-1.4/farbtastic/farbtastic.js"></script>
	<script type="text/javascript" src="<%=path%>/js/basicData/basic.js"></script>
</body>
</html>