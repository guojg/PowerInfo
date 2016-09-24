<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.github.balance.task.entity.BalanceTask"%>
 <!DOCTYPE html>
<html>
<head>
<title>当年新增装机利用系数</title>
<meta http-equiv="content-type" content="text/html;charset=UTF-8" />
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">

<!--引入此文件包含jquery_easyui的css样式与公用js以及登录用户信息-->
<%@include file="../../common/commonInclude.jsp"%>
<% 
String id = request.getParameter("id")==null ?"" : request.getParameter("id"); 
%>
<link rel="stylesheet" type="text/css" href="<%=path%>/static/css/calculatePlanStyle.css" />
<script type="text/javascript" src="<%=path %>/js/totalquantity/common/my-validatebox-ext.js"></script>

<script type="text/javascript" src="<%=path%>/js/regionalanalysis/preparedata/constantcostarg.js"></script>
<script type="text/javascript">
//var id='2016091608461600';  //机组id
	var id='<%=id%>' ;
alert(id);

</script>
</head>
<body>
<form id="paramsForm">
<input id="11" name="11" hidden="true">
	<table id="detailTable">
			<tr>
				<td class="tdlft">机组名称：</td>
				<td  class="tdrgt"><input id="100" name="100" type="text"  /></td>	
				<td  class="tdlft">所属发电厂：</td>
				<td  class="tdrgt"><input id="200" name="200" class="comboboxComponent" type="text"  /></td>	
			</tr>	
			<tr>
				<td class="tdlft">额定容量：</td>
				<td  class="tdrgt"><input id="300" name="300" type="text"  /></td>	
				<td  class="tdlft">投运日期：</td>
				<td  class="tdrgt"><input id="400" name="400" class="easyui-datebox" type="text"  /></td>	
			</tr>			
			
	</table>
	

	<div class="div_submit" >
			<a id="btn_save"   href="javascript:baoCun();" ><img src='<%=path%>/static/images/save.gif'
			align='top' border='0' title='保存' /></a>
	</div> 
</form> 
</body>
</html>