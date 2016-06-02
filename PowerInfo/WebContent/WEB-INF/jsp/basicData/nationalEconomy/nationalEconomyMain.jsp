<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>国民经济发展概况</title>
<meta http-equiv="content-type" content="text/html;charset=UTF-8" /> 
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
	
<!--引入此文件包含jquery_easyui的css样式与公用js以及登录用户信息-->
<%@include file="../../common/commonInclude.jsp" %>	

<script type="text/javascript">
$( function() {
	$(window).bind('resize', function(event) {
		$("#tt").tabs('resize');
	});
	//加载选项卡页面
	var srcs = {
			'表' : path+'/nationalEconomy/table',
			'图' : path+'/nationalEconomy/image'
	};
	var Height_Page = $("html").height();
	var datagrid_height = $("#datagrid_div").position().top;
	var height= Height_Page-datagrid_height;
	$('#iframe0').attr('src', srcs['表']);
	$('#tt').tabs({
		fitColumns : true,
		height : height,
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
<div id="datagrid_div"  class="easyui-layout">
	<div id="tt" class="easyui-tabs" data-options="tools:'#tab-tools'">
		<div title="表"  id="p1">
			<iframe id='iframe0' scrolling="auto" frameborder="0"  src="" style="width:100%;height:100%;"></iframe>
		</div>
		<div title="图" id="p2" >
			<iframe id='iframe1' scrolling="auto" frameborder="0"  src="" style="width:100%;height:100%;"></iframe>
		</div>
	</div>
</div>
</body>
</html>		