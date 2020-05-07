<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>注册成功</title>
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
	    <div class="content-border">
	    	<div class="order-result order-result-right">
	        	<div class="tt t-center"><p class="fn-inline-block"><span></span>恭喜您，注册成功</p></div>
	            <p class="f-16 t-center mt-20 mb-30"><span class="c-orange js-cout-down">8</span>秒 后自动进入首页</p>
	            <div class="list t-center">
	            	<p class="f-16 mt-20">您的账号是：<span class="c-orange">${mobilePhone!}</span></p>
	                <p class="mt-20 mb-20"><a href="${request.contextPath}/homepage/register/syghLogin/loginForPassport?username=${username!}&mobliePhone=${mobilePhone!}" class="abtn abtn-sm abtn-red" hidefocus="true">进入首页</a></p>
	            </div>
	       </div>
	    </div>
	</div>
</div>

<script src="${request.contextPath}/static/career/js/jquery-1.9.1.min.js"></script>
<script src="${request.contextPath}/static/career/js/jquery.FormNice.js"></script>
<script src="${request.contextPath}/static/career/js/myscript.js"></script>
<script>
$(function(){
	function jump(count) { 
        window.setTimeout(function(){ 
            count--; 
            if(count > 0) { 
                $('.js-cout-down').text(count); 
                jump(count); 
            } else { 
                //alert('这里输入跳转到页面');
                location.href="${request.contextPath}/homepage/register/syghLogin/loginForPassport?username=${username!}&mobliePhone=${mobilePhone!}";
            } 
        }, 1000); 
    } 
    jump(8);
})

</script>
</body>
</html>