$(function(){	
	//������
	function appendHidden(name,value){return "<input type='hidden' name='"+name+"' value='"+value+"' /><br>";}
	//����ת��
	function encodeURIUtils(value){return encodeURIComponent(encodeURIComponent(value));}	
	//��ȡdatagrid����code��title�����excel�����Ӧ
	function getColNameAndCodes(frozenColumns,columns){		
		var obj = {colNames:"",colTitles:"",colIndex:""};
		var colNames = new Array(),colTitles = new Array();
		function getMulitRowsField(columns){
			//���У����е������
			//3��head���ݷֱ��ʾ1�У�2�У�3�е��ֶμ���
			var head1 = columns[0],head2 = null,head3=null;
			if(columns.length>1){head2=columns[1];}
			if(columns.length>2){head3=columns[2];}
			//cLen ��һ���ֶεĸ���
			//rowIndex��ʾ��ʾ������
			//colIndex��ʾ��head2��ȡ���ݵ��±�
			//colIndex2��ʾ��head3��ȥ���ݵ��±�
			var cLen = head1.length,rowIndex = 1,colIndex = 0,colIndex2 = 0;
			for ( var i = 0; i < cLen; i++) {
				var field = head1[i];
				//��������checkbox����������
				if(field["hidden"] || field["checkbox"]){continue;}
				var fc = field["field"];
				if(fc==null || fc=="" || fc==undefined){ 
					//���е��ֶ�fieldΪnull
					//ȡ������������head2�����ȡֵ������� ��ȡ��ǰ���±ߵ���
					var _len = colIndex + parseInt(field["colspan"]);
					for ( var j = colIndex; j < _len ; j++) {
						var f2 = head2[j];
						if(f2==null || f2=="" || f2==undefined){continue;}
						var f2f = f2["field"];
						//���head2���е��ֶ�fieldΪnull��ʾ������
						if(f2f==null || f2f=="" || f2f==undefined ){
							//���п��д���
							var _len2 = colIndex2 + parseInt(f2["colspan"]);
							//�������������С����ֹ���������еĿ������
							_len = _len - parseInt(f2["colspan"])+1;
							for(var n = colIndex2;n<_len2;n++){
								var f3 = head3[n];
								colNames.push(f3["field"]);
								//�����ԡ�=���ָ��̨����Ӧ�Ĺ�������� ����
								colTitles.push(field["title"]+"="+f2["title"]+"="+f3["title"]);
							}colIndex2 = _len2;
						}else{
							//���п��д���
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
			//�̶��д���
			getMulitRowsField(frozenColumns);
			//�����У��й̶��еķ��ض���������
			obj["colIndex"] = colNames.length;
		}
		//��ͨ��
		getMulitRowsField(columns);
		obj["colNames"] = colNames.join(",");
		obj["colTitles"] = colTitles.join(",");
		return obj;
	}
	//ȡҳ��չʾ����
	function getDataPanel(colNames,panel){
		colNames = colNames.split(",");
		//ȡ�̶�����div
		var body1 = $(panel).find("div.datagrid-view1 > div.datagrid-body");
		//ȡ��ͨ����div
		var body2 = $(panel).find("div.datagrid-view2 > div.datagrid-body");
		//�̶���
		var bodyRow1 = $(body1).find("tr.datagrid-row");
		//��ͨ��
		var bodyRow2 = $(body2).find("tr.datagrid-row");
		var len = bodyRow1.length,retObj = new Array();
		for ( var i = 0; i < len; i++) {
			//�ϲ��̶�������ͨ��
			var row = $(bodyRow1[i]).html()+$(bodyRow2[i]).html();
			var cols = $("<tr>"+row+"</tr>").find("td");
			var clen = cols.length,rd = new Array();
			for ( var j = 0; j < clen; j++) {
				//ȡ����ʾ�����ݣ��ԣ��ָ�����ж��μ���
				var _field = $(cols[j]).attr("field");
				rd.push("\""+_field+"\":\""+$(cols[j]).text()+"\"");
			}
			retObj.push(appendHidden("pageCacheData",encodeURIUtils("{"+rd.join(",")+"}")));
		}
		return retObj.join('');
	}
	//datagrid���������������ֶκϲ��з���
	$.extend($.fn.datagrid.methods,{
		exportList:function(jq,options){
			return jq.each(function(){
				options = options || {};
				//����Ϣ
				var form = new Array();
				var opts = $.data(this,"datagrid").options;
				var panel = $.data(this,"datagrid").panel;
				var columns = opts.columns;
				var frozenColumns =opts.frozenColumns;	
				//��ȡ���У��ԡ�,���ָ�obj["colNames"]��obj["colTitles"]
				var objCol = getColNameAndCodes(frozenColumns,columns);
				form.push(appendHidden("colNames",objCol["colNames"]));
				form.push(appendHidden("colTitles",encodeURIUtils(objCol["colTitles"])));
				form.push(appendHidden("colIndex",objCol["colIndex"]));
				if(options["isLockCol"]){
					form.push(appendHidden("isLockCol",options["isLockCol"]));
				}
				//ȡҳ��չʾ����
				if(options["isPageData"]){
					//ȡҳ����ʾ������,����ķ�ʽ�洢
					var data = getDataPanel(objCol["colNames"],panel);
					form.push(data);
					form.push(appendHidden("isPageData","1"));
				}				
				//�������ò���
				//���ڼ��ֶ�ת��
				if(options["convertDate"]){
					form.push(appendHidden("convertDate",options["convertDate"]));				 
				}
				//ת���ֶ�
				if(options["convertField"]){
					form.push(appendHidden("convertField",options["convertField"]));				 
				}
				//convertYnת���Ƿ�
				if(options["convertYn"]){
					form.push(appendHidden("convertYn",options["convertYn"]));				 
				}
				//excel����
				if(!options["excelTitle"]){options["excelTitle"] = document.title;}				
				//��λ
				if(!options["unitTitle"]){options["unitTitle"] = $("h5:contains('��λ')").text();}
				//�ļ�����
				if(!options["fileName"]){options["fileName"] = options["excelTitle"];}
				form.push(appendHidden("excelTitle",encodeURIUtils(options["excelTitle"])));
				form.push(appendHidden("unitTitle",encodeURIUtils(options["unitTitle"])));
				form.push(appendHidden("fileName",encodeURIUtils(options["fileName"])));
				form.push(appendHidden("bizcName",options["bizcName"]));
				//mergeColumns�ϲ���,��ʱֻ֧������ϲ�
				if(options["mergeColumns"]){
					//�кϲ��е��������ȡ���е���������̨����Ӧ������кϲ�����
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
				//��ѯͷ��������
				var otherp = $.param(opts["queryParams"]);
				if(otherp){
					//ת��datagrid��queryParams�Ĳ���
					var oarr = otherp.split("&");
					for(var i=0;i<oarr.length;i++){
						var tfd = oarr[i].split("=");
						form.push(appendHidden(decodeURIComponent(tfd[0]),tfd[1]));										
					}
				}
				if(options["url"]){
					//�������ӵĴ���
					if (options["url"].indexOf("?") > 0) {
						options["url"] = options["url"] + "&_t="+new Date().getTime();
					}else{
						options["url"] = options["url"] + "?_t="+new Date().getTime();
					}
				}else{
					$.messager.alert("��ʾ","δ����ִ�е�����url");
					return false;
				}				
				//����form�ύ
				var htmlForm = $("<FORM id='exportForm' method='post'></FORM>");
				htmlForm.append(form.join(''));
				htmlForm.attr("action",options["url"]);
				htmlForm.appendTo(document.body);
				htmlForm.submit();
				$("#exportForm").remove();				
			});}
	
		,mergeColumns:function(jq,mergeColumns){
			//�ϲ��еķ���
			mergeColumns = mergeColumns || [];
			if(mergeColumns!=null && mergeColumns.length>0){
				return jq.each(function(){
					//��ȡdatagird��panel
					var panel = $.data(this,"datagrid").panel;
					//����
					var dataLen = $.data(this,"datagrid").data.rows.length;
					//����2�н��кϲ����򷵻�
					if(dataLen<2){return false;}
					var mLen = mergeColumns.length;
					//��������DIV
					var body = $(panel).find("div.datagrid-body");
					var colText = null,colNText=null,_field = null;;
					for(var i=0;i<mLen;i++){
						_field = mergeColumns[i];
						//�����ֶλ�ȡҪ�ϲ��������ݼ���
						var td = $(body).find("td[field="+mergeColumns[i]+"]");
						var tdLen = td.length,mergeRIdx=null,mRowSpan=1;
						colText = $(td[0]).text();
						mergeRIdx = $(td[0]).parent().attr("datagrid-row-index");
						for ( var j = 1; j < tdLen; j++) {							
							colNText =$(td[j]).text();
							//ȡ��һ������һ�бȽ�����ֵͬ���ϲ���++������ͽ��кϲ����ҳ�ʼ��һ�εıȽϲ���
							if(colText==colNText){
								mRowSpan++;
							}else{
								//ֵ����ȵ�ʱ�ϲ���һ�εıȽ�
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
						//�ϲ����һ�ε��ж�
						if(mRowSpan!=1){
							$(this).datagrid("mergeCells",{
								field:_field,
								index:parseInt(mergeRIdx),
								rowspan:parseInt(mRowSpan)
							});
						}}});}}
		});	
});