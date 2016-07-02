<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<title>图表生成</title>
		<!-- 加载CSS -->
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/js/jquery-easyui-1.4/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/js/jquery-easyui-1.4/themes/icon.css" />
		<link rel="stylesheet" href="<%=request.getContextPath()%>/static/js/jquery-easyui-1.4/farbtastic/farbtastic.css" />
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css//button.css" >
		<link rel="stylesheet" href="<%=request.getContextPath()%>/static/css/chart.css" />
		<link rel="stylesheet" href="<%=request.getContextPath()%>/static/css/index.css" />
		<script type="text/javascript">
			var path = '<%=request.getContextPath()%>';
		</script>
	</head>
	<body>
		  <div id="container" style="width:100%;height:100%"></div>
		
	    
		<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/Highcharts-4.0.1/raphael.js"></script>
    	<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/jquery-easyui-1.4/jquery-1.8.3.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/jquery-easyui-1.4/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/jquery-easyui-1.4/ext/jquery.datagrid.megincell-ext.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/Highcharts-4.0.1/js/highcharts.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/Highcharts-4.0.1/js/themes/custom.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/Highcharts-4.0.1/js/modules/exporting.js"></script>
 		<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/jquery-easyui-1.4/farbtastic/farbtastic.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/basicData/basic.js"></script>
	</body> 
</html>