$.extend($.fn.datagrid.methods, {
	editCell: function(jq,param){
		return jq.each(function(){
			var opts = $(this).datagrid('options');
			var fields = $(this).datagrid('getColumnFields',true).concat($(this).datagrid('getColumnFields'));
			for(var i=0; i<fields.length; i++){
				var col = $(this).datagrid('getColumnOption', fields[i]);
				col.editor1 = col.editor;
				if (fields[i] != param.field){
					col.editor = null;
				}
			}
			$(this).datagrid('beginEdit', param.index);
			for(var i=0; i<fields.length; i++){
				var col = $(this).datagrid('getColumnOption', fields[i]);
				col.editor = col.editor1;
			}
		});
	}
});

var editIndex = undefined;
function endEditing(datagrid){
	if (editIndex == undefined){return true;}
	if ($('#'+datagrid).datagrid('validateRow', editIndex)){
		$('#'+datagrid).datagrid('endEdit', editIndex);
		editIndex = undefined;
		return true;
	} else {
		return false;
	}
}
function onClickCell(index, field,datagrid){
	if (endEditing(datagrid)){
		 $('#'+datagrid).datagrid('selectRow', index)
				.datagrid('editCell', {index:index,field:field});
		/* var editor = $('#datagrid').datagrid('getEditor', {index:index,field:field});
		 editor.target.focus().blur(
				function() {					
					endEditing(datagrid);
				});*/
		editIndex = index;
	}
}