<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ page import="com.github.totalquantity.task.entity.TotalTask"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<title>图表生成</title>
		<!-- 加载CSS -->
		<%@include file="../../common/commonInclude.jsp" %>	
		<% 
TotalTask tt=  (TotalTask)request.getSession().getAttribute("totaltask");
String algorithm = tt.getAlgorithm() ;
String taskid = tt.getId();
String planyear = tt.getPlanyear();
%>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/js/jquery-easyui-1.4/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/js/jquery-easyui-1.4/themes/icon.css" />
		<link rel="stylesheet" href="<%=request.getContextPath()%>/static/js/jquery-easyui-1.4/farbtastic/farbtastic.css" />
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css//button.css" >
		<link rel="stylesheet" href="<%=request.getContextPath()%>/static/css/chart.css" />
		<link rel="stylesheet" href="<%=request.getContextPath()%>/static/css/index.css" />
		<script type="text/javascript">
	var taskid='<%=taskid%>';  //任务号
	var planyear='<%=planyear%>';//规划年
	var algorithm='<%=algorithm%>';//算法
			var path = '<%=request.getContextPath()%>';
			$(function() {
				$("#tool_query").bind("click", function() {
					drawChart();
				});

				var itemJson =[{    
				    "ID":planyear,    
				    "TEXT":planyear+"年"   
				}];
				$('#years').combobox({   
				    data:itemJson,   
				    valueField:'ID',   
				    textField:'TEXT',
				    multiple:false
				});
				$('#years').combobox('setValue', planyear);
				comboBoxInit({
					id : "pic_type",
					url : path + '/js/basicData/chartType.json',
					textkey : "text",
					valuekey : "id",
					defaultVal : "column"
				});
				
				$("#pic_type").combobox({
					onSelect:function(){
						drawChart();
					}
				});

				//queryData();
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