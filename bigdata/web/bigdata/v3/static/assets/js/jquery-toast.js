//$('.toast').toast({
//	event: 0,
//});
//toast切换
;
(function($, window, document, undefined){
	//默认参数
	var defaults = {
		title: '成功', //提示文字	
		type: 1,      //类型：1-成功succeed、2-失败failure、3-警告warning、4-出错error、5-加载loading、6-纯文本text
		time: 2000,   //自动隐藏时间
		mask: 0       //遮罩层：0-纯透明、1-灰色半透明
	};
	
	//定义构造函数
	function Toast(element, setting) {
		this.element = element;
		this.options = $.extend({}, defaults, setting);
		this.show();
	};

	//定义方法
	Toast.prototype = {
		//切换
		show: function() {
			//console.log(this.options);
			var _ = this,
				_el = $(this.element),
				title = this.options.title,
				type = this.options.type,
				time = this.options.time,
				mask = this.options.mask;
			_el.on('click', function(e) {
				e.preventDefault();
				var str = '',
					className = '';
				switch (type){
					//1-成功succeed、2-失败failure、3-警告warning、4-出错error、5-加载loading、6-纯文本text
					case 1:
						className = ' toast-succeed';
						break;
					case 2:
						className = ' toast-failure';
						break;
					case 3:
						className = ' toast-warning';
						break;
					case 4:
						className = ' toast-error';
						break;
					case 5:
						className = ' toast-loading';
						break;
					case 6:
						className = ' toast-text';
						break;	
				}
				if (mask === 1) {
					className += ' toast-gray';
				}
				if (type == 6) {
					str = '<div class="toast' + className + '"><div class="toast-outer"><div class="toast-inner">' + title + '</div></div></div>'
				} else{
					str = '<div class="toast' + className + '"><div class="toast-outer"><div class="toast-inner"><i></i><br />' + title + '</div></div></div>'
				}
				$('body').addClass('scroll-no').append(str);
				if(time != 0){
					$('.toast').fadeIn(300).delay(time).fadeOut(300,function(){
						$('.toast').remove();
						$('body').removeClass('scroll-no')
					});
				} else{
					$('.toast').fadeIn(300);
				};
			});
		}
	};
	
	//定义插件
	$.fn.toast = function(options) {
        return this.each(function(index, element) {
            element.toast = new Toast(element, options);
        });
	};
	
})(jQuery, window, document);