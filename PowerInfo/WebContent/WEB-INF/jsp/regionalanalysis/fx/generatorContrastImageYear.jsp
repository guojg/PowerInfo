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
<style type="text/css">
#container {position:absolute;width:49.9%;height:15%;z-index:1;left:0px;}
#container1{position:absolute;width:49.9%;height:15%;z-index:2;left:49.9%;}
#container2 {position:absolute;width:49.9%;height:15%;z-index:1;left:0px;}
#container3{position:absolute;width:49.9%;height:15%;z-index:2;left:49.9%;}
</style>
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/js/jquery-easyui-1.4/themes/default/easyui.css" />
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/js/jquery-easyui-1.4/themes/icon.css" />
		<link rel="stylesheet" href="<%=request.getContextPath()%>/static/js/jquery-easyui-1.4/farbtastic/farbtastic.css" />
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/button.css" >
		<link rel="stylesheet" href="<%=request.getContextPath()%>/static/css/chart.css" />
		<link rel="stylesheet" href="<%=request.getContextPath()%>/static/css/index.css" />
		<script type="text/javascript">
		var id='<%=id%>' ;

		
	</script>
		</head>
		<body>
		 <div style="min-width: 100%; height: 50%;margin: 0px 0px 0px 0px;padding:0">
<div id="container" style="width: 49.9%; height: 49%; reflow:true"></div>
<div id="container1" style="width: 49.9%; height: 49%; reflow:true"></div>
</div>
 <div style="min-width: 100%; height: 50%;margin: 0px 0px 0px 0px;padding:0" >
<div id="container2" style="width: 49.9%; height: 49%;  reflow:true"></div>
<div id="container3" style="width: 49.9%; height: 49%;  reflow:true"></div>
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
	<script type="text/javascript" src="<%=path%>/js/regionalanalysis/fx/generatorContrastImageYear.js"></script>
		</body>
</html>