<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>����</title>
<%@include file="../common/commonInclude.jsp" %>	


<script type="text/javascript">

</script>
</head>
<body>
<form action="" method="get"> 
��ϲ����ˮ����<br /><br /> 
<div>
	<input name="Fruit" type="checkbox" value="apple" />ƻ��
	<table id="apple">
	<tr>
		<td>���ֵ</td><td>	<input name="maxvalue"  id="maxvalue" type="text" /></td>
	</tr>
	<tr>
		<td>��Сֵ</td><td><input name="minvalue"  id="minvalue" type="text" /></td>
	</tr>
	</table>
 </div> 
<div>
		<input name="Fruit" type="checkbox" value="tz" />����
		<table id="tz">
		<tr>
			<td>���ֵ</td><td>	<input name="maxvalue"  id="maxvalue" type="text" /></td>
		</tr>
		<tr>
			<td>��Сֵ</td><td><input name="minvalue1"  id="minvalue1" type="text" /></td>
		</tr>
		</table>
 </div>  
<div><input name="Fruit" type="checkbox" value="xj" />�㽶</div> 
<div><input name="Fruit" type="checkbox" value="li" />�� </div> 
<button value="���ֵ" onclick="abc()">���ֵ</button>
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