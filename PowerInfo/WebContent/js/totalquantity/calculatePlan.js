	var algorithmStr='1,3,5,6';  //算法代号
	var algorithms=algorithmStr.split(",");
$(function() {

	/*
	 * 给算法复选框赋值
	 */
	var trHtml="<tr><td></td><td><div id='6'><br>";
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
			trHtml +="<input type='text' name='weight"+algorithms[i]+"' id='weight"+algorithms[i]+"' ><br>";
		}
	}
	debugger;
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
    var $tr=$("#detailTable tr:last");
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
function baoCun(){
	var m=[];//请求的数组
	for (var i=0 ; i<algorithms.length;++i){
		/*
		 * 每个算法的需要的文本值
		 */
		var b={};
		var algorithmsId="algorithms"+algorithms[i];
		b[algorithmsId]=algorithms[i];
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
				_menus = data;
			
			}
		});
 
 }

function start111(){
	 $.ajax({
			type : 'POST',
			async : false,
			dataType: 'json',
			url :  '/PowerInfo/calculatePlan/startCalculate',
			success : function(data) {
				_menus = data;
			
			}
		});
}