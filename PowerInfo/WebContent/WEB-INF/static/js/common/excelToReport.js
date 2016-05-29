$(function(){
	/**
	 * Datagrid粘贴方法
	 */
	var getCopyInfo = function(){
		//获取粘贴板文本信息
		var copyInfo=window.clipboardData.getData("text"); 
		//如果文本信息为空
		if(copyInfo == null){
			 $.messager.alert('提示', '无文本粘贴信息');
			return false;
		}
		//创建cellFiels数组，用来存放field和field正则信息
		var cellFields = [];
		var columnFields1 = $('#report_datagrid').datagrid('getColumnFields', true);
		var columnFields2 = $('#report_datagrid').datagrid('getColumnFields');       // 获取冻结列
		var columnFields = $.merge(columnFields1,columnFields2);
		//循环表头
		$.each(columnFields,function(i,v){
				var field = v;//获取field
				var cell = {};
				cell.field = field;
				//获取当前field的编辑信息
				var e = $('#report_datagrid').datagrid('getColumnOption', field).editor;
				if(e != null && e.options){//如果存在编辑信息并且有配置信息
					var validInfo = e.options.validType;//那么就获取当前field的正则信息
					if(validInfo){//如果存在正则信息
						//因为获取到的正则信息为 regValidator[正则表达式] 如：regValidator[/^(0|[1-9][0-9]{0,5})(\.[0-9]{1,4})?$/]
						//那么就截取正则表达式内容
						cell.validInfo = eval(validInfo.substring(13,validInfo.length-1));
					}else{
						cell.validInfo = null;
					}
				}
				//将field信息和正则表达式信息放入cellFields中
				cellFields[cellFields.length]=cell;
				
		});
		//将获取到的粘贴信息切割行
		var copys = copyInfo.split('\n');
		//获取Datagrid的数据信息
		var data = $('#report_datagrid').datagrid('getData');
		var len = data.total;
		//创建错误对象，用来存放粘贴数据的错误信息，存放错误对象为index：错误单元格在第几行，field错误单元格的field
		var errorInfo = [];
		//循环copy的行
		$.each(copys,function(i,v){
			//将当前切割成td
			var rows = copys[i].split('\t');
			//循环copy到的td
			$.each(rows,function(k,j){
				//如果粘贴的td超过了当前Datagrid的td那么就继续下一行的循环
				if(k>=cellFields.length){
					return false;
				}
				//获取field的正则表达式
				var valid = cellFields[k].validInfo;
				//copy到td的内容
				var cell = $.trim(rows[k]);
				//如果cell不为空，并且验证为错误数据类型
				if(cell.length > 0 && valid && !valid.test(cell)){
					var errorCell = {}
					errorCell.index= i;//获取到行号
					errorCell.field= cellFields[k].field;//获取错误的单元格的field
					errorInfo[errorInfo.length] = errorCell;//放入错误对象中
				} 
				//将单元的信息放入Datagrid的数据中
				$(data.rows[i]).attr(cellFields[k].field,cell);
			})
//			if(i>=len-1){
//				return false;
//			}
		});
		//重新load Datagrid的数据
		 $('#report_datagrid').datagrid('loadData',data);
		 //循环错误信息，标记错误单元格(禁填单元格不标记)
		 $.each(errorInfo,function(i,v){
			 var index = v.index;
			 var field = v.field;
			 var cell = $('tr[datagrid-row-index='+ index +']').find('td[field='+field+']');
			 //如果是不是禁填的单元格 那么就标记为错误
			 if($(cell).attr('disablecell')!='true'){
				 $(cell).attr('error','true');
				 $(cell).css('background', 'red');
			 }
		 });
		 $('#tool_zhantie').attr('saveAll','true');
	}
	var initUI = function(){
		$('#btn_div').delegate('#tool_zhantie','click',getCopyInfo);
	}
	initUI();
});