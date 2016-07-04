function getSysDict(){
	var algorithmJson='';
	var algorithmParam={"domain_id":10};
	$.ajax({
		type : 'POST',
		async : false,
		data: algorithmParam,
		url :  '/PowerInfo/sysdict/getData',
		success : function(data) {
			algorithmJson =jQuery.parseJSON(data);			
		}
	});
	return algorithmJson;
}
