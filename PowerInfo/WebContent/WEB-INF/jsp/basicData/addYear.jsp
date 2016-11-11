<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="content-type" content="text/html;charset=UTF-8" /> 
<title>Insert title here</title>
</head>
<%@include file="../common/commonInclude.jsp" %>	
<script type="text/javascript">
$(function() {
	$.ajax({
		  type: "post",
		  url: path + '/basicData/getyears',
		  success:function(msg){
			 	if(typeof(msg)!="undefined"&&msg!=null)
				$("#startyear").val(msg[0].year);
				$("#endyear").val(msg[msg.length-1].year);
			}
		});
});
//取消
function cancel(){
	//关闭窗口
	window.parent.$('#win_div').window('close');
}
function validate(startyear,endyear){
	var regExp=/19|20\d{2}/;
	if(!regExp.test(startyear)){
		window.parent.$.messager.alert('提示','开始年份填写错误！','info');
		return false;
	}
	if(!regExp.test(endyear)){
		window.parent.$.messager.alert('提示','终止年份填写错误！','info');
		return false;
	}
	if(startyear>=endyear){
		window.parent.$.messager.alert('提示','终止年份必须要大于开始年份！','info');
		return false;
	}
	
	
	return true;
	
}
function save(){
	var startyear=$("#startyear").val();
	var endyear=$("#endyear").val();
	if(!(validate(startyear,endyear))){
		return;
	}
	var operationdata = new Object();
	operationdata["startyear"]=startyear;
	operationdata["endyear"]=endyear;

	
	
	var param={"data":JSONH.stringify(operationdata)};
	$.ajax({
		  type: "post",
		  url: path + '/basicData/addyear',
		  data:param,
		  success:function(msg){
			  var obj=$.parseJSON(msg);
			  if(obj.flag=='1'){
				 
				  window.parent.$.messager.alert('提示','创建成功！','info');
				  window.parent.$('#win_div').window('close');
			  }else{
					$.messager.alert('提示','创建失败！','info');
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
				<td class="tdlft" style='width: 70px'>开始年份：</td>
				<td class="tdrgt" style='width: 120px'><input id="startyear"
					type="text" style='width: 120px' /></td>
			</tr>
			<tr>
				<td class="tdlft" style='width: 70px'>终止年份：</td>
				<td class="tdrgt" style='width: 120px'><input id="endyear"
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