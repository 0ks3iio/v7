<!DOCTYPE html>
<html lang="en">
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
	<meta charset="UTF-8">
	<title>登录中心</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
	<script>
	_deviceNumber = "${deviceNumber!}";
	_view = "${view!}";
	</script>
	<link rel="stylesheet" href="${request.contextPath}/static/eclasscard/verticalshow/plugins/slick/slick.css">
	<link rel="stylesheet" href="${request.contextPath}/static/eclasscard/verticalshow/css/style.css?vid=2">
</head>
<body class="login">
	<header class="header">
		<div class="back">
			<a class="arrow" href="javascript:void(0);" onclick="backIndex()">返回</a>
			<span>登录中心</span>
		</div>
		<div class="date">
			<span class="time"></span>
			<div class="right">
				<span class="day"></span>
				<span class="week"></span>
			</div>
		</div>
	</header>
	<div class="main-container center">
		<div class="login-container">
			<div class="login-box">
				<div class="login-body">
					<div class="login-info">
						<img src="${request.contextPath}/static/eclasscard/verticalshow/images/login-guide.png" alt="">
					</div>
					<div class="login-form">
						<div class="login-form-main">
							<h3>空间登录</h3>
							<div class="form-group">
								<span><i class="icon icon-user"></i></span>
								<p id="cardNumber">请刷卡识别用户名</p>
							</div>
							<div class="form-group">
								<span><i class="icon icon-lock"></i></span>
								<input id="password" type="password" placeholder="请输入密码" value="">
								<div class="login-form-tip" id="errermsg"><i></i>密码错误，请重新输入</div>
							</div>
							<button class="btn btn-lg btn-block" onclick="doLoginEcc()">登&emsp;录&emsp;<span class="icon icon-arrow-right"></span></button>
						</div>
					</div>
				</div>
			</div>
		</div>
		<p class="copyleft">&copy;2017 ${schoolName!} 版权所有</p>
	</div>
	<script src="${request.contextPath}/static/eclasscard/verticalshow/plugins/jquery/jquery.min.js"></script>
	<script src="${request.contextPath}/static/eclasscard/verticalshow/plugins/slick/slick.js"></script>
	<script src="${request.contextPath}/static/eclasscard/verticalshow/js/myscript.js"></script>
	<script>
		$(document).ready(function(){
			//打开页面60秒不操作就跳转  
	   		load_time = setTimeout(function(){  
	        	backIndex();  
	    	},1000*60); 
		
			$('input[type=password]').on('focus', function(){
				$(this).parent().addClass('focus');
			}).on('blur', function(){
				$(this).parent().removeClass('focus');
			})
		})
		//打卡触发
		function eccClockIn(cardNumber){
			if(!cardNumber||cardNumber==''){
				return;
			}
			$("#cardNumber").text(cardNumber);
		}
		var submit = false;
		function doLoginEcc(){
			if(submit){
				return;
			}
			var cardNumber = $("#cardNumber").text();
			var password = $("#password").val();
			if(cardNumber=='请刷卡识别用户名'||isSmartBlank(cardNumber)){
        		showSmartError("请先刷卡！");
        		return ;
    		}
    		if(isSmartBlank(password)){
        		showSmartError("请输入密码！");
        		return ;
    		}
 			submit = true;
    		$.ajax({
        		url:'${request.contextPath}/eccShow/eclasscard/stuLoginUser/page',
        		data:{"deviceNumber":_deviceNumber,"cardNumber":cardNumber,"password":password},
        		type:'post',
        		success:function(data){
            		var jsonO = JSON.parse(data);
            		if(jsonO.success){
            			$('input[type=password]').parent().removeClass('error');
                		location.href = "${request.contextPath}/eccShow/eclasscard/studentSpace/index?deviceNumber="+_deviceNumber+"&view="+_view;
           		 	}else{
                		submit = false;
                		showSmartError(jsonO.msg);
            		}
        		},
       			error : function(XMLHttpRequest, textStatus, errorThrown) {
            		submit = false;
          		  	showSmartError("登录失败，请联系系统管理员");
       		 	}
    		});
		}
		
		//提示
		function showSmartError(msg){
			$("#errermsg").text(msg);
			$('input[type=password]').parent().addClass('error');
		}
		
		function isSmartBlank(str) {
   			 return !str || str.replace(/(^s*)|(s*$)/g, "").length ==0;
		}
		
		function backIndex() {
			location.href = "${request.contextPath}/eccShow/eclasscard/showIndex?deviceNumber="+_deviceNumber+"&view="+_view;
		}
	</script>
</body>
</html>