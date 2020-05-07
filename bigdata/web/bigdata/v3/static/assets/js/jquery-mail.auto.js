//邮箱自动输入
;
(function($, window, document, undefined){
	//默认参数
	var defaults = {
		emails:['qq.com','163.com','126.com','sina.com','sohu.com','yahoo.cn','gmail.com','hotmail.com','live.cn']
	};
	
	//定义构造函数
	function MailAuto(element, setting) {
		this.element = element;
		this.options = $.extend({}, defaults, setting);
		this.show();
	};

	//定义方法
	var eindex = -1;
	MailAuto.prototype = {
		//显示
		show: function() {
    		var _ = this,
				_el = $(this.element),
	    		emails = this.options.emails,
	    		initStr = '<div class="mailAuto-list"></div>';
			_el.on('focus.mailAuto', function(e) {
				$('.mailAuto-from').removeClass('mailAuto-from');
				_el.addClass('mailAuto-from');
				if ($('.mailAuto-list')) {
					$('.mailAuto-list').remove();
				}
				$('body').append(initStr);
				var o_top = _el.offset().top + _el.height();
				var o_left = _el.offset().left;
				$('.mailAuto-list').css({top: o_top, left: o_left});
			});
			_el.on('keyup.mailAuto focus.mailAuto', function(e) {
				var _mail = $('.mailAuto-list'),
					txt = _el.val();
				_mail.text(txt);
				if(txt != '') {
					//console.log(txt.indexOf('@'));
					var emailList = '<p>请选择邮箱类型</p><ul>';
					if(txt.indexOf('@') === -1) {
						for(var i = 0; i < emails.length; i++) {
							emailList += '<li>' + txt + '@' + emails[i] + '</li>';
						};
					} else {
						email_suffix = txt; //获取输入内容
						sp_email_suffix = email_suffix.split("@"); //分割@
						email_sub = sp_email_suffix[1]; //获取邮箱后缀-@后面部分
						if(email_sub.indexOf('.') === 0 || txt.split('@').length - 1 > 1) {
							//调用layer.js
							layer.msg('邮箱格式不正确！', {
								icon: 2,
								time: 10500,
								shade: 0.2
							});
						} else {
							for(var i = 0; i < emails.length; i++) {
								if(emails[i].indexOf(email_sub) > -1) {
									var email_mate = emails[i].replace(email_sub, ''); //去掉匹配项@后面的相关字符
									emailList += '<li>' + txt + email_mate + '</li>';
								};
							};
						};
					};
					emailList += '</ul>';
					_mail.html(emailList).show();
				} else {
					_mail.hide();
				}
				//添加鼠标事件
				$('.mailAuto-list li').hover(function(){
					$(this).addClass('active').siblings('li').removeClass('cmail');
				},function(){
					$(this).removeClass('active');
				}).click(function(){
					_el.val($(this).text());
					_mail.remove();
				});
			});
			_el.on('keyup.mailAuto', function(event) {
				_.selected();
			});
			$(document).click(function(event){
				eo = $(event.target);
				_.hide();
			});
	    },
	    //隐藏功能组件
	    hide: function(){
			if(!eo.hasClass('mailAuto-from')){
				$('.mailAuto-list').remove();
				$('.mailAuto-from').removeClass('mailAuto-from');
			}
	    },
		//选择、设置高亮
		selected: function() {
			var _ = this,
				_el = $(this.element),
				_mail = $('.mailAuto-list'),
				_li = $('.mailAuto-list li');
			//上键
			if(event.keyCode === 40){
				eindex ++;
				if(eindex >= _li.length){
					eindex = 0;
				}
				setEmailLi(eindex);
			//下键
			} else if(event.keyCode === 38){
				eindex --;
				if(eindex < 0){
					eindex = _li.length-1;
				}
				setEmailLi(eindex);
			//回车键
			} else if(event.keyCode === 13){
				if(eindex >= 0){
					_el.val(_li.eq(eindex).text()).blur();
					_mail.remove();
				}
				eindex = -1;
			}
		}
	};
	
	//定义插件
	$.fn.mailAuto = function(options) {
        return this.each(function(index, element) {
            element.mailAuto = new MailAuto(element, options);
        });
	};
	
	//设置高亮li
	function setEmailLi(index){
		$('.mailAuto-list li:eq(' + index + ')').addClass('active').siblings('li').removeClass('active');
	}
	
})(jQuery, window, document);