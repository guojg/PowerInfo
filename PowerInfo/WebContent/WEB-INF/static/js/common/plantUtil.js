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

	if(!isNotEmpty(data["plant_name"])){
		$.messager.alert('提示','电厂名称不能为空！');
		return false;
	}
	if(!isNotEmpty(data["plant_capacity"])){
		$.messager.alert('提示','装机容量不能为空！');
		return false;
	}else{
		if(!regExp.test(data["plant_capacity"])){
			$.messager.alert('提示','装机容量必须为数字！');
			return false;
		}
	}
	if(!isNotEmpty(data["index_item"])){
		$.messager.alert('提示','电源类型不能为空！');
		return false;
	}
	if(data["power_type"]==3){
		if(!isNotEmpty(data["cooling_type"])){
			$.messager.alert('提示','冷却类型不能为空！');
			return false;
		}
	}
	if(!isNotEmpty(data["area_id"])){
		$.messager.alert('提示','所属地区不能为空！');
		return false;
	}
	if(!isNotEmpty(data["organ"])){
		$.messager.alert('提示','所属企业不能为空！');
		return false;
	}
//	if(!isNotEmpty(data["start_date"])){
//		$.messager.alert('提示','投产日期不能为空！');
//		return false;
//	}
//	if(isNotEmpty(data["end_date"])){
//		if(!validateDate(data["start_date"],data["end_date"])){
//			$.messager.alert('提示','退役日期必须小于投产日期！');
//			return false;
//		}
//		
//	}
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