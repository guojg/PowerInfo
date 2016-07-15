<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<%@include file="../../common/commonInclude.jsp" %>	
<script type="text/javascript"> 
$(function() {
	$('#baseyear').combobox({    
		 valueField:'id',    
		 textField:'text' ,
		 data:[{    
			    "id":2015,    
			    "text":"2015"   
			},{    
			    "id":2016,    
			    "text":"2016"   
			}]  

	});
	$('#planyear').combobox({    
		 valueField:'id',    
		 textField:'text' ,
		 data:[{    
			    "id":2020,    
			    "text":"2020"   
			},{    
			    "id":2021,    
			    "text":"2021"   
			}]  

	});
});
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
		'algorithm':algorithm
	 };
	 if(validate(param)){
		 $.ajax({
				type : 'POST',
				async : false,
				dataType: 'json',
				data: param,
				url :  '/PowerInfo/task/saveData',
				success : function(data) {
					window.parent.$.messager.alert('提示','新增成功！','info');
					//关闭窗口
					window.parent.$('#win_div').window('close');
					window.parent.$('#datagrid').datagrid('reload');
				
				}
			});
	 }

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
	<table id="detailTable">
			<tr>
				<td class="tdlft">任务名称：</td>
				<td ><input id="task_name" name="task_name" type="text"  /></td>	
			</tr>
			<tr>
				<td  class="tdlft">基准年：</td>
				<td ><input id="baseyear" name="baseyear" type="text"  /></td>	
			</tr>
			<tr>
				<td class="tdlft">预测年：</td>
				<td ><input id="planyear" name="planyear" type="text"  /></td>	
			</tr>
			<tr>
				<td class="tdcength"><input name="algorithm" type="checkbox" value="1" />平均增长率法</td>
				<td  class="tdcength"><input name="algorithm" type="checkbox" value="2" />产值单耗法</td>
			</tr>
			<tr>
				<td  class="tdcength"><input name="algorithm" type="checkbox" value="3" />弹性系数法</td>
				<td  class="tdcength"><input name="algorithm" type="checkbox" value="4" />人均用电量法</td>
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
		</div> 
</body>
</html>