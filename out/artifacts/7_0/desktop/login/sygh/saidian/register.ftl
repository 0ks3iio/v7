<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>注册</title>
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/career/css/public.css">
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/career/css/global-V5.8.css">
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/career/css/style.css">
</head>

<body>
<div class="container-wrap">
	<div class="header-wrap-login">
		<div class="header-inner header-login-inner fn-clear">
	    	<div class="logo"><a href="#"><img src="${request.contextPath}/static/career/images/logo10.png" alt=""></a></div>
	        <span class="fn-right f-14 c-999 mt-40">已有账号,<a class="c-blue" href="${request.contextPath}/homepage/register/syghLogin/page">马上登录</a></span>
	    </div>
	</div>
	
	<div id="container">
	    <div class="content-border">
	   	 	<form name="registerForm" method="post" id="registerForm" onsubmit="return false;">
	        <table border="0" cellspacing="0" cellpadding="0" class="login-table">
	        	<tr>
	            	<td width="449"></td>
	                <td width="300"><h3 class="f-18 t-center">欢迎注册</h3></td>
	                <td width="449"></td>
	            </tr>
	            <tr>
	            	<td></td>
	                <td>
	                    <input type="text" id="realName" name="realName" maxlength="64" placeholder="姓名(非必填)" autocomplete="new-password" class="input-txt width-block" onblur="checkRealName()">
	                </td>
	                <td class="tips-class"></td>
	            </tr>
	            <tr>
	            	<td></td>
	                <td>
	                    <input type="text" id="mobilePhone" name="mobilePhone" maxlength="11" placeholder="手机号" autocomplete="new-password" class="input-txt width-block" onblur="checkMobilePhone(1,'${request.contextPath}')" oninput="showGetCode();">
	                </td>
	                <td class="tips-class"></td>
	            </tr>
	            <tr>
	            	<td></td>
	            	<td>
	                    <div class="fn-clear">
	                        <input type="text" id="msgCode" name="msgCode" placeholder="验证码" autocomplete="new-password" class="input-txt fn-left" style="width:190px;" onblur="checkMsgCode()">
	                        <div class="fn-right">
	                            <a href="javascript:;" id="getCode" class="get-code get-code-disable" hidefocus="true">获取验证码</a>
	                        </div>
	                    </div>
	                </td>
	                <td class="tips-class"></td>
	            </tr>
	            <tr>
	            	<td></td>
	                <td>
	                    <input type="password" id="firstPwd" name="password" maxlength="18" placeholder="密码" autocomplete="new-password" class="input-txt width-block" onblur="checkFirstPwd()" oninput="checkFirstPwd()">
	                </td>
	                <td class="fn-rel tips-class"></td>
	            </tr>
	            <tr>
	            	<td></td>
	                <td>
	                	<input type="password" id="secondPwd" maxlength="18" placeholder="确认密码" autocomplete="new-password" class="input-txt width-block" onblur="checkSecondPwd()">
	                </td>
	                <td class="tips-class"></td>
	            </tr>
	            <tr>
	                <td></td>
	                <td><a href="javascript:void(0);" id="subBtn" onclick="saveUser()"class="abtn abtn-big abtn-red width-block">注册</a></td>
	                <td class="tips-class"></td>
	            </tr>
	        </table>
	        </form>
	    </div>
	</div>
</div>

<script src="${request.contextPath}/static/career/js/jquery-1.9.1.min.js"></script>
<script src="${request.contextPath}/static/career/js/jquery.FormNice.js"></script>
<script src="${request.contextPath}/static/career/js/myscript.js"></script>
<script src="${request.contextPath}/static/career/js/register.js"></script>
<script src="${request.contextPath}/static/js/tool.js"></script>
<script>
$(function(){
	sendcode('#mobilePhone','#getCode',59,5,'${request.contextPath}',1)
})

function showGetCode(){
	var mobilePhone = $("#mobilePhone").val();
	if(mobilePhone!=""&& mobilePhone.length==11){
		checkMobilePhone(1,'${request.contextPath}');
	}
}

function checkRealName(){
	var realName = $("#realName").val();
	if(realName!="" && getLength(realName)>64){
		addError("realName",'姓名不能超过64个字节（一个汉字为两个字节）');
		return;	
	}else{
		addSuccess("realName");
	}
}

var isSubmit=false;
function saveUser(){
	addSuccess("subBtn");
	
	checkMobilePhone(1,'${request.contextPath}');
	checkMsgCode();
	checkFirstPwd();
	checkSecondPwd();
	
	var mobilePhone = $("#mobilePhone").val();
	
	if($(".input-txt-error").size()>0){
		return;
	}
	
	if(isSubmit){
		return ;
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
				window.location.href='${request.contextPath}/homepage/register/syghRegSuccess/page?mobilePhone='+mobilePhone+'&username='+data.msg;
			}else{
				isSubmit=false;
				addError("subBtn",data.msg);
			}
		}
	};
	$.ajax(options);
}
</script>
</body>
</html>