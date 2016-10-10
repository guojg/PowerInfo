<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<%@include file="../../../common/commonInclude.jsp" %>	
<script type="text/javascript" src="<%=path %>/js/totalquantity/common/my-validatebox-ext.js"></script>
<script type="text/javascript" src="<%=path %>/js/regionalanalysis/bnCodeCompany.js"></script>

<%
String id=request.getParameter("id")==null?"":request.getParameter("id");
Object obj=  request.getSession().getAttribute("maparea");
String organCode="";
if(obj != null) {
	organCode = obj.toString() ; 
}

%>
<script type="text/javascript"> 
var id='<%=id%>';
var organCode='<%=organCode%>';
$(function() {
		if(organCode==""){
			alert("请选择从地图中区域");
			return ;
		}
	  $("#task_name").validatebox({
           required: true,
           novalidate: true,
           validType: ['checkText','maxLength[25]'],
           missingMessage: '任务名不能为空。',
           invalidMessage: '任务名称的输入长度不能超过25个汉字。'
       });
	  comboBoxInit({
			id : "area_id",
			url : path + '/sysdict/getCompany',
			textkey : "value",
			valuekey : "code",
			multiple : false,
			defaultVal:organCode
		});
	  if(id!=""){
		  initData();
	  }
});
function initData(){
	 var url=path+"/taskfx/initData?id="+id;
	   //commonHelper.updInit(url);
	 $.ajax({
			type: "POST",
			url:url,
			dataType:"json",
			success:function(item){
				//赋值开始
				$('#task_name').val(item['TASK_NAME']);
				$('#area_id').combobox('setValue',item['AREA_ID']);
		 		
			}
		});
}
 function save(){
	 var task_name = $('#task_name').val();
	 var area_id = $('#area_id').combobox('getValue');
	 var param = {
		'task_name':task_name,
		'id':id,
		'area_id':area_id
	 };
	 if(!validate(param)){
		 return ;
	 }
	 //if(validate(param)){
		 $.ajax({
				type : 'POST',
				async : false,
				dataType: 'json',
				data: param,
				url :  '/PowerInfo/taskfx/saveData',
				success : function(data) {
					window.parent.$.messager.alert('提示','保存成功！','info');
					//关闭窗口
					window.parent.$('#win_div').window('close');
					window.parent.$('#datagrid').datagrid('reload');
				
				}
			});
	 //}

 }
//取消
 function cancel(){
 	//关闭窗口
 	window.parent.$('#win_div').window('close');
 }
 function validate(param){
	 var flag=true;
	 var resultNmame="";
	 var resultAlgorithm="";
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
	
	$('#validateMessage').html(resultNmame+resultAlgorithm);
	 return flag ;

		
 }

</script>
</head>
<body>
<form id="paramsForm">
	<table id="detailTable">
			<tr>
				<td class="tdlft">区域：</td>
				<td  class="tdrgt"><input id="area_id" class="comboboxComponent"   disabled="disabled"/></td>	
			</tr>
			<tr>
				<td class="tdlft">任务名称：</td>
				<td  class="tdrgt" style="width:250px"><input id="task_name" name="task_name" type="text" style="width:220px" /></td>	
			</tr>
			
			<tr>
			<td colspan="2"><span>提示信息：</span><span style="color:red;" id="validateMessage"></span></td>
			</tr>
			
			
	</table> 
	<div class="div_submit" >
			<a id="tool_save" href="javascript:save();" > <img src='<%=path%>/static/images/save.gif'
			align='top' border='0' title='保存' /></a>
			<a id="btn_cancel" href="javascript:cancel();" ><img src="/PowerInfo/static/images/quxiao.gif" border="0" style="vertical-align: middle"></a>
			
		</div> 
		</form>
</body>
</html>