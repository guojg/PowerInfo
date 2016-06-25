<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<%@include file="../../common/commonInclude.jsp" %>	
<script type="text/javascript" src="<%=path%>/js/totalquantity/jsonutil.js"></script>
<script type="text/javascript" src="<%=path %>/js/totalquantity/calculatePlan.js"></script>
<script type="text/javascript"> 

</script>
</head>
<body>
	<table id="detailTable">
		
			<tr>
				<td><input name="algorithm" type="checkbox" value="1" />平均增长率法</td>
				<td><input name="algorithm" type="checkbox" value="2" />产值单耗法</td>
			</tr>
			<tr>
				<td>
					<div id="1">
						期望增长率<br>
						    最大值：<input  type="text" name="maxRate" id="maxRate"><br>
						    最小值：<input  type="text" name="minRate" id="minRate"><br>
						最可能值：<input  type="text" name="possibleRate" id="possibleRate"><br>
					</div> 
				</td>
				<td>
					<div id="2">
						    一产单耗增长率：<input  type="text" name="oneProductionRate" id="oneProductionRate"><br>
						     二产单耗增长率：<input  type="text" name="twoProductionRate" id="twoProductionRate"><br>
						 三产单耗增长率：<input  type="text" name="threeProductionRate" id="threeProductionRate"><br>
					</div> 
				</td>
			</tr>
			<tr>
				<td ><input name="algorithm" type="checkbox" value="3" />弹性系数法</td>
			
				<td><input name="algorithm" type="checkbox" value="4" />人均用电量法</td>
			
			</tr>
			<tr>
				<td>	
					<div id="3">
						    期间电力弹性系数：<input  type="text" name="coefficient" id="coefficient"><br>
						   国内生产总值平均年增长速度：<input  type="text" name="incrementSpeed" id="incrementSpeed">
					</div> 
				</td>
				<td>	
					<div id="4">
						    人均用电量增长率：<input  type="text" name="avgPowerRate" id="avgPowerRate"><br>
					</div> 
				</td>
			</tr>
			<tr>
				<td ><input name="algorithm" type="checkbox" value="5" />平均值法</td>
				<td><input name="algorithm" type="checkbox" value="6" />最优权重法</td>
			</tr>
			
			
	</table>
	
 
	<div class="div_submit" >
			<a id="btn_save"   href="javascript:baoCun();" >计算</a>
		</div> 
		<div class="div_submit" >
			<a id="btn_start"   href="javascript:start111();" >启动</a>
		</div> 
</body>
</html>