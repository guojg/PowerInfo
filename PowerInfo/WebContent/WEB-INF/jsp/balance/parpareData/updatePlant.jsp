<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="content-type" content="text/html;charset=UTF-8" /> 
<title>修改电厂</title>
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
	  comboBoxInit({
			id : "area_id",
			url : path + '/sysdict/getCompany',
			textkey : "value",
			valuekey : "code",
			multiple : false
		});
	 comboBoxInit({
			id : "cooling_type",
			url : path + '/sysdict/getDataByCodeValue?domain_id=302',
			textkey : "value",
			valuekey : "code"
			});
	  $("#index_item").combo("readonly",true);
		 $("#index_item + span > input :eq(0)").css("background-color","gray");
		 $("#cooling_type").combo("readonly",true);
		 $("#cooling_type + span > input :eq(0)").css("background-color","gray");
	var rows = window.parent.$('#datagrid').datagrid('getChecked');
	var row=rows[0];
	$('#plant_name').val(row.plant_name);
	$('#plant_capacity').val(row.plant_capacity);
	//$('#start_date').datebox("setValue",row.start_date);
	//$('#end_date').datebox("setValue",row.end_date);
	$('#index_item').combobox("setValue",row.index_item);
	$('#area_id').combobox("setValue",row.area_id);
	$('#cooling_type').combobox("setValue",row.cooling_type);
	$('#organ').val(row.organ);


}
);
//取消
function cancel(){
	//关闭窗口
	window.parent.$('#win_div').window('close');
}
function save(){
	var row = window.parent.$('#datagrid').datagrid('getChecked');
	var operationdata = new Object();
	operationdata["id"]=row[0].id;
	operationdata["plant_name"]=$('#plant_name').val();
	operationdata["plant_capacity"]=$('#plant_capacity').val();
	//operationdata["start_date"]=$('#start_date').datebox('getValue');
	//operationdata["end_date"]=$('#end_date').datebox('getValue');
	operationdata["index_item"]=$('#index_item').datebox('getValue');
	operationdata["area_id"]=$('#area_id').datebox('getValue');
	operationdata["cooling_type"]=$('#cooling_type').combo('getValue');
	operationdata["organ"]=$('#organ').val();
	if(!validate(operationdata)){
		return;
	}
	var param={"editObj":JSONH.stringify(operationdata)};
	$.ajax({
		  type: "post",
		  url: path + '/electricPowerPlant/updateRecord',
		  data:param,
		  success:function(obj){
			  if(obj=='1'){
				  window.parent.$.messager.alert('提示','修改成功！','info',function(){
						//关闭窗口
						window.parent.queryData();
						});
				  window.parent.$('#win_div').window('close');
			  }else{
					$.messager.alert('提示','修改失败！','info');
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
				<td class="tdlft" style='width: 120px'>电厂名称：</td>
				<td class="tdrgt" style='width: 180px'><input id="plant_name"
					type="text" style='width: 180px' /></td>
				<td class="tdlft" style='width: 120px'>装机容量(万千瓦)：</td>
				<td class="tdrgt" style='width: 180px'><input id="plant_capacity"
					type="text" style='width: 180px' /></td>
			</tr>
			<tr>
				<td class="tdlft" style='width: 120px'>电源类型：</td>
				<td class="tdrgt" style='width: 180px'><input id="index_item"
					type="text" style='width: 180px'/></td>
					<td class="tdlft" style='width: 180px'>冷却类型：</td>
				<td class="tdrgt" style='width: 180px'><input class="comboboxComponent" id="cooling_type"
					type="text" style='width: 180px' /></td>
			

			</tr>	
			<tr>
				<td class="tdlft" style='width: 120px'>所属地区：</td>
				<td class="tdrgt" style='width: 180px'><input id="area_id"
					type="text" style='width: 180px' /></td>
						<td class="tdlft" style='width: 120px'>所属企业：</td>
				<td class="tdrgt" style='width: 180px'><input id="organ"
					type="text" style='width: 180px' /></td>
			</tr>
		</table>
	</form>
	<div class="div_submit">
		<a id="btn_save"   href="javascript:save();" ><img src="/PowerInfo/static/images/save.gif" border="0" style="vertical-align: middle"></a>
		<a id="btn_cancel" href="javascript:cancel();" ><img src="/PowerInfo/static/images/quxiao.gif" border="0" style="vertical-align: middle"></a>
	</div>
</body>
</html>