<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>找回密码</title>
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/career/css/public.css">
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/career/css/global-V5.8.css">
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/career/css/style.css">
</head>

<body>
<div class="container-wrap">
	<div class="header-wrap-login">
		<div class="header-inner header-login-inner fn-clear">
	    	<div class="logo"><a href="#"><img src="${request.contextPath}/static/career/images/logo.png" alt=""></a></div>
	        <span class="fn-right f-14 c-999 mt-40">已有账号,<a class="c-blue" href="${request.contextPath}/homepage/register/syghLogin/page">马上登录</a></span>
	    </div>
	</div>
	
	<div id="container">
	    <div class="content-border">
	   	 	<form name="getbackForm" method="post" id="getbackForm">
	   	 	<table border="0" cellspacing="0" cellpadding="0" class="login-table">
	       		<tr>
	            	<td width="449"></td>
	                <td width="300"><p class="t-center f-14">您已经成功重置密码，请使用新密码登录</p></td>
	                <td width="449"></td>
	            </tr>
	            <tr>
	                <td></td>
	                <td><a href="${request.contextPath}/homepage/register/syghLogin/page" class="abtn abtn-big abtn-red width-block">马上登录</a></td>
	                <td></td>
	            </tr>
	        </table>
	        </form>
	    </div>
	</div>
</div>

<script src="${request.contextPath}/static/career/js/jquery-1.9.1.min.js"></script>
<script src="${request.contextPath}/static/career/js/jquery.FormNice.js"></script>
<script src="${request.contextPath}/static/career/js/myscript.js"></script>
</body>
</html>