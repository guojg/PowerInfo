window.onbeforeunload = function()   { 
    if(is_form_changed())   
    {  
        return "您的修改内容还没有保存，您确定离开吗？";      
     }   
}
function is_form_changed()   {  
 
    //检测页面是否有保存按钮  
    var t_save = jQuery("#tool_save");   
 
    //检测到保存按钮,继续检测元素是否修改  
    if(t_save.length>0)       {    
        var is_changed = false;   
        jQuery("body input, body textarea, body select").each(function()   
        {  
 
            var _v = jQuery(this).attr('_value');   
            if(typeof(_v) == 'undefined')  
                 _v = '';    
 
            if(_v != jQuery(this).val())   
                is_changed = true;   
        });  
 
        return is_changed;       }   
    return false; 
    }   
  
$(document).ready(function()  {  
	$(window).on('beforeunload', function(){  
	    return 'Are you sure you want to leave?';  
	});  
    $("body input, body textarea, body select").each(function()   
    {  
 
        $(this).attr('_value', jQuery(this).val());   
    });  
    
});   
