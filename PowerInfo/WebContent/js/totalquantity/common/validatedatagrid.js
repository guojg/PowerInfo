/**
	 * 数字验证
	 **/
	function validate(o, upts, arr, a, b) {
		var opts = o.datagrid('options');
		var flag = true;
		var regExp = /^-?[1-9]+(\.\d+)?$|^-?0(\.\d+)?$|^-?[1-9]+[0-9]*(\.\d+)?$/; //数字验证
		var regExp2 = '/^-?(?:\\d{1,' + a + '})(?:\\.\\d{1,' + b + '})?$/';
		regExp2 = eval(regExp2);
		for (var i = 0; i < upts.length; i++) {
			var rowIndex = -1;
			rowIndex = o.datagrid('getRowIndex', upts[i]);
			if (rowIndex < 0)
				return;
			$.each(upts[i], function(oi, j) {
				if (oi == opts.idField || j == '') {
					return;
				}
				var rflag = false;
				$.each(arr, function(ii, n) {
					if (oi == n) {
						rflag = true;
						return false;
					}
				});
				if (rflag) {
					return;
				}
				var $cell = $('tr[datagrid-row-index=' + rowIndex + ']').find(
						'td[field=' + oi + ']');
				if ($.trim(j) == '') {
					$cell.css('background', 'red');
					$cell.showTipp({
						flagInfo : '数据不能为空格！',
						flagWidth : '100'
					});
					flag = false;
					return;
				}
				if (!regExp.test(j)) {
					$cell.css('background', 'red');
					$cell.showTipp({
						flagInfo : '输入字符必须为数字！',
						flagWidth : '150'
					});
					flag = false;
					return;
				}
				if (!regExp2.test(j)) {
					$cell.css('background', 'red');
					$cell.showTipp({
						flagInfo : '输入字符精度为[' + a + ',' + b + ']！',
						flagWidth : '110'
					});
					flag = false;
				}
			});
		}
		return flag;
	}