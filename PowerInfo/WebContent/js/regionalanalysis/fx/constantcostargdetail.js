
	var indexArr = {};
$(function() {
	/*
	 * 获取表单文本
	 */
	$.ajax({
		type : 'POST',
		async : false,
		data: {"domain_id":35,"condition":"100,200,300,400,9001,10001,11001,12001,13001,15001,16001,17001"},
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
				 trHtml +="<td class='tdrgt'><input  type='text' name='"+i+"' id='"+i+"'  disabled='disabled'  onblur='constantcost()'></td>";
			 }else if(i=='700'){
				 trHtml +="<td class='tdrgt'><input  type='text' name='"+i+"' id='"+i+"' value=8  disabled='disabled' onblur='constantcost()'></td>";
			 }else if(i=='800'){
				 trHtml +="<td class='tdrgt'><input  type='text' name='"+i+"' id='"+i+"' value=30  disabled='disabled' onblur='constantcost()'></td>";
			 }else if(i=='900'){
				 trHtml +="<td class='tdrgt'><input  type='text' name='"+i+"' id='"+i+"'  disabled='disabled' onblur='constantcost()'></td>";
			 }else{
				 trHtml +="<td class='tdrgt'><input  type='text' name='"+i+"' id='"+i+"'  disabled='disabled' ></td>";
			 }
		 
		 }else{
			 trHtml +="<td class='tdlft'>"+indexArr[i]+"：</td>";
			 if(i=='600'){
				 trHtml +="<td class='tdrgt'><input  type='text' name='"+i+"' id='"+i+"'  disabled='disabled' onblur='constantcost()'></td></tr>";
			 }else if(i=='700'){
				 trHtml +="<td class='tdrgt'><input  type='text' name='"+i+"' id='"+i+"' value=8  disabled='disabled' onblur='constantcost()'></td></tr>";
			 }else if(i=='800'){
				 trHtml +="<td class='tdrgt'><input  type='text' name='"+i+"' id='"+i+"' value=30  disabled='disabled' onblur='constantcost()'></td></tr>";
			 }else if(i=='900'){
				 trHtml +="<td class='tdrgt'><input  type='text' name='"+i+"' id='"+i+"'  disabled='disabled' onblur='constantcost()'></td></tr>";
			 }else{
				 trHtml +="<td class='tdrgt'><input  type='text' name='"+i+"' id='"+i+"'  disabled='disabled'></td></tr>";
			 }
		 }
		
	 }
	 $tr.after(trHtml);
	
		 comboBoxInit({
				id : "200",
				url :  path+"/constantCostArgController/getPlant",
				textkey : "value",
				valuekey : "code",
				multiple:false,
				defaultVal:"first"
		});
	//$('#900').css("background-color","red");
	if(id !="" || $("#11").val()!=""){
	
		initData();
	}else{
		$("#11").val("");
	}

});
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
			url :  path+"/constantCostFxArgController/initData",
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
	var v1000 = $('#1000').val()==""?"": ($('#1000').val()/365.0/48.0).toFixed(2);
	var v1100 = $('#1100').val()==""?"": ($('#1100').val()/365.0/48.0).toFixed(2);
	var v1200 = $('#1200').val()==""?"":($('#1200').val()/365.0/48.0).toFixed(2);
	var v1300 = $('#1300').val()==""?"": ($('#1300').val()/365.0/48.0).toFixed(2);
	var v1500 = $('#1500').val()==""?"": ($('#1500').val()/365.0/48.0).toFixed(2);
	var v1600 = $('#1600').val()==""?"": ($('#1600').val()/365.0/48.0).toFixed(2);
	var v1700 = $('#1700').val()==""?"": ($('#1700').val()/365.0/48.0).toFixed(2);

	$('#9001').val(v900);
	$('#10001').val(v1000);
	$('#11001').val(v1100);
	$('#12001').val(v1200);
	$('#13001').val(v1300);
	$('#15001').val(v1500);
	$('#16001').val(v1600);
	$('#17001').val(v1700);

}
