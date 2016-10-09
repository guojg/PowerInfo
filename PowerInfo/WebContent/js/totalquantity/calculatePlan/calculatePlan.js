var algorithms=algorithmStr.split(",");
	var algorithmJson=getSysDict();
$(function() {
		$('#task_name').html('<b>'+task_name+'</b>');
	/*
	 * 给算法复选框赋值
	 */
	 if(algorithmRadio!=null && algorithmRadio!="" && algorithmRadio!="undefined"){
			$("input:radio[value="+algorithmRadio+"]").attr('checked','true');
			
	}
	
	
	
	addWeight(); 
	init();
	
	/*
	 * 将复选框置灰和未被选中的复选框对应的div设置不可编辑，置灰
	 */
	var c = $("input:checkbox");
	for(var t=0 ; t<c.length;++t){
		$("input:checkbox[value="+c[t].value+"]").attr('disabled',true);//将复选框置灰
		/*
		 * 未被选中的复选框对应的div设置不可编辑，置灰
		 */
		if(!c[t].checked){
			  $("#"+c[t].value+" input[type=text]").each(function(){				  
				  var id = $(this).attr("id");
				  $("#"+id).attr("disabled",true);
			});
		}
	}
	 if(algorithms.length<=1){
		 removeTr("ccc");
		  removeTr("bbb");
	 }

});
/*
 * 追加行
 */
function addTr(trHtml){
    var $tr=$("#calculateTable tr:last");
    if($tr.size()==0){
       alert("指定的table id或行数不存在！");
       return;
    }
    $tr.after(trHtml);
 }
/*
 * 追加行
 */
function removeTr(id){
    var $tr=$("#"+id+"");
    if($tr.size()==0){
       alert("指定的table id或行数不存在！");
       return;
    }
    //$tr.css('display','none');
    $tr.remove();
 }

function addTd(trHtml){
    var $tr=$("#calculateTable tr:last");
    $tr.each(function (i) { 
    	var  $td=$(this).children("td:eq(1)"); 
    	$td.append(trHtml);
    	}); 
 
 }

function addWeight(){
	var trHtml="<table id='6' class='bs2'>";
	for (var i=0 ; i<algorithms.length;++i){
		$("input:checkbox[value="+algorithms[i]+"]").attr('checked','true');		
		trHtml +="<tr> <td class='tdlft'> "+algorithmJson[algorithms[i]]+"权重(%)：</td> " +
		"<td class='tdrgt'>" +
		"<input type='text' name='weight"+algorithms[i]+"' id='weight"+algorithms[i]+"' >" +
		"</td></tr>";
	}
	trHtml +="</table>";
	addTd(trHtml);
}
/*
 * 保存
 */
function save(){
	
	var m=[];//请求的数组
	var algorithmRadioValue=$('input:radio:checked').val();
	
	var algorithmAndRadio = (algorithmStr+","+algorithmRadioValue).split(",");
	var flag=true;
	var wflag=false;
	for (var i=0 ; i<algorithmAndRadio.length;++i){
		/*
		 * 每个算法的需要的文本值
		 */
		var b={};
		var wv=0;
		var algorithmsId="algorithms"+algorithmAndRadio[i];
		b[algorithmsId]=algorithmAndRadio[i];
		  b["taskid"]=taskid;
		  $("#"+algorithmAndRadio[i]+" input[type=text]").each(function(){		  
			  var id = $(this).attr("id");
			  var thisval = $(this).val();
			  if(algorithmAndRadio[i]==6){
				  wflag=true;
				  wv +=parseFloat(thisval);
			  }
			   b[id]=thisval;
			   if(thisval==""){
				   flag=false; 
			   }
			  });
		  
		  m.push(b);
	}
	
	var param={
				  "param":JSONH.stringify(m),
				  "algorithmRadio":algorithmRadioValue
		  };
	if(!validate(m)){
		return ;
	}
	if(wflag && wv!=100){
		$.messager.alert('提示','权重之和为100！','info');
		return ;
	}
	if(!flag){
		$.messager.confirm('提示', '存在未填写的数据，是否保存?', function(r) {
			if (r) {
				saveajax(param);
				}
			});
	}else{
		saveajax(param);
	}
	
 
 }

function saveajax(param){
	 $.ajax({
			type : 'POST',
			async : false,
			dataType: 'json',
			data: param,
			url :  '/PowerInfo/calculatePlan/saveData',
			success : function(data) {
				if(data=="1"){
					$.messager.alert('提示','保存成功！','info');
				}else{
					$.messager.alert('提示','保存失败！','info');

				}
			
			}
		});
}

function init(){
	var taskParam={
			taskid:taskid
	} ;
	var jsonResult={};
	 $.ajax({
			type : 'POST',
			async : false,
			dataType: 'json',
			data: taskParam,
			url :  '/PowerInfo/calculatePlan/initData',
			success : function(data) {
				jsonResult = data;
			}
		});
	 for (key in jsonResult){
		  $("#"+key).val(jsonResult[key]);
	 }
	
}


function validate(param){
	if(algorithms.length>1 && $('input:radio:checked').val() ==undefined ){
		$('#avgvalidate').html("请你选择一种综合算法作为您的最终推荐值");
		$(".fontred").css('color','red');
		return false;
	}else{
		$('#avgvalidate').html("");
		$(".fontred").css('color','black');
	}
	return true;
}

