<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
 <!DOCTYPE html>

<html>
 <head>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6 lt8"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7 lt8"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8 lt8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="en" class="no-js"> <!--<![endif]-->
        <meta charset="UTF-8" />
        <!-- <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">  -->
        <title>登录</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0"> 
        <meta name="description" content="Login and Registration Form with HTML5 and CSS3" />
        <meta name="keywords" content="html5, css3, form, switch, animation, :target, pseudo-class" />
        <meta name="author" content="Codrops" />
        <link rel="stylesheet" type="text/css" href="/PowerInfo/static/css/demo.css" />
        <link rel="stylesheet" type="text/css" href="/PowerInfo/static/css/style.css" />
		<link rel="stylesheet" type="text/css" href="/PowerInfo/static/css/animate-custom.css" />
		    <link rel="stylesheet" type="text/css" href="/PowerInfo/static/js/jquery-easyui-1.4/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="/PowerInfo/static/js/jquery-easyui-1.4/themes/icon.css" />
    <script type="text/javascript" src="/PowerInfo/static/js/jquery-easyui-1.4/jquery-1.8.3.js"></script>
    <script type="text/javascript" src="/PowerInfo/static/js/jquery-easyui-1.4/jquery.easyui.min.js"></script>
		<%	String flag = request.getAttribute("flag")==null ? null : request.getAttribute("flag")+""; 
			String message = request.getAttribute("message")==null ? "" : request.getAttribute("message")+""; 

		%>
		
		<script type="text/javascript">
		var flag='<%=flag%>';
		var message='<%=message%>';
		function checkform_success(e){
			e=e||window.event;
			if(document.register.passwordsignup.value!=document.register.passwordsignup_confirm.value){
			$.messager.alert("提示", "两次密码不一致，请重新输入!");
			if(document.all) e.returnValue=false;//ie,window.event.returnValue=false阻止元素默认行为
			else e.preventDefault();//火狐,event.preventDefault阻止元素默认行为
			}
			}
		 function loadTopWindow(){ 
			 if (window.top!=null && window.top.document.URL!=document.URL){ 
				 window.top.location= document.URL; //这样就可以让登陆窗口显示在整个窗口了
				 } 
			 } 
		function Fkey() {
			/*var docElm = document.documentElement;
			 
			//W3C 
			 
			if (docElm.requestFullscreen) { 
			 
			  docElm.requestFullscreen(); 
			 
			}
			 
			//FireFox 
			 
			else if (docElm.mozRequestFullScreen) { 
			 
			  docElm.mozRequestFullScreen(); 
			 
			}
			 
			//Chrome等 
			 
			else if (docElm.webkitRequestFullScreen) { 
			 
			  docElm.webkitRequestFullScreen(); 
			 
			}
			 
			//IE11
			 
			else if (docElm.msRequestFullscreen) {
			 
			 docElm.msRequestFullscreen();
			 
			}*/
			document.getElementById('myform').submit();
		}
		$(function() {
			if(flag=="1"){
				window.location.href = '/PowerInfo/loginIndex';				
				alert("提示", "注册成功!");

			}else if(flag=="0"){
				window.location.href = '/PowerInfo/loginIndex';
				$.messager.alert("提示", "注册失败!");
			}else{
				
			}
			if(message !=""){
				$.messager.alert("提示", message);
			}else{
				
			}
			
		});
		
		</script>
    </head>
<body onload="loadTopWindow()">
 <div class="container">
            <!-- Codrops top bar -->
            <!--<div class="codrops-top">
                <a href="">
                    <strong>&laquo; Previous Demo: </strong>Responsive Content Navigator
                </a>
                <span class="right">
                    <a href=" http://tympanus.net/codrops/2012/03/27/login-and-registration-form-with-html5-and-css3/">
                        <strong>Back to the Codrops Article</strong>
                    </a>
                </span>
                <div class="clr"></div>
            </div>--><!--/ Codrops top bar -->
            <header>
                <h1>区域电力市场预测分析平台</h1>
			
            </header>
            <section>				
                <div id="container_demo" >
                    <!-- hidden anchor to stop jump http://www.css3create.com/Astuce-Empecher-le-scroll-avec-l-utilisation-de-target#wrap4  -->
                   <a class="hiddenanchor" id="toregister"></a>
                    <a class="hiddenanchor" id="tologin"></a>
                    <div id="wrapper">
                        <div id="login" class="animate form">
                            <form  action="login"  id="myform"autocomplete="on" method="post"> 
                                <h1>登录</h1> 
                                <p> 
                                    <label for="username" class="uname" data-icon="u" > 用户名 </label>
                                    <input id="username" name="username" required="required" type="text" placeholder="用户名" value="<shiro:principal/>"/>
                                </p>
                                <p> 
                                    <label for="password" class="youpasswd" data-icon="p">密码 </label>
                                    <input id="password" name="password" required="required" type="password" placeholder="密码 " /> 
                                </p>
                                <!--
                                <p class="keeplogin"> 
									<input type="checkbox" name="loginkeeping" id="loginkeeping" value="loginkeeping" /> 
									<label for="loginkeeping">Keep me logged in</label>
								</p>
								-->
                                <p class="login button"> 
                                   <input type="button" onclick="Fkey()" value="登录"/>
								</p>
 						 <p class="change_link">
									还没有账户吗 ?
									<a href="#toregister" class="to_register">注册</a>
								</p>
                            </form>
                        </div>
  
                        <div id="register" class="animate form">
                            <form  action="register/index" autocomplete="on" name="register" method="post"> 
                                <h1> 注册 </h1> 
                                <p> 
                                    <label for="usernamesignup" class="uname" data-icon="u">用户名</label>
                                    <input id="usernamesignup" name="usernamesignup" required="required" type="text" placeholder="mysuperusername690" />
                                </p>
                               
                                <p> 
                                    <label for="passwordsignup" class="youpasswd" data-icon="p">密码 </label>
                                    <input id="passwordsignup" name="passwordsignup" required="required" type="password" placeholder="eg. X8df!90EO"/>
                                </p>
                                <p> 
                                    <label for="passwordsignup_confirm" class="youpasswd" data-icon="p">确认密码 </label>
                                    <input id="passwordsignup_confirm" name="passwordsignup_confirm" required="required" type="password" placeholder="eg. X8df!90EO"/>
                                </p>
                                 <p> 
                                    <label for="rolename" data-icon="u">角色</label>
                                    <select id="rolename" name="rolename"><option value ="2" selected="selected">普通员工</option></select>
                                </p>
                                
                                <p class="signin button"> 
									<input type="submit" value="注册" onClick="checkform_success(event)"/> 
								</p>
                                <p class="change_link">  
									已经有账户吗 ?
									<a href="#tologin" class="to_register"> 登录 </a>
								</p>
                            </form>
                        </div>
					
                    </div>
                </div>  
            </section>
        </div>
</body>
</html>