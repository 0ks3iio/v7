/*
2015-12-09
基于jquery的发送验证码倒数功能
用法：
1、html：不做任何改变，只需有一个id或是class
2、js根据id或class来控制
sendcode('#phoneNum','#btnSendCode',60,5)
*/

function sendcode(num,obj,count,n){
	//因为ios应用页面是不会刷新的
	var timeCount = count; //定义倒计时总数
	var sendTime = n; //发送验证码次数
	$(obj).attr('data-max',sendTime);
	//正则验证手机号码有效性
	var phoneNum = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/;
	$(num).on('keyup focus',function(){
		if(!$(obj).hasClass('get-code-in')){
			if(!phoneNum.test($(num).val())){ 
				$(obj).removeClass('get-code-able').addClass('get-code-disable'); 
			}else{
				$(obj).addClass('get-code-able').removeClass('get-code-disable'); 
			};
		};
	});
	$(obj).click(function(e){
		e.preventDefault();
		if($(this).hasClass('get-code-able')){
			var start_time = new Date();
			start_time = start_time.getTime();//获取开始时间的毫秒数
			var newTime = parseInt($(obj).attr('data-max')-1);
			if(newTime >= 0){
				$(obj).attr('data-max',newTime).addClass('get-code-disable get-code-in').removeClass('get-code-able').text(timeCount + "秒后重获取");
				
				downTime = setInterval(function(){
					//倒计时实时结束时间
					var end_time = new Date();
					end_time = end_time.getTime();
					//得到剩余时间
					var dtime = timeCount - Math.floor((end_time - start_time) / 1000);
					$(obj).text(dtime + "秒后重获取");
						if(dtime <= 0){
						$(obj).addClass('get-code-able').removeClass('get-code-disable get-code-in').text("获取验证码");;//启用按钮
						window.clearInterval(downTime);
					};
				},1000);
			}else{
				alert('发送次数过多，请明天再来！');
			};
		};
	});
};