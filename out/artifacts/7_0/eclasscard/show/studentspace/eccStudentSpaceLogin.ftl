<!DOCTYPE html>
<html lang="en">
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
	<meta charset="UTF-8">
	<title>登录中心</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
	<script>
	_cardId = "${cardId!}";
	</script>
	<link rel="stylesheet" href="${request.contextPath}/static/eclasscard/show/plugins/slick/slick.css">
	<link rel="stylesheet" href="${request.contextPath}/static/eclasscard/show/css/style.css">
</head>
<body class="index">
	<header class="header">
		<div class="back">
			<a class="arrow" href="javascript:void(0);" onclick="backIndex()"></a>
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
	<div class="main-container grid">
		<div class="login-tips">温馨提示：登录后若您在1分钟内没有任何操作，为您的信息安全考虑系统将自动退出</div>
		<div class="login-container">
			<div class="login-box">
				<div class="login-body">
					<div class="login-info">
						<div class="login-info-main">
							<h3>登录请先刷卡</h3>
							<p>Please swipe your card first</p>
							<span class="icon icon-audio-wave"></span>
						</div>
					</div>
					<div class="login-form">
						<div class="login-form-main">
							<h3>登录中心</h3>
							<div id="errermsgDiv" style="display:none" class="login-form-tip"><i></i><span id="errermsg">密码错误，请重新输入</span></div>
							<div class="form-group">
								<i class="icon icon-user"></i>
								<input type="text" id="cardNumber" placeholder="请刷卡识别用户名"></p>
							</div>
							<div class="form-group">
								<i class="icon icon-lock"></i>
								<input id="password" type="password" placeholder="请输入密码">
							</div>
							<!--
							<button class="login-btn" onclick="eccClockIn('123456')" >登&emsp;录11</button>
							-->
							<button class="btn btn-block"  onclick="doLoginEcc()">登&emsp;录</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
<script src="${request.contextPath}/static/eclasscard/show/plugins/jquery/jquery.min.js"></script>
<script src="${request.contextPath}/static/eclasscard/show/plugins/slick/slick.js"></script>
<script src="${request.contextPath}/static/eclasscard/show/js/myscript.js"></script>
<script>
var load_time = null;  
var down_time = null;  
$(document).ready(function(){
	//打开页面60秒不操作就跳转  
	load_time = setTimeout(function(){  
		backIndex(); 
	},1000*60);  
})
function stopLoadTime(){ 
   clearInterval(load_time);  
   if(null != down_time){  
       clearInterval(down_time);  
   }  
   down_time = setTimeout(function(){  
       backIndex();  
   },1000*60);  
}
document.addEventListener('touchstart', function(event) {
     // 如果这个元素的位置内只有一个手指的话
    if (event.targetTouches.length == 1) {
　　　	 //event.preventDefault();
         stopLoadTime();
    }
}, false);  
//打卡触发
function eccClockIn(cardNumber){
	if(!cardNumber||cardNumber==''){
		return;
	}
	$("#cardNumber").val(cardNumber);
	stopLoadTime();
}
var submit = false;
function doLoginEcc(){
	if(submit){
		return;
	}
	var cardNumber = $("#cardNumber").val();
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
        data:{"cardId":_cardId,"cardNumber":cardNumber,"password":password},
        type:'post',
        success:function(data){
            var jsonO = JSON.parse(data);
            if(jsonO.success){
            	$("#errermsgDiv").hide();
                location.href = "${request.contextPath}/eccShow/eclasscard/studentSpace/index?cardId="+_cardId+"&view="+${view!};
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
function isSmartBlank(str) {
    return !str || str.replace(/(^s*)|(s*$)/g, "").length ==0;
}
//提示
function showSmartError(msg){
	$("#errermsg").text(msg);
	$("#errermsgDiv").show();
}
function backIndex(){
	location.href = "${request.contextPath}/eccShow/eclasscard/showIndex?cardId="+_cardId+"&view="+${view!};
}
      
</script>
</body>
</html>