
	var indexArr = {};
$(function() {
	/*
	 * 获取表单文本
	 */
	$.ajax({
		type : 'POST',
		async : false,
		data: {"domain_id":35,"condition":"100,200,300,400,9001,10001,11001,12001,13001,15001,16001,17001,17002,17003,17004,17005"},
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
				 trHtml +="<td class='tdrgt'><input  type='text' name='"+i+"' id='"+i+"' onblur='constantcost()'></td>";
			 }else if(i=='800'){
				 trHtml +="<td class='tdrgt'><input  type='text' name='"+i+"' id='"+i+"' onblur='constantcost()'></td>";
			 }else if(i=='900'){
				 trHtml +="<td class='tdrgt'><input  type='text' name='"+i+"' id='"+i+"'  style='background-color:#bcbcbc;' readonly='readonly'></td>";
			 }else if(i=='18001'){
				 trHtml +="<td class='tdrgt'><input  type='text' name='"+i+"' id='"+i+"' onblur='coalCon()'></td>";
			 }else if(i=='19001'){
				 trHtml +="<td class='tdrgt'><input  type='text' name='"+i+"' id='"+i+"'  style='background-color:#bcbcbc;' readonly='readonly'></td>";
			 }else if(i=='500'){
				 trHtml +="<td class='tdrgt'><input  type='text' name='"+i+"' id='"+i+"'  onblur='coalCon()'></td>";
			 }else{
				 trHtml +="<td class='tdrgt'><input  type='text' name='"+i+"' id='"+i+"'></td>";
			 }
		 
		 }else{
			 trHtml +="<td class='tdlft'>"+indexArr[i]+"：</td>";
			 if(i=='600'){
				 trHtml +="<td class='tdrgt'><input  type='text' name='"+i+"' id='"+i+"' onblur='constantcost()'></td></tr>";
			 }else if(i=='700'){
				 trHtml +="<td class='tdrgt'><input  type='text' name='"+i+"' id='"+i+"' onblur='constantcost()'></td></tr>";
			 }else if(i=='800'){
				 trHtml +="<td class='tdrgt'><input  type='text' name='"+i+"' id='"+i+"' onblur='constantcost()'></td></tr>";
			 }else if(i=='900'){
				 trHtml +="<td class='tdrgt'><input  type='text' name='"+i+"' id='"+i+"' style='background-color:#bcbcbc;' readonly='readonly'></td></tr>";
			 }else if(i=='18001'){
				 trHtml +="<td class='tdrgt'><input  type='text' name='"+i+"' id='"+i+"' onblur='coalCon()'></td></tr>";
			 }else if(i=='19001'){
				 trHtml +="<td class='tdrgt'><input  type='text' name='"+i+"' id='"+i+"' style='background-color:#bcbcbc;' readonly='readonly'></td></tr>";
			 }else if(i=='500'){
				 trHtml +="<td class='tdrgt'><input  type='text' name='"+i+"' id='"+i+"' onblur='coalCon()'></td>";
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
	        validType: ['validNumberPrecision[10,4]'],
	        missingMessage: '额定容量不能为空。',
	        invalidMessage: '额定容量请输入整数位不超过10位，小数位不超过4位的数字。'
	    });
	 for(var i in indexArr){
	 $("#"+i).validatebox({
			required: true,
	        novalidate: true,
	        validType: ['validNumberPrecision[10,4]'],
	        missingMessage: indexArr[i]+'不能为空。',
	        invalidMessage: indexArr[i]+'请输入整数位不超过10位，小数位不超过4位的数字。'
	    });
	 }
		 comboBoxInit({
				id : "200",
				url :  path+"/constantCostDbArgController/getPlant",
				textkey : "value",
				valuekey : "code",
				multiple:false,
				defaultVal:"first"
		});
	//$('#900').css("background-color","red");
			var child11 = $("#iframe1", window.parent.$("#tt")).contents().find("#11").val();
if(id =="" &&  $("#11").val() !=""){
	id=$("#11").val();
}else if(id =="" &&  child11 !=""){
	id=child11;
}else{
	
}
	if(id !=""){
	
		initData();
	}else{
		$("#11").val("");
	}

});

function coalCon(){
	var i = $('#500').val() ;
	var t = $('#18001').val() ;
	var v=fixNum(i*t/1000000);
	$("#19001").val(v);
}

function fixNum(num){
	return num.toFixed(4);
}
function initData(){
	var taskParam={
			id:id
	} ;
	
	var jsonResult={};
	 $.ajax({
			type : 'POST',
			async : false,
			dataType: 'json',
			data: taskParam,
			url :  path+"/constantCostDbArgController/initData",
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
	 $("#11").val(id);
}
function baoCun(){
	jisuan();
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
			url:path+"/constantCostDbArgController/saveData",
			data:fmserialObj,
			dataType:"json",
			success:function(res){
			    if(res!=null && res!=""){
					$.messager.confirm('提示', '保存成功,是否继续填写燃煤成本', function(r) {
						if (r) {
							window.parent.parent.queryData();
							 $("#11").val(res);
							 window.parent.$('#tt').tabs('select','燃煤成本');
							// var tab =window.parent.$('#tt').tabs('getSelected');
							
							//tab.panel('refresh', path+'/coalCost/main?fdj_id='+res);
							 /*window.parent.$('#tt').tabs('update', {
									tab: tab,
									options: {
										title: '燃煤成本',
										href:  path+'/coalCost/main?fdj_id='+res  // 新内容的URL
									}
								});*/

						}else{
							window.parent.parent.queryData();
							  window.parent.parent.$('#win_div').window('close');
						}
					});
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

function jisuan(){

	var v900 = $('#900').val()==""?"": ($('#900').val()/365.0/48.0).toFixed(2);
	var v1500 = $('#1500').val()==""?"": ($('#1500').val()/365.0/48.0).toFixed(2);
	var v1600 = $('#1600').val()==""?"": ($('#1600').val()/365.0/48.0).toFixed(2);
	var v1700 = $('#1700').val()==""?"": ($('#1700').val()/365.0/48.0).toFixed(2);

	$('#9001').val(v900);
	$('#15001').val(v1500);
	$('#16001').val(v1600);
	$('#17001').val(v1700);

}
