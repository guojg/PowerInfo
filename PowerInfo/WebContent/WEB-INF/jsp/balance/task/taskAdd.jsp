<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<%@include file="../../common/commonInclude.jsp" %>	
<script type="text/javascript" src="<%=path %>/js/totalquantity/common/my-validatebox-ext.js"></script>
<%
String id=request.getParameter("id")==null?"":request.getParameter("id");
%>
<script type="text/javascript"> 
var id='<%=id%>';
$(function() {
	  $("#task_name").validatebox({
           required: true,
           novalidate: true,
           validType: ['checkText','maxLength[25]'],
           missingMessage: '任务名不能为空。',
           invalidMessage: '任务名称的输入长度不能超过25个汉字。'
       });
	  comboBoxInit({
			id : "year",
			url : path + '/balancetask/getyears',
			textkey : "yearName",
			valuekey : "year",
			multiple : true
		});
	  if(id!=""){
		  initData();
	  }
});
function initData(){
	 var url=path+"/balancetask/initData?id="+id;
	   commonHelper.updInit(url);
}
 function save(){
	 var task_name = $('#task_name').val();
	 var year =$('#year').combobox('getValues').join(",");
	 var param = {
		'task_name':task_name,
		'year':year,
		'id':id
	 };
	 if(!validate(param)){
		 return ;
	 }
		 $.ajax({
				type : 'POST',
				async : false,
				dataType: 'json',
				data: param,
				url :  '/PowerInfo/balancetask/saveData',
				success : function(data) {
					window.parent.$.messager.alert('提示','保存成功！','info');
					//关闭窗口
					window.parent.$('#win_div').window('close');
					window.parent.$('#datagrid').datagrid('reload');
				
				}
			});

 }
 function validate(param){
	 var flag=true;
	 var resultNmame="";
	 $('.validatebox-text').each(function(i,obj){
			$(this).validatebox('enableValidation').validatebox('validateTip');
		});
		var tipsStr = $("#validateMessage").html();
		var tipsArr = tipsStr.split(",");
		if(tipsArr[1]!=null){
			resultNmame=tipsArr[1];
		}
		
		
		var flagV=$('#detailTable').form('validate');
		if(flagV==false){
			flag=flagV;
		}
	
	$('#validateMessage').html(resultNmame);
	 return flag ;

		
 }
 
</script>
</head>
<body>
<form id="paramsForm">

	<table id="detailTable">
			<tr>
				<td class="tdlft">任务名称：</td>
				<td ><input id="task_name" name="task_name" type="text"  /></td>	
			</tr>
			<tr>
				<td  class="tdlft">水平年：</td>
				<td ><input id="year" name="year" type="text" class="comboComponentarr"  /></td>	
			</tr>
			<tr>
			<td colspan="2"><span>提示信息：</span><span style="color:red;" id="validateMessage"></span></td>
			</tr>
		
			
	</table> 
	<div class="div_submit" >
			<a id="tool_save" href="javascript:save();" > <img src='<%=path%>/static/images/save.gif'
			align='top' border='0' title='保存' /></a>
		</div> 
		</form>
</body>
</html>