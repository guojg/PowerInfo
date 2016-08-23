<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.github.balance.task.entity.BalanceTask"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="content-type" content="text/html;charset=UTF-8" />
<title>外购外送新增</title>
</head>
<%@include file="../../common/commonInclude.jsp"%>
<% 
		BalanceTask tt=  (BalanceTask)request.getSession().getAttribute("balancetask");
		String taskid = tt.getId();
		%>
<script type="text/javascript">
var taskid='<%=taskid%>';
$(function() {
	comboBoxInit({
		id : "pro_type",
		url : path + '/sendData/gettypes',
		textkey : "name",
		valuekey : "code"	
		});

}
);
	//取消
	function cancel() {
		//关闭窗口
		window.parent.$('#win_div').window('close');
	}
	function save() {
		var operationdata = new Object();
		operationdata["pro_name"] = $('#pro_name').val();
		operationdata["pid"] = $('#pro_type').datebox('getValue');
		operationdata["task_id"] = taskid;
		var param = {
			"data" : JSONH.stringify(operationdata)
		};
		$.ajax({
			type : "post",
			url : path + '/sendData/addProData',
			data : param,
			success : function(obj) {
				if (obj == '1') {
					window.parent.$.messager.alert('提示', '创建成功！', 'info',
							function() {
								//关闭窗口
								window.parent.queryData();
							});
					window.parent.$('#win_div').window('close');
				} else {
					$.messager.parent.alert('提示', '创建失败！', 'info');
					//关闭窗口
					window.parent.$('#win_div').window('close');
				}
			}
		});
	}
</script>
<body>
	<form id="paramsForm">
		<table id="detailTable">
			<tr>
				<td class="tdlft" style='width: 100px'>项目类型：</td>
				<td class="tdrgt" style='width: 120px'><input id="pro_type"
					type="text" style='width: 120px' /></td>
			</tr>
			<tr>
				<td class="tdlft" style='width: 100px'>项目名称：</td>
				<td class="tdrgt" style='width: 120px'><input id="pro_name"
					type="text" style='width: 120px'/></td>
			</tr>
		</table>
	</form>
	<div class="div_submit">
		<a id="btn_save" href="javascript:save();"><img
			src="/PowerInfo/static/images/save.gif" border="0"
			style="vertical-align: middle"></a> <a id="btn_cancel"
			href="javascript:cancel();"><img
			src="/PowerInfo/static/images/quxiao.gif" border="0"
			style="vertical-align: middle"></a>
	</div>
</body>
</html>