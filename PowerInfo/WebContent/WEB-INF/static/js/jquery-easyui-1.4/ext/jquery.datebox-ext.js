/**
 * 给时间框控件扩展一个清除的按钮
 */
$.fn.datebox.defaults.cleanText = '清空';

(function ($) {
    var buttons = $.extend([], $.fn.datebox.defaults.buttons);
    buttons.splice(1, 0, {
        text: function (target) {
            return $(target).datebox("options").cleanText
        },
        handler: function (target) {
            $(target).datebox("setValue", "");
            $(target).datebox("hidePanel");
        }
    });
    $.extend($.fn.datebox.defaults, {
        buttons: buttons
    });

})(jQuery)