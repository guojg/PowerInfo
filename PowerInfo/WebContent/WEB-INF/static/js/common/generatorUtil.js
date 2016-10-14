function isNotEmpty(str){
	var flag = true;
	if(str==null||str==""){
		flag=false;
	}
	return flag;
}
/**
 * 数字验证
 **/
function validate(data) {
	var regExp = /^-?[1-9]+(\.\d+)?$|^-?0(\.\d+)?$|^-?[1-9]+[0-9]*(\.\d+)?$/; //数字验证

	if(!isNotEmpty(data["gene_name"])){
		$.messager.alert('提示','机组名称不能为空！');
		return false;
	}
	if(!isNotEmpty(data["plant_id"])){
		$.messager.alert('提示','所属电厂不能为空！');
		return false;
	}
	if(!isNotEmpty(data["gene_capacity"])){
		$.messager.alert('提示','装机容量不能为空！');
		return false;
	}else{
		if(!regExp.test(data["gene_capacity"])){
			$.messager.alert('提示','装机容量必须为数字！');
			return false;
		}
	}
	if(!isNotEmpty(data["index_item"])){
		$.messager.alert('提示','电源类型不能为空！');
		return false;
	}
	if(!isNotEmpty(data["start_date"])){
		$.messager.alert('提示','投产日期不能为空！');
		return false;
	}
	if(isNotEmpty(data["end_date"])){
		if(!validateDate(data["start_date"],data["end_date"])){
			$.messager.alert('提示','退役日期必须小于投产日期！');
			return false;
		}
		
	}
	return true;
}
function validateDate(startdate,enddata){
    var start=new Date(startdate.replace("-", "/").replace("-", "/"));  
    var end=new Date(enddata.replace("-", "/").replace("-", "/"));  
    if(end<start){  
        return false;  
    }  
    return true;  
	
}