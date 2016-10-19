var commonHelper = (function(){
	//CommonHelper类
	function CommonHelper(){
		this.editIndex = undefined;//编辑表格
		this.queryParams={};//查询参数
		this.queryArrayParams={};//数组风格的查询参数
	}
	//动态创建按钮
	CommonHelper.prototype.common_button = function(commonButtonObj){
		//图片
	    var dom_img = document.createElement("img");
	    dom_img.setAttribute("src", commonPath+"/resources/images/"+commonButtonObj.icons); 
	    dom_img.setAttribute("align", "top"); 
	    dom_img.setAttribute("border", "0");
	    dom_img.setAttribute("title", commonButtonObj.title);
	    if(commonButtonObj.width!=null && commonButtonObj.width!=""){
	    	dom_img.setAttribute("width", commonButtonObj.width);
	    }
	    //超链接
		var dom_a = document.createElement("a");
		dom_a.setAttribute("id", commonButtonObj.id);
		if(commonButtonObj.defineFunction!=null && commonButtonObj.defineFunction!=""){
			dom_a.setAttribute("href","javascript:"+commonButtonObj.defineFunction+";");
		}
		//将img加入到a
		dom_a.innerHTML=dom_img.outerHTML;
		//按钮所在div
		var obj = $("#btn_div");
		obj.append(dom_a);
		return $("#"+commonButtonObj.id);
		
	}
	//所有公共方法开始------------------------------------------------------->
	//新增按钮操作--//3个参数,参数1--->跳转的url,参数2--->window的宽度,参数3--->window的高度
	CommonHelper.prototype.toAdd = function(toAddObj){
		forward_add(toAddObj);
	};
	//新增页面保存表单--//参数1--属性所在form表单id,参数2执行访问操作url,参数3父页面的datagrid_id(datagrid保存以后做刷新操作)
	CommonHelper.prototype.save = function(saveObj){
		save_operate(saveObj);
	};
	//删除按钮操作-->参数1--table的id,参数2执行删除操作url,依据删除的主键pid
	CommonHelper.prototype.del = function(delObj){
		if(delObj.flag){
			del_operate_edit(delObj);
		}else{
			del_operate(delObj);
		}
	};
	//修改按钮操作-->//5个参数，参数1--->datagrid的id,参数2--->跳转的url,参数3--->window的宽度,参数4--->window的高度,参数5--->依据主键
	CommonHelper.prototype.toUpd = function(toUpdObj){
		forward_upd(toUpdObj);
	};
	//初始化修改页面操作-->1个参数，参数1--->访问的url
	CommonHelper.prototype.updInit=function(url){
		init_operate(url);
	};
	//更新页面保存表单--//参数1--属性所在form表单id,参数2执行访问操作url,参数3父页面的datagrid_id(datagrid保存以后做刷新操作)
	CommonHelper.prototype.upd = function(updObj){
		update_operate(updObj);
	};
	//
	/*************************************目前已知列表加载的种类开始*****(可继续扩展)********************************************/
	/**************************1.此种情况适用于普通的纯div布局格式的页面*********************/
	//列表初始化--5个参数，参数1--->table的id,参数2--->显示的冰冻列名字,参数3--->显示的非冰冻列名字,参数4--->查询参数,参数5--->访问的url
	CommonHelper.prototype.qryList=function(qryListObj){
		onLoadDataGrid(qryListObj);
	};
	/**************************2.此种情况适用于应用了easyui--layout布局格式的页面*********************/
	//列表初始化--5个参数，参数1--->table的id,参数2--->显示的冰冻列名字,参数3--->显示的非冰冻列名字,参数4--->查询参数,参数5--->访问的url
	CommonHelper.prototype.qryListLayout=function(qryListLayoutObj){
		onLoadDataGridLayOut(qryListLayoutObj);
	};
	/**************************3.此种情况适用于启用了编辑单元格布局格式的页面*********************/
	//列表初始化--6个参数，参数1--->table的id,参数2--->显示的冰冻列名字,参数3--->显示的非冰冻列名字,参数4--->查询参数,参数5--->访问的url,参数6--->要合并的单元格列名数组
	CommonHelper.prototype.qryListEditCell=function(qryListEditCellObj){
		onLoadDataGridEditCell(qryListEditCellObj,this);
	};
	//列表初始化--able的id,参数2--->显示的冰冻列名字,参数3--->显示的非冰冻列名字,参数4--->查询参数,参数5--->访问的url
	CommonHelper.prototype.qryListFooter=function(qryListEditCellObj){
		onLoadDataGridFooter(qryListEditCellObj,this);//带有统计行
	};
	/*************************************目前已知列表加载的种类结束************************************************************/
	//导出按钮操作--1个参数,参数1导出访问的url
	CommonHelper.prototype.exportList=function(datagrid_id,url){
		$('#'+datagrid_id).datagrid("exportList",{url:url});
	};
	//获取组装查询条件--
	CommonHelper.prototype.getParams=function(){
		var classObj = this;
		$("#search_tbl input").each(function(){
			var attrName = $(this).attr('id');//id
			if(attrName!=null && attrName!=""){
				var clas= $(this).attr('class');//用于区分是否是combo
				var inputVal ="";
				if(clas!=null && clas!=""){
					if(clas.indexOf("combotreeComponent")>=0){//combotree
						inputVal = $(this).combobox("getValues");
	            	}else if(clas.indexOf("comboboxComponent")>=0){//combobox
	            		inputVal = $(this).combobox("getValues");
	            	}
					classObj.queryParams[attrName]=classObj.arr2Str(inputVal);
				}else{
            		inputVal = $(this).val();//普通文本
            		classObj.queryParams[attrName]=encodeURIComponent(encodeURIComponent(inputVal));
            	}
				
		    }
		});
		return classObj.queryParams;
	};
	//获取组装查询条件--数组风格
	CommonHelper.prototype.getArrayParams=function(){
		var classObj = this;
		$("#search_tbl input").each(function(){
			var attrName = $(this).attr('id');//id
			if(attrName!=null && attrName!=""){
				var clas= $(this).attr('class');//用于区分是否是combo
				var encodeURIFlag = "encodeURITrue_";//用于区分是否加密，在jquery-1.8.3.js--7312行有相应的解析
				var inputVal ="";
				if(clas!=null && clas!=""){
					if(clas.indexOf("combotreeComponent")>=0){//combotree
						inputVal = $(this).combotree("getValues");
	            	}else if(clas.indexOf("comboboxComponent")>=0){//combobox
	            		inputVal = $(this).combobox("getValues");
	            	}else if(clas.indexOf("easyui-datebox")>=0){
	            		inputVal = $(this).datebox("getValue");
	            	}
					classObj.queryArrayParams[attrName]=inputVal;
				}else{
            		inputVal = $(this).val();//普通文本
            		classObj.queryArrayParams[attrName]=encodeURIComponent(encodeURIComponent(inputVal));
            	}
		    }
		});
		return classObj.queryArrayParams;
	};
	//将字符串数组转换成以,隔开的字符串
	CommonHelper.prototype.arr2Str=function(arr){
		var str = "";
		for(var i=0;i<arr.length;i++){
			str = str+","+arr[i];	
		}
		if(str!=""){
			str = str.substring(1,str.length);
		}
		return str;
	};
	
	return new CommonHelper();

})();

//所有公共方法结束<-------------------------------------------------------
//3个参数,参数1--->跳转的url,参数2--->window的宽度,参数3--->window的高度
function forward_add(toAddObj){
	var win = $('#win_div').window({
		title:toAddObj.title,
		width:toAddObj.width,
		height:toAddObj.height,
		collapsible:false,
		minimizable:false,
		maximizable:false,
		closabled:true,
		closed: false,
		draggable:true,
		resizable:false,
		cache:false,
		modal:true,
		onResize:function(){
			$(this).window('center');
		},
		onClose:function(){
			//$("#win_src").attr("src",commonPath+"/resources/jsp/kong.jsp");
		}
	});
	$("#win_src").attr("src",toAddObj.url);
}
//参数1--table的id,参数2执行删除操作url,依据删除的主键pid
function del_operate(delObj){
	var checkeds = $('#'+delObj.dataGridId).datagrid('getChecked');
	var length = checkeds.length;
	if(length == 0){
		$.messager.alert('提示','您至少要选择一条要删除的记录!','warning');
		return;
	}
	$.messager.confirm('提示', '您确定要删除所选中的记录吗?', function(flag){
		if(flag){
			var ids = '';
			for(var i=length-1;i>=0;i--){
				ids += checkeds[i][delObj.primaryKey]+",";
				var index = $('#'+delObj.dataGridId).datagrid('getRowIndex',checkeds[i]);
				$('#'+delObj.dataGridId).datagrid('deleteRow',index);
			}
			ids = ids.substr(0,ids.length-1); 
			//调用后台做删除
			$.ajax({
			   type: "post",
			   url: delObj.url,
			   data: 'ids='+ids,
			   async:false,
			   dataType:"json",
			   success: function(res){
			   	 //如果msg.flag为true说明成功
			   	 if(res!=null && res.success=="true"){
			   		//刷新列表
			   		queryData();
			     }
			   }
			});
		}
	});
}
function del_operate_edit(delObj){
	//遍历表格身
	var tbd={};
    var rows = $('#'+delObj.dataGridId).datagrid('getRows')//获取当前的数据行        
	for(var i = 0;i<rows.length;i++){
		tbd[rows[i][delObj.primaryKey]]=true; 
	}        
    //遍历表格头
	var thd=[];
	$(".datagrid-header-inner").find("input[type=checkbox][checked='checked']").each(function(){
		if($(this).val()!=""){
			thd.push($(this).val());
		}
	});
	//组装表格
	var tbl=[];
	for(j in tbd){ 
		if(tbd.hasOwnProperty(j)){
			for(var k=0;k<thd.length;k++){
				var obj={};
				obj.name=j;
				obj.yr=thd[k];
				tbl.push(obj);
			}
		}
	}
	if(tbl.length==0){
		$.messager.alert('提示','您至少要选择一条要删除的记录!','warning');
		return;
	}else{
		$.messager.confirm('提示', '您确定要删除所选中的记录吗?', function(flag){
			if(flag){
				//调用后台做删除
			}
		});
	}
}
//5个参数，参数1--->datagrid的id,参数2--->跳转的url,参数3--->window的宽度,参数4--->window的高度,参数5--->依据主键
function forward_upd(toUpdObj){
	var checkeds = $('#'+toUpdObj.dataGridId).datagrid('getChecked');
	var length = checkeds.length;
	if(length == 0){
		$.messager.alert('提示','请您选择要修改的记录!','warning');
		return;
	}
	if(length > 1){
		$.messager.alert('提示','您只能选择一条记录进行修改!','warning');
		return;
	}
	var id = checkeds[0][toUpdObj.primaryKey];
	var url="";
	if(toUpdObj.url.indexOf("?")>-1){
		url=toUpdObj.url+"&id="+id;
	}else{
		url=toUpdObj.url+"?id="+id;
	}
	$("#win_div").window({
		title:toUpdObj.title,
		width:toUpdObj.width,
		height:toUpdObj.height,
		collapsible:false,
		minimizable:false,
		maximizable:false,
		closable:false,
		closed: false,
		draggable:true,
		resizable:false,
		cache:false,
		modal:true,
		onResize:function(){
			$(this).window('center');
		},
		onClose:function(){
			$("#win_src").attr("src",commonPath+"/resources/jsp/kong.jsp");
		}
	});
	
	$("#win_src").attr("src",url);
}
//5个参数，参数1--->table的id,参数2--->显示的冰冻列名字,参数3--->显示的非冰冻列名字,参数4--->查询参数,参数5--->访问的url
//-------->纯div控制布局
function onLoadDataGrid(qryListObj) {
	//点击左侧url菜单,右侧弹出的的整个tab页面高度
	var Height_Page = $("html").height();
	var datagrid_title_height = $("#datagrid_div").position().top;
	var datagrid_note_heigth = $("#datagrid_note").height();
	var height = Height_Page - datagrid_title_height - datagrid_note_heigth - 5;
	
	//加载datagrid
	$('#'+qryListObj.dataGridId).datagrid({    	 
         autoHeight:true,
         height:height,
         frozenColumns:qryListObj.frozenCols,
         columns: qryListObj.cols,
         queryParams:qryListObj.queryParams,
         url:qryListObj.url,
         pagination: qryListObj.hasOwnProperty('pagination')?qryListObj.pagination:true,
         rownumbers: qryListObj.hasOwnProperty('rownumbers')?qryListObj.rownumbers:true,
         checkOnSelect:qryListObj.hasOwnProperty('checkOnSelect')?qryListObj.checkOnSelect:true,
         remoteSort: qryListObj.hasOwnProperty('remoteSort')?qryListObj.remoteSort:false,
         singleSelect:qryListObj.hasOwnProperty('singleSelect')?qryListObj.singleSelect:false,
         onLoadSuccess : qryListObj.hasOwnProperty('onLoadSuccess')?qryListObj.onLoadSuccess:function(data) {
                 //合并行
                 if (qryListObj.mergeColNames != "" && qryListObj.mergeColNames != null && qryListObj.mergeColNames.length > 0) {
                     for (var i = 0; i < qryListObj.mergeColNames.length; i++) {
                         $('#' + qryListObj.dataGridId).datagrid('mergeCellsByField', qryListObj.mergeColNames[i]);
                     }
                 }
                 //以下代码是调用数据追溯展示功能
                 if(undefined!=qryListObj.initTooltip&&undefined!=qryListObj.tabName){
                	 qryListObj.initTooltip(qryListObj.tabName,qryListObj.dataGridId);
                 }
 		 }
//         onLoadSuccess: function(data){
//				//console.log(this);
//				//datagrid头部 table 的第一个tr 的td们，即columns的集合
//		        var theads = $(".datagrid-header table tr:first-child").children();
//		        //datagrid主体 table 的第一个tr 的td们，即第一个数据行
//		        var tbodys = $(".datagrid-body table tr:first-child").children();
//		        //循环设置宽度
//		        tbodys.each(function (i, obj) {
//		            var theadTd = $(theads.get(i));
//		            var tbodyTd = $(tbodys.get(i));		            
//		            var theadTdWidth = theadTd.width();
//		            var tbodyTdWidth = tbodyTd.width();		            
//		            //如果头部列名宽度比主体数据宽度宽，则它们的宽度都设为头部的宽度。
//		            if (theadTdWidth > tbodyTdWidth) {
//		            	theadTd.width(theadTdWidth);
//		            	tbodyTd.width(theadTdWidth);
//		            } else {
//		            	theadTd.width(tbodyTdWidth);
//		            	tbodyTd.width(tbodyTdWidth);
//		            }					
//					//return false;(只设置第一行数据宽度)					
//		        });
//		    }		
    });
	
}
/**
 * 行底有统计行的datagrid
 * @param {Object} qryListObj
 */
function onLoadDataGridFooter(qryListObj) {
		//点击左侧url菜单,右侧弹出的的整个tab页面高度
		var Height_Page = $("html").height();
		var datagrid_title_height = $("#datagrid_div").position().top;
		var datagrid_note_heigth=$("#datagrid_note").height();
		var height= Height_Page-datagrid_title_height-datagrid_note_heigth-5;
		//重写datagrid
		var myview = $.extend({}, $.fn.datagrid.defaults.view, { 
	    renderFooter: function(target, container, frozen){ 
	        var opts = $.data(target, 'datagrid').options; 
	        var rows = $.data(target, 'datagrid').footer || []; 
	        var fields = $(target).datagrid('getColumnFields', frozen); 
	        var table = ['<table class="datagrid-ftable" cellspacing="0" cellpadding="0" border="0"><tbody>']; 
	          for(var i=0; i<rows.length; i++){ 
	            var styleValue = opts.rowStyler ? opts.rowStyler.call(target, i, rows[i]) : ''; 
	            var style = styleValue ? 'style="' + styleValue + '"' : ''; 
	            table.push('<tr class="datagrid-row" datagrid-row-index="' + i + '"' + style + '>'); 
	           	table.push(this.renderRow.call(this, target, fields, frozen, i, rows[i])); 
	            table.push('</tr>'); 
	        } 
	        table.push('</tbody></table>'); 
	        $(container).html(table.join('')); 
	    } 
	});

	
	//加载datagrid
	$('#'+qryListObj.dataGridId).datagrid({    	 
         autoHeight:true,
         height:height,
         frozenColumns:qryListObj.frozenCols,
         columns: qryListObj.cols,
         queryParams:qryListObj.queryParams,
         url:qryListObj.url,
         pagination: true,
         rownumbers: true,
		// sortName: 'xmjd',
		// sortOrder: 'desc',
		 remoteSort: false,
         checkOnSelect:true,
         singleSelect:false,
		 view :myview,//如果有属性showFooter设置背景色要重写datagrid 
		 showFooter:qryListObj.showFooter,//定义是否显示行底（如果是做统计表格，这里可以显示总计等）
		 pageList: [10,30,50],
		 rowStyler: function(index,row){//设置背景色
             if (typeof(styleField) != 'undefiend' && styleField != '') {
                 if (row[styleField] == "总计:" || row[styleField] == "本页小计:") {
                     return 'color:block;font-weight:bolder;';
                 }
             }
			},
            onLoadSuccess: function(data) {
                //合并行
                if (qryListObj.mergeColNames != "" && qryListObj.mergeColNames != null && qryListObj.mergeColNames.length > 0) {
                    for (var i = 0; i < qryListObj.mergeColNames.length; i++) {
                        $('#' + qryListObj.dataGridId).datagrid('mergeCellsByField', qryListObj.mergeColNames[i]);
                    }
                }
                if (qryListObj.showFooter) {
                    //获取固定列showFooter的div的class
                    var $datagridView1 = $('.datagrid-view1');
                    //获取showFooter的table下的tr
                    var footRowsView1 = $datagridView1.find('.datagrid-ftable tbody > tr');
                    //并对获取的行循环
                    $.each(footRowsView1, function(r, obj) {
                        //获取每行的td
                        var tds = footRowsView1.eq(r).find("td");
                        //对td进行颜色设置
                        for (var j = 0; j < tds.length; j++) {
                            var td = tds.eq(j);
                            if (j == 0) {
                                td.css({
                                    'border-right-color': '#ffffff'
                                });
                            } else if (j == tds.length - 1) {
                                td.css({
                                    'border-left-color': '#ffffff'
                                });
                            } else {
                                td.css({
                                    'border-left-color': '#ffffff',
                                    'border-right-color': '#ffffff'
                                });
                            }
                        }
                    });
                }
            }
	});
}

//5个参数，参数1--->table的id,参数2--->显示的冰冻列名字,参数3--->显示的非冰冻列名字,参数4--->查询参数,参数5--->访问的url
//-------->layout控制布局
function onLoadDataGridLayOut(qryListLayoutObj){
	//点击左侧url菜单,右侧弹出的的整个tab页面高度
	var Height_Page = $("html").height();
	var datagrid_title_height = $("#datagrid_div").position().top;
	var datagrid_note_heigth=$("#datagrid_note").height();
	var height= Height_Page-datagrid_title_height-datagrid_note_heigth-5;
	//加载布局
	$("#datagrid_div").layout('resize',{
		width: $("#btn_div").width(),
		height: height
	});
	$('#datagrid_div').layout('collapse','east');
	//加载datagrid
	$('#'+qryListLayoutObj.dataGridId).datagrid({    	 
         autoHeight:true,
         height:height,
         frozenColumns:qryListLayoutObj.frozenCols,
         columns: qryListLayoutObj.cols,
         queryParams:qryListLayoutObj.queryParams,
         rownumbers: true,
         remoteSort: false,
         onLoadSuccess : function(data) {
			//合并行
			if(qryListLayoutObj.mergeColNames!=""&&qryListLayoutObj.mergeColNames!=null&&qryListLayoutObj.mergeColNames.length>0){
				for(var i=0;i<qryListLayoutObj.mergeColNames.length;i++){
					$('#'+qryListLayoutObj.dataGridId).datagrid('mergeCellsByField', qryListLayoutObj.mergeColNames[i]);
				}
			}
		 },
         url:qryListLayoutObj.url,
         checkOnSelect:true,
         singleSelect:false
    });
	
}
//6个参数，参数1--->table的id,参数2--->显示的冰冻列名字,参数3--->显示的非冰冻列名字,参数4--->查询参数,参数5--->访问的url,参数6--->要合并的单元格列名数组
//-------->编辑单元格控制布局
function onLoadDataGridEditCell(qryListEditCellObj,classObj){
	var Height_Page = $("html").height();
	var datagrid_title_height = $("#datagrid_div").position().top;
	var datagrid_note_heigth=$("#datagrid_note").height();
	var height= Height_Page-datagrid_title_height-datagrid_note_heigth-5;
	$('#'+qryListEditCellObj.dataGridId).datagrid({    	 
         autoHeight:true,
         height:height,
         frozenColumns:qryListEditCellObj.frozenCols,
         columns: qryListEditCellObj.cols,
         queryParams:qryListEditCellObj.queryParams,
         url:qryListEditCellObj.url,
         rownumbers: true,
         remoteSort: false,
         singleSelect:false,
         checkOnSelect:true,
         onLoadSuccess : function(data) {
			//合并行
			if(qryListEditCellObj.mergeColNames!=""&&qryListEditCellObj.mergeColNames!=null&&qryListEditCellObj.mergeColNames.length>0){
				for(var i=0;i<qryListEditCellObj.mergeColNames.length;i++){
					$('#'+qryListEditCellObj.dataGridId).datagrid('mergeCellsByField', qryListEditCellObj.mergeColNames[i]);
				}
			}
    	 },
         onClickCell: function(index,field,value){
    		//结束上次编辑
    		if (classObj.editIndex != undefined){
    			$('#'+qryListEditCellObj.dataGridId).datagrid('endEdit',classObj.editIndex);
    		}
    		//获取解冻列
			var fields = $('#'+qryListEditCellObj.dataGridId).datagrid('getColumnFields');
			for(var i=0; i<fields.length; i++){
				var col = $('#'+qryListEditCellObj.dataGridId).datagrid('getColumnOption', fields[i]);
				col.editor1 = col.editor;
				if (fields[i] != field){
					col.editor = null;
				}
			}
			//开始编辑
			$('#'+qryListEditCellObj.dataGridId).datagrid('beginEdit',index);
			for(var i=0; i<fields.length; i++){
				var col = $(this).datagrid('getColumnOption', fields[i]);
				col.editor = col.editor1;
			}
			//全局变量赋值
     		classObj.editIndex = index;
     		//合并行
     		if(qryListEditCellObj.mergeColNames!=""&&qryListEditCellObj.mergeColNames!=null&&qryListEditCellObj.mergeColNames.length>0){
				for(var i=0;i<qryListEditCellObj.mergeColNames.length;i++){
					$('#'+qryListEditCellObj.dataGridId).datagrid('mergeCellsByField', qryListEditCellObj.mergeColNames[i]);
				}
			}
		 }
		 
    });
}
//1个参数，访问的url
function init_operate(url){
	$.ajax({
		type: "POST",
		url:url,
		dataType:"json",
		success:function(item){
			//赋值开始
	    	$("#paramsForm input,textarea").each(function(){
	    		var attrName = $(this).attr('id');//id
	        	if(attrName!=null && attrName!=""){
	        		var upperCaseAttrName = attrName.toUpperCase();//将id转换成大写
	        		var clas= $(this).attr('class');//用于区分是否是combo
	        		if(clas!=null && clas!=""){
	        			if(clas.indexOf("combotreeComponent")>=0){//combotree
	                		$(this).combotree('setValue',item[upperCaseAttrName]);
	                	}else if(clas.indexOf("comboboxComponent")>=0){//combobox
	                		$(this).combobox('setValue',item[upperCaseAttrName]);
	                	
	                	}else if(clas.indexOf("comboComponentarr")>=0){//combobox
	                		if(item[upperCaseAttrName]!=null){
	                			$(this).combobox('setValues',item[upperCaseAttrName].split(","));
	                		}
                	
	                	}else if(clas.indexOf("comboComponent")>=0){//combobox
	                		
	                		$(this).combobox('setValue',item[upperCaseAttrName]);
	                	
	                	}else if(clas.indexOf("easyui-numberspinner")>=0){//combobox
	                		
	                		$(this).numberspinner('setValue',item[upperCaseAttrName]);
	                	
	                	}
	                	else if(clas.indexOf("validatebox")>=0){//修改的时候因为加验证会把其class样式改变
	                		$(this).val(item[upperCaseAttrName]);
	                	}else if(clas.indexOf("easyui-datebox")>=0){//日期
		            		inputVal = $(this).datebox('setValue',item[upperCaseAttrName]);
		            	}else if(clas.indexOf("easyui-datetimebox")>=0){
		            		inputVal = $(this).datetimebox('setValue',item[upperCaseAttrName]);

		            	}
	        		}else{
	        			$(this).val(item[upperCaseAttrName]);//普通文本
	        		}
	            }
	    	});
	    	//赋值结束
		}
	});
}
//3个参数-->参数1--属性所在form表单id,参数2执行访问操作url,参数3父页面的datagrid_id
function save_operate(saveObj){

	//验证消息开始
	$('.validatebox-text').each(function(i,obj){
		$(this).validatebox('enableValidation').validatebox('validateTip');
	});
	var tipsStr = $("#msg").html();
	var tipsArr = tipsStr.split(",");
	$("#msg").html(tipsArr[1]);
	
	var flag=$('#'+saveObj.formId).form('validate');
	//动态创建表格
	var f=document.getElementById(saveObj.formId);  
	//验证消息结束
	//参数封装
	$(".comboComponent").each(function(){
		var inputVal = $(this).combobox("getValue");
		var attrName = $(this).attr('id');
		if(inputVal!=""){
			$("input[name="+attrName+"]").val(inputVal);
			var piPeiInput = document.createElement("input");   
			piPeiInput.type="hidden";   
			piPeiInput.name=attrName;
			piPeiInput.value=inputVal;
			f.appendChild(piPeiInput);
		}
	});
	var chuansongVal;
	var fmserialObj =  $("#"+saveObj.formId).serialize();
	if(saveObj.strName==null || saveObj.strName==""){
		chuansongVal = fmserialObj;
	}else{
		var strParams =fmserialObj.split("&");
		var objParams = {};
		for(var i=0;i<strParams.length;i++){
			var objParam = strParams[i];
			var objName = objParam.substring(0,objParam.indexOf("="));
			var objValue = objParam.substring(objParam.indexOf("=")+1,objParam.length);
		}
		var strName = saveObj.strName;
		chuansongVal= {strName:encodeURIComponent(JSON.stringify(objParams))};
	}
	if(flag){
		$.ajax({
			type:"post",
			url:saveObj.url,
			data:chuansongVal,
			dataType:"json",
			success:function(res){
			    if(res!=null && res.success=="true"){
					$.messager.alert('提示','保存成功!','info',function(){
						//关闭窗口
						window.parent.$("#win_src").attr("src",commonPath+"/resources/jsp/kong.jsp");
						window.parent.$('#win_div').window('close');
						window.parent.$("#"+saveObj.dataGridId).datagrid('reload');
					});
				}
			}
		});
	}
	
}
//3个参数-->参数1--属性所在form表单id,参数2执行访问操作url,参数3父页面的datagrid_id
function update_operate(updObj){
	
	//验证消息开始
	$('.validatebox-text').each(function(i,obj){
		$(this).validatebox('enableValidation').validatebox('validateTip');
	});
	var tipsStr = $("#msg").html();
	var tipsArr = tipsStr.split(",");
	$("#msg").html(tipsArr[1]);
	
	var flag=$('#'+updObj.formId).form('validate');
	
	//动态创建表格
	var f=document.getElementById(updObj.formId);  
	//验证消息结束
	
	//参数封装
	$(".combotreeComponent").each(function(){
		var inputVal = $(this).combobox("getValues");
		var attrName = $(this).attr('id');
		if(inputVal!=""){
			$("input[name="+attrName+"]").val(inputVal);
			var piPeiInput = document.createElement("input");   
			piPeiInput.type="hidden";   
			piPeiInput.name=attrName;
			piPeiInput.value=inputVal;
			f.appendChild(piPeiInput);
		}
	});
	//参数封装
	$(".comboboxComponent").each(function(){
		var inputVal = $(this).combobox("getValues");
		var attrName = $(this).attr('id');
		if(inputVal!=""){
			$("input[name="+attrName+"]").val(inputVal);
			var piPeiInput = document.createElement("input");   
			piPeiInput.type="hidden";   
			piPeiInput.name=attrName;
			piPeiInput.value=inputVal;
			f.appendChild(piPeiInput);
		}
	});
	
	var chuansongVal;
	var fmserialObj =  $("#"+updObj.formId).serialize();
	if(updObj.strName==null || updObj.strName==""){
		chuansongVal = fmserialObj;
	}else{
		var strParams =fmserialObj.split("&");
		var objParams = {};
		for(var i=0;i<strParams.length;i++){
			var objParam = strParams[i];
			var objName = objParam.substring(0,objParam.indexOf("="));
			var objValue = objParam.substring(objParam.indexOf("=")+1,objParam.length);
		}
		var strName = saveObj.strName;
		chuansongVal= {strName:encodeURIComponent(JSON.stringify(objParams))};
	}
	
	if(flag){
		$.ajax({
			type:"post",
			url:updObj.url,
			data:chuansongVal,
			dataType:"json",
			success:function(res){
			    if(res!=null && res.success=="true"){
					$.messager.alert('提示','保存成功!','info',function(){
						//关闭窗口
						window.parent.$("#win_src").attr("src",commonPath+"/resources/jsp/kong.jsp");
						window.parent.$('#win_div').window('close');
						window.parent.$("#"+updObj.dataGridId).datagrid('reload');
					});
				}
			}
		});
	}
	
}

