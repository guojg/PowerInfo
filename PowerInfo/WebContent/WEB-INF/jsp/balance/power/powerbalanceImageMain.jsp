<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>图形分析</title>
<meta http-equiv="content-type" content="text/html;charset=UTF-8" /> 
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
	
<!--引入此文件包含jquery_easyui的css样式与公用js以及登录用户信息-->
<%@include file="../../common/commonInclude.jsp" %>	

<script type="text/javascript">
$( function() {
	var tablepath="";
	var imagepath="";
	//加载选项卡页面
	
		tablepath=path+'/powerbalance/powerbalanceImage1';
		imagepath= path+'/powerbalance/powerbalanceImage2';
	var srcs = {
			'装机盈余' : tablepath,
			'年底装机容量' :imagepath
	};
	var Height_Page = $(document).height();
	var datagrid_height = $("#datagrid_div").position().top;
	var height= Height_Page-datagrid_height;

	$('#iframe0').attr('src', srcs['装机盈余']);
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
<div id="datagrid_div"  class="easyui-layout" style="overflow-y:hidden">
	<div id="tt" class="easyui-tabs" data-options="tools:'#tab-tools'" >
		<div title="装机盈余"  id="p1">
			<iframe id='iframe0' scrolling="auto" frameborder="0"  src="" style="width:100%;height:98%;"></iframe>
		</div>
		<div title="年底装机容量" id="p2" >
			<iframe id='iframe1' scrolling="auto" frameborder="0"  src="" style="width:100%;height:98%;"></iframe>
		</div>
	</div>
</div>
</body>
</html>		