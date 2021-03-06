<%@ page pageEncoding="UTF-8" language="java" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<%
String flag=request.getParameter("page")==null?"":request.getParameter("page").toString();
%>
<html>
 <head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>区域电力市场预测分析平台</title>
    <link rel="stylesheet" type="text/css" href="/PowerInfo/static/js/jquery-easyui-1.4/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="/PowerInfo/static/js/jquery-easyui-1.4/themes/icon.css" />
    <script type="text/javascript" src="/PowerInfo/static/js/jquery-easyui-1.4/jquery-1.8.3.js"></script>
    <script type="text/javascript" src="/PowerInfo/static/js/jquery-easyui-1.4/jquery.easyui.min.js"></script>
	<script type="text/javascript" src='/PowerInfo/js/menu/outlook.js'> </script>
	<script type="text/javascript" src="/PowerInfo/static/js/common/common_page.js"></script>
   	<%String path = request.getContextPath(); %>
   	<script type="text/javascript">
   	var path="<%=path%>";
   	var flag="<%=flag%>";
   	var role="";
   	var _menus = "";
   	</script>
	<%@include file="common/commonDefineBtn.jsp" %>
<style>
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

#io{
background:url(/PowerInfo/static/images/main-bg.png);

}

#css3menu  li{
height:78px;
float:left;
margin:0;
padding:0;
width:120px;
box-sizing:border-box;
}
a{
display:inline-block;
text-decoration:none;
}
a:link{
color:black;
}
</style>
</head>
<body class="easyui-layout" style="overflow-y: hidden" id="contentboby">

    <div id="io" region="north" border="false" style="overflow: hidden; height: 45px;
       font-family: Verdana, 微软雅黑,黑体">
<div  style="float:left;width:24%;height:100%; vertical-align: middle;">
	     	 <h2 style="color:#7cbcd6;width:100%;color: #fafafa;letter-spacing: 0; margin-top:5px;
text-shadow: 0px 1px 0px #999, 0px 2px 0px #888, 0px 3px 0px #777, 0px 4px 0px #666, 0px 5px 0px #555, 0px 6px 0px #444, 0px 7px 0px #333, 0px 8px 7px #001135" >区域电力市场预测分析平台</h2>
	     </div>
		<div  style="float:left;width:54%;height:100%;text-align:center;">
			<ul id="css3menu">
					<li >
						<a name="basic" href="#">
						<span style="display: block;margin-top: 7px;font-size: medium;"><b>市场信息数据</b> </span>
						</a>
					</li>
					<li style="float:left;margin:0;padding:0;">
						<a name="totalQuantity" href="#">
							<span style="display: block;margin-top: 7px;font-size: medium;"><b>电力需求预测</b></span>
						</a>
					</li>
					<li style="float:left;margin:0;padding:0;">
						<a name="balance" href="#">
							<span style="display: block;margin-top: 7px;font-size: medium;"><b>电力电量平衡</b></span>
						</a>
					</li>
					<li style="float:left;margin:0;padding:0;">
						<a name="station" href="#">
							<span style="display: block;margin-top: 7px;font-size: medium;"><b>电厂竞争力分析</b></span>
						</a>
					</li>
					<li style="float:left;margin:0;padding:0;">
						<a name="elecplant" href="#">
							<span style="display: block;margin-top: 7px;font-size: medium;"><b>电厂竞价预测</b></span>
						</a>
					</li>
			</ul>
		</div>
		<input type="hidden" id="fullscreenbtn">
		<div  style="float:right;width:21%;min-width:225px;height:100%;text-align: right;margin-top: 12px">
			<a href="#" id="editpass"><b>修改密码</b></a> 
			<a href="#" id="loginOut"><b>安全退出</b></a>
		</div>
    </div>
    <div region="south" split="true" style="height: 30px; background: #D2E0F2; ">
        <div class="footer"></div>
    </div>
    <div region="west" hide="true" split="true" title="导航菜单" style="width:220px;background:#EDF1FA" id="west">
		<ul id='tt'></ul>
		<!--  导航内容 -->
				
    </div>
    <div id="mainPanle" region="center" style="background: #eee; overflow-y:hidden">
        <div id="tabs" class="easyui-tabs"  fit="true" border="false" >
		 <!--	<div title="欢迎使用" style="padding:20px;overflow:hidden;background:#EDF1FA;text-align: center;" id="home">
				
			<h1 style="color:#7cbcd6;width:100%;color: #fafafa;letter-spacing: 0;margin-top:240px;
			text-shadow: 0px 1px 0px #999, 0px 2px 0px #888, 0px 3px 0px #777, 0px 4px 0px #666, 0px 5px 0px #555, 0px 6px 0px #444, 0px 7px 0px #333, 0px 8px 7px #001135">区域电力市场预测分析平台!</h1>

			</div>-->
		</div>
    </div>
    <!--修改密码窗口-->
    <div id="w" class="easyui-window" title="修改密码" collapsible="false" minimizable="false"
        maximizable="false" icon="icon-save"  style="width: 300px; height: 150px; padding: 5px;
        background: #fafafa;">
        <div class="easyui-layout" fit="true">
            <div region="center" border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
                <table cellpadding=3>
                    <tr>
                        <td>新密码：</td>
                        <td><input id="txtNewPass" type="Password"  required="required"  class="txt01" /></td>
                    </tr>
                    <tr>
                        <td>确认密码：</td>
                        <td><input id="txtRePass" type="Password"  required="required"   class="txt01" /></td>
                    </tr>
                </table>
            </div>
            <div region="south" border="false" style="text-align: right; height: 35px; line-height: 35px;">
                <a id="btnEp" class="easyui-linkbutton" icon="icon-ok" href="javascript:void(0)" >
                    确定</a> <a id="btnCancel" class="easyui-linkbutton" icon="icon-cancel" href="javascript:void(0)">取消</a>
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
		<div onclick="append()"  iconcls="icon-add">添加指标</div>
		<div onclick="update()" iconcls="icon-edit">修改指标</div>
		<div onclick="removenode()"iconcls="icon-remove">删除指标</div>
		<div id="addyear" onclick="addyear()"iconcls="icon-add">年份管理</div>
		<div id="operationunit" onclick="operationunit()"iconcls="icon-add">单位维护</div>
	</div>


</body>
</html>