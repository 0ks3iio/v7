<div class="login-container">
	<div class="login-box">
		<div class="login-body">
			<div class="login-info">
				<div class="login-info-main">
					<img src="${request.contextPath}/static/eclasscard/standard/show/images/card-guide.png" alt="">
					<div class="login-info-tip">登录请先刷卡识别用户名</div>
				</div>
			</div>
			<div class="login-form">
				<div class="login-form-main">
					<h3>空间登录</h3>
					<div class="form-group">
						<span><i class="icon icon-user"></i></span>
						<#if hasStu>
						<input type="hidden" id="login-studentid" name="id" value="${studentId!}" >
						${stuName!}<p >请刷卡识别用户名</p>
						<#else>
						<p id="cardNumber">请刷卡识别用户名</p>
						</#if>
					</div>
					<#if loginType==1>
					<div class="form-group">
						<span><i class="icon icon-lock"></i></span>
						<input id="password" type="password" placeholder="请输入密码">
						<div class="login-form-tip" id="errermsg"><i></i>密码错误，请重新输入</div>
					</div>
					<a class="btn btn-lg btn-block" onclick="doLoginEcc()">登&emsp;录&emsp;<span class="icon icon-arrow-right"></span></a>
					<#else>
					<a class="btn btn-lg btn-block">刷卡登录<span class="icon icon-arrow-right"></span></a>
					</#if>
					<a id="showFaceA" style="display: none" class='btn btn-lg btn-block' onClick='openFaceView()'>刷 脸 登 录</a>
				</div>
			</div>
		</div>
	</div>
</div>
<script>
	$(document).ready(function(){
		if (isActivate == "true") {
			<#if loginType==1>
			$(".login-form").addClass("login-form-made");
			$("#showFaceA").addClass("form-group btn-login");
			</#if>
			$("#showFaceA").attr("style","display: block");
		}
	    <#if loginType==0>
		   $("#clock-tab-type").val(3);
	    <#else>
	       $("#clock-tab-type").val(4);
		   $('input[type=password]').on('focus', function(){
			   $(this).parent().addClass('focus');
		   }).on('blur', function(){
			   $(this).parent().removeClass('focus');
		   })
	    </#if>
	})
	
	<#if loginType==0>
	function stuScheduleLogin(cardNumber){
	    var studentId = $("#login-studentid").val();
	    var toleaveMsg = '0';
        <#if hasStu>
            toleaveMsg = '1';
        </#if>
        $.ajax({
            url:'${request.contextPath}/eccShow/eclasscard/standard/stuLoginUser/page',
            data:{"cardId":_cardId,"cardNumber":cardNumber,"type":"schedule","studentId":studentId},
            type:'post',
            success:function(data){
                var jsonO = JSON.parse(data);
                if(jsonO.success){
					showPrompt("clockSuc");
                    var studentSpaceUrl = "${request.contextPath}/eccShow/eclasscard/standard/studentSpace/index?cardId="+_cardId+"&view="+_view+"&toleaveMsg="+toleaveMsg;
                    $("#mainContainerDiv").load(studentSpaceUrl);
                    $("#clock-tab-type").val(0);
                }else{
                    submit = false;
                    showMsgTip(jsonO.msg);
                }
            },
            error : function(XMLHttpRequest, textStatus, errorThrown) {
                submit = false;
                showMsgTip('登录失败，请联系系统管理员');
            }
        });
    }
	<#else>
	var submit = false;
	function doLoginEcc(){
		if(submit){
			return;
		}
		var cardNumber = $("#cardNumber").text();
		var studentId = $("#login-studentid").val();
		var password = $("#password").val();
		var toleaveMsg = '0';
		<#if hasStu>
			toleaveMsg = '1';
		<#else>
			if(cardNumber=='请刷卡识别用户名'||isSmartBlank(cardNumber)){
	            showSmartError("请先刷卡！");
	            return ;
	        }
		</#if>
		if(isSmartBlank(password)){
    		showSmartError("请输入密码！");
    		return ;
		}
		submit = true;
		$.ajax({
    		url:'${request.contextPath}/eccShow/eclasscard/standard/stuLoginUser/page',
    		data:{"cardId":_cardId,"cardNumber":cardNumber,"studentId":studentId,"password":password},
    		type:'post',
    		success:function(data){
        		var jsonO = JSON.parse(data);
        		if(jsonO.success){
        			$('input[type=password]').parent().removeClass('error');
            		var studentSpaceUrl = "${request.contextPath}/eccShow/eclasscard/standard/studentSpace/index?cardId="+_cardId+"&view="+_view+"&toleaveMsg="+toleaveMsg;
       		 		$("#mainContainerDiv").load(studentSpaceUrl);
       		 		$("#clock-tab-type").val(0);
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
	</#if>
</script>
