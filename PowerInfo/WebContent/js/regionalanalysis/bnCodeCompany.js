function getSysDict(){
	var algorithmJson={};
	$.ajax({
		type : 'POST',
		async : false,
		dataType : 'json',
		url :  '/PowerInfo/sysdict/getCompany',
		success : function(data) {
			algorithmJson = data;
		}
	});
	return algorithmJson;
}
