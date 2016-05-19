$(function(){
	/*validatebox�Զ������֤��ʽ*/
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
				//�ǿ���֤
                if (opts.required) {
					//��ͨ�ı�Ϊ"",����ѡ��Ϊ��ѡ����Ĭ�Ͽ�ֵ����Ϊ��ѡ��  
                    if (value == "" || value.indexOf("��ѡ��")>-1) {  
                        setTipMessage(opts.missingMessage);
						$(jq).validatebox('showTip', jq);
                        return false;  
                    }
					 //�Ƿ��ַ���
					if (value.indexOf("&")>-1 || value.indexOf("?")>-1) {  
                        setTipMessage(opts.missingMessage+"�����Ƿ��ַ�");
                        box.addClass("validatebox-invalid");
						$(jq).validatebox('showTip', jq);
                        return false;  
                    }  
                }
				//���ʽ��֤  
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
				//������ʾ��Ϣ
                var msg = $.data(jq, "validatebox").message;
				//������ʾ��Ϣ
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
	
	//��չeasyui������֤  
	$.extend($.fn.validatebox.defaults.rules, {  
		//������ʽ��֤��ʼ-------------------------------
    	mobile: { 
        	validator: function (value) {  
            	var reg = /^1[3|4|5|8|9]\d{9}$/;  
            	return reg.test(value);  
        	},  
        	message: '�����ֻ������ʽ��׼ȷ��'  
    	},
    	//������ʽ��֤����------------------------------
    	//�����ַ�������֤��ʼ---------------------------------------------
    	minLength: {    
	        validator: function(value, param){    
	            return value.length >= param[0];    
	        },    
	        message: '���������� {0} �ַ���'   
	    },
	    maxLength: {    
	        validator: function(value, param){    
	            return value.length <= param[0];    
	        },    
	        message: '��������� {0} �ַ���'   
	    },length : {  
            validator : function(value, param) {  
                this.message = '�������ַ�����Ϊ {0} �� {1}.';  
                var len = $.trim(value).length;  
                if (param) {  
                    for (var i = 0; i < param.length; i++) {  
                        this.message = this.message.replace(new RegExp(  
                                        "\\{" + i + "\\}", "g"), param[i]);  
                    }  
                }  
                return len >= param[0] && len <= param[1];  
            },  
            message : '�������ַ�����Ϊ {0} �� {1}.'  
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
            message : '��Ϣ��д����.'  
        },
	    //�����ַ�������֤����--------------------------------------------
	    //������֤��ʼ-----------------------------------
	    validNumber: {   
			validator: function (value) {
	        	var reg = /^[0-9]*$/;  
	        	return reg.test(value);  
			},  
			message: '���������֡�'  	
		},
		validNumberPrecision: {   
			validator: function (value,param) {
	        	//var reg =/^(\d{1,13})+(\.\d{1,2})?$/;
	        	var reg= new RegExp("^(\\d{1,"+param[0]+"})(\\.\\d{1,"+param[1]+"})?$");
	        	return reg.test(value);  
			},  
			message: '����������λ������{0}λ����Ч���ֲ��ܳ���{1}λ��������'  	
		},
		percentageNumber:{
			validator: function (value) {
	        	var reg = /^\d+(\.\d+)?%$/;  
	        	return reg.test(value);  
			},  
			message: '������ٷ�����' 
		}
		//������֤����--------------------------------
		//cy���������֤---------------------------------
		//������
		,integer:{
			validator: function (value) {
	        	var reg = /^[\-\+]?\d+$/;  
	        	return reg.test(value);  
			},  
			message: '������Ч��������'
		},email:{
			validator: function (value) {
	        	var reg = /^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?$/i;  
	        	return reg.test(value);  
			},  
			message: '�ʼ���ַ��Ч��'
		},enterprisePhone:{
			validator: function (value) {
	        	var reg = /^((\d{11})|^((\d{7,8})|(\d{4}|\d{3})-(\d{7,8})|(\d{4}|\d{3})-(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1})|(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1}))$)$/;  
	        	return reg.test(value);  
			},  
			message: '�ֻ�������Ч��'
		},dateTimeFormat:{
			validator: function (value) {
	        	var reg = /^\d{4}[\/\-](0?[1-9]|1[012])[\/\-](0?[1-9]|[12][0-9]|3[01])\s+(1[0-9]|2[0-4]|0?[1-9]){1}:(0?[1-5]|[0-6][0-9]){1}:(0?[0-6]|[0-6][0-9])\s$|^(?:(?:(?:0?[13578]|1[02])(\/|-)31)|(?:(?:0?[1,3-9]|1[0-2])(\/|-)(?:29|30)))(\/|-)(?:[1-9]\d\d\d|\d[1-9]\d\d|\d\d[1-9]\d|\d\d\d[1-9])$|^((1[012]|0?[1-9]){1}\/(0?[1-9]|[12][0-9]|3[01]){1}\/\d{2,4}\s+(1[012]|0?[1-9]){1}:(0?[1-5]|[0-6][0-9]){1}:(0?[0-6]|[0-6][0-9]){1}\s+(am|pm|AM|PM){1})$/;  
	        	return reg.test(value);  
			},  
			message: '��Ч������ʱ���ʽ'
		},dateFormat:{
			validator: function (value) {
	        	var reg = /^\d{4}[\/\-](0?[1-9]|1[012])[\/\-](0?[1-9]|[12][0-9]|3[01])$|^(?:(?:(?:0?[13578]|1[02])(\/|-)31)|(?:(?:0?[1,3-9]|1[0-2])(\/|-)(?:29|30)))(\/|-)(?:[1-9]\d\d\d|\d[1-9]\d\d|\d\d[1-9]\d|\d\d\d[1-9])$|^(?:(?:0?[1-9]|1[0-2])(\/|-)(?:0?[1-9]|1\d|2[0-8]))(\/|-)(?:[1-9]\d\d\d|\d[1-9]\d\d|\d\d[1-9]\d|\d\d\d[1-9])$|^(0?2(\/|-)29)(\/|-)(?:(?:0[48]00|[13579][26]00|[2468][048]00)|(?:\d\d)?(?:0[48]|[2468][048]|[13579][26]))$/;  
	        	return reg.test(value);  
			},  
			message: '��Ч�����ڸ�ʽ'
		},onlyLetterNumber:{
			validator: function (value) {
	        	var reg = /^[0-9a-zA-Z]+$/;  
	        	return reg.test(value);  
			},  
			message: '���ֺ���ĸ'
		},onlyLetter:{
			validator: function (value) {
	        	var reg = /^[a-zA-Z]+$/;  
	        	return reg.test(value);  
			},  
			message: '��������ĸ'
		},chinese:{
			validator: function (value) {
	        	var reg = /^[\u4e00-\u9fa5]+$/;  
	        	return reg.test(value);  
			},  
			message: '����������'
		}
		
	});   
});
