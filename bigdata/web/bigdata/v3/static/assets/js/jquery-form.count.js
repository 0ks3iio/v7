//$('.form-count').formCount({
//	max: 100
//});
//表单字数校验
;
(function($, window, document, undefined){
	//默认参数
	var defaults = {
		max: 100
	};
	
	//定义构造函数
	function FormCount(element, setting) {
		this.element = element;
		this.options = $.extend({}, defaults, setting);
		this.init();
	};

	//定义方法
	FormCount.prototype = {
		//初始化
		init: function() {
			//console.log(this.options);
			var _ = this,
				_el = $(this.element);
			_el.addClass('form-control').wrap('<div class="form-count"></div>').after('<span class="form-count-max"><span class="form-count-num">0</span>/' + this.options.max + '</span>');
			_el.on('focus.formCount keydown.formCount keyup.formCount', function(e) {
				_.verify();
			});
		},
		//校验字数
		verify: function() {
			var _ = this,
				_el = $(this.element),
				_num = _el.next('.form-count-max').children('.form-count-num'),
				num = _el.val().length,
				count = this.options.max;
			//计算字符
			if(num > count){
				var moreCount = parseInt(num - count);
				_num.css('color','#f00').text('超过'+moreCount);
			} else{
				_num.removeAttr('style').text(num);
			};
		}
	};
	
	//定义插件
	$.fn.formCount = function(options) {
        return this.each(function(index, element) {
            element.formCount = new FormCount(element, options);
        });
	};
	
})(jQuery, window, document);