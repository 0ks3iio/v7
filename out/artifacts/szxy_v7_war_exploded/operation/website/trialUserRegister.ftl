<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>申请试用</title>
    <link rel="stylesheet" href="css/base.css">
    <link rel="stylesheet" href="css/common.css">
    <link rel="stylesheet" href="css/page.css">
    <script src="js/jquery.js"></script>
    <script src="js/layer/layer.js"></script>
</head>
<body>
<div class="wrap">
    <div class="topWrap">
        <div class="top">
            <div class="nav fl">
                <a class="logo" href="http://www.wanpeng.com/" target="_blank"><i class="ico"></i>万朋教育</a>
                <a href="http://xk.msyk.cn/operation/website/index"  style="color: #3a7deb;">新高考</a>
                <a href="http://www.msyk.cn/" target="_blank">美师优课</a>
                <a href="https://www.kehou.com/index.htm" target="_blank">课后网</a>
                <a href="http://edu.wanpeng.com/" target="_blank">三通两平台</a>
                <a href="http://wk.wanpeng.com/" target="_blank">微课掌上通</a>
                <a href="http://www.wanpeng.com/cpzx-tbkt.html" target="_blank">同步课堂</a>
            </div>
        </div>
    </div>
   <div class="headerWrap">
        <div class="header">
            <a class="logo" href="${request.contextPath}/operation/website/index"><img src="images/img/logo.png"></a>
            <div class="nav">
                <a href="${request.contextPath}/operation/website/index">首页</a>
                <a href="${request.contextPath}/operation/website/educationAdminPage">教务管理</a>
                <a href="${request.contextPath}/operation/website/bigdataPage">大数据分析</a>
                <a href="${request.contextPath}/operation/website/mixedAbilityPage">综合素质</a>
                <a href="${request.contextPath}/operation/website/careerPlanPage">生涯规划</a>
                <a href="${request.contextPath}/operation/website/classCardPage">电子班牌</a>
            </div>
            <div class="lR">
                <a class="btn btn03" href="${request.contextPath}/operation/website/trialUserRegister">申请试用</a>
                <a class="btn btn02" id="login" href="${request.contextPath}/operation/website/loginForOperation">登录</a>
            </div>
            <div class="admin">
                <a class="name" href="#" id="realNameView">${realName}</a>
                <div id="linkto">
                	<ul>
                		<li>
                			<a href="${request.contextPath}/desktop/index/page" target="_blank">智慧校园</a>
                		</li>
	                    <li>
                			<a href="http://www.msyk.cn/" target="_blank">智慧课堂</a>
                		</li>
                	</ul>
                    <a class="btn btn04" 
                    	href="${request.contextPath}/homepage/logout/page?call=${call!}">退出</a>
                </div>

            </div>
        </div>
    </div>
    <div class="centerWrap">
    	<div class="apply-infor">
    		<div class="center">申请体验</div>
        </div>
    	<div class="center">
    		<img src="images/img/process.png">
            <div class="form">
                <ul class="list">
                    <li class="clearfix">
                        <div class="name"><span class="c-red">*</span>姓名：</div>
                        <div class="value">
                            <p class="pr"><input id="realName" class="text01 w455" type="text" autocomplete="off" placeholder="请输入"><i id="realNameI"></i></p>
                            <p id="realNameTip" class="tip"></p>
                        </div>
                    </li>
                    <li class="clearfix">
                        <div class="name"><span class="c-red">*</span>电话：</div>
                        <div class="value">
                            <p class="pr"><input id="telphone" class="text01 w455" type="text" autocomplete="off" placeholder="请输入"><i id="telphoneI"></i></p>
                            <p id="telphoneTip" class="tip"></p>
                        </div>
                    </li>
                    <li class="clearfix">
                        <div class="name"><span class="c-red">*</span>单位：</div>
                        <div class="value">
                            <p class="pr"><input id="company" class="text01 w455" type="text" autocomplete="off" placeholder="请输入"><i id="companyI"></i></p>
                            <p id="companyTip" class="tip"></p>
                        </div>
                    </li>
                    <li class="clearfix">
                        <div class="name"><span class="c-red">*</span>邮箱：</div>
                        <div class="value">
                            <p class="pr"><input id="email" class="text01 w455" type="text" autocomplete="off" placeholder="请输入"><i id="emailI"></i></p>
                            <p id="emailTip" class="tip"></p>
                        </div>
                    </li>
                    <li class="clearfix">
                        <div class="name">&nbsp;</div>
                        <div class="value"><a class="btn btn01" id="applyBtn" href="#">立即申请</a></div>
                    </li>
                </ul>
            </div>
        </div>
    </div>
    <div class="footerWrap">
		<div class="footer">
			<div class="footer-inner clearfix">
				<div class="footer-info-logo fl">
					<img src="images/img/footer-logo2018.png" alt="">
				</div>
				<div class="footer-info fl">
					<h3 class="title">旗下网站</h3>
					<div class="infos-main">
						<a href="https://www.kehou.com/index.htm">课后网</a>
						<a href="http://pad.msyk.cn/">美师优课</a>
						<a href="http://edu.wanpeng.com/index.html">三通两平台</a>
						<a href="http://wk.wanpeng.com/">微课掌上通</a>
						<a href="http://xgk.wanpeng.com/cpzx-zhykt.html">智慧云课堂</a>
					</div>
				</div>
				<div class="footer-info footer-certified fl">
					<h3 class="title">公司认证</h3>
					<div class="infos-main">
						<p>通过CMMI L5国际软件认证</p>
						<p>通过ISO 9001国际质量认证</p>
						<p>通过ISO27001信息安全认证</p>
					</div>
				</div>
				<div class="footer-info footer-contact fl">
					<h3 class="title">联系电话</h3>
                    <div class="infos-main">
                        <p>400-863-2003（教育云平台和其他产品）</p>
                        <p>400-617-1997（课后网）</p>
                    </div>
				</div>
				<div class="aboutus fr">
					<h3 class="title">关于我们</h3>
					<div class="aboutus-main">
						<img src="images/img/code04.jpg" alt="">
						<p>官方微信</p>
					</div>
				</div>
			</div>
			<div class="footer-content">
				<p>Copyright ©2013-2018   浙江万朋教育科技股份有限公司版权所有   备案号:浙B2-20100206-17</p>
			</div>
		</div>
	</div>
</div>
<div id="applyLayer">
		<div class="m20 clearfix">
			<img class="fl mr20" id="rightPic" src="js/layer/skin/myskin/right.png">
			<div class="fl c-333">
				<p id="state" class="f16 mb5"><b>申请成功</b></p>
				<p id="message" class="f14">客服会进行回访，请保持通讯顺畅。</p>
			</div>
		</div>
	</div>
</body>

<script>
    	
</script>
<script type="text/javascript">
	layer.config({extend:'myskin/style.css',skin:'layer-ext-myskin'});
	$(function() {
		$("#applyLayer").hide();
		if($("#realNameView").html()=="未登录"){
            $("#realNameView").hide();
			$("#linkto").hide();
		}else{
			$("#login").hide();
		}
		
		$("#applyBtn").on("click",function(){
			if(!infoCheck()){
				return;
			}
			var url="${request.contextPath}/operation/trialUser/trialUserRegister"
			var params={
				realName:$("#realName").val(),
				telphone:$("#telphone").val(),
				company:$("#company").val(),
				email:$("#email").val()
				
			}
			console.log(params);
			$.post(url,params,function(data){
	    		if(data=='ok'){
	    			$("#rightPic").show();
					layer.open({
						type:1,
						title:false,
						content:$('#applyLayer'),
						area:['400px','150px'],
						btn:['确定'],
						yes: function(index, layero) {
							window.location.href="${request.contextPath}/operation/website/index"
                        }
					});
    				
    			}else{
    				var str=data.substring(0, 2);
    				var obj;
    				switch(str){
						case '单位':
						 obj=$("#company")
						  break;
						case '邮箱':
						  obj=$("#email")
						  break;
						case '姓名':
						  obj=$("#realName")
						  break;
						case '电话':
						  obj=$("#telphone")
						  break;
					}
					setError(obj,data);
    			}
			});
		});
	})
		
	//注册判断
	function infoCheck(){
		if(nameCheck() && telphoneCheck() && companyCheck() && emailCheck()){
			return true;
		}else{
			return false;
		}
	}
	
	
	//姓名校验
	$('#realName').bind('input propertychange', nameCheck)
	
	function nameCheck(){
		var realName=$('#realName').val();
		if(!(/^[\u0391-\uFFE5a-zA-Z]{1,25}$/.test(realName))){
			var msg;
			if(realName==""){
				msg='用户名不能为空';
			}else if(realName.length>25) {
				msg='用户名长度限制25个字符';
			}else{
				msg='用户名只能为中文和英文字母';
			}
			setError($('#realName'),msg);
			return false; 
		}else{
			setRight($('#realName'));
			return true;
		}	
		
	}
	//电话号码校验
	$("#telphone").blur(telphoneCheck)
	
	function telphoneCheck(){
		var tel=$('#telphone').val();
		//正则校验
		if(!(/^1[34578]\d{9}$/.test(tel))){ 
			setError($('#telphone'),tel==""?'手机号不能为空':'不是正确的手机号码');
			return false;  
 		}else{
 			setRight($('#telphone'));
 			return true;
 		} 
	
	}
	
	
	//单位校验
	$('#company').bind('input propertychange',companyCheck)
	
	 function companyCheck(){
		if($('#company').val().trim().length<1){
			setError($('#company'),'单位信息不能为空');
			return false; 
		}else if($('#company').val().trim().length<51){
			setRight($('#company'));
			return true;
		}else{
			setError($('#company'),'单位信息长度限制50个字符');
			return false;
		}
	}
	
	//邮箱失去焦点
	$("#email").blur(emailCheck)
	
	function emailCheck(){
		//邮箱校验
			var email=$("#email").val();
			var reg = /^([a-zA-Z]|[0-9])(\w|\-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/;
			//正则校验
			
			if($('#email').val().trim().length>40){
				setError($("#email"),'邮箱长度限制40个字符');
				return false; 
			}else if($('#email').val().trim().length==0){
				setError($("#email"),'邮箱不能为空');
				return false;
			}
			
			if(!(reg.test(email))){
				setError($("#email"),'不是正确的邮箱格式');
				return false; 
	 		}else{
	 			setRight($("#email"));
	 			return true;
	 		} 
	}
	
	
	//标记错误
	function setError(error,msg){
		error.parent().next().html(msg);
		error.next().removeClass("correct");
		error.next().addClass("del");
		error.addClass("error");
	}
	//标记正确
	function setRight(right){
		right.parent().next().html('');
	 	right.next().addClass("correct");
		right.next().removeClass("del");
	 	right.removeClass("error");
	}
</script>
</html>