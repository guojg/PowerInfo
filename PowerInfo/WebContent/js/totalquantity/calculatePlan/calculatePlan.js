var algorithms=algorithmStr.split(",");
	var algorithmJson=getSysDict();
$(function() {
	
	/*
	 * 给算法复选框赋值
	 */
	var trHtml="<tr><td></td><td></td><td><table id='6'>";
	for (var i=0 ; i<algorithms.length;++i){
		$("input:checkbox[value="+algorithms[i]+"]").attr('checked','true');		
		
		if(algorithms[i]==6){
			/*
			 * 追加权重
			 */
			trHtml+="</td></div> </tr>";
			addTr(trHtml);
		}else if(algorithms[i]==5){
			
		}else{
			trHtml +="<tr> <td class='tdlft'> "+algorithmJson[algorithms[i]]+"权重：</td> " +
					"<td class='tdrgt'>" +
					"<input type='text' name='weight"+algorithms[i]+"' id='weight"+algorithms[i]+"' >" +
					"</td></tr>";
		}
	}
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

