<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title>登录</title>
    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/static/mui/css/mui.min.css"/>
    <link href="${request.contextPath}/static/mui/css/pages.css" rel="stylesheet"/>
</head>
<body class="mui-bg-white">
    <header class="mui-bar mui-bar-nav">
        <a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left"></a>
        <button class="mui-btn mui-btn-link mui-pull-right" onclick="doRegister()">注册</button>
    </header>		
    <div class="mui-content mui-bg-white career">
        <div class="career-hd">密码登录</div>
        <form class="career-form mui-input-group">
        	<div class="mui-input-row">
        	    <label>手机号</label>
        	    <input id="mobilePhone" name="mobilePhone" type="text" class="mui-input-clear" placeholder="请输入手机号">
        	</div>
        	<div class="mui-input-row">
        	    <label>密码</label>
        	    <input id="password" name="password" type="password" class="mui-input-password" placeholder="请输入密码">
        	</div>
        </form>
        <button type="button" class="mui-btn mui-btn-blue mui-btn-block no-padding btn-radius-22 lh44" onclick="doLogin()">登录</button>
        <div class="mui-text-right"><a class="f-14 c-999" href="${request.contextPath}/homepage/register/syghPwd/page">忘记密码？</a></div>
    </div>
    <script src="${request.contextPath}/static/mui/js/jquery-1.9.1.min.js"></script>
    <script src="${request.contextPath}/static/mui/js/mui.min.js" type="text/javascript" charset="utf-8"></script>
    <script type="text/javascript">
    	$(function(){
    		$(".career-form .mui-input-row input").focus(function(){
    			$(this).parent().addClass("focus")
	    	})
    		$(".career-form .mui-input-row input").blur(function(){
    			$(this).parent().removeClass("focus")
	    	})
    	})
    	
    	function doRegister(){
    		window.location.href="${request.contextPath}/homepage/register/syghReg/page";
    	}
    	
    	var isSubmit = false;
    	function doLogin() {
    		if(isSubmit){
    			return;
    		}
			var mobilePhone = $("#mobilePhone").val();
			if(mobilePhone==""){
				mui.toast('请输入手机号')
				//$("#mobilePhone").focus();
				return;
			}else{
				var mobilePhoneReg = /^1(3|4|5|6|7|8|9)\d{9}$/;
				if(!mobilePhoneReg.test(mobilePhone)){
					mui.toast('请输入正确的手机号码');
					return;
				}
			}
			var password = $("#password").val();
			if(password==""){
				mui.toast('请输入密码')
				//$("#password").focus();
				return;
			}
			isSubmit = true;
			var options = {  
				url:'${request.contextPath}/homepage/register/syghLogin/login',
				data:{"mobilePhone":mobilePhone,"password":password,"autoLogin":"1"},
				clearForm : false,
		   		resetForm : false,
				dataType:'json',
				type:'post',
				success:function(data){
					if(data.success){
						window.location.href='${request.contextPath}/homepage/register/syghLogin/loginForPassport?username='+data.msg;
					}else{
						isSubmit=false;
						mui.toast(data.msg);
					}
				}
			};
			$.ajax(options);
		}
    </script>
</body>
</html>
