<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="content-type" content="text/html;charset=UTF-8" /> 
<title>增加电厂</title>
</head>
<%@include file="../../common/commonInclude.jsp" %>
<%
	Object obj=request.getSession().getAttribute("maparea");
	String organCode="";
	if(obj!=null){
		organCode=obj.toString();
	}
%>
<script type="text/javascript" src="<%=path%>/static/js/common/plantAnalysisUtil.js"></script>
<script type="text/javascript">
var area_id="<%=organCode%>";
$(function() {
	 comboBoxInit({
			id : "power_type",
			url : path + '/sysdict/getDataByCodeValue?domain_id=12',
			textkey : "value",
			valuekey : "code"
			}).combobox( {
				onSelect : function(data) {
					if(data.code==3){
						$("#cooling_type").combo("readonly",false);
						$("#cooling_type + span > input :eq(0)").css("background-color","white");
					}else{
						$("#cooling_type + span > input :eq(0)").css("background-color","#bcbcbc");
						$("#cooling_type").combo("readonly",true);
						fzData(data.code);
					}
					}
				});

	 comboBoxInit({
			id : "cooling_type",
			url : path + '/sysdict/getDataByCodeValue?domain_id=302',
			textkey : "value",
			valuekey : "code"
			}).combobox( {
				onSelect : function(data) {
					var id=$("#power_type").combobox("getValue")+data.code
					fzData(id);
					}
				});
	//fzData();

}
);
//取消
function cancel(){
	//关闭窗口
	window.parent.$('#win_div').window('close');
}
//赋值
function fzData(id){
	var items=null;
	$.ajax({
		  type: "post",
		  url: path + '/plantAnalysis/queryTemplateData?id='+id,
		  dataType:'json',
		  async: false,
		  success:function(obj){
				items=obj.rows;
			}
		});
		$("#consumption_rate").val(items[0].value);
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
	debugger;
	if(!validate(formData)){
		return;
	}
	var param={"editObj":JSONH.stringify(formData),"area_id":area_id};
	$.ajax({
		  type: "post",
		  url: path + '/plantAnalysis/addRecord',
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
				<td class="tdlft" style='width: 180px'>电厂名称：</td>
				<td class="tdrgt" style='width: 120px'><input id="plant_name"
					type="text" style='width: 120px' /></td>
				<td class="tdlft" style='width: 180px'>装机总容量（WM）：</td>
				<td class="tdrgt" style='width: 120px'><input id="plant_capacity"
					type="text" style='width: 120px' /></td>
				<td class="tdlft" style='width: 180px'>开工年：</td>
				<td class="tdrgt" style='width: 120px'><input id="product_year"
					type="text" style='width: 120px'/></td>
			</tr>
			<tr>
				<td class="tdlft" style='width: 180px'>建成年：</td>
				<td class="tdrgt" style='width: 120px'><input id="build_year"
					type="text" style='width: 120px' /></td>
				<td class="tdlft" style='width: 180px'>静态投资（万元）：</td>
				<td class="tdrgt" style='width: 120px'><input id="start_outlay"
					type="text" style='width: 120px' /></td>
				<td class="tdlft" style='width: 180px'>电源类型：</td>
				<td class="tdrgt" style='width: 120px'><input class="comboboxComponent" id="power_type"
					type="text" style='width: 120px' /></td>
			</tr>	
			<tr>
				<td class="tdlft" style='width: 180px'>冷却类型：</td>
				<td class="tdrgt" style='width: 120px'><input class="comboboxComponent" id="cooling_type"
					type="text" style='width: 120px' /></td>
				<td class="tdlft" style='width: 180px'>厂用电率：</td>
				<td class="tdrgt" style='width: 120px'><input id="consumption_rate"
					type="text" style='width: 120px;background-color:#bcbcbc;' disabled="disabled"/></td>
				<td class="tdlft" style='width: 180px'>厂用电量（千瓦时）：</td>
				<td class="tdrgt" style='width: 120px'><input id="electricity_consumption"
					type="text" style='width: 120px' /></td>
			</tr>	
			<tr>
				<td class="tdlft" style='width: 180px'>电厂材料费（元/年）：</td>
				<td class="tdrgt" style='width: 120px'><input id="materials_cost"
					type="text" style='width: 120px' /></td>
				<td class="tdlft" style='width: 180px'>工资、奖金及福利费（元/年）：</td>
				<td class="tdrgt" style='width: 120px'><input id="salary"
					type="text" style='width: 120px' /></td>
				<td class="tdlft" style='width: 180px'>修理费（元/年）：</td>
				<td class="tdrgt" style='width: 120px'><input id="repairs_cost"
					type="text" style='width: 120px' /></td>
			</tr>
			<tr>

				<td class="tdlft" style='width: 180px'>其他费用（元/年）：</td>
				<td class="tdrgt" style='width: 120px'><input id="other_cost"
					type="text" style='width: 120px' /></td>
			</tr>	
		</table>
	</form>
	<div class="div_submit">
		<a id="btn_save"   href="javascript:save();" ><img src="/PowerInfo/static/images/save.gif" border="0" style="vertical-align: middle"></a>
		<a id="btn_cancel" href="javascript:cancel();" ><img src="/PowerInfo/static/images/quxiao.gif" border="0" style="vertical-align: middle"></a>
	</div>
</body>
</html>