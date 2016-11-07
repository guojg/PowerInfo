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
	
	var regExp_year=/19|20\d{2}/;

	if(!isNotEmpty(data["plant_name"])){
		$.messager.alert('提示','电厂名称不能为空！');
		return false;
	}
	if(!isNotEmpty(data["plant_capacity"])){
		$.messager.alert('提示','装机总容量不能为空！');
		return false;
	}else{
		if(!regExp.test(data["plant_capacity"])){
			$.messager.alert('提示','装机容量必须为数字！');
			return false;
		}
	}
	if(isNotEmpty(data["product_year"])){
		if(!regExp_year.test(data["product_year"])){
			$.messager.alert('提示','开工年：填写正确的年份！');
			return false;
		}
	}
	if(isNotEmpty(data["build_year"])){
		if(!regExp_year.test(data["build_year"])){
			$.messager.alert('提示','建成年：填写正确的年份！');
			return false;
		}
	}
	if(isNotEmpty(data["start_outlay"])){
		if(!regExp.test(data["start_outlay"])){
			$.messager.alert('提示','静态投资必须是数字！');
			return false;
		}
	}
	if(!isNotEmpty(data["power_type"])){
		$.messager.alert('提示','电源类型不能为空！');
		return false;
	}
	if(data["power_type"]==3){
		if(!isNotEmpty(data["cooling_type"])){
			$.messager.alert('提示','冷却类型不能为空！');
			return false;
		}
	}
	if(isNotEmpty(data["electricity_consumption"])){
		if(!regExp.test(data["electricity_consumption"])){
			$.messager.alert('提示','厂用电量必须是数字！');
			return false;
		}
	}
	if(!isNotEmpty(data["materials_cost"])){
		$.messager.alert('提示','电厂材料费（元/年）不能为空！');
		return false;
	}else{
		if(!regExp.test(data["materials_cost"])){
			$.messager.alert('提示','电厂材料费（元/年）必须为数字！');
			return false;
		}
	}
	if(!isNotEmpty(data["salary"])){
		$.messager.alert('提示','工资、奖金及福利费（元/年）不能为空！');
		return false;
	}else{
		if(!regExp.test(data["salary"])){
			$.messager.alert('提示','工资、奖金及福利费（元/年）必须为数字！');
			return false;
		}
	}
	if(!isNotEmpty(data["repairs_cost"])){
		$.messager.alert('提示','修理费（元/年）不能为空！');
		return false;
	}else{
		if(!regExp.test(data["repairs_cost"])){
			$.messager.alert('提示','修理费（元/年）必须为数字！');
			return false;
		}
	}
	if(!isNotEmpty(data["other_cost"])){
		$.messager.alert('提示','其他费用（元/年）不能为空！');
		return false;
	}else{
		if(!regExp.test(data["other_cost"])){
			$.messager.alert('提示','其他费用（元/年）必须为数字！');
			return false;
		}
	}
	return true;
}
