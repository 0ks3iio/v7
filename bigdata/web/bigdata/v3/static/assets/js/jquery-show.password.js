//$('.js-password').showPassord({
//	show: true
//});
//密码显示隐藏
;
(function($, window, document, undefined){
	//默认参数
	var defaults = {
		show: false
	};
	
	//定义构造函数
	function ShowPassword(element, setting) {
		this.element = element;
		this.options = $.extend({}, defaults, setting);
		this.init();
	};

	//定义方法
	ShowPassword.prototype = {
		//初始化
		init: function() {
			//console.log(this.options);
			var _ = this,
				_el = $(this.element),
				className;
	    	if (this.options.show === false) {
	    		_el.prop('type', 'password');
	    		className = 'hide';
	    	} else{
	    		_el.prop('type', 'text');
	    		className = 'show';
	    	}
	    	_el.wrap('<div class="form-group-pwd"></div>').after('<i class="' + className + '"></i>');	
			_el.siblings('i').on('click.showPassword', function(e) {
				e.preventDefault();
				_i = $(this);
				_.change();
			});
		},
		//改变状态
		change: function() {
			if (_i.hasClass('hide')) {
    			_i.addClass('show').removeClass('hide').siblings('.form-control').attr('type', 'text');
    		} else{
    			_i.addClass('hide').removeClass('show').siblings('.form-control').attr('type', 'password');
    		}
		}
	};
	
	//定义插件
	$.fn.showPassword = function(options) {
        return this.each(function(index, element) {
            element.showPassword = new ShowPassword(element, options);
        });
	};
	
})(jQuery, window, document);