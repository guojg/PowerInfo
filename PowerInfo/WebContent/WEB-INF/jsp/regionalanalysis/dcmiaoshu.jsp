<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>描述</title>
<%@include file="../common/commonInclude.jsp" %>	
<link rel="stylesheet" type="text/css" href="<%=path%>/static/css/calculatePlanStyle.css" />

</head>
<body>
	<p class="p1">
	流程:
	</p>
	<p class="p2">
				<img src="/PowerInfo/static/images/dclc.gif"></img>
	</p>
				<br>
		<p class="p1">
	区域:
	</p>
		<p class="p2">
			点击地图上的区域，进入该区域电厂和机组分析。
		</p>
				<br>	
	<p class="p1">
	电厂:
	</p>
		<p class="p2">
			录入电厂基本信息

		</p>
				<br>
		
	<p class="p1">
	发电机:
	</p>
		<p class="p2">
				录入机组的基本信息，选择所属电厂
		</p>
		<br>
	<p class="p1">
			
	
	成本分析:
	</p>
	<p class="p2">
			主要是对一个电厂或者一个机组进行成本分析，在分析的过程中可以修改相应的基本参数。修改的基本参数只会在当前任务下进行改动，<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;不会影响其他任务的电厂和机组分析
		</p>
				<br>
		<p class="p2">
			操作流程：先点击任务，然后点击单一电厂或者单一机组
		</p>
		
				<br>
		
	<p class="p1">
	成本对比:
	</p>
		<p class="p2">
			主要是对几个电厂或者几个机组进行对比，在对比的过程中可以修改相应的基本参数。修改的基本参数只会在当前任务下进行改动，<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;不会影响其他任务的电厂和机组分析
			
		</p>
				<br>
		<p class="p2">
			操作流程：先点击任务，然后点击电厂对比或者机组对比
		</p>
		
		

</body>
</html>