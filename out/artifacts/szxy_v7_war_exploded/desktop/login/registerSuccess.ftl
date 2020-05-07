<!DOCTYPE html>
<html lang="en">
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
		<meta charset="utf-8" />
		<title>注册成功</title>

		<meta name="description" content="" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />

		<!-- bootstrap & fontawesome -->
		<link rel="stylesheet" href="${request.contextPath}/static/components/bootstrap/dist/css/bootstrap.css" />
		<link rel="stylesheet" href="${request.contextPath}/static/components/font-awesome/css/font-awesome.css" />

		<link rel="stylesheet" href="${request.contextPath}/static/css/components.css">
		<link rel="stylesheet" href="${request.contextPath}/static/css/basic-data.css">
		<!--[if lte IE 9]>
		  <link rel="stylesheet" href="${request.contextPath}/static/css/pages-ie.css" />
		<![endif]-->
		<!-- inline styles related to this page -->

		<!-- HTML5shiv and Respond.js for IE8 to support HTML5 elements and media queries -->

		<!--[if lte IE 8]>
			<script src="${request.contextPath}/static/components/html5shiv/dist/html5shiv.min.js"></script>
			<script src="${request.contextPath}/static/components/respond/dest/respond.min.js"></script>
		<![endif]-->
	</head>

	<body class="super">
		<div class="box box-default-dq">
			<div class="box-body-dq">
				<div class="js-auto-skip">
					<p class="tt"><i class="fa fa-check-circle"></i>&nbsp;恭喜您，注册成功</p><br />
					<p class="dd"><span class="js-cout-down">8</span><span>秒</span>&nbsp;后自动返回登录页</p>
				</div>
				<div class="reg-result">
					<#if ownerType?default('1')=='1'>
						<p>您的账号是：<span></span>${username!}&emsp;<span></span></p>
					<#else>
						<p>您的账号是：<span></span>${username!}&emsp;或&emsp;${mobilePhone!}<span></span></p>
					</#if>
					<p class="mt-30">
						<button class="btn btn-blue" type="button" onclick="onJump()">返回登录页</button>
					</p>
				</div>
			</div>
		</div>

		<!--[if !IE]> 
		<script src="${request.contextPath}/static/components/jquery/dist/jquery.js"></script>-->
		<script src="${request.contextPath}/static/components/jquery/dist/jquery.min.js"></script>
		<!-- <![endif]-->

		<!--[if IE]>
		<script src="${request.contextPath}/static/components/jquery.1x/dist/jquery.js"></script>
		<![endif]-->
		<script type="text/javascript">
			if('ontouchstart' in document.documentElement) document.write("<script src='${request.contextPath}/static/components/_mod/jquery.mobile.custom/jquery.mobile.custom.js'>"+"<"+"/script>");
		</script>
		<script src="${request.contextPath}/static/components/bootstrap/dist/js/bootstrap.js"></script>

		<!-- page specific plugin scripts -->
		<script src="${request.contextPath}/static/components/layer/layer.js"></script>

		<!-- inline scripts related to this page -->
		<script>
			function onJump(){
				location.href="${request.contextPath}/homepage/loginPage/page";
			}
		
			$(function(){
				function jump(count) { 
			        window.setTimeout(function(){ 
			            count--; 
			            if(count > 0) { 
			                $('.js-cout-down').text(count); 
			                jump(count); 
			            } else { 
			                //location.href="index.html";
			                //alert('这里输入跳转到页面');
			                location.href="${request.contextPath}/homepage/loginPage/page";
			            } 
			        }, 1000); 
			    } 
			    jump(8);
			    
				function winLoad(){
					var window_h=$(window).height();
					var login_h=$('.box-default-dq').outerHeight();
					var login_top=parseInt((window_h-login_h)/2);
					$('.box-default-dq').css('margin-top',login_top);
				};
				winLoad();
				$(window).resize(function(){
					winLoad();		
				});
			})
		</script>
	</body>
</html>
