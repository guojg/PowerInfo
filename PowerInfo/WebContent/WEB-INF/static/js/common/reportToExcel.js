$(function(){
	var fieldsInfo = [];//获取单元格的field 供解析Datagrid tbody使用
	var weidu = {};
	weidu.index = 1;
	weidu.colspan = 1;
	weidu.rowspan = 1;
	weidu.bg = true;
	weidu.text="维度";

	var mergeTable = function(tableClass){
		fieldsInfo = [];		//获取单元格的field
		var tableList = [];		//创建表格JSON对象
		var endMergeCells = [];	//创建跨行对象信息
		var maxTrNumber = 0;	//因为datagrid 包含多个table，所以要获取最大的行数用来判断跨行和创建表格JSON对象
		
		
		//-----------------初始化需要合并表格的参数信息-----------------
		//根据传递过来的table class标识来 循环这些table 获取最大的行数
		$.each($('.'+tableClass),function(i,tab){
			//当前table的总行数大于maxTrNumber吗？如果大于那么maxTrNumber就等于最大的table行数
			maxTrNumber = maxTrNumber < $(this).find('tr').length ? $(this).find('tr').length : maxTrNumber;
		});
		//根据获取到的最大行数来初始化表格JSON对象集合,和跨行对象信息集合
		//maxTrNumber+1 这个地方加1 是再多创建一行用来存放维度信息
		for(var i = 0; i < maxTrNumber+1; i++){
			tableList[tableList.length] = [];		//初始化表格JSON对象
			endMergeCells[endMergeCells.length] = [];//初始化跨行对象
		}
		//循环表头的所有Table，转换成tableList对象信息
		tableList[tableList.length-1][0] = weidu; //表格对象最后一行的第一列 设置为 维度 cell
		//-----------------初始化需要合并表格的参数信息结束------------------
		
		
		//循环表头所有table 转换成tableList对象信息
		for(var t = 0; t < $('.'+tableClass).length; t++){
		$.each($('.'+tableClass+':eq('+t+') tr'),function(i,tr){
			var trCount = $(this).parents('table').find('tr').length;	//获取当前Table有多少行
			var tdCount = $(this).find('td').length;					//当前行有多少列
			//循环当前行的所有列
			for(var j = 0; j < tdCount; j++){
				var cellJSON = {};//创建单元格对象
				var rowspan = $(this).find('td').eq(j).attr('rowspan');	//获取跨行数
				var colspan = $(this).find('td').eq(j).attr('colspan'); //获取跨列数
				if(typeof(rowspan) == 'undefined'){				//如果不跨行
					rowspan = 1;
				}
				
				if(trCount < maxTrNumber){//如果maxTrNumber大于当前Table行数
					rowspan = maxTrNumber/trCount;	//那么当前单元格的跨行数是	maxTrNumber/trCount;
					if(maxTrNumber%trCount>0 && j==(tdCount-1)){//如果有模
						rowspan = maxTrNumber%trCount; //那么当前单元格的跨行数是 maxTrNumber%trCount
					}
				}else{
					rowspan = parseInt(rowspan);
				}
				
				if(typeof(colspan) == 'undefined'){				//如果过跨列
					colspan = 1;
				}else{
					colspan = parseInt(colspan);
				}
				
				var text = $.trim($(this).find('td').eq(j).text());		//该单元格的文本内容
				var index = 1;				//单元格的索引，默认是1
				if(tableList[i].length > 0){ 
					//tableList[i].length 如何当前行的当前单元格之前存在单元格，
					//那么index就等于自己前一个单元格的index + colspan;
					index = tableList[i][tableList[i].length-1].index + tableList[i][tableList[i].length-1].colspan;
				}
				//查看endMergeCells是否有跨行到了当前单元格的
				$.each(endMergeCells[i],function(k,v){
					if(v.index == index){
						index = v.index + v.colspan;
						return false;
					}
				});
				if(rowspan > 1){//如果跨了多行
						endMergeCells = setMegerCells(endMergeCells,rowspan,colspan,i,index);
				}
				//-----------------单元信息对方封装 开始--------------------
				cellJSON.index = index;
				cellJSON.rowspan = rowspan;
				cellJSON.colspan = colspan;
				cellJSON.text = text;
				tableList[i][tableList[i].length] = cellJSON;
				//封装维度单元格信息
				if($(this).find('td').eq(j).attr('field')){
					var fi = {};
					fi.field = $(this).find('td').eq(j).attr('field');
					fi.index = index;
					fieldsInfo[fieldsInfo.length] = fi;
					fi.colspan = colspan;
					fi.rowspan = 1;
					fi.text = fi.field;
					fi.bg = true;
					tableList[tableList.length-1][tableList[tableList.length-1].length] = fi;
				}
				//-----------------单元信息对方封装 结束--------------------
			}
			
		});
		}
		return tableList;
	};
	
	var setMegerCells = function(endMergeCells,rowspan,colspan,rowNumber,index){
		for(var i = 1 ; i < rowspan; i++){		//循环跨行数
			var hasMergerCell = false;	//是否有连续合并的单元格
			var cell = {}	;		//创建跨行的单元格对象
			if(endMergeCells[rowNumber+i].length == 0){	//如果当前行的下一行不存在跨行
				cell.index = index;						//那么跨行的索引就等于index
				cell.colspan = colspan;					//跨列数
			}else{//如果当前行的下一行存在跨行
				var lastIndex = endMergeCells[rowNumber+i].length > 1 ? endMergeCells[rowNumber+i].length-1 : 0;
				//获取最后一组单元格的index
				var cIndex = endMergeCells[rowNumber+i][lastIndex].index;
				//获取最后一组单元格的cColspan
				var cColspan = endMergeCells[rowNumber+i][lastIndex].colspan;
				//如果cIndex + cColspan == index那么就说明这一个单元格和他前面一个单元格是连续跨行
				if(cIndex + cColspan ==index){
					cell.index = cIndex;	//那么就把这几个连续跨行的单元格看作是一个单元格
					cell.colspan = cColspan + colspan;
					hasMergerCell = true;
				}else{//如果没有连续跨行
					cell.index = index;
					cell.colspan = colspan;
				}
			}
			if(!hasMergerCell){//如果有连续合并的单元格那么合并单元格对象做更新
				endMergeCells[rowNumber+i][endMergeCells[rowNumber+i].length] = cell;
			}else{//否则做新增
				endMergeCells[rowNumber+i][endMergeCells[rowNumber+i].length-1] = cell;
			}
		}
		return endMergeCells;
	}
	var mergeBTable = function(){
		var tHeadInfo = [];
		var data = $('#datagrid').datagrid('getData');
		var mergeRules = data.mergeRules;
		var rows = data.rows;
		
		
		//循环rows
		for(var i = 0; i < rows.length; i++){
			var hdmsCell = {};
			hdmsCell.index = 1;
			hdmsCell.colspan = 1;
			hdmsCell.rowspan = 1;
			hdmsCell.text=rows[i].row_index;
			hdmsCell.bg = true;
			tHeadInfo[i] = [];
			tHeadInfo[i][0] = hdmsCell;
			//根据fields 获取数据 和 合并规则
			for(var j = 0; j < fieldsInfo.length; j++){
				if($('tr[datagrid-row-index='+ i +']').find('td[field='+fieldsInfo[j].field+']').css('display')=='none'){
					continue;
				}
				var text = "";
				var index = fieldsInfo[j].index;
				var rowspan = $('tr[datagrid-row-index='+ i +']').find('td[field='+fieldsInfo[j].field+']').attr('rowspan');
				var colspan = $('tr[datagrid-row-index='+ i +']').find('td[field='+fieldsInfo[j].field+']').attr('colspan');
				if(!rowspan){
					rowspan = 1;
				}
				if(!colspan){
					colspan = 1;
				}
				if($(rows[i]).attr(fieldsInfo[j].field)){//如果行数据
					text = $(rows[i]).attr(fieldsInfo[j].field);
				}
				index = fieldsInfo[j].index;
				var cellModel = {};
				cellModel.text = text+"";
				cellModel.index = index;
				cellModel.rowspan = rowspan;
				cellModel.colspan = colspan;
				tHeadInfo[i][tHeadInfo[i].length] = cellModel;
			}
		}
		return tHeadInfo;
	}
	
	
	/**
	 * 导出事件
	 */
	var dataGridToTable = function(){

		var titleList = [];			//Datagrid 数据对象集合
		var htitle = mergeTable('datagrid-htable');	//DataGrid 表头对象集合
		var btitle = mergeBTable(); //DataGrid 表内容对象集合
		titleList = $.merge(htitle,btitle);			//DataGrid 表头和表内容对象集合合并
		//createExcelXml(titleList);				//如果不调用后台 调用页面导出就用这个方法
		var titleInfo = JSON.stringify(titleList);	//将DataGrid 集合对象转换为JSONS
		var tabName = $.trim($('#datagrid_title').text()); //表名称
		var remarks = $('#datagrid_note ul').eq(1).html(); //表备注
        if(remarks){
            remarks = remarks.replace(/<\/LI>\W+<LI>/ig,'\r\n');
            remarks = remarks.replace('<LI>','');
            remarks = remarks.replace('</LI>','');
        }
		var dataModel = {};							//传递到后台的数据封装
		dataModel.tabName = encodeURIComponent(encodeURIComponent(tabName)); //表名 转码
		dataModel.tabTitle = encodeURIComponent(encodeURIComponent(titleInfo)); //表内容转码
		if($.trim(remarks) != ''){	//如果表备注不为空那么就设置表备注
			dataModel.remarks = encodeURIComponent(encodeURIComponent(remarks));
		}
		var url = basePath+"export/index";//后台请求的URL
        var form = $("<form>");  //==>$.ajax不能下载, 所以创建隐藏表单  
        form.attr('style','display:none');    
        form.attr('target','');    
        form.attr('method','post');    
        form.attr('action',url);    
        form.append('<textarea name="tabTitle">'+dataModel.tabTitle+'</textarea>');
        form.append('<input name="tabName" value="'+dataModel.tabName+'" />');
        if($.trim(remarks) != ''){
        	form.append('<input name="remarks" value="'+dataModel.remarks+'" />');
        }
        $('body').append(form);    
        form.submit();    
        form.remove(); 
	};
	
	function showProgressBar(){
	    dataGridToTable();
	}
    $('#tool_export').bind('click',showProgressBar);//导出按钮事件绑定

});