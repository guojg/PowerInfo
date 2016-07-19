<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<%@include file="../../common/commonInclude.jsp" %>	
<link rel="stylesheet" type="text/css" href="<%=path%>/static/css/calculatePlanStyle.css" />

<script type="text/javascript">

function startCalculate(){
	 $.ajax({
			type : 'POST',
			async : false,
			dataType: 'json',
			url :  '/PowerInfo/calculatePlan/startCalculate',
			success : function(data) {
				
				$.messager.alert("提示", "计算成功！");

			}
		});
}
</script>
</head>
<body>
	<p class="p1">
	平均增长率法:
	</p>
		<p class="p2">
			基准年电量*（1+i）^(2020-2015)，其中i=(a+4m+b)/6（主观概率计算）
		</p>
				<br>
		
	<p class="p1">
	产值单耗法:
	</p>
		<p class="p2">
			基准年一产单耗*一产单耗增长率+基准年二产单耗*二产单耗增长率+
基准年三产单耗*三产单耗增长率+基准年人均居民生活用电量*人均居民生活用电量增长率*预测年人口

		</p>
				<br>
		
	<p class="p1">
	弹性系数法:
	</p>
		<p class="p2">
				规划期末期用电量=规划期初期(即基准年)用电量*
	 （1+电力弹性系数*国内生产总值平均年增长速度）的预测年限（即预测年减去基准年）次方；
		</p>
		<br>
	<p class="p1">
			
	
	人均用电量法:
	</p>
		<p class="p2">
			基准年人均用电量* （1+i）^(2020-2015)*预测年人口数
		</p>
				<br>
		
	<p class="p1">
	平均值法:
	</p>
		<p class="p2">
		(方法一预测值+方法二预测值+方法三预测值+方法四预测值)/4
		</p>
				<br>
		
	<p class="p1">
	最优权重法:
	</p>
		<p class="p2">
		方法一预测值*权重1+方法二预测值*权重2+方法三预测值*权重3+方法四预测值*权重4   ，其中四个权重值之和为1
		</p>
	
	
 
	<div class="div_submit" >
		<a id="tool_start" href="javascript:startCalculate();" > <img src='<%=path%>/static/images/js.gif'
			align='top' border='0' title='保存' /></a>
	</div> 

</body>
</html>