<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title><#if step?default('3')=='3'>注册<#else>找回密码</#if></title>
    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/static/mui/css/mui.min.css"/>
    <link href="${request.contextPath}/static/mui/css/pages.css" rel="stylesheet"/>
</head>
<body class="mui-bg-white">
    <header class="mui-bar mui-bar-nav">
        <a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left"></a>
    </header>		
    <div class="mui-content mui-bg-white career">
        <div class="career-hd">设置密码</div>
        <form class="career-form mui-input-group" id="registerForm" onsubmit="return false;">
    	    <input type="hidden" name="realName" value="${realName!}">
    	    <input type="hidden" name="mobilePhone" value="${mobilePhone!}">
    	    <input type="hidden" name="msgCode" value="${msgCode!}">
    	    <input type="hidden" name="type" value="1">
        	<div class="mui-input-row">
        	    <input type="password" id="password" name="password" autocomplete="new-password" maxlength="18" class="mui-input-password" placeholder="请设置8-18位密码">
        	</div>
        </form>
        <#if step?default('3')=='3'>
        <button type="button" class="mui-btn mui-btn-blue mui-btn-block no-padding btn-radius-22 lh44" onclick="saveUser()">注册并登录</button>
        <#else>
        <button type="button" class="mui-btn mui-btn-blue mui-btn-block no-padding btn-radius-22 lh44" onclick="savePassword()">确定</button>
        </#if>
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
    	
		var isSubmit=false;
		function saveUser(){
			if(isSubmit){
				return;
			}
			var pwd =$("#password").val();
			if(pwd==""){
				mui.toast("请输入密码");
				return;
			}else if(pwd.length<8){
				mui.toast("密码不能少于8位");
				return;
			}
			isSubmit = true;
			var options = {  
				url:'${request.contextPath}/homepage/register/syghSaveUser',
				data:$("#registerForm").serialize(),
				clearForm : false,
		   		resetForm : false,
				dataType:'json',
				type:'post',
				success:function(data){
					if(data.success){
						window.location.href='${request.contextPath}/homepage/register/syghLogin/loginForPassport?&mobliePhone=${mobilePhone!}&username='+data.msg;
					}else{
						isSubmit=false;
						mui.toast(data.msg);
					}
				}
			};
			$.ajax(options);
		}
		function savePassword(){
			if(isSubmit){
				return;
			}
	    	var pwd =$("#password").val();
			if(pwd==""){
				mui.toast("请输入密码");
				return;
			}else if(pwd.length<8){
				mui.toast("密码不能少于8位");
				return;
			}
			isSubmit = true;
			var options = {  
				url:'${request.contextPath}/homepage/register/syghResetPwd',
				data:$("#registerForm").serialize(),
				clearForm : false,
		   		resetForm : false,
				dataType:'json',
				type:'post',
				success:function(data){
					if(data.success){
						mui.confirm('您已经成功重置密码，请使用新密码登录', '提示消息', ['确定'], function(e) {
							window.location.href='${request.contextPath}/homepage/register/syghLogin/page';
					    })
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
