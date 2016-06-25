<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>增加</title>
<%@include file="../common/commonInclude.jsp" %>	


<script type="text/javascript">

</script>
</head>
<body>

<table id="detailTable">
			<tr>
				<td colspan="2" class="tdlft">基准年</td>
				<td ><input id="base_year" name="base_year" type="text"  /></td>	
			</tr>
			<tr>
				<td colspan="2" class="tdlft">预测年</td>
				<td ><input id="predict_year" name="predict_year" type="text"  /></td>	
			</tr>
			<tr>
				<td rowspan="3" class="tdlft">期望增长率</td>
				<td class="tdlft">最大值</td>
				<td ><input id="max_value" name="max_value" type="text"  /></td>	
			</tr>
			<tr>
				<td class="tdlft">最小值</td>
				<td ><input id="min_value" name="min_value" type="text"  /></td>	
			</tr>
			<tr>
				<td class="tdlft">最可能值</td>
				<td ><input id="maybe_value" name="maybe_value" type="text"  /></td>	
			</tr>
			
</table> 
	<div class="div_submit" >
			<a id="btn_save"   href="javascript:baoCun();" >计算</a>
		</div> 
</body>

</html>