<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%

String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()+ path + "/";
String commonPath = path;
   
%>
 <!DOCTYPE html>
<html lang="en" class="no-js">
<head>
	<link rel="stylesheet" href="<%=path%>/static/js/menunew/reset.css"/> <!-- CSS reset -->
	<link rel="stylesheet" href="<%=path%>/static/js/menunew/style.css"/> <!-- Resource style -->
	<title></title>
</head>
<body>
<div style="text-align:center;clear:both">
<script src="/follow.js" type="text/javascript"></script>
</div>
  <div class="radmenu"><a href="#" class="show" >开始</a>
  <ul>
    <li>
      <a href="<%=path%>?page=0"  class="">市场信息数据</a>
    </li>
    <li>
      <a href="<%=path%>?page=1" >电力需求预测</a>
    </li>
    <li>
      <a href="<%=path%>?page=2">电力电量平衡</a>
    </li>
    <li>
      <a href="<%=path%>?page=3" >电厂竞争力分析</a>
    </li>
    <li>
      <a href="<%=path%>?page=4" >电厂竞价预测</a>
    </li>
  </ul>
</div>

  <script src="<%=path%>/static/js/menunew/index.js"></script>
</body>

</html>