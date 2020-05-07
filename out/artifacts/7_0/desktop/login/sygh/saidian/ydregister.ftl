<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title>注册</title>
    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/static/mui/css/mui.min.css"/>
    <link href="${request.contextPath}/static/mui/css/pages.css" rel="stylesheet"/>
</head>
<body class="mui-bg-white">
    <header class="mui-bar mui-bar-nav">
        <a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left"></a>
    </header>		
    <div class="mui-content mui-bg-white career">
        <div class="career-hd">你好，<br>欢迎注册学业规划</div>
        <form class="career-form mui-input-group" id="registerForm" onsubmit="return false;">
        	<div class="mui-input-row">
        	    <label>姓名</label>
        	    <input type="text" id="realName" name="realName" maxlength="64" autocomplete="new-password" placeholder="请输入姓名(非必填)" class="mui-input-clear">
        	</div>
        	<div class="mui-input-row">
        	    <label>手机号</label>
        	    <input type="text" id="mobilePhone" name="mobilePhone" autocomplete="new-password" maxlength="11" class="mui-input-clear" placeholder="请输入手机号">
        	</div>
        </form>
        <button type="button" class="mui-btn mui-btn-blue mui-btn-block no-padding btn-radius-22 lh44" id="toastBtn">获取验证码</button>
        <button type="button" class="mui-btn mui-btn-link mui-btn-block c-333" style="width:auto;margin:auto;" id="loginBtn">密码登录</button>
    </div>
    <script src="${request.contextPath}/static/mui/js/jquery-1.9.1.min.js"></script>
    <script src="${request.contextPath}/static/mui/js/mui.min.js" type="text/javascript" charset="utf-8"></script>
    <script src="${request.contextPath}/static/js/tool.js"></script>
    <script type="text/javascript">
    	$(function(){
    		$(".career-form .mui-input-row input").focus(function(){
    			$(this).parent().addClass("focus")
	    	})
    		$(".career-form .mui-input-row input").blur(function(){
    			$(this).parent().removeClass("focus")
	    	})
    	})
    	$("#toastBtn").on('tap',function(){
			var realName = $("#realName").val();
    		if(getLength(realName)>64){
				mui.toast('姓名不能超过64个字节（一个汉字为两个字节）');
				return;	
			}
			var mobilePhone = $("#mobilePhone").val();
			var mobilePhoneReg = /^1(3|4|5|6|7|8|9)\d{9}$/;
			if(mobilePhone==""){
		    	mui.toast('请输入手机号');
			}else if(mobilePhoneReg.test(mobilePhone)){
			 	$.ajax({
		            url:'${request.contextPath}/homepage/register/syghCheckMobilePhone',
		            data:$("#registerForm").serialize(),
		            type:'post',
		            success:function(data){
		                var jsonO = JSON.parse(data);
			                if(jsonO.success){
			                	$.ajax({
						            url:'${request.contextPath}/homepage/register/syghGetMsgCode',
						            data:{"mobilePhone":mobilePhone,"type":"1"},
						            type:'post',
						            success:function(data){
					                	window.location.href='${request.contextPath}/homepage/register/syghReg/page?step=2&realName='+realName+'&mobilePhone='+mobilePhone;
						            },
						            error : function(XMLHttpRequest, textStatus, errorThrown) {
						            }
							    });
			                }else{
	    						mui.toast('该手机号已被注册');
			                }
		            },
		            error : function(XMLHttpRequest, textStatus, errorThrown) {
		            }
		        });
			 }else{
				mui.toast('请输入正确的手机号码');
			 }
	    });
    	$("#loginBtn").on('tap',function(){
    		window.location.href='${request.contextPath}/homepage/register/syghLogin/page';
	    });
	    
    </script>
</body>
</html>
