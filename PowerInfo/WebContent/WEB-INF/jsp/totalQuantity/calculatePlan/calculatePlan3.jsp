<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.github.totalquantity.task.entity.TotalTask"%>
 <!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<%@include file="../../common/commonInclude.jsp" %>	
<% 
TotalTask tt=  (TotalTask)request.getSession().getAttribute("totaltask");
String algorithm = tt.getAlgorithm() ;
String taskid = tt.getId();
String algorithmRadio = tt.getAlgorithmRadio();
%>
<link rel="stylesheet" type="text/css" href="<%=path%>/static/css/calculatePlanStyle.css" />
<script type="text/javascript" src="<%=path %>/js/totalquantity/common/sysdict.js"></script>
<script type="text/javascript" src="<%=path%>/js/totalquantity/jsonutil.js"></script>
<script type="text/javascript">
var algorithmStr='<%=algorithm%>';  //算法代号
var taskid='<%=taskid%>';  //算法代号
var algorithmRadio='<%=algorithmRadio%>';//综合算法
</script>
<script type="text/javascript" src="<%=path %>/js/totalquantity/calculatePlan/calculatePlan.js"></script>

</head>
<body>
	<table id="calculateTable">
		
			<tr>
			<td rowspan="2" class="bs1">预测算法</td>
				<td  class="bs3"><input name="algorithm" type="checkbox" value="1" />平均增长率法</td>
				<td class="bs3"><input name="algorithm" type="checkbox" value="2" />产值单耗法</td>
				<td class="bs3"><input name="algorithm" type="checkbox" value="3" />弹性系数法</td>
				<td class="bs3"><input name="algorithm" type="checkbox" value="4" />人均用电量法</td>
			</tr>
			<tr>
				<td class="bs3">
					<table id="1"  class="bs2">
						   <tr> <td class="tdlft">最大值：</td> <td class="tdrgt"><input  type="text" name="maxRate" id="maxRate"></td></tr>
						    <tr> <td  class="tdlft">最小值：</td> <td class="tdrgt"><input  type="text" name="minRate" id="minRate"></td></tr>
						 <tr> <td class="tdlft">最可能值：</td> <td class="tdrgt"><input  type="text" name="possibleRate" id="possibleRate"></td></tr>
					</table> 
				</td>
				<td class="bs3">
					<table id="2" class="bs2">
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
				<td  class="bs3">	
					<table id="3" class="bs2">
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
				<td class="bs3">
					<table id="4"  class="bs2" >
						   <tr> <td class="tdlft">最大值：</td> <td class="tdrgt"><input  type="text" name="avgMaxRate" id="avgMaxRate"></td></tr>
						    <tr> <td  class="tdlft">最小值：</td> <td class="tdrgt"><input  type="text" name="avgMinRate" id="avgMinRate"></td></tr>
						 <tr> <td class="tdlft">最可能值：</td> <td class="tdrgt"><input  type="text" name="avgPossibleRate" id="avgPossibleRate"></td></tr>
					</table> 
				</td>
			</tr>
			<tr id="ccc">
				
				<td class="bs1" id="aaa" rowspan="2">综合预测</td>
				<td class="bs3"><input name="algorithmRadio" type="radio" value="5" /><font class="fontred">平均值法</font></td>
				<td class="bs3"><input name="algorithmRadio" type="radio" value="6" /><font class="fontred">最优权重法 </font></td>
			</tr>
			<tr id="bbb">
				<td class="bs3">
				<div id="avgvalidate" class="divred"></div>
				</td>
				<td class="bs3"></td>
			</tr>
		
			
			
	</table>
	
 
	<div class="div_submit" >
		<a id="tool_save" href="javascript:save();" > <img src='<%=path%>/static/images/save.gif'
			align='top' border='0' title='保存' /></a>
		</div> 

</body>
</html>