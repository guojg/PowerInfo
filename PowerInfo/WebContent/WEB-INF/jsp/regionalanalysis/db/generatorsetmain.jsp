<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.github.regionalanalysis.db.task.entity.DbTask"%>

<!DOCTYPE html>
<html>
<head>
<title>单一机组</title>
<meta http-equiv="content-type" content="text/html;charset=UTF-8" />
<!--引入此文件包含jquery_easyui的css样式与公用js以及登录用户信息-->
<%@include file="../../common/commonInclude.jsp"%>
<%@include file="../../common/commonDefineBtn.jsp" %>
<% 
String id = request.getParameter("id")==null ?"" : request.getParameter("id"); 
String plant_id = request.getParameter("plant_id")==null ?"" : request.getParameter("plant_id"); 

DbTask tt=  (DbTask)request.getSession().getAttribute("dbtask");
String taskid = tt.getId();
String task_name = tt.getTask_name();

%>
<script type="text/javascript">
	var id='<%=id%>' ;
	var task_id='<%=taskid%>';
	var plant_id='<%=plant_id%>';

	$(function() {
	

		//加载选项卡页面
		var srcs = {
				'基本信息' : path+'/constantCostDbArgController/index?id='+id+'&task_id='+task_id+'&plant_id='+plant_id,
				'燃煤成本' : path+'/coalCostdb/main?fdj_id='+id+'&task_id='+task_id+'&plant_id='+plant_id
		};
		$('#iframe0').attr('src', srcs['基本信息']);
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
		<div title="基本信息"  id="p1">
			<iframe id='iframe0' scrolling="auto" frameborder="0"  src="" style="width:100%;height:98%;"></iframe>
		</div>
		<div title="燃煤成本" id="p2" >
			<iframe id='iframe1' scrolling="auto" frameborder="0"  src="" style="width:100%;height:98%;"></iframe>
		</div>
		
	</div>
</div>

</body>
</html>