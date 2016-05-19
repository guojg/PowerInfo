$(function(){
	/*validatebox自定义的验证方式*/
	$.extend($.fn.validatebox.methods, {
            remove:function(jq, newposition){  
                return jq.each(function() {  
                    $(this).removeClass("validatebox-text validatebox-invalid").unbind('focus.validatebox').unbind('blur.validatebox');  
                });  
            },  
            reduce:function(jq, newposition){  
                return jq.each(function() {  
                    var opt = $(this).data().validatebox.options;  
                    $(this).addClass("validatebox-text").validatebox(opt);  
                });  
            },  
            validateTip:function(jq){
                jq = jq[0];  
                var opts = $.data(jq, "validatebox").options;  
                var tip = $.data(jq, "validatebox").tip;  
                var box = $(jq);  
                var value = box.val();
                function setTipMessage(msg) {  
                    $.data(jq, "validatebox").message = msg;
                };  
                var disabled = box.attr("disabled");  
                if (disabled == true || disabled == "true") {  
                    return true;  
                }
				//非空验证
                if (opts.required) {
					//普通文本为"",下拉选项为请选择，且默认空值必须为请选择  
                    if (value == "" || value.indexOf("请选择")>-1) {  
                        setTipMessage(opts.missingMessage);
						$(jq).validatebox('showTip', jq);
                        return false;  
                    }
					 //非法字符串
					if (value.indexOf("&")>-1 || value.indexOf("?")>-1) {  
                        setTipMessage(opts.missingMessage+"包含非法字符");
                        box.addClass("validatebox-invalid");
						$(jq).validatebox('showTip', jq);
                        return false;  
                    }  
                }
				//表达式验证  
                if (opts.validType) {
					var result = true;
					if(!$.isArray(opts.validType)){
						var type = opts.validType;
						opts.validType = [];
						opts.validType[0] = type;
					}
					$.each(opts.validType,function(i,v){
						var result = /([a-zA-Z_]+)(.*)/.exec(v);  
						var rule = opts.rules[result[1]];  
						if (value && rule) {  
							var param = eval(result[2]);  
							if (!rule["validator"](value, param)) {  
								box.addClass("validatebox-invalid");  
								var message = rule["message"];  
								if (param) {  
									for (var i = 0; i < param.length; i++) {  
										message = message.replace(new RegExp("\\{" + i + "\\}", "g"), param[i]);  
									}  
								}  
								setTipMessage(opts.invalidMessage || message);  
								$(jq).validatebox('showTip', jq);  
								result = false;  
								return false;
							}  
						}
					});
                    if(!result){
						return result;
					}
					
                }  
                box.removeClass("validatebox-invalid");  
                $(jq).validatebox('hideTip', jq);  
                return true;  
            },  
            showTip : function(jq){
                jq = jq[0];
				//单个提示信息
                var msg = $.data(jq, "validatebox").message;
				//所有提示信息
                $("#msg").html($("#msg").html()+","+msg);  
            },  
            hideTip : function(jq){  
                jq = jq[0];  
                var tip = $.data(jq, "validatebox").tip;  
                if(tip){  
                    tip.remove;  
                    $.data(jq, "validatebox").tip = null;  
                }  
            }  
	}); 
	
	//扩展easyui表单的验证  
	$.extend($.fn.validatebox.defaults.rules, {  
		//正则表达式验证开始-------------------------------
    	mobile: { 
        	validator: function (value) {  
            	var reg = /^1[3|4|5|8|9]\d{9}$/;  
            	return reg.test(value);  
        	},  
        	message: '输入手机号码格式不准确。'  
    	},
    	//正则表达式验证结束------------------------------
    	//汉字字符长度验证开始---------------------------------------------
    	minLength: {    
	        validator: function(value, param){    
	            return value.length >= param[0];    
	        },    
	        message: '请至少输入 {0} 字符。'   
	    },
	    maxLength: {    
	        validator: function(value, param){    
	            return value.length <= param[0];    
	        },    
	        message: '请最多输入 {0} 字符。'   
	    },length : {  
            validator : function(value, param) {  
                this.message = '请输入字符长度为 {0} 到 {1}.';  
                var len = $.trim(value).length;  
                if (param) {  
                    for (var i = 0; i < param.length; i++) {  
                        this.message = this.message.replace(new RegExp(  
                                        "\\{" + i + "\\}", "g"), param[i]);  
                    }  
                }  
                return len >= param[0] && len <= param[1];  
            },  
            message : '请输入字符长度为 {0} 到 {1}.'  
        },regValidator : {  
            validator : function(value, param) {
				var	isValid = true;		
                var len = $.trim(value).length;  
                if (param) {  
                    for (var i = 0; i < param.length; i++) {
						isValid = param[i].test(value); 
                        return param[i].test(value); 
                    }  
                }  
				return isValid;
            },  
            message : '信息填写有误.'  
        },
	    //汉字字符长度验证结束--------------------------------------------
	    //数字验证开始-----------------------------------
	    validNumber: {   
			validator: function (value) {
	        	var reg = /^[0-9]*$/;  
	        	return reg.test(value);  
			},  
			message: '请输入数字。'  	
		},
		validNumberPrecision: {   
			validator: function (value,param) {
	        	//var reg =/^(\d{1,13})+(\.\d{1,2})?$/;
	        	var reg= new RegExp("^(\\d{1,"+param[0]+"})(\\.\\d{1,"+param[1]+"})?$");
	        	return reg.test(value);  
			},  
			message: '请输入整数位不超过{0}位且有效数字不能超过{1}位的正数。'  	
		},
		percentageNumber:{
			validator: function (value) {
	        	var reg = /^\d+(\.\d+)?%$/;  
	        	return reg.test(value);  
			},  
			message: '请输入百分数。' 
		}
		//数字验证结束--------------------------------
		//cy添加数据验证---------------------------------
		//正整数
		,integer:{
			validator: function (value) {
	        	var reg = /^[\-\+]?\d+$/;  
	        	return reg.test(value);  
			},  
			message: '不是有效的整数。'
		},email:{
			validator: function (value) {
	        	var reg = /^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?$/i;  
	        	return reg.test(value);  
			},  
			message: '邮件地址无效。'
		},enterprisePhone:{
			validator: function (value) {
	        	var reg = /^((\d{11})|^((\d{7,8})|(\d{4}|\d{3})-(\d{7,8})|(\d{4}|\d{3})-(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1})|(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1}))$)$/;  
	        	return reg.test(value);  
			},  
			message: '手机号码无效。'
		},dateTimeFormat:{
			validator: function (value) {
	        	var reg = /^\d{4}[\/\-](0?[1-9]|1[012])[\/\-](0?[1-9]|[12][0-9]|3[01])\s+(1[0-9]|2[0-4]|0?[1-9]){1}:(0?[1-5]|[0-6][0-9]){1}:(0?[0-6]|[0-6][0-9])\s$|^(?:(?:(?:0?[13578]|1[02])(\/|-)31)|(?:(?:0?[1,3-9]|1[0-2])(\/|-)(?:29|30)))(\/|-)(?:[1-9]\d\d\d|\d[1-9]\d\d|\d\d[1-9]\d|\d\d\d[1-9])$|^((1[012]|0?[1-9]){1}\/(0?[1-9]|[12][0-9]|3[01]){1}\/\d{2,4}\s+(1[012]|0?[1-9]){1}:(0?[1-5]|[0-6][0-9]){1}:(0?[0-6]|[0-6][0-9]){1}\s+(am|pm|AM|PM){1})$/;  
	        	return reg.test(value);  
			},  
			message: '无效的日期时间格式'
		},dateFormat:{
			validator: function (value) {
	        	var reg = /^\d{4}[\/\-](0?[1-9]|1[012])[\/\-](0?[1-9]|[12][0-9]|3[01])$|^(?:(?:(?:0?[13578]|1[02])(\/|-)31)|(?:(?:0?[1,3-9]|1[0-2])(\/|-)(?:29|30)))(\/|-)(?:[1-9]\d\d\d|\d[1-9]\d\d|\d\d[1-9]\d|\d\d\d[1-9])$|^(?:(?:0?[1-9]|1[0-2])(\/|-)(?:0?[1-9]|1\d|2[0-8]))(\/|-)(?:[1-9]\d\d\d|\d[1-9]\d\d|\d\d[1-9]\d|\d\d\d[1-9])$|^(0?2(\/|-)29)(\/|-)(?:(?:0[48]00|[13579][26]00|[2468][048]00)|(?:\d\d)?(?:0[48]|[2468][048]|[13579][26]))$/;  
	        	return reg.test(value);  
			},  
			message: '无效的日期格式'
		},onlyLetterNumber:{
			validator: function (value) {
	        	var reg = /^[0-9a-zA-Z]+$/;  
	        	return reg.test(value);  
			},  
			message: '数字和字母'
		},onlyLetter:{
			validator: function (value) {
	        	var reg = /^[a-zA-Z]+$/;  
	        	return reg.test(value);  
			},  
			message: '请输入字母'
		},chinese:{
			validator: function (value) {
	        	var reg = /^[\u4e00-\u9fa5]+$/;  
	        	return reg.test(value);  
			},  
			message: '请输入中文'
		}
		
	});   
});
