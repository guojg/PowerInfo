;(function($){
    jQuery.fn.extend({
        showTipp:function(settings)
        {
            $(this).each(function(){
                //初始化配置信息
                var options = jQuery.extend({
                    flagCss:"tip",
                    flagWidth:$(this).outerWidth(),
                    flagInfo:$(this).attr("title"),
                    isAnimate:false
                },
                settings);
                if(!options.flagInfo)
                {
                    return;
                }
                $(this).removeAttr("title");
                $(this).mouseover(function(){
                	if($('#tooltip')[0]){
                    	$('#tooltip').remove();
                    }
                    //设置提示信息最小宽度为163
                    options.flagWidth = (parseInt(options.flagWidth) < 120) ? 120 : parseInt(options.flagWidth);
                    var oTip = $("<div class='ui-slider-tooltip  ui-corner-all' id='tooltip'></div>");
                    var oPointer = $("<div class='ui-tooltip-pointer-down'><div class='ui-tooltip-pointer-down-inner'></div></div>");
                    var oTipInfo = $("<div style='word-break: break-all;word-wrap: break-word' >" + options.flagInfo + "</div>").attr("class",options.flagCss).css("width",options.flagWidth + "px");
                    //合并提示信息
                    var oToolTip = $(oTip).append(oTipInfo).append(oPointer);
                    //添加淡入效果
                    if(options.isAnimate)
                    {
                        $(oToolTip).fadeIn("slow");
                    }
                    $(document.body).append(oToolTip);
                    
                    //计算提示信息的top、left和width
//                    var position = $(this).position();
                    //var oTipTop = eval(position.top- $(oTip).outerHeight()+8);//-8
//                    var oTipTop=eval(this.getBoundingClientRect().bottom-60);
//                    var oTipLeft = position.left;
                    var oTipTop = $(this).offset().top - 35;
                    var oTipLeft = $(this).offset().left;
                    $(oToolTip).css("top" , oTipTop + "px").css("left" , oTipLeft + "px");
                    
                    $(this).mouseout(function(){
                        $(oToolTip).remove();
                    });
					//function(){
					//$(this).animate({opacity: "hide", top: "-85"}, "fast");
					//}
                });
				
            });
            return this;
        },
        removeTipp:function(){
        	$(this).unbind("mouseover");
        }
    })
})(jQuery);