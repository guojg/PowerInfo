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
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6 lt8"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7 lt8"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8 lt8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="en" class="no-js"> <!--<![endif]-->
 	<meta charset="UTF-8" />
 	<meta http-equiv="X-UA-Compatible" content="IE=9">
	<meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="Login and Registration Form with HTML5 and CSS3" />
    <meta name="keywords" content="html5, css3, form, switch, animation, :target, pseudo-class" />
	<link rel="stylesheet" href="<%=path%>/static/js/menu/reset.css"/> <!-- CSS reset -->
	<link rel="stylesheet" href="<%=path%>/static/js/menu/style.css"/> <!-- Resource style -->
	<title></title>
</head>
<body>

		<ul class="cd-nav" style="margin-top:10%;margin-left:25%;margin-right:25%">
			<li>
				<a href="#" onclick="modelchange(0)">
					<span>
					<img src="<%=path%>/static/images/4.jpg" >
					</span>

					<em>基 础 数 据</em>
				</a>
			</li>

			<li>
				<a href="#" onclick="modelchange(1)">
					<span>
					<img src="<%=path%>/static/images/2.jpg"></img>
					</span>

					<em>电力需求预测</em>
				</a>
			</li>

			<li>
				<a href="#" onclick="modelchange(2)">
					<span>
					<img src="<%=path%>/static/images/3.jpg"></img>
					</span>

					<em>电力电量平衡</em>
				</a>
			</li>

			<li>
				<a href="#" onclick="modelchange(3)">
					<span>
					<img src="<%=path%>/static/images/1.gif"></img>
					</span>

					<em>区域电厂竞争力</em>
				</a>
			</li>
		</ul>
<script src="<%=path%>/static/js/menu/jquery-2.1.1.js"></script> <!-- Resource jQuery -->
<script src="<%=path%>/static/js/menu/main.js"></script> <!-- Resource jQuery -->
<script type="text/javascript">
  function modelchange(page){
	  
	 window.location.href="<%=path%>?page="+page;
	 /** var target="<%=path%>?page="+page;
		  newwindow=window.open("","","fullscreen = 1")
		  if (document.all){
		  newwindow.moveTo(0,0)
		 // fullscreen
		  newwindow.resizeTo(screen.width,screen.height)
		  }
		  newwindow.location=target**/
  }
</script>
</body>
</html>