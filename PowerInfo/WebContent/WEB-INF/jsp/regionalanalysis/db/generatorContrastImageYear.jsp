<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
 <!DOCTYPE html>
<html>
	<head>
		<title>图表生成</title>
		<meta http-equiv="content-type" content="text/html;charset=UTF-8" /> 
		
		<!-- 加载CSS -->
		<%@include file="../../common/commonInclude.jsp" %>	
		<% 
String id = request.getParameter("id")==null ?"" : request.getParameter("id"); 

%>

		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/js/jquery-easyui-1.4/themes/default/easyui.css" />
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/js/jquery-easyui-1.4/themes/icon.css" />
		<link rel="stylesheet" href="<%=request.getContextPath()%>/static/js/jquery-easyui-1.4/farbtastic/farbtastic.css" />
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/button.css" >
		<link rel="stylesheet" href="<%=request.getContextPath()%>/static/css/chart.css" />
		<link rel="stylesheet" href="<%=request.getContextPath()%>/static/css/index.css" />
		<script type="text/javascript">
		var id='<%=id%>' ;

		$(function() {

			$("#tool_query").bind("click", function() {
				
				drawChart();
			});
			 comboBoxInit({
					id : "index_y",
					url : path + '/sysdict/getDataByCodeValue?domain_id=303',
					textkey : "value",
					valuekey : "code",
					multiple:false,
					defaultVal:1
				});
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
			$("#index_y").combobox({
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
	   				<a id="exportBtn"> <img
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
						<td class="tdlft">图标类型：</td>
						<td class="tdrgt"><input id="pic_type"
							class="comboboxComponent" /></td>
					</tr>
						<tr style="display:none">
			<td>	<input id="index_x" class="comboboxComponent" type="hidden"/>
		   </td>
			</tr>
				</table>
			</fieldset>
			<div id="container" style="width: 80%; height: 70%">
			</div>


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
	<script type="text/javascript" src="<%=path%>/js/regionalanalysis/db/generatorContrastImageYear.js"></script>
		</body>
</html>