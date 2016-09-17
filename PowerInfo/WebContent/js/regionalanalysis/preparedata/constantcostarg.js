
	var indexArr = {};
$(function() {
	/*
	 * 获取表单文本
	 */
	$.ajax({
		type : 'POST',
		async : false,
		data: {"domain_id":35,"condition":"100,200,300,400"},
		dataType : 'json',
		url :  '/PowerInfo/sysdict/getDataByNotCondition',
		success : function(data) {
			indexArr = data;
		}
	});
	/*
	 * 遍历拼表单
	 */
	  var $tr=$("#detailTable tr:last");
	 var trHtml="";
	 var count=0;
	 for(var i in indexArr){
		 ++count;
		 if(count%2==1){
		 trHtml +="<tr><td class='tdlft'>"+indexArr[i]+"：</td>";
			 if(i=='600'){
				 trHtml +="<td class='tdrgt'><input  type='text' name='"+i+"' id='"+i+"' onblur='constantcost()'></td>";
			 }else if(i=='700'){
				 trHtml +="<td class='tdrgt'><input  type='text' name='"+i+"' id='"+i+"' value=8 onblur='constantcost()'></td>";
			 }else if(i=='800'){
				 trHtml +="<td class='tdrgt'><input  type='text' name='"+i+"' id='"+i+"' value=30 onblur='constantcost()'></td>";
			 }else if(i=='900'){
				 trHtml +="<td class='tdrgt'><input  type='text' name='"+i+"' id='"+i+"'  style='background-color:gray;readonly:true ' onblur='constantcost()'></td>";
			 }else{
				 trHtml +="<td class='tdrgt'><input  type='text' name='"+i+"' id='"+i+"'></td>";
			 }
		 
		 }else{
			 trHtml +="<td class='tdlft'>"+indexArr[i]+"：</td>";
			 if(i=='600'){
				 trHtml +="<td class='tdrgt'><input  type='text' name='"+i+"' id='"+i+"' onblur='constantcost()'></td></tr>";
			 }else if(i=='700'){
				 trHtml +="<td class='tdrgt'><input  type='text' name='"+i+"' id='"+i+"' value=8 onblur='constantcost()'></td></tr>";
			 }else if(i=='800'){
				 trHtml +="<td class='tdrgt'><input  type='text' name='"+i+"' id='"+i+"' value=30 onblur='constantcost()'></td></tr>";
			 }else if(i=='900'){
				 trHtml +="<td class='tdrgt'><input  type='text' name='"+i+"' id='"+i+"' style='background-color:gray;readonly:true ' onblur='constantcost()'></td></tr>";
			 }else{
				 trHtml +="<td class='tdrgt'><input  type='text' name='"+i+"' id='"+i+"'></td></tr>";
			 }
		 }
		
	 }
	 trHtml +="<tr><td class='tdlft'>提示信息：</td><td class='tdrgt' colspan='3' > <span id='validateMessage' class='tipsValidate' /></td></tr>";
	 $tr.after(trHtml);
	 $("#100").validatebox({
         required: true,
         novalidate: true,
         validType: ['checkText','maxLength[25]'],
         missingMessage: '机组名称不能为空。',
         invalidMessage: '机组名称的输入长度不能超过25个汉字。'
     });
	 $("#200").combobox({
         required: true,
         novalidate: true,
         missingMessage: '所属电厂不能为空。'
     });
	 $("#400").datebox({
         required: true,
         novalidate: true,
         missingMessage: '投产日期不能为空。'
     });
	 $("#300").validatebox({
			required: true,
	        novalidate: true,
	        validType: ['validNumberPrecision[10,2]'],
	        missingMessage: '额定容量不能为空。',
	        invalidMessage: '额定容量请输入整数位不超过10位，小数位不超过2位的数字。'
	    });
	 for(var i in indexArr){
	 $("#"+i).validatebox({
			required: true,
	        novalidate: true,
	        validType: ['validNumberPrecision[10,2]'],
	        missingMessage: indexArr[i]+'不能为空。',
	        invalidMessage: indexArr[i]+'请输入整数位不超过10位，小数位不超过2位的数字。'
	    });
	 }
		 comboBoxInit({
				id : "200",
				url :  path+"/constantCostArgController/getPlant",
				textkey : "value",
				valuekey : "code",
				multiple:false,
				defaultVal:"first"
		});
	//$('#900').css("background-color","red");
	if(id !=""){
		initData();
	}else{
		$("#11").val("");
	}

});
function initData(){
	var taskParam={
			id:id
	} ;
	 $("#11").val(id);
	var jsonResult={};
	 $.ajax({
			type : 'POST',
			async : false,
			dataType: 'json',
			data: taskParam,
			url :  path+"/constantCostArgController/initData",
			success : function(data) {
				jsonResult = data;
			}
		});
	 for (key in jsonResult){
		 if(key=='200'){
			 $("#"+key).combobox('setValue',jsonResult[key]) ;
		 }else if(key=='400'){
			$("#"+key).datebox("setValue",jsonResult[key]);

		 }else{
			 $("#"+key).val(jsonResult[key]);
		 }
		 
	 }
}
function baoCun(){
	debugger;
	//验证消息开始
	$('.validatebox-text').each(function(i,obj){
		$(this).validatebox('enableValidation').validatebox('validateTip');
	});
	var tipsStr = $("#validateMessage").html();
	var tipsArr = tipsStr.split(",");
	$("#validateMessage").html(tipsArr[1]);
	
	var flag=$('#paramsForm').form('validate');
	var fmserialObj =  $("#paramsForm").serialize();
if(flag){
		$.ajax({
			type:"post",
			url:path+"/constantCostArgController/saveData",
			data:fmserialObj,
			dataType:"json",
			success:function(res){
			    if(res!=null && res=="1"){
					$.messager.alert('提示','保存成功!');
					$("#validateMessage").html("");
				}
			}
		});
		
}
}
//固定成本
function constantcost(){
	var cost =$('#600').val();
	var i=$('#700').val()/100.0 ;
	var t = $('#800').val() ;
	var m = Math.pow((1+i), t) ;
	var v =cost*i*m/(m-1);
	$('#900').val(v.toFixed(2));
}
