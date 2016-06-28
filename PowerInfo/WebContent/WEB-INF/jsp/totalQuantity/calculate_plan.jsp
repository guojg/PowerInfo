<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>增加</title>
<%@include file="../common/commonInclude.jsp" %>	


<script type="text/javascript">

</script>
</head>
<body>
<form action="" method="get"> 
您喜欢的水果？<br /><br /> 
<div>
	<input name="Fruit" type="checkbox" value="apple" />苹果
	<table id="apple">
	<tr>
		<td>最大值</td><td>	<input name="maxvalue"  id="maxvalue" type="text" /></td>
	</tr>
	<tr>
		<td>最小值</td><td><input name="minvalue"  id="minvalue" type="text" /></td>
	</tr>
	</table>
 </div> 
<div>
		<input name="Fruit" type="checkbox" value="tz" />桃子
		<table id="tz">
		<tr>
			<td>最大值</td><td>	<input name="maxvalue"  id="maxvalue" type="text" /></td>
		</tr>
		<tr>
			<td>最小值</td><td><input name="minvalue1"  id="minvalue1" type="text" /></td>
		</tr>
		</table>
 </div>  
<div><input name="Fruit" type="checkbox" value="xj" />香蕉</div> 
<div><input name="Fruit" type="checkbox" value="li" />梨 </div> 
<button value="获得值" onclick="abc()">获得值</button>
</form> 
	<script type="text/javascript">
	function abc(){
		$("input[type=checkbox]").each(function(){
		    //var chk = $(this).find("[checked]");
		    if(this.checked){
		    alert($(this).val());
		    }
		}); 
	}
 

</script>
</body>

</html>