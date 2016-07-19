function getValidateFlag(detailTable){
	$('.validatebox-text').each(function(i,obj){
		$(this).validatebox('enableValidation').validatebox('validateTip');
	});
	var tipsStr = $("#validateMessage").html();
	var tipsArr = tipsStr.split(",");
	$("#validateMessage").html(tipsArr[1]);
	
	var flag=$('#'+detailTable+'').form('validate');
	return flag;
}
