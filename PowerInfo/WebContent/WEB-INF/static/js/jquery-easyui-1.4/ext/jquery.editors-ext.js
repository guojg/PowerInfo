$.extend($.fn.datagrid.defaults.editors, {    
    text: {    
        init: function(container, options){    
            var input = $('<input type="text" class="datagrid-editable-input">').appendTo(container);    
            return input;    
        },    
        getValue: function(target){    
            return $(target).val();    
        },    
        setValue: function(target, value){    
            $(target).val(value);    
        },    
        resize: function(target, width){    
            var input = $(target);    
            if ($.boxModel == true){    
                input.width(width - (input.outerWidth() - input.width()));    
            } else {    
                input.width(width);    
            }    
        }    
    },
    numberspinner: {  
        init: function(container, options) {  
            var input = $('<input type="text">').appendTo(container);  
            return input.numberspinner(options);  
        },  
        destroy: function(target) {  
            $(target).numberspinner('destroy');  
        },  
        getValue: function(target) {  
            return $(target).numberspinner('getValue');  
        },  
        setValue: function(target, value) {  
            $(target).numberspinner('setValue', value);  
        },  
        resize: function(target, width) {  
            $(target).numberspinner('resize', width);  
        }  
    }  
});