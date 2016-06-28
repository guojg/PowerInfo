<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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
 function baoCun(){
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
	 alert(algorithm);
	 var param = {
		'task_name':task_name,
		'baseyear':baseyear,
		'planyear':planyear,
		'algorithm':algorithm
	 };
	 
	 $.ajax({
			type : 'POST',
			async : false,
			dataType: 'json',
			data: param,
			url :  '/PowerInfo/task/saveData',
			success : function(data) {
				_menus = data;
			
			}
		});
 }
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
				<td ><input name="algorithm" type="checkbox" value="1" />平均增长率法</td>
				<td><input name="algorithm" type="checkbox" value="2" />产值单耗法</td>
			</tr>
			<tr>
				<td ><input name="algorithm" type="checkbox" value="3" />弹性系数法</td>
				<td><input name="algorithm" type="checkbox" value="4" />人均用电量法</td>
			</tr>
			<tr>
				<td ><input name="algorithm" type="checkbox" value="5" />平均值法</td>
				<td><input name="algorithm" type="checkbox" value="6" />最优权重法</td>
			</tr>
			
	</table> 
	<div class="div_submit" >
			<a id="btn_save"   href="javascript:baoCun();" >计算</a>
		</div> 
</body>
</html>