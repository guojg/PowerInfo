;(function($){
	//调用此插件的对象
	var _$this = null;
	//默认设置
	var _default = {};

	//-----------------定义插件----------------------
	$.fn.jtip = function(options, param) {
		//如果第一个参数是字符串，认为是方法调用，其它的作为{}属性传值
		if (typeof options == 'string'){
			var method = $.fn.jtip.methods[options];
			if (method){
				return method(this, param);
			} else {
				return false;
			}
		}

		//将自己定义options和插件默认值整合在一起
		$.extend($.fn.jtip.defaults, options);
		//获取默认设置
		_default = $.fn.jtip.defaults;

		return this.each(function() {
			//获取当前调用此插件的对象
			_$this = $(this);
			//初始化
			_init();
		});
	};

	//-----------------私有函数----------------------
	function _init() {
		//先移除以前的jtip
		_remove();
		//获取jtip并追加到文档中
		$("body").append(_getJtipHtml());
		//设置jtip的样式
		_setStyles();
		//显示jtip
		$('.jtip').show();
	};
		
	function _getJtipHtml() {
		return $("<div class='jtip'>" + 
					 "<div><div></div></div>" +
					 "<div>" + _default.message + "</div>" +
				 "</div>");
	}

	function _setStyles() {
		//设置箭头样式
		_setArrowType();
		//设置其它可定制化样式
		_setCustomStyle();
	}

	function _setArrowType() {
		switch(_default.arrowType) {
			case 'top-left':	$('.jtip > div:first').addClass

('jtip_arrow_top-left');
								$('.jtip > div:last').addClass
								
('jtip_content_top-left');
								break;
			case 'top-right':	$('.jtip > div:first').addClass

('jtip_arrow_top-right');
								$('.jtip > div:last').addClass
('jtip_content_top-right');
							    break;
			case 'right-top':   $('.jtip > div:first').addClass

('jtip_arrow_right-top');
							    $('.jtip > div:last').addClass('jtip_content_right-top');
							    break;
			case 'right-bottom':$('.jtip > div:first').addClass

('jtip_arrow_right-bottom');
							    $('.jtip > div:last').addClass('jtip_content_right-bottom');
							    break;
			case 'bottom-left': $('.jtip > div:first').addClass

('jtip_arrow_bottom-left');
							    $('.jtip > div:last').addClass('jtip_content_bottom-left');
							    break;
			case 'bottom-right':$('.jtip > div:first').addClass

('jtip_arrow_bottom-right');
							    $('.jtip > div:last').addClass('jtip_content_bottom-right');
								break;
			case 'left-top':    $('.jtip > div:first').addClass

('jtip_arrow_left-top');
							    $('.jtip > div:last').addClass('jtip_content_left-top');
								break;
			case 'left-bottom': $('.jtip > div:first').addClass

('jtip_arrow_left-bottom');
							    $('.jtip > div:last').addClass('jtip_content_left-bottom');
							    break;
			default:			$('.jtip > div:first').addClass('jtip_arrow_top-left');
								$('.jtip > div:last').addClass('jtip_content_top-left');
								break;
		}
	}

	function _setCustomStyle() {		
		/********************** 整体样式 *******************************/
		_setWholeStyle();
		/********************** 箭头样式 *******************************/
		 _setArrowStyle();
		/********************** 文本样式 *******************************/
		_setTextStyle();
		
	}

	//设置整体样式
	function _setWholeStyle() {
		var elementX = _getAbsoluteLeft() + _default.marginX;
		var elementY = _getAbsoluteTop() + _default.marginY - 14;
		$('.jtip').css('width', _default.width);
		$('.jtip').css('height', _default.height);
		$('.jtip').css('left', elementX );
		$('.jtip').css('top', elementY);
		$('.jtip').css('background', _default.backgroundColor);
		$('.jtip').css('border-color', _default.borderColor);
	}
	
	//设置箭头的样式
	function _setArrowStyle() {
		var left = 0;
		var top = 0;
		if (_default.showArrow) {
			if (_isHorizontal()){
				//水平向上
				if (_isHorizontalTop()){
					//左上
					if (_isHorizontalTopLeft()) {
						left = 3 + _getCorrectX();
					} else {
						left = -8 + _getCorrectX();
					}
					//设置箭头大小
					//top = -14;
					top = (_default.arrowSize=="large" ? -14 : 

((_default.arrowSize=="middle") ? -12 : -8)); 
				} else {
					left = 2 + _getCorrectX();
					//top = ;
					top = (_default.arrowSize=="large" ? 

_default.height - 14 : ((_default.arrowSize=="middle") ? _default.height - 18 : 

_default.height - 22)); 
				}
			} else {
				if (_isVerticalLeft()) {
					left = (_default.arrowSize=="large" ? -16 : 

((_default.arrowSize=="middle") ? -12 : -8)); 
					if (_isVerticalLeftTop()) {
						top = -1 + _getCorrectY();
					} else {
						top = -15 + _getCorrectY();
					}
				} else {
					//left = _default.width - 14;
					left = (_default.arrowSize=="large" ? 

_default.width - 14 : ((_default.arrowSize=="middle") ? _default.width - 18 : 

_default.width - 22)); 
					if (_isVerticalRightTop()){
						top = -1 + _getCorrectY();
					} else {
						top = -12 + _getCorrectY();
					}	
				}
			}
		} else {
			//隐藏箭头
			left = -10000;
			top = -10000;
		}
		
		$('.jtip > div:first').css('left', left);
		$('.jtip > div:first').css('top', top);
		$('.jtip > div:first').css('border-' + _getArrowTypeOfLast() + '- color', _default.borderColor);
		$('.jtip > div:first > div').css('border-' + _getArrowTypeOfLast() + 

'-color', _default.backgroundColor);
	}

	//设置文本样式
	function _setTextStyle() {
		$('.jtip > div:last').css('width', (_default.width - 4));
		$('.jtip > div:last').css('height', _default.height - 20);
		$('.jtip > div:last').css('color', _default.textColor);
		$('.jtip > div:last').css('text-align', _default.textAlign);
		$('.jtip > div:last').css('background', _default.backgroundColor);
	}

	//获取X修正值
	function _getCorrectX() {
		if (_default.arrowX < 0) {
			return 0;
		} else if (_default.arrowX > (_default.width - 25)){
		    return (_default.width - 25);
		} else {
			return _default.arrowX;
		}
	}

	//获取Y修正值
	function _getCorrectY() {
		if (_default.arrowY < 0) {
			return 0;
		} else if (_default.arrowY > _default.height - 16){
		    return _default.height - 16;
		} else {
			return _default.arrowY;
		}
	}

	//是否是水平方向
	function _isHorizontal() {
		return _isHorizontalTop() || _isHorizontalBottom();
	}

	//是否是水平方向上
	function _isHorizontalTop() {
		if (_isHorizontalTopLeft() || _isHorizontalTopRight()) {
			return true;
		}

		return false;
	}

	//是否水平左上
	function _isHorizontalTopLeft() {
		if (_default.arrowType=="top-left") {
			return true;
		}

		return false;
	}

	//是否水平右上
	function _isHorizontalTopRight() {
		if ( _default.arrowType=="top-right") {
			return true;
		}

		return false;
	}

	//是否是水平方向下
	function _isHorizontalBottom() {
		if (_isHorizontalBottomLeft() || _isHorizontalBottomRight()) {
			return true;
		}

		return false;
	}

	//是否水平左下
	function _isHorizontalBottomLeft() {
		if (_default.arrowType=="bottom-left") {
			return true;
		}

		return false;
	}
	
	//是否水平右下
	function _isHorizontalBottomRight() {
		if (_default.arrowType=="bottom-right") {
			return true;
		}

		return false;
	}

	//是否垂直
	function _isVertical() {
		return _isVerticalLeft() || _isVerticalRight();
	}

	//是否垂直左侧
	function _isVerticalLeft() {
		return _isVerticalLeftTop() || _isVerticalLeftBottom();
	}

	//是否垂直左上
	function _isVerticalLeftTop() {
		if (_default.arrowType=="left-top") {
			return true;
		}

		return false;
	}

	//是否垂直左下
	function _isVerticalLeftBottom() {
		if (_default.arrowType=="left-bottom") {
			return true;
		}

		return false;
	}

	//是否垂直右侧
	function _isVerticalRight() {
		return _isVerticalRightTop() || _isVerticalRightTop();
	}

	//是否垂直右上
	function _isVerticalRightTop() {
		if (_default.arrowType=="right-top") {
			return true;
		}

		return false;
	}

	//是否垂直右下
	function _isVerticalRightBottom() {
		if (_default.arrowType=="right-bottom") {
			return true;
		}

		return false;
	}

		//获取左侧的绝对值
	function _getAbsoluteLeft() {
		//把调用者作为当前使用对象
		var _current = _$this[0];
		//获取左侧定位
		var _left = _current.offsetLeft;
		//调用者的父对象
		var _parent = null;
		//向上迭代直到顶层容器
		while( _current.offsetParent != null) {
			_parent = _current.offsetParent;
			_left += _parent.offsetLeft;
			_current = _parent;
		}

		return _left;
	};

	//获取左侧的绝对值
	function _getAbsoluteTop() {
		//把调用者作为当前使用对象
		var _current = _$this[0];
		//获取左侧定位
		var _top = _current.offsetTop;
		//调用者的父对象
		var _parent = null;
		//向上迭代直到顶层容器
		while( _current.offsetParent != null) {
			_parent = _current.offsetParent;
			_top += _parent.offsetTop;
			_current = _parent;
		}

		return _top;
	};
	
	//获取箭头类型-线后面的信息
	function _getArrowTypeOfLast(){
		var index = _default.arrowType.indexOf("-");
		var length = _default.arrowType.length;
		var type = _default.arrowType.substring(index + 1,length);
		return type;
	}
	
	//移除弹出层
	function _remove() {
		$('.jtip').empty();
		$('.jtip').remove();
	};

	//-----------------公开函数----------------------
	$.fn.jtip.methods = {
		hide: _remove
	};

	//-----------------插件的默认值----------------------
	$.fn.jtip.defaults = {
		width: 100,					// 控件宽度
		height: 26,					// 控件高度
		marginX: 10,				// 距离调用控件的水平距离
		marginY: 1,				    // 距离调用控件的垂直距离
		message: '',				// 文本内容
		textAlign: 'center',		// 文本对齐方式
		textColor: 'green',			// 文本颜色
		backgroundColor: 'white',	// 背景颜色
		showArrow: true,			// 是否显示箭头
		arrowType: 'left-top',	    // 箭头类型 
		borderColor: '#CCC',		// 边框颜色
		arrowX: 0,					// 垂直方向时，此参数

//不起作用
		arrowY: 3,					// 水平方向时，此参数

//不起作用
		arrowSize: 'middle'         // 箭头大小 small/middle/large
	};
})(jQuery);