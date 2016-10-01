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
var area_id="1";
$(function() {
	fzData();
}
);
//赋值
function fzData(){
	var rows = window.parent.$('#datagrid').datagrid('getChecked');
	var item=rows[0];
	
	//赋值开始
	$("#detailTable input").each(function(){
		var attrName = $(this).attr('id');//id
  	if(attrName!=null && attrName!=""){
  		var upperCaseAttrName = attrName;
  		var clas= $(this).attr('class');//用于区分是否是combo
  		if(clas!=null && clas!=""){
  			if(clas.indexOf("comboboxComponent")>=0){//combobox
          		$(this).combobox('setValue',item[upperCaseAttrName]);
          	}else if(clas.indexOf("easyui-datebox")>=0){
          		$(this).datebox('setValue',item[upperCaseAttrName]);
          	}
  		}else{
  			$(this).val(item[upperCaseAttrName]);//普通文本
  		}
      }
	});
}
//取消
function cancel(){
	//关闭窗口
	window.parent.$('#win_div').window('close');
}
function save(){
	var formData = new Object();
	//取值
	$("#detailTable input").each(function(){
		var attrName = $(this).attr('id');//id
  	if(attrName!=null && attrName!=""){
  		var upperCaseAttrName = attrName;
  		var clas= $(this).attr('class');//用于区分是否是combo
  		if(clas!=null && clas!=""){
  			if(clas.indexOf("comboboxComponent")>=0){//combobox
          		_value=$(this).combobox('getValue');
  				formData[attrName]=_value;
          	}else if(clas.indexOf("easyui-datebox")>=0){
          		_value=$(this).datebox('getValue');
          		formData[attrName]=_value;
          	}
  		}else{
  			_value=$(this).val();//普通文本
  			formData[attrName]=_value;
  		}
      }
	});
	//if(!validate(operationdata)){
	//	return;
	//}
	var param={"editObj":JSONH.stringify(formData),"area_id":area_id};
	$.ajax({
		  type: "post",
		  url: path + '/plantAnalysis/updateRecord',
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
				<td class="tdlft" style='width: 100px'>电厂名称：</td>
				<td class="tdrgt" style='width: 120px'><input id="plant_name"
					type="text" style='width: 120px' /></td>
				<td class="tdlft" style='width: 100px'>装机容量：</td>
				<td class="tdrgt" style='width: 120px'><input id="plant_capacity"
					type="text" style='width: 120px' /></td>
				<td class="tdlft" style='width: 100px'>发电量：</td>
				<td class="tdrgt" style='width: 120px'><input id="generating_capatity"
					type="text" style='width: 120px'/></td>
			</tr>
			<tr>
				<td class="tdlft" style='width: 100px'>电厂损耗：</td>
				<td class="tdrgt" style='width: 120px'><input id="plant_loss"
					type="text" style='width: 120px' /></td>
				<td class="tdlft" style='width: 100px'>初始投资：</td>
				<td class="tdrgt" style='width: 120px'><input id="start_outlay"
					type="text" style='width: 120px' /></td>
				<td class="tdlft" style='width: 100px'>投产年：</td>
				<td class="tdrgt" style='width: 120px'><input id="product_year"
					type="text" style='width: 120px' /></td>
			</tr>	
			<tr>
				<td class="tdlft" style='width: 100px'>经济运行寿命：</td>
				<td class="tdrgt" style='width: 120px'><input id="economical_life"
					type="text" style='width: 120px' /></td>
				<td class="tdlft" style='width: 100px'>期望收益率：</td>
				<td class="tdrgt" style='width: 120px'><input id="equired_return"
					type="text" style='width: 120px' /></td>
				<td class="tdlft" style='width: 100px'>年财务成本：</td>
				<td class="tdrgt" style='width: 120px'><input id="financial_cost"
					type="text" style='width: 120px' /></td>
			</tr>	
			<tr>
				<td class="tdlft" style='width: 100px'>发电煤耗：</td>
				<td class="tdrgt" style='width: 120px'><input id="generation_coal"
					type="text" style='width: 120px' /></td>
				<td class="tdlft" style='width: 100px'>运行维护费率：</td>
				<td class="tdrgt" style='width: 120px'><input id="operation_rate"
					type="text" style='width: 120px' /></td>
				<td class="tdlft" style='width: 100px'>运行维护成本：</td>
				<td class="tdrgt" style='width: 120px'><input id="operation_cost"
					type="text" style='width: 120px' /></td>
			</tr>	
			<tr>
				<td class="tdlft" style='width: 100px'>燃料单位成本：</td>
				<td class="tdrgt" style='width: 120px'><input id="unit_cost"
					type="text" style='width: 120px' /></td>
				<td class="tdlft" style='width: 100px'>电厂材料费（元/年）：</td>
				<td class="tdrgt" style='width: 120px'><input id="materials_cost"
					type="text" style='width: 120px' /></td>
				<td class="tdlft" style='width: 100px'>工资、奖金及福利费（元/年）：</td>
				<td class="tdrgt" style='width: 120px'><input id="salary"
					type="text" style='width: 120px' /></td>
			</tr>	
			<tr>
				<td class="tdlft" style='width: 100px'>修理费（元/年）：</td>
				<td class="tdrgt" style='width: 120px'><input id="repairs_cost"
					type="text" style='width: 120px' /></td>
				<td class="tdlft" style='width: 100px'>其他费用（元/年）：</td>
				<td class="tdrgt" style='width: 120px'><input id="other_cost"
					type="text" style='width: 120px' /></td>
			</tr>	
			<input type="hidden" id="id"/>
 		</table>
	</form>
	<div class="div_submit">
		<a id="btn_save"   href="javascript:save();" ><img src="/PowerInfo/static/images/save.gif" border="0" style="vertical-align: middle"></a>
		<a id="btn_cancel" href="javascript:cancel();" ><img src="/PowerInfo/static/images/quxiao.gif" border="0" style="vertical-align: middle"></a>
	</div>
</body>
</html>