$(function(){	
	//隐藏域
	function appendHidden(name,value){return "<input type='hidden' name='"+name+"' value='"+value+"' /><br>";}
	//汉字转码
	function encodeURIUtils(value){return encodeURIComponent(encodeURIComponent(value));}	
	//获取datagrid标题code与title与输出excel的相对应
	function getColNameAndCodes(frozenColumns,columns){		
		var obj = {colNames:"",colTitles:"",colIndex:""};
		var colNames = new Array(),colTitles = new Array();
		function getMulitRowsField(columns){
			//多行（跨列的情况）
			//3个head数据分别表示1行，2行，3行的字段集合
			var head1 = columns[0],head2 = null,head3=null;
			if(columns.length>1){head2=columns[1];}
			if(columns.length>2){head3=columns[2];}
			//cLen 第一行字段的个数
			//rowIndex表示显示的行数
			//colIndex表示从head2里取数据的下标
			//colIndex2表示从head3里去数据的下标
			var cLen = head1.length,rowIndex = 1,colIndex = 0,colIndex2 = 0;
			for ( var i = 0; i < cLen; i++) {
				var field = head1[i];
				//隐藏域与checkbox跳过不处理
				if(field["hidden"] || field["checkbox"]){continue;}
				var fc = field["field"];
				if(fc==null || fc=="" || fc==undefined){ 
					//跨列的字段field为null
					//取出跨列列数与head2数组的取值索引相加 获取当前列下边的列
					var _len = colIndex + parseInt(field["colspan"]);
					for ( var j = colIndex; j < _len ; j++) {
						var f2 = head2[j];
						if(f2==null || f2=="" || f2==undefined){continue;}
						var f2f = f2["field"];
						//如果head2的中的字段field为null表示跨列了
						if(f2f==null || f2f=="" || f2f==undefined ){
							//三行跨列处理
							var _len2 = colIndex2 + parseInt(f2["colspan"]);
							//二层跨列索引减小，防止访问其他列的垮列情况
							_len = _len - parseInt(f2["colspan"])+1;
							for(var n = colIndex2;n<_len2;n++){
								var f3 = head3[n];
								colNames.push(f3["field"]);
								//标题以“=”分割，后台有相应的规则程序处理 跨列
								colTitles.push(field["title"]+"="+f2["title"]+"="+f3["title"]);
							}colIndex2 = _len2;
						}else{
							//两行跨行处理
							colNames.push(f2["field"]);
							if(f2["rowspan"]){
							   colTitles.push(field["title"]+"="+f2["title"]+"="+f2["title"]);
							}else{
							   colTitles.push(field["title"]+"="+f2["title"]);}}
						}
					colIndex = _len;
				}else{
					colTitles.push(field["title"]);
					colNames.push(field["field"]);
				}}}
		if(frozenColumns && frozenColumns.length>0){
			//固定列处理
			getMulitRowsField(frozenColumns);
			//冻结列（有固定列的返回冻结索引）
			obj["colIndex"] = colNames.length;
		}
		//普通列
		getMulitRowsField(columns);
		obj["colNames"] = colNames.join(",");
		obj["colTitles"] = colTitles.join(",");
		return obj;
	}
	//取页面展示数据
	function getDataPanel(colNames,panel){
		colNames = colNames.split(",");
		//取固定数据div
		var body1 = $(panel).find("div.datagrid-view1 > div.datagrid-body");
		//取普通数据div
		var body2 = $(panel).find("div.datagrid-view2 > div.datagrid-body");
		//固定行
		var bodyRow1 = $(body1).find("tr.datagrid-row");
		//普通行
		var bodyRow2 = $(body2).find("tr.datagrid-row");
		var len = bodyRow1.length,retObj = new Array();
		for ( var i = 0; i < len; i++) {
			//合并固定行于普通行
			var row = $(bodyRow1[i]).html()+$(bodyRow2[i]).html();
			var cols = $("<tr>"+row+"</tr>").find("td");
			var clen = cols.length,rd = new Array();
			for ( var j = 0; j < clen; j++) {
				//取出显示的数据，以，分割，并进行二次加码
				var _field = $(cols[j]).attr("field");
				rd.push("\""+_field+"\":\""+$(cols[j]).text()+"\"");
			}
			retObj.push(appendHidden("pageCacheData",encodeURIUtils("{"+rd.join(",")+"}")));
		}
		return retObj.join('');
	}
	//datagrid导出方法及根据字段合并行方法
	$.extend($.fn.datagrid.methods,{
		exportList:function(jq,options){
			return jq.each(function(){
				options = options || {};
				//表单信息
				var form = new Array();
				var opts = $.data(this,"datagrid").options;
				var panel = $.data(this,"datagrid").panel;
				var columns = opts.columns;
				var frozenColumns =opts.frozenColumns;	
				//获取到列，以“,”分割obj["colNames"]，obj["colTitles"]
				var objCol = getColNameAndCodes(frozenColumns,columns);
				form.push(appendHidden("colNames",objCol["colNames"]));
				form.push(appendHidden("colTitles",encodeURIUtils(objCol["colTitles"])));
				form.push(appendHidden("colIndex",objCol["colIndex"]));
				if(options["isLockCol"]){
					form.push(appendHidden("isLockCol",options["isLockCol"]));
				}
				//取页面展示数据
				if(options["isPageData"]){
					//取页面显示的数据,数组的方式存储
					var data = getDataPanel(objCol["colNames"],panel);
					form.push(data);
					form.push(appendHidden("isPageData","1"));
				}				
				//其他配置参数
				//日期及字段转换
				if(options["convertDate"]){
					form.push(appendHidden("convertDate",options["convertDate"]));				 
				}
				//转换字段
				if(options["convertField"]){
					form.push(appendHidden("convertField",options["convertField"]));				 
				}
				//convertYn转换是否
				if(options["convertYn"]){
					form.push(appendHidden("convertYn",options["convertYn"]));				 
				}
				//excel标题
				if(!options["excelTitle"]){options["excelTitle"] = document.title;}				
				//单位
				if(!options["unitTitle"]){options["unitTitle"] = $("h5:contains('单位')").text();}
				//文件名称
				if(!options["fileName"]){options["fileName"] = options["excelTitle"];}
				form.push(appendHidden("excelTitle",encodeURIUtils(options["excelTitle"])));
				form.push(appendHidden("unitTitle",encodeURIUtils(options["unitTitle"])));
				form.push(appendHidden("fileName",encodeURIUtils(options["fileName"])));
				form.push(appendHidden("bizcName",options["bizcName"]));
				//mergeColumns合并列,暂时只支持纵向合并
				if(options["mergeColumns"]){
					//有合并行的情况，获取其列的索引，后台有相应程序进行合并处理
					var colNarr = objCol["colNames"].split(",");
					var mNarr = options["mergeColumns"].split(",");
					var mLen = mNarr.length;
					var s = "";
					for ( var i = 0; i < mLen; i++) {
						var _fi = $.inArray(mNarr[i],colNarr);
						s += i>0 ? "," : "";s += _fi;
					}
					form.push(appendHidden("mergeColumns",s));
				}
				//查询头参数处理
				var otherp = $.param(opts["queryParams"]);
				if(otherp){
					//转换datagrid中queryParams的参数
					var oarr = otherp.split("&");
					for(var i=0;i<oarr.length;i++){
						var tfd = oarr[i].split("=");
						form.push(appendHidden(decodeURIComponent(tfd[0]),tfd[1]));										
					}
				}
				if(options["url"]){
					//导出链接的处理
					if (options["url"].indexOf("?") > 0) {
						options["url"] = options["url"] + "&_t="+new Date().getTime();
					}else{
						options["url"] = options["url"] + "?_t="+new Date().getTime();
					}
				}else{
					$.messager.alert("提示","未传入执行导出的url");
					return false;
				}				
				//构造form提交
				var htmlForm = $("<FORM id='exportForm' method='post'></FORM>");
				htmlForm.append(form.join(''));
				htmlForm.attr("action",options["url"]);
				htmlForm.appendTo(document.body);
				htmlForm.submit();
				$("#exportForm").remove();				
			});}
	
		,mergeColumns:function(jq,mergeColumns){
			//合并行的方法
			mergeColumns = mergeColumns || [];
			if(mergeColumns!=null && mergeColumns.length>0){
				return jq.each(function(){
					//获取datagird的panel
					var panel = $.data(this,"datagrid").panel;
					//行数
					var dataLen = $.data(this,"datagrid").data.rows.length;
					//超过2行进行合并否则返回
					if(dataLen<2){return false;}
					var mLen = mergeColumns.length;
					//查找数据DIV
					var body = $(panel).find("div.datagrid-body");
					var colText = null,colNText=null,_field = null;;
					for(var i=0;i<mLen;i++){
						_field = mergeColumns[i];
						//根据字段获取要合并的列数据集合
						var td = $(body).find("td[field="+mergeColumns[i]+"]");
						var tdLen = td.length,mergeRIdx=null,mRowSpan=1;
						colText = $(td[0]).text();
						mergeRIdx = $(td[0]).parent().attr("datagrid-row-index");
						for ( var j = 1; j < tdLen; j++) {							
							colNText =$(td[j]).text();
							//取上一列与下一列比较有相同值，合并行++，否则就进行合并，且初始下一次的比较参数
							if(colText==colNText){
								mRowSpan++;
							}else{
								//值不相等的时合并上一次的比较
								if(mRowSpan!=1){
									$(this).datagrid("mergeCells",{
										field:mergeColumns[i],
										index:parseInt(mergeRIdx),
										rowspan:parseInt(mRowSpan)
									});
								}
								colText = $(td[j]).text();mRowSpan=1;
								mergeRIdx = $(td[j]).parent().attr("datagrid-row-index");								
							}
						}
						//合并最后一次的判断
						if(mRowSpan!=1){
							$(this).datagrid("mergeCells",{
								field:_field,
								index:parseInt(mergeRIdx),
								rowspan:parseInt(mRowSpan)
							});
						}}});}}
		});	
});