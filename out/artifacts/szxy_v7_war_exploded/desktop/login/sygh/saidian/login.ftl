<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>赛点教育学业规划</title>
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/career/css/public.css">
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/career/css/global-V5.8.css">
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/career/css/style.css">
</head>

<body class="loginContainer">
<div class="container-wrap">
	<div id="container" style="margin-top:0;">
		<div class="login-outter">
			<div class="t-center pt-70 mb-10" style="padding-top:150px;">
				<img src="${request.contextPath}/static/career/images/logo11.png">
			</div>
			<p class="login-tips"></p>
			<div class="login-form fn-clear">
				<span class="input-group-addon"><img src="${request.contextPath}/static/career/images/icon/user.png" ></span>
				<input id="mobilePhone" name="mobilePhone" type="text" class="form-control" placeholder="请输入手机号">
			</div>
			<div class="login-form fn-clear">
				<span class="input-group-addon"><img src="${request.contextPath}/static/career/images/icon/lock.png" ></span>
				<input id="password" name="password" type="password" class="form-control" placeholder="请输入密码">
			</div>
			<div class="login-form login-verCode fn-clear">
				<span class="input-group-addon"></span>
				<input type="text" id="imgCode" name="imgCode" class="form-control" placeholder="请输入验证码">
				<a class="fn-right" href="javascript:void(0)" onclick="flashCodeImg('${request.contextPath}')"><img id="codeImg" src="${request.contextPath}/desktop/verifyImage" width="83" height="38" alt=""></a>
			</div>
			<div class="login-form fn-clear f-14">
				<label class="fn-left">
					<input class="fn-left mt-2 mr-3" type="checkbox" id="cookieSaveType" name="cookieSaveType"/>
					<span>记住密码</span>
				</label>
				<a class="fn-right" href="${request.contextPath}/homepage/register/syghReg/page">注册</a>
				<span class="fn-right">&nbsp;|&nbsp;</span>
				<a class="fn-right" href="${request.contextPath}/homepage/register/syghPwd/page">忘记密码?</a>
			</div>
			<a href="javascript:doLogin('${request.contextPath}',0);" class="abtn abtn-big abtn-red width-block" hidefocus="true">登录</a>
		</div>
	</div>
	<!-- ${copyRight!}-->
	<div class="footer-wrap-login">Copyright 1997-2019 [ZDSoft] All Rights Reserved. 赛点教育 版权所有</div>
</div>

<script src="${request.contextPath}/static/career/js/jquery-1.9.1.min.js"></script>
<script src="${request.contextPath}/static/career/js/jquery.FormNice.js"></script>
<script src="${request.contextPath}/static/career/js/myscript.js"></script>
<script src="${request.contextPath}/static/js/login/jquery.cookies.js"></script>
<script src="${request.contextPath}/static/career/js/constants.js"></script>
<script src="${request.contextPath}/static/career/js/register.js"></script>
<script>
$(function(){
	initLoginSygh('${request.contextPath}');
})
</script>
</body>
</html>