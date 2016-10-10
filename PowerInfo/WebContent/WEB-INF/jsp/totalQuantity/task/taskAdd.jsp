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
			id : "baseyear",
			url : path + '/task/getBaseYears',
			textkey : "yearName",
			valuekey : "year",
			multiple : false,
			defaultVal:"last"
		});
	  comboBoxInit({
			id : "planyear",
			url : path + '/task/getPlanYears',
			textkey : "yearName",
			valuekey : "year",
			multiple:false,
			defaultVal:"first"
		});
	  if(id!=""){
		  initData();
	  }
});
function initData(){
	 var url=path+"/task/initData?id="+id;
	   //commonHelper.updInit(url);
	 $.ajax({
			type: "POST",
			url:url,
			dataType:"json",
			success:function(item){
				debugger;
				//赋值开始
				$('#task_name').val(item['TASK_NAME']);
		 		$('#baseyear').combobox('setValue',item['BASEYEAR']);
				$('#planyear').combobox('setValue',item['PLANYEAR']);
		 		var alstrs = item['ALGORITHM'].split(",");
				for (var i=0 ; i<alstrs.length;++i){
					$("input:checkbox[value="+alstrs[i]+"]").attr('checked','true');		
				}
			}
		});
}
 function save(){
	 var task_name = $('#task_name').val();
	 var baseyear =$('#baseyear').combobox('getValue');
	 var planyear =$('#planyear').combobox('getValue');
	 var algorithm='';
	 $("input[type=checkbox]").each(function(){
		    if(this.checked){
		    	algorithm+=$(this).val()+",";
		    }
		}); 
	 algorithm=algorithm.substring(0,algorithm.length-1);
	 var param = {
		'task_name':task_name,
		'baseyear':baseyear,
		'planyear':planyear,
		'algorithm':algorithm,
		'id':id
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
				url :  '/PowerInfo/task/saveData',
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
	 var algorithmVal= param["algorithm"];
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
	if(algorithmVal==null || algorithmVal==""){
		resultAlgorithm="请至少选择一种算法。";
		flag= false;
	}else{
		resultAlgorithm="";
		//flag=true;
	}
	$('#validateMessage').html(resultNmame+resultAlgorithm);
	 return flag ;

		
 }
 /*去掉平均值法和最优权重法就没用了
 function validate(param){
	 var algorithm = param["algorithm"];
	 var flag= validateAlgorithm(algorithm) ;
	 return flag ;
 }
 function validateAlgorithm(algorithm){
	 var algorithmArray = algorithm.split(",");
	 var algorithmLength = algorithmArray.length;
	 var count=0;

	 var index5= $.inArray('5',algorithmArray);
	 if(index5>0){
	   algorithmArray.splice(index5,1);
	 	++count;
	 }
	 var index6=$.inArray('6',algorithmArray);
	 if(index6>0){
	    algorithmArray.splice(index6,1);
	    ++count;
	 }
	 if(count==2){
		 count=1;
	 }
	 var algorithmLengthLast =  algorithmArray.length;
	if(algorithmLength-algorithmLengthLast<=count){
		$.messager.alert('提示','至少选择两个算法才有意义！','info');
		return false;
	}
	return true ;
	 
 }*/
</script>
</head>
<body>
<form id="paramsForm">
	<table id="detailTable">
			<tr>
				<td class="tdlft">任务名称：</td>
				<td  class="tdrgt" style="width:250px"><input id="task_name" name="task_name" type="text" style="width:220px" /></td>	
			</tr>
			<tr>
				<td  class="tdlft">基准年：</td>
				<td  class="tdrgt"><input id="baseyear" name="baseyear" class="comboboxComponent" type="text"  /></td>	
			</tr>
			<tr>
				<td class="tdlft">预测年：</td>
				<td  class="tdrgt"><input id="planyear" name="planyear" class="comboboxComponent"  type="text"  /></td>	
			</tr>
			<tr>
			<td rowspan="4" class="tdlft">算法：</td>
				<td  class="tdrgt"><input name="algorithm" type="checkbox" value="1" style="margin-left: 30px;"/>平均增长率法</td>
			</tr>
			<tr>
				<td  class="tdrgt"><input name="algorithm" type="checkbox" value="2" style="margin-left: 30px;"/>产值单耗法</td>
			
				
			</tr>
			<tr>
				<td  class="tdrgt"><input name="algorithm" type="checkbox" value="3" style="margin-left: 30px;"/>弹性系数法</td>
			</tr>
			<tr>
				<td  class="tdrgt"><input name="algorithm" type="checkbox" value="4" style="margin-left: 30px;"/>人均用电量法</td>

			</tr>
			<tr>
			<td colspan="2"><span>提示信息：</span><span style="color:red;" id="validateMessage"></span></td>
			</tr>
			<!--  <tr>
				<td  class="tdcength"><input name="algorithm" type="checkbox" value="5" />平均值法</td>
				<td  class="tdcength"><input name="algorithm" type="checkbox" value="6" />最优权重法</td>
			</tr>
			-->
			
	</table> 
	<div class="div_submit" >
			<a id="tool_save" href="javascript:save();" > <img src='<%=path%>/static/images/save.gif'
			align='top' border='0' title='保存' /></a>
			<a id="btn_cancel" href="javascript:cancel();" ><img src="/PowerInfo/static/images/quxiao.gif" border="0" style="vertical-align: middle"></a>
			
		</div> 
		</form>
</body>
</html>