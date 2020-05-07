<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title>找回密码</title>
    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/static/mui/css/mui.min.css"/>
    <link href="${request.contextPath}/static/mui/css/pages.css" rel="stylesheet"/>
</head>
<body class="mui-bg-white">
    <header class="mui-bar mui-bar-nav">
        <a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left"></a>
    </header>		
    <div class="mui-content mui-bg-white career">
        <div class="career-hd">找回密码</div>
        <form class="career-form mui-input-group" id="registerForm" onsubmit="return false;">
        	<div class="mui-input-row">
        	    <label>手机号</label>
        	    <input type="text" id="mobilePhone" name="mobilePhone" maxlength="11" class="mui-input-clear" placeholder="请输入手机号">
        	</div>
        	<div class="mui-input-row">
        		<label>验证码</label>
        	    <input type="text" id="msgCode" name="msgCode" placeholder="请输入验证码">
        	    <button type="button" id="getCode" class="code mui-btn mui-btn-blue mui-btn-outlined">获取验证码</button>
        	</div>
        </form>
        <button type="button" id="toastBtn" class="mui-btn mui-btn-blue mui-btn-block no-padding btn-radius-22 lh44">下一步</button>
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
    	
		$("#getCode").on('tap',function(e){
			e.preventDefault();
			var mobilePhone = $("#mobilePhone").val();
			if(!checkMobilePhone(mobilePhone)){
				return;
			}
			var timeCount = 59; //定义倒计时总数
			if(!$(this).is(":disabled")){
				$obj = $(this);
				$.ajax({
		            url:'${request.contextPath}/homepage/register/syghGetMsgCode',
		            data:{"mobilePhone":mobilePhone,"type":"2"},
		            type:'post',
		            success:function(data){
		                var jsonO = JSON.parse(data);
		                if(jsonO.success){
		                	mui.toast(jsonO.msg);
							var start_time = new Date();
							start_time = start_time.getTime();//获取开始时间的毫秒数
							$obj.attr("disabled","disabled").text(timeCount + "s");
							downTime = setInterval(function(){
								//倒计时实时结束时间
								var end_time = new Date();
								end_time = end_time.getTime();
								//得到剩余时间
								var dtime = timeCount - Math.floor((end_time - start_time) / 1000);
								$obj.text(dtime + "s");
								if(dtime <= 0){
									$obj.removeAttr("disabled").text("重新获取");//启用按钮
									window.clearInterval(downTime);
								};
							},1000);
		                }else{
		                	mui.toast(jsonO.msg);
		                }
		            },
		            error : function(XMLHttpRequest, textStatus, errorThrown) {
		            }
			    });
			};
		});
		function checkMobilePhone(mobilePhone){
			var mobilePhoneReg = /^1(3|4|5|6|7|8|9)\d{9}$/;
			if(mobilePhone==""){
		    	mui.toast('请输入手机号');
		    	return false;
			}else if(mobilePhoneReg.test(mobilePhone)){
				var flag=false;
			 	$.ajax({
		            url:'${request.contextPath}/homepage/register/syghCheckMobilePhone',
		            data:$("#registerForm").serialize(),
		            type:'post',
		            async:false, 
		            success:function(data){
		                var jsonO = JSON.parse(data);
		                if(jsonO.success){
    						mui.toast('该手机号还未注册');
		                }else{
		                	flag=true;
		                }
		            },
		            error : function(XMLHttpRequest, textStatus, errorThrown) {
		            }
		        });
		        return flag;
			 }else{
				mui.toast('请输入正确的手机号码');
				return false;
			 }
	    };
	    
		$("#toastBtn").on('tap',function(){
			var mobilePhone = $("#mobilePhone").val();
			if(!checkMobilePhone(mobilePhone)){
				return;
			}
			var msgCode = $("#msgCode").val();
			if(msgCode==""){
            	mui.toast("请输入验证码");
            	return;
			}
			$.ajax({
	            url:'${request.contextPath}/homepage/register/syghCheckMsgCode',
	            data:$("#registerForm").serialize(),
	            type:'post',
	            success:function(data){
	                var jsonO = JSON.parse(data);
	                if(jsonO.success){
	                	window.location.href='${request.contextPath}/homepage/register/syghReg/page?step=4&mobilePhone='+mobilePhone+'&msgCode='+msgCode;
	                }else{
	                	mui.toast(jsonO.msg);
	                }
	            },
	            error : function(XMLHttpRequest, textStatus, errorThrown) {
	            }
	    	});
		})
    </script>
</body>
</html>
