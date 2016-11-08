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
	 var nowYear =new Date().getFullYear();
	  $("#task_name").validatebox({
           required: true,
           novalidate: true,
           validType: ['checkText','maxLength[25]'],
           missingMessage: '任务名不能为空。',
           invalidMessage: '任务名称的输入长度不能超过25个汉字。'
       });
	  $("#year").combobox({
          required: true,
          novalidate: true,
          missingMessage: '水平年不能为空。'
      });
		//$('#startyear').numberspinner('setValue', nowYear-1); 
		//$('#stopyear').numberspinner('setValue', nowYear+4); 
var yearJsons=[];
		$('#startyear').numberspinner({
			onChange:function(){
				var startChange=$('#startyear').numberspinner('getValue');
				  $('#stopyear').numberspinner({min:parseInt(startChange)+1});
				//alert(startChange+1);
				var stopChange = $('#stopyear').numberspinner('getValue');
				yearJsons=[];

				for(var i=parseInt(startChange);i<=parseInt(stopChange);++i){
					yearJsons.push({
						'year':i+"",
						'yearName':i+"年"
					});
				}
				initComboBoxByData({
					id:"year",
					valuekey:'year',    
					textkey:'yearName',
					data:yearJsons,
					multiple:true
				});
				
				
			}
		});
		$('#stopyear').numberspinner({
			onChange:function(){
	
				var startChange1=$('#startyear').numberspinner('getValue');
				//alert(startChange+1);
				var stopChange1 = $('#stopyear').numberspinner('getValue');
				yearJsons=[];

				for(var i=parseInt(startChange1);i<=parseInt(stopChange1);++i){
					yearJsons.push({
						'year':i+"",
						'yearName':i+"年"
					});
				}
				initComboBoxByData({
					id:"year",
					valuekey:'year',    
					textkey:'yearName',
					data:yearJsons,
					multiple:true
				});
			}
		});
	
		$.ajax({
			type : 'POST',
			async : false,
			url : path + '/basicparam/initData',
			success : function(data) {
	    		$("#startyear").numberspinner('setValue',data["START_YEAR"]);
	    		$("#stopyear").numberspinner('setValue',data["STOP_YEAR"]);


			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
			}
		});
		
		initComboBoxByData({
			id:"year",
			valuekey:'year',    
			textkey:'yearName',
			data:yearJsons,
			multiple:true
		});
	  /*comboBoxInit({
			id : "year",
			url : path + '/balancetask/getyears',
			textkey : "yearName",
			valuekey : "year",
			multiple : true
		});*/
	  if(id!=""){
		  initData();
	  }
});
function initData(){
	 var url=path+"/balancetask/initData?id="+id;
	  // commonHelper.updInit(url);
	 $.ajax({
			type : 'POST',
			async : false,
			url :url,
			success : function(data) {
	    		$("#task_name").val(data["TASK_NAME"]);
    			$("#year").combobox('setValues',data["YEAR"].split(","));



			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
			}
		});
}
 function save(){
	 var task_name = $('#task_name').val();
	 var year =$('#year').combobox('getValues').join(",");
	 var startYear = $('#startyear').numberspinner('getValue');
	 var stopYear = $('#stopyear').numberspinner('getValue');
	 var param = {
		'task_name':task_name,
		'year':year,
		'id':id,
		'stopyear':stopYear,
		'startyear':startYear
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
//取消
 function cancel(){
 	//关闭窗口
 	window.parent.$('#win_div').window('close');
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
		if(flagV==false || resultNmame!=""){
			flag=false;
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
				<td  class="tdrgt" style="width:250px"><input id="task_name" name="task_name" type="text" style="width:220px" /></td>	
			</tr>
			<tr  style="display:none">
				<td class="tdlft">起始年：</td>
				<td  class="tdrgt"><input id="startyear" name="startyear" class="easyui-numberspinner" min="2000" max="5000" data-options="required:true,suffix:'年'" type="text"  /></td>	
			</tr >
			<tr  style="display:none">
				<td class="tdlft">终止年：</td>
				<td  class="tdrgt"><input id="stopyear" name="stopyear" class="easyui-numberspinner" min="2000" max="5000" data-options="required:true,suffix:'年'" type="text"  /></td>	
			</tr>
			<tr>
				<td  class="tdlft">水平年：</td>
				<td class="tdrgt" ><input id="year" name="year" type="text" class="comboComponentarr"  /></td>	
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