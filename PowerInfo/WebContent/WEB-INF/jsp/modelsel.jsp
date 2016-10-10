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
	<!--------------------------------------easyui公共style样式------------------------------------------>

	<link rel="stylesheet" href="<%=path%>/static/js/menunew/reset.css"/> <!-- CSS reset -->
	<link rel="stylesheet" href="<%=path%>/static/js/menunew/style.css"/> <!-- Resource style -->
	<!------------引用js,jquery版本过高会导致兼容性问题，所以用jquery-1.8.3.js取代jquery.easyui.min.js------------>

	<title></title>
</head>
<body>
<div style="text-align:center;clear:both">
</div>
  <div class="radmenu"><a href="#" id="mainmenu" class="show" >开始</a>
  <ul>
    <li>
      <a href="<%=path%>/index2?page=0"  class="">市场信息数据</a>
    </li>
    <li>
      <a href="<%=path%>/index2?page=1" >电力需求预测</a>
    </li>
    <li>
      <a href="<%=path%>/index2?page=2">电力电量平衡</a>
    </li>
    <li>
      <a href="<%=path%>/index2?page=3" >电厂竞争力分析</a>
    </li>
    <li>
      <a href="<%=path%>/index2?page=4" >电厂竞价预测</a>
    </li>
  </ul>
</div>

  <script src="<%=path%>/static/js/menunew/index.js"></script>
</body>

</html>