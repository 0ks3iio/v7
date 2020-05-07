//$('.form-number').formNumber({
//	step: 2,
//	min: 0,
//	max: 9999
//});
//表单字数校验
;
(function($, window, document, undefined){
	//默认参数
	var defaults = {
		step: 1,
		min: 0,
		max: 9999
	};
	
	//定义构造函数
	function FormNumber(element, setting) {
		this.element = element;
		this.options = $.extend({}, defaults, setting);
		this.init();
	};

	//定义方法
	FormNumber.prototype = {
		//初始化
		init: function() {
			//console.log(this.options);
			var _ = this,
				_el = $(this.element);
			_el.val(this.options.min).addClass('form-control').wrap('<div class="form-number"></div>').after('<a href="#" class="form-number-add">+</a><a href="#" class="form-number-sub">-</a>');
			_el.on('keyup.formNumber', function(e) {
				_.verify();
			});
			_el.siblings('a').on('click.formNumber', function(e) {
				e.preventDefault();
				_a = $(this);
				_.calculate();
			});
		},
		//格式校验
		verify: function() {
			var _ = this,
				_el = $(this.element),
				val = _el.val(),
				min = this.options.min,
				max = this.options.max,
				num = /^(\-?)\d+$/;
			if((!num.test(val) && val != '' && val != '-') || (val.length > 1 && val.substring(0,1) == 0)){
				//调用layer.js
				layer.msg('请输入规范的数字！', {
					icon: 2,
					time: 1500,
					shade: 0.2
				});
				_el.val('');
			};
			if(val < min){
				//调用layer.js
				layer.msg('请输入大于等于' + min + '的数字！', {
					icon: 2,
					time: 1500,
					shade: 0.2
				});
				_el.val(min);
			};
			if(val > max){
				//调用layer.js
				layer.msg('请输入小于等于' + max + '的数字！', {
					icon: 2,
					time: 1500,
					shade: 0.2
				});
				_el.val(max);
			};
		},
		//加减运算
		calculate: function() {
			var _ = this,
				_el = $(this.element),
				val = _el.val(),
				min = this.options.min,
				max = this.options.max,
				step = this.options.step;
			if(!val) val = 0;
			var num = parseInt(val);
			if(_a.hasClass('form-number-add')) {
				num += step;
			} else {
				num -= step;
			}
			if (num <= min) {
				//调用layer.js
				layer.msg(min + '为最小值', {
					icon: 2,
					time: 1500,
					shade: 0.2
				});
				num = min;
			} else if (num > max) {
				//调用layer.js
				layer.msg(max + '为最大值', {
					icon: 2,
					time: 1500,
					shade: 0.2
				});
				num = max;
			}
			_el.val(num);
		}
	};
	
	//定义插件
	$.fn.formNumber = function(options) {
        return this.each(function(index, element) {
            element.formNumber = new FormNumber(element, options);
        });
	};
	
})(jQuery, window, document);