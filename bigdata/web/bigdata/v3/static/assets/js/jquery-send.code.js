//$('.js-password').sendCode({
//	btn: 'js-code-btn',
//	counts: 60,
//	time: 5
//});
//发送验证码
;
(function($, window, document, undefined){
	//默认参数
	var defaults = {
		btn: '.js-code-btn',
		counts: 60,
		times: 5
	};
	
	//定义构造函数
	function SendCode(element, setting) {
		this.element = element;
		this.options = $.extend({}, defaults, setting);
		this.init();
	};

	//定义方法
	SendCode.prototype = {
		//初始化
		init: function() {
			//console.log(this.options);
			var _ = this,
				_el = $(this.element),
				_btn = $(this.options.btn);
			_el.attr({'maxlength': 11});
			_el.on('focus.sendCode keyup.sendCode', function(e) {
				_.verify();
			});
			_btn.on('click.sendCode', function(e) {
				e.preventDefault();
				_.send();
			});
		},
		//校验格式
		verify: function() {
			var _ = this,
				_el = $(this.element),
				_btn = $(this.options.btn),
				val = _el.val(),
				phoneNum = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/;
			if (val.length === 11) {
				if(!phoneNum.test(val)){
					//调用layer.js
					layer.msg('请输入正确的手机号码！', {
						icon: 2,
						time: 1500,
						shade: 0.2
					});
				} else{
					_btn.prop('disabled', false);
				};
			} else{
				_btn.prop('disabled', true);
			}
		},
		//发送验证码
		send: function() {
			var _ = this,
				_el = $(this.element),
				_btn = $(this.options.btn),
	    		times = this.options.times,
				counts = this.options.counts;
			times--;
			this.options.times = times;
			if(_btn.prop('disabled') === false){
				var start_time = new Date();
				start_time = start_time.getTime();//获取开始时间的毫秒数
				if(times >= 0){
					_btn.prop('disabled', true).text(counts + "秒后重获取");
					downTime = setInterval(function(){
						//倒计时实时结束时间
						var end_time = new Date();//获取结束时间的毫秒数
						end_time = end_time.getTime();
						//得到剩余时间
						var d_time = counts - Math.floor((end_time - start_time) / 1000);
						_btn.text(d_time + "秒后重获取");
							if(d_time <= 0){
								_btn.prop('disabled', false).text("获取验证码");//启用按钮
								_el.prop('disabled', false);//启用输入框
								window.clearInterval(downTime);
							};
					},1000);
					_el.prop('disabled', true);
				} else{
					//调用layer.js
					layer.msg('发送次数过多，请明天再来！', {
						icon: 2,
						time: 1500,
						shade: 0.2
					});
				};
			};
	    }
	};
	
	//定义插件
	$.fn.sendCode = function(options) {
        return this.each(function(index, element) {
            element.sendCode = new SendCode(element, options);
        });
	};
	
})(jQuery, window, document);