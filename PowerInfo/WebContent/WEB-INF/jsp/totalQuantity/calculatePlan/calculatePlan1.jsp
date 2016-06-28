<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<%@include file="../../common/commonInclude.jsp" %>	
<script type="text/javascript" src="<%=path%>/js/totalquantity/jsonutil.js"></script>
<script type="text/javascript"> 
$(function() {
	var abc='1,3,5';
	var bbb=abc.split(",");
	for (var i=0 ; i<bbb.length;++i){
		$("input:checkbox[value="+bbb[i]+"]").attr('checked','true');
	}
	debugger;
	var c = $("input:checkbox");
	for(var t=0 ; t<c.length;++t){
		if(!c[t].checked){
			//alert(c[t].value);
			  $("#"+c[t].value+" input[type=text]").each(function(){
				  
				  var id = $(this).attr("id");
				    //alert(id+"--"+$(this).val());
				  $("#"+id).attr("disabled",true);
				  });
		}
		
	}
	$("input:checkbox[checked=false]").size();

});
//$("#div1 input[type=text]")
 function baoCun(){
	//alert($("#1 input[type=text]").size());
	var m=[];
	//b.name='2005';
	//b.age="2006";
	var abc='1,2';
	var bbb=abc.split(",");
	for (var i=0 ; i<bbb.length;++i){
		var b={};
		  $("#"+bbb[i]+" input[type=text]").each(function(){
			  
			  var id = $(this).attr("id");
			    //alert(id+"--"+$(this).val());
			   b[id]=$(this).val();
			  });
		  m.push(b);
	}
	debugger;
	  /*$("#1 input[type=text]").each(function(){
		  
		  var id = $(this).attr("id");
		    //alert(id+"--"+$(this).val());
		   b[id]=$(this).val();
		  });*/
		  var param={
				  "abc":JSONH.stringify(m)
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
				<td><input name="algorithm" type="checkbox" value="1" />平均增长率法</td>
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
	<div id="1">
		<input type="text" name="h" id="h" value="1">
		<input  type="text" name="a" id="a">
		<input  type="text" name="b" id="b">
	</div> 
	<div id="2">
		<input  type="text" name="t" id="t" value="2">
		<input  type="text" name="c" id="c">
	</div> 
	<div class="div_submit" >
			<a id="btn_save"   href="javascript:baoCun();" >计算</a>
		</div> 
</body>
</html>