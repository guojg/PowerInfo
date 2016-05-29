/*combotree的加载*****自定义传递参数*****/
function comboTreeInit(obj){
	$("#"+obj.id).combotree({
		  multiple:obj.multiple,
	      url:commonPath+'/dataFetchServlet?code='+obj.code+'&param1='+obj.param1+'&showLevel='+obj.showLevel+'&isShowArea='+obj.isShowArea+'&componentType=tree',
	      onBeforeExpand : function(node) {
				$('#'+obj.id).combotree('tree').tree('options').url = commonPath+'/dataFetchServlet?code='+obj.code+'&isShowArea='+obj.isShowArea+'&showLevel='+obj.showLevel+'&componentType=tree&param2=' + node.id;
		  },
		  editable:false,
		  onClick:function(node){
       		$('#'+obj.id).combotree("setValue",node.id).combotree("setText",node.text);
		  },
		  lines:true,
		  cascadeCheck:false,
		  onLoadSuccess:function(){
			  if(obj.param1!=null && obj.param1 != ""){
				  $("#"+obj.id).combotree('setValues',obj.param1.split(","));
			  }
		  }
	});
	return $("#"+obj.id);
}

/*combobox的加载*****自定义传递参数*****/
function comboBoxInit(obj){
	//url
	if(obj.url!=null && obj.url!="" ){
		url = obj.url;
	}else{
		if(obj.code==null){
			obj.code="";
		}
		if(obj.param1==null){
			obj.param1="";	
		}
		if(obj.param2==null){
			obj.param2="";
		}
		if(obj.showLevel==null){
			obj.showLevel="";
		}
		url = commonPath+"/dataFetchServlet?code="+obj.code+"&param1="+obj.param1+"&param2="+obj.param2+'&showLevel='+obj.showLevel+"&componentType=combobox";
	}
	$.ajax({ 
		url: url, 
		type: 'post',
		dataType: 'json',
		async: false,
		success:function(data){
			if(obj.multiple){//多选加上清空,全选
				if(typeof(obj.defaultVal)=="undefined"){//默认值不传默认全选
					var option ={};
					option.ID="";
					option.TEXT="<input type='checkbox' id='"+obj.id+"_qxf_checkbox' style='width:12px;' checked='checked'>全选";
					data.unshift(option);
					var comboboxObj = makeCombobox_common_dictionary(obj,data);
					//如果是全选的默认把值都选上。
					var defaultValArr = new Array();
					for(var i=0;i<data.length;i++){
						if(data[i].ID!=""){
							defaultValArr.push(data[i].ID+'');
						}
					}
					comboboxObj.combobox("setValues",defaultValArr);
					
				}else if(obj.defaultVal==""){//默认值请选择,此时默认值是请选择
					var option ={};
					option.ID="";
					option.TEXT="<input type='checkbox' id='"+obj.id+"_qxf_checkbox' style='width:12px;'>全选";
					data.unshift(option);
					var comboboxObj = makeCombobox_common_dictionary(obj,data);
					comboboxObj.combobox("setValue",[]);
					comboboxObj.combobox("setText","请选择...");
				}else{//其它情况是默认值传递过来的情况
					var option ={};
					option.ID="";
					option.TEXT="请选择...";
					data.unshift(option);
					var comboboxObj = makeCombobox_common_dictionary(obj,data);
					var arrDefaultV  =(obj.defaultVal+"").split(",");
					comboboxObj.combobox("setValues",arrDefaultV);
				}
			}else{//单选的话加上请选择,默认单选
				//debugger;
				if(obj.singleOne){
					if(data!= null && data.length>0){
						obj.defaultVal =data[0].ID;

					}
					$("#"+obj.id).combobox({
						valueField:'ID',
						textField:'TEXT',
						multiple:false,
						editable:false,
						value:obj.defaultVal,
						data:data
					});
				}else{
					var option ={};
					option.ID="-1";
					option.TEXT="请选择...";
					data.unshift(option);
					$("#"+obj.id).combobox({
						valueField:'ID',
						textField:'TEXT',
						multiple:false,
						editable:false,
						value:obj.defaultVal,
						data:data
					});
				}
			}
		}
	});
	return $("#"+obj.id);
}
/**
 * 
 * 名称: scmncomboBoxInit
 * 功能描述: 生产模拟
 * @param obj
 * @return         
 * 调用关系及被调用关系:
 */
function scmncomboBoxInit(obj){
	//url
	if(obj.url!=null && obj.url!="" ){
		url = obj.url;
	}else{
		if(obj.code==null){
			obj.code="";
		}
		if(obj.param1==null){
			obj.param1="";	
		}
		if(obj.param2==null){
			obj.param2="";
		}
		if(obj.showLevel==null){
			obj.showLevel="";
		}
		url = commonPath+"/dataFetchServlet?code="+obj.code+"&param1="+obj.param1+"&param2="+obj.param2+'&showLevel='+obj.showLevel+"&componentType=combobox";
	}
	$.ajax({ 
		url: url, 
		type: 'post',
		dataType: 'json',
		async: false,
		success:function(data){
			if(obj.multiple){//多选加上清空,全选
				 //debugger;
				if(typeof(obj.defaultVal)=="undefined"){//默认值不传默认全选
					var comboboxObj = scmn_makeCombobox_common_dictionary(obj,data);
					//如果是全选的默认把值都选上。
					//debugger;
					var defaultValArr = new Array();
					for(var i=0;i<data.length;i++){
						if(data[i].ID!=""){
							defaultValArr.push(data[i].ID+'');
						}
					}
					comboboxObj.combobox("setValues",defaultValArr);
					
				}else if(obj.defaultVal==""){//默认值请选择,此时默认值是请选择
					var comboboxObj = scmn_makeCombobox_common_dictionary(obj,data);
					comboboxObj.combobox("setValue",[]);
					comboboxObj.combobox("setText","请选择...");
				}else{//其它情况是默认值传递过来的情况
					var comboboxObj = scmn_makeCombobox_common_dictionary(obj,data);
					var arrDefaultV  =(obj.defaultVal+"").split(",");
					comboboxObj.combobox("setValues",arrDefaultV);
				}
			}else{//单选的话加上请选择,默认单选
				//debugger;
				if(obj.singleOne){
					obj.defaultVal =data[0].ID;
					$("#"+obj.id).combobox({
						valueField:'ID',
						textField:'TEXT',
						multiple:false,
						editable:false,
						value:obj.defaultVal,
						data:data
					});
				}else{
					var option ={};
					option.ID="-1";
					option.TEXT="请选择...";
					data.unshift(option);
					$("#"+obj.id).combobox({
						valueField:'ID',
						textField:'TEXT',
						multiple:false,
						editable:false,
						value:obj.defaultVal,
						data:data
					});
				}
			}
		}
	});
	return $("#"+obj.id);
}
function initComboBoxByData(obj){
	var data = obj.data;
	var dataTemp = new Array();
	for(var i=0;i<data.length;i++){
		dataTemp.push(data[i]);
	}
	if(obj.multiple){//多选加上清空,全选
		if(typeof(obj.defaultVal)=="undefined"){//默认值不传默认全选
			var option ={};
			option.ID="";
			option.TEXT="<input type='checkbox' id='"+obj.id+"_qxf_checkbox' style='width:12px;' checked='checked'>全选";
			dataTemp.unshift(option);
			var comboboxObj = makeCombobox_common_dictionary(obj,dataTemp);
			//如果是全选的默认把值都选上。
			var defaultValArr = new Array();
			for(var i=0;i<dataTemp.length;i++){
				if(dataTemp[i].ID!=""){
					defaultValArr.push(dataTemp[i].ID+'');
				}
			}
			comboboxObj.combobox("setValues",defaultValArr);
			
		}else if(obj.defaultVal==""){//默认值请选择,此时默认值是请选择
			var option ={};
			option.ID="";
			option.TEXT="<input type='checkbox' id='"+obj.id+"_qxf_checkbox' style='width:12px;'>全选";
			dataTemp.unshift(option);
			var comboboxObj = makeCombobox_common_dictionary(obj,dataTemp);
			comboboxObj.combobox("setValue",[]);
			comboboxObj.combobox("setText","请选择...");
		}else{//其它情况是默认值传递过来的情况
			var option ={};
			option.ID="";
			option.TEXT="请选择...";
			dataTemp.unshift(option);
			var comboboxObj = makeCombobox_common_dictionary(obj,dataTemp);
			var arrDefaultV  =(obj.defaultVal+"").split(",");
			comboboxObj.combobox("setValues",arrDefaultV);
		}
	}else{//单选的话加上请选择,默认单选
		//debugger;
		if(obj.singleOne){
			obj.defaultVal =dataTemp[0].ID;
			$("#"+obj.id).combobox({
				valueField:'ID',
				textField:'TEXT',
				multiple:false,
				editable:false,
				value:obj.defaultVal,
				data:dataTemp
			});
		}else{
			var option ={};
			option.ID="";
			option.TEXT="请选择...";
			dataTemp.unshift(option);
			$("#"+obj.id).combobox({
				valueField:'ID',
				textField:'TEXT',
				multiple:false,
				editable:false,
				value:obj.defaultVal,
				data:dataTemp
			});
		}
	}
	return $("#"+obj.id);
}
/*-------------------------------------通用字典-----------------------------------------------*/
function sortMaoPao_common_dictionary(hasCheckeds){
	//冒泡排序
	for(var i=0;i<hasCheckeds.length;i++){
		for(var j=i+1;j<hasCheckeds.length;j++){
			var temp="";
			var val_i = hasCheckeds[i];
			var val_j = hasCheckeds[j];
			if(val_i>val_j){
				temp =  hasCheckeds[j];
				hasCheckeds[j] =  hasCheckeds[i];
				hasCheckeds[i]=temp;
			}
		}
	}
	return hasCheckeds;
}
function makeCombobox_common_dictionary(obj,data){
	
	$("#"+obj.id).combobox({
		valueField:'ID',
		textField:'TEXT',
		multiple:true,
		editable:false,
		data:data,
		onSelect:function(record){
			var qxf_flag = $("#"+obj.id+"_qxf_checkbox").attr("checked");//全选是否选中标示
			if(record.ID==""){//选中全选
				var vals = new Array();
				if(typeof(qxf_flag) != "undefined" && qxf_flag=="checked"){//全选是否选中标示
					for(var i=0;i<data.length;i++){
						if(data[i].ID!=""){
							vals.push(data[i].ID+'');
						}
					}
					$(this).combobox("setValues",vals);
				}else{
					$(this).combobox("reset");
					$(this).combobox("setText","请选择...");
				}
			}else{
				var hasCheckeds = $(this).combobox("getValues");
				
				if(hasCheckeds.length==data.length-1){//去除没有意义的全选
					$("#"+obj.id+"_qxf_checkbox").attr("checked","checked");
				}else{
					$("#"+obj.id+"_qxf_checkbox").attr("checked",false);
				}
				if(hasCheckeds.length==0){
					$(this).combobox("reset");
					$(this).combobox("setText","请选择...");
				}else{
					$(this).combobox("setValues",sortMaoPao_common_dictionary(hasCheckeds));
				}
			}
		},
		onUnselect:function(record){
			var qxf_flag = $("#"+obj.id+"_qxf_checkbox").attr("checked");//全选是否选中标示
			if(record.ID==""){//选中全选
				var vals = new Array();
				if(typeof(qxf_flag) != "undefined" && qxf_flag=="checked"){//全选是否选中标示
					for(var i=0;i<data.length;i++){
						if(data[i].ID!=""){
							vals.push(data[i].ID+'');
						}
					}
					$(this).combobox("setValues",vals);
				}else{
					$(this).combobox("reset");
					$(this).combobox("setText","请选择...");
				}
			}else{
				var hasCheckeds = $(this).combobox("getValues");
				if(hasCheckeds.length==data.length-1){//去除没有意义的全选
					$("#"+obj.id+"_qxf_checkbox").attr("checked","checked");
				}else{
					$("#"+obj.id+"_qxf_checkbox").attr("checked",false);
				}
				if(hasCheckeds.length==0){
					$(this).combobox("reset");
					$(this).combobox("setText","请选择...");
				}else{
					$(this).combobox("setValues",sortMaoPao_common_dictionary(hasCheckeds));
				}
			}
			
		}
	});
	
	return $("#"+obj.id);
}
function scmn_makeCombobox_common_dictionary(obj,data){
	
	$("#"+obj.id).combobox({
		valueField:'ID',
		textField:'TEXT',
		multiple:true,
		editable:false,
		data:data,
		onSelect:function(record){
		//debugger;
			var qxf_flag = $("#"+obj.id+"_qxf_checkbox").attr("checked");//全选是否选中标示
			if(record.ID==""){//选中全选
				var vals = new Array();
				if(typeof(qxf_flag) != "undefined" && qxf_flag=="checked"){//全选是否选中标示
					for(var i=0;i<data.length;i++){
						if(data[i].ID!=""){
							vals.push(data[i].ID+'');
						}
					}
					$(this).combobox("setValues",vals);
				}else{
					$(this).combobox("reset");
					$(this).combobox("setText","请选择...");
				}
			}else{
				var hasCheckeds = $(this).combobox("getValues");
				
				for(var i=0;i<hasCheckeds.length;i++){
					if(hasCheckeds[i]==""){
						hasCheckeds.splice(i,i+1);
					}
				}
				if(hasCheckeds.length==data.length-1){//去除没有意义的全选
					$("#"+obj.id+"_qxf_checkbox").attr("checked","checked");
				}else{
					$("#"+obj.id+"_qxf_checkbox").attr("checked",false);
				}
				if(hasCheckeds.length==0){
					$(this).combobox("reset");
					$(this).combobox("setText","请选择...");
				}else{
					$(this).combobox("setValues",sortMaoPao_common_dictionary(hasCheckeds));
				}
			}
		},
		onUnselect:function(record){
			var qxf_flag = $("#"+obj.id+"_qxf_checkbox").attr("checked");//全选是否选中标示
			if(record.ID==""){//选中全选
				var vals = new Array();
				if(typeof(qxf_flag) != "undefined" && qxf_flag=="checked"){//全选是否选中标示
					for(var i=0;i<data.length;i++){
						if(data[i].ID!=""){
							vals.push(data[i].ID+'');
						}
					}
					$(this).combobox("setValues",vals);
				}else{
					$(this).combobox("reset");
					$(this).combobox("setText","请选择...");
				}
			}else{
				var hasCheckeds = $(this).combobox("getValues");
				if(hasCheckeds.length==data.length-1){//去除没有意义的全选
					$("#"+obj.id+"_qxf_checkbox").attr("checked","checked");
				}else{
					$("#"+obj.id+"_qxf_checkbox").attr("checked",false);
				}
				if(hasCheckeds.length==0){
					$(this).combobox("reset");
					$(this).combobox("setText","请选择...");
				}else{
					$(this).combobox("setValues",sortMaoPao_common_dictionary(hasCheckeds));
				}
			}
			
		}
	});
	
	return $("#"+obj.id);
}
