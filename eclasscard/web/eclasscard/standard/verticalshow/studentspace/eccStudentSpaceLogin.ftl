<div class="wapper vetically-center">
	<div class="login-container">
		<div class="login-box">
			<div class="login-body">
				<div class="login-info-main">
					<img src="${request.contextPath}/static/eclasscard/standard/verticalshow/images/card-guide.png" alt="">
					<div class="login-info-tip">登录请先刷卡识别用户名</div>
				</div>
				<div class="login-form">
					<div class="login-form-main">
						<h3>空间登录</h3>
						<div class="form-group">
							<span><i class="icon icon-user"></i></span>
							<#if hasStu>
                                <input type="hidden" id="login-studentid" name="id" value="${studentId!}" >
                                ${stuName!}：<p id="cardNumber">请刷卡识别用户名</p>
                            <#else>
                                <p id="cardNumber">请刷卡识别用户名</p>
                            </#if>
						</div>
						<#if loginType==1>
						<div class="form-group">
							<span><i class="icon icon-lock"></i></span>
							<input id="password" type="password" placeholder="请输入密码">
							<div class="login-form-tip" id="errermsg"><i>密码错误，请重新输入</i></div>
						</div>
						<button class="btn btn-lg btn-block" onclick="doLoginEcc()">登&emsp;录&emsp;<span class="icon icon-arrow-right"></span></button>
					    <#else>
                            <button class="btn btn-lg btn-block">刷&emsp;卡&emsp;登&emsp;录&emsp;<span class="icon icon-arrow-right"></span></button>
                        </#if>
						<button id="showFaceA" class="btn btn-lg btn-block text-center form-group btn-login" style="display: none" onClick="openFaceView()">刷 脸 登 录</button>
					</div>
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
		var toleaveMsg = '0';
		var password = $("#password").val();
		<#if hasStu>
			toleaveMsg = '1';
		</#if>
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
		setTimeout(function(){
    		$('input[type=password]').parent().removeClass('error');
    	},5000);
	}
	
	function isSmartBlank(str) {
		 return !str || str.replace(/(^s*)|(s*$)/g, "").length ==0;
	}
	</#if>
</script>