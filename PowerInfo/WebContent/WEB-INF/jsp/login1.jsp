<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<html>
<head>
    <title>登录</title>
    <style>.error{color:red;}</style>
       <link href="static/js/lib/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="static/fonts/css/font-awesome.min.css" rel="stylesheet">
    <link href="static/fonts/system-fonts/style.css" rel="stylesheet">
    <link href="static/css/login.css" rel="stylesheet" type="text/css">
</head>
<body>

<div class="error">${error}</div>
 <form action="" method="post">
    用户名：<input type="text" name="username" value="<shiro:principal/>"><br/>
    密码：<input type="password" name="password"><br/>
    自动登录：<input type="checkbox" name="rememberMe" value="true"><br/>
    <input type="submit" value="登录">
</form>
<!-- <div class="center-parent login">
        <div class="center-container">
            <div class="container">
                <div class="row">
                    <div class="col-lg-10 col-lg-offset-1">
                        <div class="row">
                            <div class="col-lg-6 col-lg-offset-3">
                                <div class="panel">
                                    <div class="panel-heading">
                                        <span>
                                            <i class="icon-logo logo" style=""></i>
                                            <span class="title">一体化电网规划设计平台</span>
                                        </span>
                                    </div>
                                    <div class="panel-body">
                                        <form class="form-horizontal" action="" method="post">
                                            <div class="form-group">
                                                <label for="username" class="col-sm-2 control-label">账号：</label>
                                                <div class="col-sm-10">
                                                    <input type="text" class="form-control" id="username" value="<shiro:principal/>" placeholder="请输入账号">
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label for="password" class="col-sm-2 control-label">密码：</label>
                                                <div class="col-sm-10">
                                                    <input type="password" class="form-control" id="password" placeholder="请输入密码">
                                                </div>
                                            </div>
                                            <div class="form-group" id="messageGroup">
                                                <div class="col-sm-offset-2  col-sm-10">
                                                    <div class="alert alert-danger text-left" id="errorMessage">
                                                       ${error}
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <div class="col-sm-offset-2 col-sm-10">
                                                    <button id="loginBtn" type="submit" class="btn btn-default form-control">登录</button>
                                                </div>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>-->
</body>
</html>