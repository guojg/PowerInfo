;(function($){
	//���ô˲���Ķ���
	var _$this = null;
	//Ĭ������
	var _default = {};

	//-----------------������----------------------
	$.fn.jtip = function(options, param) {
		//�����һ���������ַ�������Ϊ�Ƿ������ã���������Ϊ{}���Դ�ֵ
		if (typeof options == 'string'){
			var method = $.fn.jtip.methods[options];
			if (method){
				return method(this, param);
			} else {
				return false;
			}
		}

		//���Լ�����options�Ͳ��Ĭ��ֵ������һ��
		$.extend($.fn.jtip.defaults, options);
		//��ȡĬ������
		_default = $.fn.jtip.defaults;

		return this.each(function() {
			//��ȡ��ǰ���ô˲���Ķ���
			_$this = $(this);
			//��ʼ��
			_init();
		});
	};

	//-----------------˽�к���----------------------
	function _init() {
		//���Ƴ���ǰ��jtip
		_remove();
		//��ȡjtip��׷�ӵ��ĵ���
		$("body").append(_getJtipHtml());
		//����jtip����ʽ
		_setStyles();
		//��ʾjtip
		$('.jtip').show();
	};
		
	function _getJtipHtml() {
		return $("<div class='jtip'>" + 
					 "<div><div></div></div>" +
					 "<div>" + _default.message + "</div>" +
				 "</div>");
	}

	function _setStyles() {
		//���ü�ͷ��ʽ
		_setArrowType();
		//���������ɶ��ƻ���ʽ
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
		/********************** ������ʽ *******************************/
		_setWholeStyle();
		/********************** ��ͷ��ʽ *******************************/
		 _setArrowStyle();
		/********************** �ı���ʽ *******************************/
		_setTextStyle();
		
	}

	//����������ʽ
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
	
	//���ü�ͷ����ʽ
	function _setArrowStyle() {
		var left = 0;
		var top = 0;
		if (_default.showArrow) {
			if (_isHorizontal()){
				//ˮƽ����
				if (_isHorizontalTop()){
					//����
					if (_isHorizontalTopLeft()) {
						left = 3 + _getCorrectX();
					} else {
						left = -8 + _getCorrectX();
					}
					//���ü�ͷ��С
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
			//���ؼ�ͷ
			left = -10000;
			top = -10000;
		}
		
		$('.jtip > div:first').css('left', left);
		$('.jtip > div:first').css('top', top);
		$('.jtip > div:first').css('border-' + _getArrowTypeOfLast() + '- color', _default.borderColor);
		$('.jtip > div:first > div').css('border-' + _getArrowTypeOfLast() + 

'-color', _default.backgroundColor);
	}

	//�����ı���ʽ
	function _setTextStyle() {
		$('.jtip > div:last').css('width', (_default.width - 4));
		$('.jtip > div:last').css('height', _default.height - 20);
		$('.jtip > div:last').css('color', _default.textColor);
		$('.jtip > div:last').css('text-align', _default.textAlign);
		$('.jtip > div:last').css('background', _default.backgroundColor);
	}

	//��ȡX����ֵ
	function _getCorrectX() {
		if (_default.arrowX < 0) {
			return 0;
		} else if (_default.arrowX > (_default.width - 25)){
		    return (_default.width - 25);
		} else {
			return _default.arrowX;
		}
	}

	//��ȡY����ֵ
	function _getCorrectY() {
		if (_default.arrowY < 0) {
			return 0;
		} else if (_default.arrowY > _default.height - 16){
		    return _default.height - 16;
		} else {
			return _default.arrowY;
		}
	}

	//�Ƿ���ˮƽ����
	function _isHorizontal() {
		return _isHorizontalTop() || _isHorizontalBottom();
	}

	//�Ƿ���ˮƽ������
	function _isHorizontalTop() {
		if (_isHorizontalTopLeft() || _isHorizontalTopRight()) {
			return true;
		}

		return false;
	}

	//�Ƿ�ˮƽ����
	function _isHorizontalTopLeft() {
		if (_default.arrowType=="top-left") {
			return true;
		}

		return false;
	}

	//�Ƿ�ˮƽ����
	function _isHorizontalTopRight() {
		if ( _default.arrowType=="top-right") {
			return true;
		}

		return false;
	}

	//�Ƿ���ˮƽ������
	function _isHorizontalBottom() {
		if (_isHorizontalBottomLeft() || _isHorizontalBottomRight()) {
			return true;
		}

		return false;
	}

	//�Ƿ�ˮƽ����
	function _isHorizontalBottomLeft() {
		if (_default.arrowType=="bottom-left") {
			return true;
		}

		return false;
	}
	
	//�Ƿ�ˮƽ����
	function _isHorizontalBottomRight() {
		if (_default.arrowType=="bottom-right") {
			return true;
		}

		return false;
	}

	//�Ƿ�ֱ
	function _isVertical() {
		return _isVerticalLeft() || _isVerticalRight();
	}

	//�Ƿ�ֱ���
	function _isVerticalLeft() {
		return _isVerticalLeftTop() || _isVerticalLeftBottom();
	}

	//�Ƿ�ֱ����
	function _isVerticalLeftTop() {
		if (_default.arrowType=="left-top") {
			return true;
		}

		return false;
	}

	//�Ƿ�ֱ����
	function _isVerticalLeftBottom() {
		if (_default.arrowType=="left-bottom") {
			return true;
		}

		return false;
	}

	//�Ƿ�ֱ�Ҳ�
	function _isVerticalRight() {
		return _isVerticalRightTop() || _isVerticalRightTop();
	}

	//�Ƿ�ֱ����
	function _isVerticalRightTop() {
		if (_default.arrowType=="right-top") {
			return true;
		}

		return false;
	}

	//�Ƿ�ֱ����
	function _isVerticalRightBottom() {
		if (_default.arrowType=="right-bottom") {
			return true;
		}

		return false;
	}

		//��ȡ���ľ���ֵ
	function _getAbsoluteLeft() {
		//�ѵ�������Ϊ��ǰʹ�ö���
		var _current = _$this[0];
		//��ȡ��ඨλ
		var _left = _current.offsetLeft;
		//�����ߵĸ�����
		var _parent = null;
		//���ϵ���ֱ����������
		while( _current.offsetParent != null) {
			_parent = _current.offsetParent;
			_left += _parent.offsetLeft;
			_current = _parent;
		}

		return _left;
	};

	//��ȡ���ľ���ֵ
	function _getAbsoluteTop() {
		//�ѵ�������Ϊ��ǰʹ�ö���
		var _current = _$this[0];
		//��ȡ��ඨλ
		var _top = _current.offsetTop;
		//�����ߵĸ�����
		var _parent = null;
		//���ϵ���ֱ����������
		while( _current.offsetParent != null) {
			_parent = _current.offsetParent;
			_top += _parent.offsetTop;
			_current = _parent;
		}

		return _top;
	};
	
	//��ȡ��ͷ����-�ߺ������Ϣ
	function _getArrowTypeOfLast(){
		var index = _default.arrowType.indexOf("-");
		var length = _default.arrowType.length;
		var type = _default.arrowType.substring(index + 1,length);
		return type;
	}
	
	//�Ƴ�������
	function _remove() {
		$('.jtip').empty();
		$('.jtip').remove();
	};

	//-----------------��������----------------------
	$.fn.jtip.methods = {
		hide: _remove
	};

	//-----------------�����Ĭ��ֵ----------------------
	$.fn.jtip.defaults = {
		width: 100,					// �ؼ����
		height: 26,					// �ؼ��߶�
		marginX: 10,				// ������ÿؼ���ˮƽ����
		marginY: 1,				    // ������ÿؼ��Ĵ�ֱ����
		message: '',				// �ı�����
		textAlign: 'center',		// �ı����뷽ʽ
		textColor: 'green',			// �ı���ɫ
		backgroundColor: 'white',	// ������ɫ
		showArrow: true,			// �Ƿ���ʾ��ͷ
		arrowType: 'left-top',	    // ��ͷ���� 
		borderColor: '#CCC',		// �߿���ɫ
		arrowX: 0,					// ��ֱ����ʱ���˲���

//��������
		arrowY: 3,					// ˮƽ����ʱ���˲���

//��������
		arrowSize: 'middle'         // ��ͷ��С small/middle/large
	};
})(jQuery);