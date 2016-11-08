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
	<script type="text/javascript" src="<%=path%>/js/regionalanalysis/fx/generatorContrastImage1.js"></script>
		</body>
</html>