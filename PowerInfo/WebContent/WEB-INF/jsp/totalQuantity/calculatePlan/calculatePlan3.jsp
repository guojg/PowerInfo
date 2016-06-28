<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<%@include file="../../common/commonInclude.jsp" %>	
<link rel="stylesheet" type="text/css" href="<%=path%>/static/css/calculatePlanStyle.css" />

<script type="text/javascript" src="<%=path%>/js/totalquantity/jsonutil.js"></script>
<script type="text/javascript" src="<%=path %>/js/totalquantity/calculatePlan.js"></script>

</head>
<body>
	<table id="calculateTable">
		
			<tr>
				<td><input name="algorithm" type="checkbox" value="1" />平均增长率法</td>
				<td><input name="algorithm" type="checkbox" value="2" />产值单耗法</td>
				<td ><input name="algorithm" type="checkbox" value="3" />弹性系数法</td>
			</tr>
			<tr>
				<td>
					<table id="1">
						<tr><td colspan="2">期望增长率</td></tr>
						   <tr> <td class="tdlft">最大值：</td> <td class="tdrgt"><input  type="text" name="maxRate" id="maxRate"></td></tr>
						    <tr> <td  class="tdlft">最小值：</td> <td class="tdrgt"><input  type="text" name="minRate" id="minRate"></td></tr>
						 <tr> <td class="tdlft">最可能值：</td> <td class="tdrgt"><input  type="text" name="possibleRate" id="possibleRate"></td></tr>
					</table> 
				</td>
				<td>
					<table id="2">
						     <tr> 
						     	<td class="tdlft"> 一产单耗增长率：</td> 
						     	<td class="tdrgt"><input  type="text" name="oneProductionRate" id="oneProductionRate"></td>
						     </tr>
						     <tr> 
						       <td class="tdlft">二产单耗增长率：</td>
						       <td class="tdrgt"><input  type="text" name="twoProductionRate" id="twoProductionRate"></td>
						     </tr>
						    <tr> 
						       <td class="tdlft">三产单耗增长率：</td> 
						       <td class="tdrgt"><input  type="text" name="threeProductionRate" id="threeProductionRate"></td>
						   </tr>
						  <tr> 
						  	   <td class="tdlft">  人均居民生活用电量增长率：</td> 
						  	   <td class="tdrgt"><input  type="text" name="avgElectricityRate" id="avgElectricityRate"></td>
						  </tr>
					</table> 
				</td>
				<td>	
					<table id="3">
						 <tr> 
						  	   <td class="tdlft">    期间电力弹性系数：</td> 
						       <td class="tdrgt"><input  type="text" name="coefficient" id="coefficient"></td>
						   </tr>
						  <tr> 
						  	   <td class="tdlft">  国内生产总值平均年增长速度：</td> 
						       <td class="tdrgt"><input  type="text" name="incrementSpeed" id="incrementSpeed"></td>
						   </tr>
					</table> 
				</td>
			</tr>
			<tr>
				
				<td><input name="algorithm" type="checkbox" value="4" />人均用电量法</td>
				<td ><input name="algorithm" type="checkbox" value="5" />平均值法</td>
				<td><input name="algorithm" type="checkbox" value="6" />最优权重法</td>
			</tr>
			<tr>

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