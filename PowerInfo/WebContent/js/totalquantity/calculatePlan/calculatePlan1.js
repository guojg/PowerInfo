//var algorithms=algorithmStr.split(",");
var algorithms="1,2,3,4".split(",");
var algorithmRadio="5";
	var algorithmJson=getSysDict();
$(function() {
	
	/*
	 * 给算法复选框赋值
	 */
	//var trHtml="<tr><td></td><td class='bs3'></td><td class='bs3'><table id='6' class='bs2'>";
	var trHtml="<table id='6' class='bs2'>";
	for (var i=0 ; i<algorithms.length;++i){
		$("input:checkbox[value="+algorithms[i]+"]").attr('checked','true');		
		
		/*if(algorithms[i]==6){
			
			trHtml+="</td></div> </tr>";
			addTr(trHtml);
		}else if(algorithms[i]==5){
			
		}else{
			trHtml +="<tr> <td class='tdlft'> "+algorithmJson[algorithms[i]]+"权重：</td> " +
					"<td class='tdrgt'>" +
					"<input type='text' name='weight"+algorithms[i]+"' id='weight"+algorithms[i]+"' >" +
					"</td></tr>";
		}*/
		trHtml +="<tr> <td class='tdlft'> "+algorithmJson[algorithms[i]]+"权重：</td> " +
		"<td class='tdrgt'>" +
		"<input type='text' name='weight"+algorithms[i]+"' id='weight"+algorithms[i]+"' >" +
		"</td></tr>";
	}
	 if(algorithmRadio!=null && algorithmRadio!=""){
			$("input:radio[value="+algorithmRadio+"]").attr('checked','true');	
		}
	//trHtml +="</table></td></tr>";
	trHtml +="</table>";
	addTd(trHtml);
	//$('#aaa').attr('rowSpan','2');
	//$('#aaa').text("综合预测");
	init();
	removeTr();
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
function removeTr(){
    var $tr=$("#bbb");
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
//$("#div1 input[type=text]")
/*
 * 保存
 */
function save(){
	var m=[];//请求的数组
	for (var i=0 ; i<algorithms.length;++i){
		/*
		 * 每个算法的需要的文本值
		 */
		var b={};
		var algorithmsId="algorithms"+algorithms[i];
		b[algorithmsId]=algorithms[i];
		  b["taskid"]=taskid;
		  $("#"+algorithms[i]+" input[type=text]").each(function(){		  
			  var id = $(this).attr("id");
			   b[id]=$(this).val();
			  });
		  
		  m.push(b);
	}
	var param={
				  "param":JSONH.stringify(m)
		  };
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
	debugger;
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

