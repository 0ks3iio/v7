<#import "/fw/macro/webmacro.ftl" as webmacro>
<@webmacro.commonWeb title="用户登陆">
<!-- ajax layout which only needs content area -->

<script src="${request.contextPath}/static/cookie/jquery.cookie.js"></script>

<body class="no-skin">
		<div class="main-content">
			<div class="main-content-inner">
				<!-- #section:basics/content.breadcrumbs -->
				<div class="page-content">
					<!-- /section:settings.box -->
					<div class="page-content-area">
						
<div class="row">
	<div class="col-xs-12">
		<!-- PAGE CONTENT BEGINS -->
		<div class="login-container">
			<div class="center">
				<h1>
					<i class="ace-icon fa fa-leaf green"></i>
					<span class="red">Zdsoft</span>
					<span class="blue" id="id-text2">浙大万朋</span>
				</h1>
				<h4 class="blue" id="id-company-text"></h4>
			</div>

			<div class="space-6"></div>
			<div class="position-relative">
				<div id="login-box" class="login-box visible widget-box no-border">
					<div class="widget-body">
						<div class="widget-main">
							<h4 class="header blue lighter bigger">
								<i class="ace-icon fa fa-coffee green"></i>
								请输入登陆信息
							</h4>
											
							<form>
								<fieldset>
									<label class="block clearfix">
										<span class="block input-icon input-icon-right">
											<input id="username" type="text" class="form-control" placeholder="用户名/手机/邮箱" />
											<i class="ace-icon fa fa-user"></i>
										</span>
									</label>


									<label class="block clearfix">
										<span class="block input-icon input-icon-right">
											<input id="password" type="password" class="form-control" placeholder="密码" />
											<i class="ace-icon fa fa-lock"></i>
										</span>
									</label>

									<div class="space"></div>

									<div class="clearfix">
										<label class="inline">
											<input id="rememberUsername" type="checkbox" class="ace" />
											<span class="lbl"> 记住用户名</span>
										</label>
										<label class="inline">
											<input id="rememberPwd" type="checkbox" class="ace" />
											<span class="lbl"> 记住密码</span>
										</label>

										<button id="loginCommit" type="button" class="width-35 pull-right btn btn-sm btn-primary">
											<i class="ace-icon fa fa-key"></i>
											<span class="bigger-110">确定</span>
										</button>
									</div>

									<div class="space-4"></div>
								</fieldset>
							</form>

							<div class="social-or-login center">
								<span class="bigger-110">其他方式登陆</span>
							</div>

							<div class="space-6"></div>

							<div class="social-login center">
								<a class="btn btn-primary">
									<i class="ace-icon fa fa-qq"></i>
								</a>

								<a class="btn btn-info">
									<i class="ace-icon fa fa-weixin"></i>
								</a>

								<a class="btn btn-danger">
									<i class="ace-icon fa fa-weibo"></i>
								</a>
							</div>
						</div><!-- /.widget-main -->

						<div class="toolbar clearfix">
							<div>
								<a href="javascript:;" class="forgot-password-link">
									<i class="ace-icon fa fa-arrow-left"></i>
									忘记密码
								</a>
							</div>

							<div>
								<a href="javascript:;" class="user-signup-link">
									注册新账号
									<i class="ace-icon fa fa-arrow-right"></i>
								</a>
							</div>
						</div>
					</div><!-- /.widget-body -->
				</div><!-- /.login-box -->
		</div>

		<!-- PAGE CONTENT ENDS -->
	</div><!-- /.col -->
</div><!-- /.row -->
					</div><!-- /.page-content-area -->
				</div><!-- /.page-content -->
			</div>
		</div><!-- /.main-content -->
		
		
		
<!-- page specific plugin scripts -->
<script type="text/javascript">
	$('.page-content-area').ace_ajax('loadScripts', [], function() {
		 jQuery(function($) {
		 
		 var username = cookie("username");
		 if(username && username != 'undefined'){
		 	$("#username").val(username);
		 };
		 var password = cookie("password");
		 if(password && password != 'undefined'){
		 	$("#password").val(password);
		 };
		 var rememberPwd = cookie("rememberPwd");
		 if(rememberPwd && rememberPwd != 'undefined'){
		 	$("#rememberPwd").attr("checked", rememberPwd == 1 ? "true" : "false");
		 };
		 var rememberUsername = cookie("rememberUsername");
		 if(rememberUsername && rememberUsername != 'undefined'){
		 	$("#rememberUsername").attr("checked", rememberUsername == 1 ? "true" : "false");
		 };
		 
		 $("#rememberPwd").on("click", function(){
		 	if($(this).is(":checked") && !$("#rememberUsername").is(":checked")){
		 		$("#rememberUsername").click();
		 	}
		 });
		 $("#rememberUsername").on("click", function(){
		 	if(!$(this).is(":checked") && $("#rememberPwd").is(":checked")){
		 		$("#rememberPwd").click();
		 	}
		 });
		 
		 		 		 
		 $("#password").keypress(function(e) {
			if(e.which == 13){
			 	if($(this).val() == ""){
			 		layer.tips("请输入密码！", "#password");
			 		return;
			 	}
				$("#loginCommit").click();
			}
		});
		
		 $("#username").keypress(function(e) {
			if(e.which == 13){
				if($(this).val() == ""){
			 		layer.tips("请输入登陆账号信息！", "#username");
			 		return;
			 	}
				$("#password").focus();
			}
		})
		
		 $("#loginCommit").on("click", function(){
		 	$("#loginCommit").attr("disabled", "true");
		 	var params = new Object();
		 	params.username = $("#username").val();
		 	if(params.username == ""){		 		
		 		layer.tips("请输入登陆账号信息！", "#username");
		 		$("#loginCommit").removeAttr("disabled");
		 		return;
		 	}
		 	params.password = $("#password").val();
		 	if(params.password == ""){
		 		layer.tips("请输入密码！", "#password");
		 		$("#loginCommit").removeAttr("disabled");
		 		return;
		 	}		 	
		 	$.ajax({  
			 	url:"${request.contextPath}/homepage/loginUser/page",
			 	data:$.param(params),
			 	type:"post",
			 	success:function(data){
			 		var jsonO = JSON.parse(data);
			 		if(!jsonO.success){
			 			if($("#" + jsonO.code).length <= 0){
			 				layer.tips(jsonO.msg, "#loginCommit", {tips: [2, '#FF0000']});
			 			}
			 			else{
			 				layer.tips(jsonO.msg, "#" + jsonO.code);
			 			}
			 			$("#loginCommit").removeAttr("disabled");
			 		}
			 		else{
				 		if($("#rememberPwd").is(":checked")){
				 			cookie("username", params.username, 30);
				 			cookie("password", params.password, 30);
				 			cookie("rememberPwd", 1, 30);
				 			cookie("rememberUsername", 1, 30);
				 		}
				 		else{
				 			cookie("rememberPwd", null);
				 			cookie("password", null);
				 			if($("#rememberUsername").is(":checked")){
				 				cookie("rememberUsername", 1, 30);
					 			cookie("username", params.username, 30);
					 			cookie("loginAuto", null);
					 		}
					 		else{
					 			cookie("rememberUsername", null);
					 			cookie("username", null);
					 		}
				 		}
				 		layer.tips("正在登陆……", "#loginCommit", {tips: [2, '#228B22']});
				 		location.href = "${request.contextPath}/homepage/index/page";
				 	}			 		
			 	},
			 	error : function(XMLHttpRequest, textStatus, errorThrown) {  
			 		$("#loginCommit").attr("disabled", "false");
			 		var text = syncText(XMLHttpRequest);
	     			swal({title: "操作失败!",text: text, type:"error",showConfirmButton: true});
	    		}
			 });
		 });
	});
	});
</script>
		
		

</@webmacro.commonWeb>
	<#if showTestInfo!true>
	<script>
		$(document).on("dblclick", function(){
			if(_keydown == 3){
				layerDiv('500px', '360px', $('#login-modal-table'), '测试要点');
			}
			_keydown = 0;
		});
	</script>
	<div id="login-modal-table" class="hide" tabindex="-1" height="100">
				<hr />
				<h3>linqz, 20160606</h3>
				<P>1. 记住用户名和密码相关关系。
				<P>2. cookie是否生效
				<P>3. 用户名或者密码出错，在相应的位置会有提示
				<P>4. 登陆成功后，会提示跳转中。
				<p>5. 登陆失败后，在登陆按钮旁边提示红色错误信息。
	</div> 
	</#if>