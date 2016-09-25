<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>机组</title>
<meta http-equiv="content-type" content="text/html;charset=UTF-8" />
<!--引入此文件包含jquery_easyui的css样式与公用js以及登录用户信息-->
<%@include file="../common/commonInclude.jsp"%>
<%@include file="../common/commonDefineBtn.jsp" %>
<% 
String id = request.getParameter("id")==null ?"" : request.getParameter("id"); 
%>
<script type="text/javascript">
	var id='<%=id%>' ;
	$(function() {
	

		//加载选项卡页面
		var srcs = {
				'表' : path+'/generatorContrastController/index?id='+id,
				'图' : path+'/generatorContrastController/image?id='+id
		};
		$('#iframe0').attr('src', srcs['表']);
		$('#tt').tabs({
			fitColumns : true,
			height : 450,
			tabHeight:32,
			onSelect : function(title, index){
				$('#iframe' + index).attr('src', srcs[title]);
			}
		});
	});

	
</script>
</head>
<body>
	<!-- 选项卡 -->	
<div id="datagrid_div"  class="easyui-layout" style="width:0px;height:0px;overflow-y:hidden">
	<div id="tt" class="easyui-tabs" data-options="tools:'#tab-tools'" >
		<div title="表"  id="p1">
			<iframe id='iframe0' scrolling="auto" frameborder="0"  src="" style="width:100%;height:98%;"></iframe>
		</div>
		<div title="图" id="p2" >
			<iframe id='iframe1' scrolling="auto" frameborder="0"  src="" style="width:100%;height:98%;"></iframe>
		</div>
		
	</div>
</div>

</body>