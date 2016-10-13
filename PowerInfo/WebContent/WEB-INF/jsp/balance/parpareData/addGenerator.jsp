<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="content-type" content="text/html;charset=UTF-8" /> 
<title>增加电厂</title>
</head>
<%@include file="../../common/commonInclude.jsp" %>
<script type="text/javascript" src="<%=path%>/static/js/common/plantUtil.js"></script>
<script type="text/javascript">
$(function() {
	 comboBoxInit({
			id : "index_item",
			url : path + '/sysdict/getDataByCodeValue?domain_id=12',
			textkey : "value",
			valuekey : "code"
			});

}
);
//取消
function cancel(){
	//关闭窗口
	window.parent.$('#win_div').window('close');
}

function save(){
	var operationdata = new Object();
	operationdata["plant_name"]=$('#plant_name').val();
	operationdata["plant_capacity"]=$('#plant_capacity').val();
	operationdata["start_date"]=$('#start_date').datebox('getValue');
	operationdata["end_date"]=$('#end_date').datebox('getValue');
	operationdata["index_item"]=$('#index_item').combo('getValue');
	
	if(!validate(operationdata)){
		return;
	}
	var param={"editObj":JSONH.stringify(operationdata)};
	$.ajax({
		  type: "post",
		  url: path + '/electricPowerPlant/addRecord',
		  data:param,
		  success:function(obj){
			  if(obj=='1'){
				 
				  window.parent.$.messager.alert('提示','创建成功！','info',function(){
						//关闭窗口
						window.parent.queryData();
						});
				  window.parent.$('#win_div').window('close');
			  }else{
					$.messager.parent.alert('提示','创建失败！','info');
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
				<td class="tdlft" style='width: 100px'>电厂名称：</td>
				<td class="tdrgt" style='width: 120px'><input id="plant_name"
					type="text" style='width: 120px' /></td>
				<td class="tdlft" style='width: 100px'>装机容量：</td>
				<td class="tdrgt" style='width: 120px'><input id="plant_capacity"
					type="text" style='width: 120px' /></td>
			</tr>
			<tr>
				<td class="tdlft" style='width: 100px'>电源类型：</td>
				<td class="tdrgt" style='width: 120px'><input id="index_item"
					type="text" style='width: 120px'/></td>
				<td class="tdlft" style='width: 100px'>投产日期：</td>
				<td class="tdrgt" style='width: 120px'><input id="start_date"
					type="text" style='width: 120px' class="easyui-datebox"/></td>

			</tr>	
			<tr>
				<td class="tdlft" style='width: 100px'>退役日期：</td>
				<td class="tdrgt" style='width: 120px'><input id="end_date"
					type="text" style='width: 120px' class="easyui-datebox"/></td>
			</tr>	
		</table>
	</form>
	<div class="div_submit">
		<a id="btn_save"   href="javascript:save();" ><img src="/PowerInfo/static/images/save.gif" border="0" style="vertical-align: middle"></a>
		<a id="btn_cancel" href="javascript:cancel();" ><img src="/PowerInfo/static/images/quxiao.gif" border="0" style="vertical-align: middle"></a>
	</div>
</body>
</html>