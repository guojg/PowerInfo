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
//				if (value.indexOf("&")>-1 || value.indexOf("?")>-1) {  
//                    setTipMessage(opts.missingMessage+"包含非法字符");
//                    box.addClass("validatebox-invalid");
//					$(jq).validatebox('showTip', jq);
//                    return false;  
//                }  
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
            $("#validateMessage").html($("#validateMessage").html()+","+msg);  
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
		checkText: {   
		validator: function (value,param) {
			//普通文本为"",下拉选项为请选择，且默认空值必须为请选择  
	        if (value == "" || value.indexOf("请选择")>-1) {  
	            return false;  
	        }
			 //非法字符串
			if (value.indexOf("&")>-1 || value.indexOf("?")>-1||value.indexOf("\"")>-1||value.indexOf("\'")>-1) {  
	            return false;  
	        }  
			return true;
		},  
		message: '包含乱码文字'  	
	},
		limitNumberEqu: {   
			validator: function (value,param) {
	        	var a =param[0];
	        	var b =param[1];
	        	if(value>=a && value<=b){
	        		return true;
	        	}else{
	        		return false;
	        	}
			},  
			message: '请输入大于等于a小于等于b的值。'  	
		},
		limitNumberNOEqu:{
			validator: function (value,param) {
	        	var a =param[0];
	        	var b =param[1];
	        	if(value>a && value<b){
	        		return true;
	        	}else{
	        		return false;
	        	}
			},  
			message: '请输入大于a小于b的值。'  
		},
		limitNumberGreatEqu:{
			validator: function (value,param) {
	        	var a =param[0];
	        	if(value>=a){
	        		return true;
	        	}else{
	        		return false;
	        	}
			},  
			message: '请输入大于等于a的值。'  
		},
		limitNumberGreat:{
			validator: function (value,param) {
	        	var a =param[0];
	        	if(value>a){
	        		return true;
	        	}else{
	        		return false;
	        	}
			},  
			message: '请输入大于a的值。'  
		},
		integerNumber: {   
			validator: function (value,param) {
	        	var reg = /^[0-9]*$/;  
	        	var a =param[0];
	        	var b =param[1];
	        	var fg = reg.test(value);
	        	
	        	if(fg==true || fg=="true"){
	        		if(parseInt(value)==0){
	        			return true;
	        		}else{
	        			if(value>=a && value<=b){
		        			return true;
		        		}else{
		        			return false;
		        		}
	        		}
	        	}else{
	        		return false;
	        	}
			},  
			message: '请输入数字。'  	
		},validNumberdecimal: {   
			validator: function (value,param) {
        	var reg= new RegExp("^(\\d{1,})(\\.\\d{1,"+param[2]+"})?$");
        	var aa= reg.test(value);
        	var a =param[0];
        	var b =param[1];
        	if(aa){
        		if(value>a&&value<b){
        			return true;
        		}else{
        			return false;
        		}
        	}else{
        		return false;
        	}
		},  
		message: '请输入数据介于[0]到[1],且小数位数不超过[3]。'  	
		}
		//数字验证结束--------------------------------
	});   
});
