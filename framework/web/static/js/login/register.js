//取消链接点击外部虚线框
$('a').attr('hideFocus','true');


function flashCodeImg(urlPath){
	$("#codeImg").get(0).src=urlPath+'/desktop/verifyImage?time_='+ Math.random();  
}
function addError(id,errormsg){
	$("#"+id).addClass("input-txt-error");
	$("#"+id).parents('td').siblings(".tips-class").html('<span class="c-red ml-10">'+errormsg+'</span>');
}

function addSuccess(id,msg){
	$("#"+id).removeClass("input-txt-error");
	if(msg){
		$("#"+id).parents('td').siblings(".tips-class").html('<span class="c-green ml-10">'+msg+'</span>');
	}else{
		$("#"+id).parents('td').siblings(".tips-class").html('');
	}
}

function checkMsgCode(){
	var msgCode = $("#msgCode").val();
	if(msgCode && msgCode.length>0 ){
		addSuccess("msgCode");
	}else{
		addError("msgCode","请输入验证码");
	}
}

function checkFirstPwd(){
	var strongRegex = /^.*(?=.{8,})(?=.*\d)(?=.*[A-Za-z])(?=.*\W).*$/;//强,8位以上同时包含数字字母和其他字符
	var enoughRegex = /^.{0,7}$/; //弱,8位以下
	var pwd =$("#firstPwd").val();
	if(pwd==""){
		addError("firstPwd","请输入密码");
	}else if(enoughRegex.test(pwd)){
		$("#firstPwd").addClass("input-txt-error");
		$("#firstPwd").parent('td').siblings('.tips-class').html(
			'<div class="popover">'
			+'<div class="c-red">强度：太弱</div>'
			+'<div class="progress"><div class="progress-bar" style="width: 33%;background: #e33003;"></div></div>'
			+'<div class="c-666">密码为8-18个字符，由英文字母（区分大小写）、数字或符号组成。</div>'
			+'</div>'
			);
	}else if(strongRegex.test(pwd)){
		$("#firstPwd").removeClass("input-txt-error");
		$("#firstPwd").parent('td').siblings('.tips-class').html(
			'<div class="popover">'
			+'<div class="c-green">强度：安全</div>'
			+'<div class="progress"><div class="progress-bar" style="width: 100%;background: #4dc600;"></div></div>'
			+'<div class="c-666">密码为8-18个字符，由英文字母（区分大小写）、数字或符号组成。</div>'
			+'</div>'
			);
	}else{
		$("#firstPwd").removeClass("input-txt-error");
		$("#firstPwd").parent('td').siblings('.tips-class').html(
			'<div class="popover">'
			+'<div class="c-yellow">强度：一般</div>'
			+'<div class="progress"><div class="progress-bar" style="width: 66%;background: #fcae00;"></div></div>'
			+'<div class="c-666">密码为8-18个字符，由英文字母（区分大小写）、数字或符号组成。</div>'
			+'</div>'
			);
	}
}

function checkSecondPwd(){
	var secondpwd = $("#secondPwd").val();
	if(secondpwd && secondpwd.length>0 ){
		var pwd =$("#firstPwd").val();
		if(pwd==secondpwd){
			addSuccess("secondPwd");
		}else{
			addError("secondPwd","两次输入的密码不一致");
		}
	}else{
		addError("secondPwd","请再次确认密码");
	}
}

function checkMobilePhone(type,urlPath){
	var mobilePhone = $("#mobilePhone").val();
	var mobilePhoneReg = /^1(3|4|5|6|7|8|9)\d{9}$/;
	if(mobilePhone==""){
		addError("mobilePhone","请输入手机号");
		$("#getCode").removeClass('get-code-able').addClass('get-code-disable');
	}else if(mobilePhoneReg.test(mobilePhone)){
	 	$.ajax({
            url:urlPath+'/homepage/register/syghCheckMobilePhone',
            data:{"mobilePhone":mobilePhone},
            type:'post',
            success:function(data){
                var jsonO = JSON.parse(data);
                if(type=="1"){//注册
	                if(jsonO.success){
	                    addSuccess("mobilePhone");
	                    if(!$("#getCode").hasClass("get-code-in")){
							$("#getCode").addClass('get-code-able').removeClass('get-code-disable');
	                    }
	                }else{
	                	addError("mobilePhone","该手机号已被注册");
						$("#getCode").removeClass('get-code-able').addClass('get-code-disable');
	                }
                }else{//找回密码
	                if(jsonO.success){
	                	addError("mobilePhone","该手机号还未注册");
						$("#getCode").removeClass('get-code-able').addClass('get-code-disable');
	                }else{
	                    addSuccess("mobilePhone");
	                    if(!$("#getCode").hasClass("get-code-in")){
							$("#getCode").addClass('get-code-able').removeClass('get-code-disable');
	                    }
	                }
                }
            },
            error : function(XMLHttpRequest, textStatus, errorThrown) {
            }
        });
	 }else{
	 	addError("mobilePhone","请输入正确的手机号码");
		$("#getCode").removeClass('get-code-able').addClass('get-code-disable');
	 }
}


/*
基于jquery的发送验证码倒数功能
用法：
1、html：不做任何改变，只需有一个id或是class
2、js根据id或class来控制
sendcode('#phoneNum','#btnSendCode',60,5)
*/
function sendcode(num,obj,count,n,urlPath,type){
	//因为ios应用页面是不会刷新的
	var timeCount = count; //定义倒计时总数
	var sendTime = n; //发送验证码次数
	$(obj).click(function(e){
		e.preventDefault();
		if($(this).hasClass('get-code-able')){
			$.ajax({
	            url:urlPath+'/homepage/register/getMsgCode',
	            data:{"mobilePhone":$(num).val(),"type":type},
	            type:'post',
	            success:function(data){
	                var jsonO = JSON.parse(data);
	                if(jsonO.success){
	                    addSuccess("msgCode",jsonO.msg);
						var start_time = new Date();
						start_time = start_time.getTime();//获取开始时间的毫秒数
						$(obj).addClass('get-code-disable get-code-in').removeClass('get-code-able').text(timeCount + "s后重新获取");
						downTime = setInterval(function(){
							//倒计时实时结束时间
							var end_time = new Date();
							end_time = end_time.getTime();
							//得到剩余时间
							var dtime = timeCount - Math.floor((end_time - start_time) / 1000);
							$(obj).text(dtime + "s后重新获取");
								if(dtime <= 0){
								$(obj).addClass('get-code-able').removeClass('get-code-disable get-code-in').text("获取验证码");;//启用按钮
								checkMobilePhone(type,urlPath);
								window.clearInterval(downTime);
							};
						},1000);
	                }else{
	                	addError("msgCode",jsonO.msg);
	                }
	            },
	            error : function(XMLHttpRequest, textStatus, errorThrown) {
	            }
		    });
		};
	});
};

$("#secondPwd").bind('keypress',function(event){
	if(event.keyCode == "13"){
    	$("#subBtn").click();
    }
});