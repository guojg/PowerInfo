$(function() {
		
});

	function baoCun(){
		var base_year='';
		var predict_year='';
		var max_value='';
		var min_value='';
		var maybe_value='';
		var param={
				'base_year':base_year,
				'predict_year':predict_year,
				'max_value':max_value,
				'min_value':min_value,
				'maybe_value':maybe_value
				} ;
		$.ajax( {
			url : basePath+'taskWebc/thTableName.so',
			dataType : 'json',
			async : false,
			type:'post',
			data:param,
			success : function(data) {
			if (data ==0) {
				$.messager.alert('提示','保存成功！','info',function(){
		
					});
			} else{
				$.messager.alert('提示','保存失败！','info',function(){
		
				});
			}
			}
		});	 
		
		
	}
	