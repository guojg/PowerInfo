$.extend($.fn.datagrid.methods, {
	/**
	 * 表头单元格合并
	 * @param {} jq
	 * @param {} params 
	 * 		包含属性
	 * 			index		表头第几行 从0开始
	 * 			rowspan		向下合并几行
	 * 			colspan		向右合并几列
	 * 			field		合并列field名称
	 * @return {}
	 */
	mergeTopCells : function(jq, params) {
		function _45e(_495, _496) {
			function find(_497) {
				if (_497) {
					for ( var i = 0; i < _497.length; i++) {
						var cc = _497[i];
						for ( var j = 0; j < cc.length; j++) {
							var c = cc[j];
							if (c.field == _496) {
								return c;
							}
						}
					}
				}
				return null;
			}
			;
			var opts = $.data(_495, "datagrid").options;
			var col = find(opts.columns);
			if (!col) {
				col = find(opts.frozenColumns);
			}
			return col;
		}
		return jq.each(function(){
			var head = $.data(this, "datagrid").dc.header1;
			var head2 = $.data(this, "datagrid").dc.header2;
			var opts = $.data(this, "datagrid").options;
			
			var rows = head.find('tbody').children();
			
			if (params.index < 0 || params.index >= rows.length) {
				return;
			}
			if (params.rowspan == 1 && params.colspan == 1) {
				return;
			}
			
			var tr = $(rows[params.index]);
			var td = tr.find("td[field=\""+params.field+"\"]");
			
			if(!td[0]){
				rows = head2.find('tbody').children();
				tr = $(rows[params.index]);
				td = tr.find("td[field=\""+params.field+"\"]");
			}
			
			td.attr("rowspan",params.rowspan).attr("colspan",params.colspan);
			td.addClass("datagrid-td-merged");
			for ( var i = 1; i < params.colspan; i++) {
				td = td.next();
				td.hide();
				rows[params.index][td.attr("field")] = td;
			}
			
			for( var i = 1; i < params.rowspan; i++) {
				tr = tr.next();
				var td = tr.find("td[field=\""+params.field+"\"]").hide();
				rows[params.index + i][td.attr("field")] = td;
				for( var j = 1; j < params.colspan; j++) {
					td = td.next();
					td.hide();
					rows[params.index + i][td.attr("field")] = td;
				}
			}
			
			var inthis = this;
			head.add(head2).find("td.datagrid-td-merged").each(function(){
				var td = $(this);
				var _490 = td.attr("colspan")||1;
				var _491 = _45e(inthis, td.attr("field")).width;
				if(!_491){
					
				} else {
					for(var i = 1; i < _490; i++){
						td = td.next();
						_491 += _45e(inthis,td.attr("field")).width + 1;
					}
					$(this).children("div.datagrid-cell")._outerWidth(_491);
				}
			});
		});
	},
	
	/**
	 * 数据列表中，某列中相互邻近行的数据一样时，自动合并
	 * @param {} jq
	 * @param params 	需要纵向合并的列的field名称，多列以逗号分开
	 * @return {}
	 */
	mergeCellsByField : function(jq, params) {
		return jq.each(function(){
			var opts = $.data(this, "datagrid").options;
			var ColArray = params.split(",");
			var tTable = $('#' + opts.id);
			var TableRowCnts = tTable.datagrid("getRows").length;
			var tmpA;
			var tmpB;
			var PerTxt = "";
			var CurTxt = "";
			var alertStr = "";
			for (j = ColArray.length - 1; j >= 0; j--) {
				//当循环至某新的列时，变量复位。
				PerTxt = "";
				tmpA = 1;
				tmpB = 0;
		
				//从第一行（表头为第0行）开始循环，循环至行尾(溢出一位)
				for (i = 0; i <= TableRowCnts; i++) {
					if (i == TableRowCnts) {
						CurTxt = "";
					} else {
						CurTxt = tTable.datagrid("getRows")[i][ColArray[j]];
					}
					if (PerTxt == CurTxt) {
						tmpA += 1;
					} else {
						tmpB += tmpA;
						tTable.datagrid('mergeCells', {
							index : i - tmpA,
							field : ColArray[j],
							rowspan : tmpA,
							colspan : null
						});
						tmpA = 1;
					}
					PerTxt = CurTxt;
				}
			}
		});
	}
});