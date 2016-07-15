<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
 <head >
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>区域电力市场预测分析平台</title>
    <link rel="stylesheet" type="text/css" href="/PowerInfo/static/js/jquery-easyui-1.4/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="/PowerInfo/static/js/jquery-easyui-1.4/themes/icon.css" />
    <script type="text/javascript" src="/PowerInfo/static/js/jquery-easyui-1.4/jquery-1.8.3.js"></script>
    <script type="text/javascript" src="/PowerInfo/static/js/jquery-easyui-1.4/jquery.easyui.min.js"></script>
	<script type="text/javascript" src='/PowerInfo/js/menu/outlook.js'> </script>

	<%String path = request.getContextPath(); %>
	<script type="text/javascript" src="<%=path%>/static/js/common/common_page.js"></script>
	<%@include file="common/commonDefineBtn.jsp" %>

    <script type="text/javascript">
    var path='<%=path%>';
var _menus = '';

        //设置登录窗口
        function openPwd() {
            $('#w').window({
                title: '修改密码',
                width: 300,
                modal: true,
                shadow: true,
                closed: true,
                height: 160,
                resizable:false
            });
        }
        //关闭登录窗口
        function closePwd() {
            $('#w').window('close');
        }

        

        $(function() {

            openPwd();

            $('#editpass').click(function() {
                $('#w').window('open');
            });

            $('#btnEp').click(function() {
                serverLogin();
            })

			$('#btnCancel').click(function(){closePwd();})

            $('#loginOut').click(function() {
                $.messager.confirm('系统提示', '您确定要退出本次登录吗?', function(r) {

                    if (r) {
                        location.href = '/ajax/loginout.ashx';
                    }
                });
            })
        });
		
		

    </script>
<Style>
.lll{
background-attachment:scroll;
background-repeat:no-repeat;
background-size:auto;
background-origin:padding-box;
background-clip:border-box;
vertical-align:top;
}

#css3menu{
list-style:none;
margin:0;
padding:0; 
box-sizing:border-box;


}

#css3menu  li{
height:78px;
float:left;
margin:0;
padding:0;
width:90px;
box-sizing:border-box;
}
a{
display:inline-block;
text-decoration:none;
}
</style>
</head>
<body class="easyui-layout" style="overflow-y: hidden"  scroll="no">

    <div region="north" border="false" style="overflow: hidden; height: 78px;
        background: #65B292;
        line-height: 20px;color: #3385FF; font-family: Verdana, 微软雅黑,黑体">
     <div  style="float:left;width:23%;min-width:240px;height:100%; ">
         <span  style="float:left;margin:0;padding:0"><h1 style="color:white">区域电力市场预测分析平台</h1></span>
	</div>
	<div  style="float:left;width:55%;height:100%;text-align:center;">
		<ul id="css3menu">
				<li ><a name="basic" href="#">
				<span class="lll"><img src="static/images/4.jpg" ></span>
				<span style="display: block;margin-top: 0px"> 基 础 数 据 </span></a></li>
				<li style="float:left;margin:0;padding:0;">
				<a name="totalQuantity" href="#">
					<span  class="lll"><img src="static/images/2.jpg"></img></span>
					<span style="display: block;margin-top: 0px">电力需求预测</span>
				</a></li>
				<li style="float:left;margin:0;padding:0;">
				<a name="balance" href="#">
					<span  class="lll"><img src="static/images/3.jpg"></img></span>
					<span style="display: block;margin-top: 0px">电力电量平衡</span>
				</a></li>
				<li style="float:left;margin:0;padding:0;">
				<a name="station" href="#">
					<span  class="lll"><img src="static/images/1.gif"></img></span>
					<span style="display: block;margin-top: 0px">区域电厂竞争力</span>
				</a></li>
		</ul>
	</div>
	<div  style="float:right;width:21%;min-width:225px;height:100%;">
	<a href="#" id="editpass">修改密码</a> <a href="#" id="loginOut">安全退出</a>
	</div>
    </div>
    <div region="south" split="true" style="height: 30px; background: #D2E0F2; ">
        <div class="footer"></div>
    </div>
    <div region="west" hide="true" split="true" title="导航菜单" style="width:180px;" id="west">
		<div id='wnav' class="easyui-accordion" fit="true" border="false">
				<ul id='tt'></ul>
		<!--  导航内容 -->
				
		</div>

    </div>
    <div id="mainPanle" region="center" style="background: #eee; overflow-y:hidden">
        <div id="tabs" class="easyui-tabs"  fit="true" border="false" >
			<div title="欢迎使用" style="padding:20px;overflow:hidden;" id="home">
				
			<h1 style="margin:0 auto; text-align:center">区域电力市场预测分析平台!</h1>

			</div>
		</div>
    </div>
    <div id="mm" class="easyui-menu" style="width:180px;">
		<div id="mm-tabupdate">刷新</div>
		<div class="menu-sep"></div>
		<div id="mm-tabcloseall">全部关闭</div>
		<div id="mm-tabcloseother">除此之外全部关闭</div>
		<div class="menu-sep"></div>
		<div id="mm-tabcloseright">当前页右侧全部关闭</div>
		<div id="mm-tabcloseleft">当前页左侧全部关闭</div>
		<div class="menu-sep"></div>
		<div id="mm-exit">退出</div>
	</div>

	<div id="treemm" class="easyui-menu" style="width:140px;">
		<div onclick="append()"  iconcls="icon-add">添加节点</div>
		<div onclick="update()" iconcls="icon-edit">修改节点</div>
		<div onclick="remove()"iconcls="icon-remove">删除节点</div>
		<div id="addyear" onclick="addyear()"iconcls="icon-add">添加年份</div>
	</div>


</body>
</html>