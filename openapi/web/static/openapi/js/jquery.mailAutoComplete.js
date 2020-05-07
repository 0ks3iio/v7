(function($){
	$.fn.mailAutoComplete = function(options){
		var opts = $.extend({}, $.fn.mailAutoComplete.defaults, options);
		return this.each(function(){
			var $this = $(this);
			var o = $.meta ? $.extend({}, opts, $this.data()) : opts;
			
			var top = $this.height() + 18;
			var left = 0;
			var width = $this.width()+26;
			var $mailBox = $('<div id="mailBox" style="top:'+top+'px;left:'+left+'px;width:'+width+'px"></div>');
			$(this).wrap('<div class="ui-mail"></div>');
			$('.ui-mail').append($mailBox);
			
			//设置高亮li
			function setEmailLi(index){
				$('#mailBox li').removeClass('cmail').eq(index).addClass('cmail');
			}
			//初始化邮箱列表
			var emails = o.emails;
			var init = function(input){
				//取消浏览器自动提示
				input.attr('autocomplete','off');
				//添加提示邮箱列表
				if(input.val()!=""){
					var emailList = '<p>请选择邮箱类型</p><ul>';
					if(input.val().indexOf('@') == -1){
						for(var i = 0; i < emails.length; i++) {
							emailList += '<li>'+input.val()+'@'+emails[i]+'</li>';
						};
					}else{
						email_suffix = input.val();	//获取邮箱后缀
						sp_email_suffix = email_suffix.split("@");
						email_sub = sp_email_suffix[1];	//获取邮箱@后部
						for(var i = 0; i < emails.length; i++) {
							if(emails[i].indexOf(email_sub)>-1){
								var email_mate = emails[i].replace(email_sub,'');	//去掉匹配项@后面的相关字符
								emailList += '<li>'+input.val()+email_mate+'</li>';
							};
						};
					};
					emailList += '</ul>';
					$mailBox.html(emailList).show(0);
				}else{
					$mailBox.hide(0);
				}
				//添加鼠标事件
				$('#mailBox li').hover(function(){
					$('#mailBox li').removeClass('cmail');
					$(this).addClass('cmail');
				},function(){
					$(this).removeClass('cmail');
				}).click(function(){
					input.val($(this).html());
					$mailBox.hide(0);
				});
			}
			//当前高亮下标
			var eindex = -1;
			//监听事件
			$this.focus(function(){
				if($this.val().indexOf('@') == -1){
					init($this);
				}else{
					$mailBox.hide(0);
				}
			}).blur(function(){
				setTimeout(function(){
					$mailBox.hide(0);
				},1000);//
			}).keyup(function(event){
				if($this.val().indexOf('@') >= -1){
					//上键
					if(event.keyCode == 40){
						eindex ++;
						if(eindex >= $('#mailBox li').length){
							eindex = 0;
						}
						setEmailLi(eindex);
					//下键
					}else if(event.keyCode == 38){
						eindex --;
						if(eindex < 0){
							eindex = $('#mailBox li').length-1;
						}
						setEmailLi(eindex);
					//回车键
					}else if(event.keyCode == 13){
						if(eindex >= 0){
							$this.val($('#mailBox li').eq(eindex).html());
							$mailBox.hide(0);
						}
					}else{
						eindex = -1;
						init($this);
					}
				}else{
					$mailBox.hide(0);
				}
			//如果在表单中，防止回车提交
			}).keydown(function(event){
				if(event.keyCode == 13){
					return false;
				}
			});
		});
	}
	$.fn.mailAutoComplete.defaults = {
		emails:[]
	}
})(jQuery);