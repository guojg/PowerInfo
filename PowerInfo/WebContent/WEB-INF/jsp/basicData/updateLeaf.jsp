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
	var node = window.parent.$('#tt').tree('getSelected');
	$('#name').val(node.text);
	
}
);
//取消
function cancel(){
	//关闭窗口
	window.parent.$('#win_div').window('close');
}
function save(){
	var node = window.parent.$('#tt').tree('getSelected');
	var operationdata = new Object();
	operationdata["id"]=node.id;
	var name =$('#name').val();
	operationdata["name"]=name;//节点名称
	
	node.text=name;
	
	var param={"data":JSONH.stringify(operationdata)};
	$.ajax({
		  type: "post",
		  url: path + '/basicData/updateleaf',
		  data:param,
		  success:function(obj){
			  if(obj.flag=='1'){
				  window.parent.$.messager.alert('提示','修改成功！','info',function(){
						//关闭窗口
						window.parent.$('#tt').tree('update',node);
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
				<td class="tdlft" style='width: 50px'>指标：</td>
				<td class="tdrgt" style='width: 120px'><input id="name"
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