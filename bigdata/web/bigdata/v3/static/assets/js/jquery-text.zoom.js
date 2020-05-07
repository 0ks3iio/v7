//$('#textZoom1').textZoom({
//	offset: 1,
//	split: 4,
//	format: '-'
//});
//文字放大校对
;
(function($, window, document, undefined){
	//默认参数
	var defaults = {
		offset: 1,
		split: 4,
		format: '-'
	};
	
	//定义构造函数
	function TextZoom(element, setting) {
		this.element = element;
		this.options = $.extend({}, defaults, setting);
		this.show();
	};

	//定义方法
	TextZoom.prototype = {
		//显示功能组件
    	show: function() {
    		var _ = this,
				_el = $(this.element),
				offset = this.options.offset,
	    		splitType = this.options.split,
	    		connector = this.options.format,
	    		initStr = '<span class="text-zoom"></span>';
			_el.on('focus.textZoom', function(e) {
				$('.text-zoom-from').removeClass('text-zoom-from');
				_el.addClass('text-zoom-from');
				if ($('.text-zoom')) {
					$('.text-zoom').remove();
				}
				$('body').append(initStr);
				if(offset === 1){
					var o_top = _el.offset().top - $('.text-zoom').height() - 10;
				} else{
					var o_top = _el.offset().top + _el.height();
				}
				var o_left = _el.offset().left;
				$('.text-zoom').css({top: o_top, left: o_left});
			});
			_el.on('keyup.textZoom focus.textZoom', function(e) {
				var _zoom = $('.text-zoom');
				_zoom.text(splitStr(splitType, connector));
				if (_zoom.text().length > 0) {
					_zoom.show();
				} else{
					_zoom.hide();
				}
			});
			$(document).click(function(event){
				eo = $(event.target);
				_.hide();
			});
	    },
		//隐藏功能组件
	    hide: function(){
			if(!eo.hasClass('text-zoom-from')){
				$('.text-zoom').remove();
				$('.text-zoom-from').removeClass('text-zoom-from');
			}
	    }
	};
	
	//定义插件
	$.fn.textZoom = function(options) {
        return this.each(function(index, element) {
            element.textZoom = new TextZoom(element, options);
        });
	};
	
	//分割字符串方法
	function splitStr(splitType, connector){
		var $from = $('.text-zoom-from');
		var splitType, connector
		var txtStr = $from.val();
		var txtLen = txtStr.length;
		var txtMax = Math.ceil(txtLen / splitType);
		var splitTxt = '';
		for(i=0;i<txtMax; i++){
			var splitStart = i * splitType;
			splitTxt = splitTxt + txtStr.substr(splitStart, splitType) + connector;
		};
		splitTxt = splitTxt.substring(0, splitTxt.length - 1);
		return splitTxt;
	};
	
})(jQuery, window, document);