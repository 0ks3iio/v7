<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta charset="utf-8" />
    <title>${logoName?default("数字校园")}</title>

    <meta name="description" content="" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />

    <!-- bootstrap & fontawesome -->
    <link rel="stylesheet" href="${request.contextPath}/static/components/bootstrap/dist/css/bootstrap.css" />
    <link rel="stylesheet" href="${request.contextPath}/static/components/font-awesome/css/font-awesome.css" />

    <!-- page specific plugin styles -->


    <link rel="stylesheet" href="${request.contextPath}/static/css/iconfont.css">
    <link rel="stylesheet" href="${request.contextPath}/static/css/components.css">
    <link rel="stylesheet" href="${request.contextPath}/static/css/page-desktop.css">
    <!--[if lte IE 9]>
    <link rel="stylesheet" href="${request.contextPath}/static/css/pages-ie.css" />
    <![endif]-->
    <!-- inline styles related to this page -->


    <!-- ace settings handler -->
    <script src="${request.contextPath}/static/assets/js/ace-extra.js"></script> 

    <!-- HTML5shiv and Respond.js for IE8 to support HTML5 elements and media queries -->

    <!--[if lte IE 8]>
    <script src="${request.contextPath}/static/components/html5shiv/dist/html5shiv.min.js"></script>
    <script src="${request.contextPath}/static/components/respond/dest/respond.min.js"></script>
    <![endif]-->
</head>

<body class="login-sso">
<!-- #section:basics/navbar.layout -->
<div id="navbar" class="navbar">
    <script type="text/javascript">
        try{ace.settings.check('navbar' , 'fixed')}catch(e){}
    </script>

    <div class="navbar-container clearfix" id="navbar-container">
        <div class="container">
            <div class="navbar-header">
                <!-- #section:basics/navbar.layout.brand -->
                <a href="#" class="navbar-brand">
                <#--${request.contextPath}/static/images/login-singleSignOn/logo.png-->
                    <img class="logo" src="${logoPicUrl!}" alt="">
                <#--    <span>${logoName?default("数字校园")}</span> -->
                </a>

                <!-- /section:basics/navbar.layout.brand -->
            </div>
        </div>
    </div><!-- /.navbar-container -->
</div>
<form id="loginForm" name="loginForm" method="post"> 
    <#-- TODO loginMode=0 username纯数字情况下可能会有问题by shenke from jangfeng
         loginMode=0 手机号登录可能会出问题 具体根据实际情况判断 loginMode=1 指定用户
         multiAccount=1多账号选择登录（需要调整）
    -->
   <input type="hidden" value="1" id="loginMode" name="loginMode"/>
    <input type="hidden" id="verifyCode1" name="verifyCode1" class="txt" maxlength="4" />
    <input type="hidden" value="0" id="username" name="username" value=""/>
    <input type="hidden" value="0" id="password" name="password" value=""/>
</form>  
<div class="main-sso">
    <div class="container pos-rel">
        <div class="login-box">
            <form id="loginForm1" name="loginForm1" type="post">
                <fieldset>
                    <h3 class="login-title">用户登录</h3>
                    <p class="login-tips" id="error_span"></p>

                    <div class="login-form">
                        <label for="" class="sr-only">用户名</label>
                        <div class="input-group">
									<span class="input-group-addon">
										<i class="fa fa-user"></i>
									</span>
                            <input type="text" id="uid" name="uid" class="form-control" placeholder="请输入用户名">
                        </div>
                    </div>

                    <div class="login-form">
                        <label for="" class="sr-only">密码</label>
                        <div class="input-group">
									<span class="input-group-addon">
										<span class="fa fa-lock"></span>
									</span>
                            <input type="password" id="pwd" name="pwd" class="form-control" placeholder="请输入密码">
                        </div>
                    </div>

                    <#if frameworkEnv.isPassport()?default('true')>
                        <div class="login-form clearfix" id="verifyField" style="display: none;">
                            <label for="" class="sr-only">验证码</label>
                            <div class="input-group login-verCode">
                                        <span class="input-group-addon">
                                            <span class="fa fa-barcode"></span>
                                        </span>
                                <input id="verifyCode" type="text" class="form-control" placeholder="请输入验证码">
                            </div>

                            <a href=""><img id="verifyImage" src="" width="84" height="36" alt=""></a>
                        </div>
                    <#else>
                        <#if isVerifyCode?default(false)>
                        <div class="login-form clearfix" id="verifyField" style="display: none;">
                            <label for="" class="sr-only">验证码</label>
                            <div class="input-group login-verCode">
                                        <span class="input-group-addon">
                                            <span class="fa fa-barcode"></span>
                                        </span>
                                <input id="verifyCode" type="text" class="form-control" placeholder="请输入验证码">
                            </div>

                            <a href=""><img id="verifyImage" src="${request.contextPath}/desktop/verifyImage" width="84" height="36" alt=""></a>
                        </div>
                        </#if>
                    </#if>

                    <div class="login-form clearfix">
                        <label class="inline">
                            <input type="checkbox" class="wp" id="cookieSaveType" name="cookieSaveType" />
                            <span class="lbl"> 记住密码</span>
                        </label>
                        <#--<a class="pull-right" href="">忘记密码?</a>-->
                    </div>

                    <input type="button" class="btn btn-block btn-blue" id="btn-login" value="登&emsp;录" />
                </fieldset>
            </form>
        </div>
  <#--      <div class="sso-animate-scene">
            <div class="sso-animate-people"></div>
            <div class="sso-animate-bubble sso-animate-bubble01"></div>
            <div class="sso-animate-bubble sso-animate-bubble02"></div>
            <div class="sso-animate-bubble sso-animate-bubble03"></div>
            <div class="sso-animate-bubble sso-animate-bubble04"></div>
        </div>  -->
    </div>
</div>
<div class="footer">
    <div class="container">
        <div class="footer-body">
            <p>${footer!}</p>
        </div>
    </div>
</div>
<!-- basic scripts -->

<!--[if !IE]> -->
<script src="${request.contextPath}/static/components/jquery/dist/jquery.js"></script>

<!-- <![endif]-->

<!--[if IE]>
<script src="${request.contextPath}/static/components/jquery.1x/dist/jquery.js"></script>
<![endif]-->
<script type="text/javascript">
    if('ontouchstart' in document.documentElement) document.write("<script src='${request.contextPath}/static/components/_mod/jquery.mobile.custom/jquery.mobile.custom.js'>"+"<"+"/script>");
</script>
<script src="${request.contextPath}/static/components/bootstrap/dist/js/bootstrap.js"></script>

<!-- page specific plugin scripts -->
<script src="${request.contextPath}/static/components/greensock/TweenMax.min.js"></script>

<!-- inline scripts related to this page -->
<script src="${request.contextPath}/static/js/login/jquery.cookies.js"></script>
<script src="${request.contextPath}/static/js/login/md5.js"></script>
<script src="${request.contextPath}/static/js/login/sha1.js"></script>
<script src="${request.contextPath}/static/js/login/login.js"></script>
<script src="${request.contextPath}/static/js/login/constants.js"></script>
<script>
    $(document).ready(function(){
        var $bubble01=$('.sso-animate-bubble01'),
                $bubble02=$('.sso-animate-bubble02'),
                $bubble03=$('.sso-animate-bubble03'),
                $bubble04=$('.sso-animate-bubble04');

        TweenLite.fromTo($bubble01, 1.5,{top:"155px",left:"462px",opacity:"0"}, {top:"-8px",left:"394px",opacity:'1', ease: Bounce.easeOut, delay:0.5});
        TweenLite.fromTo($bubble02, 1.5,{top:"155px",left:"462px",opacity:"0"}, {top:"70px",left:"393px",opacity:'1', ease: Bounce.easeOut, delay:0.5});
        TweenLite.fromTo($bubble03, 1.5,{top:"155px",left:"462px",opacity:"0"}, {top:"82px",left:"445px",opacity:'1', ease: Bounce.easeOut, delay:0.5});
        TweenLite.fromTo($bubble04, 1.5,{top:"155px",left:"462px",opacity:"0"}, {top:"160px",left:"610px",opacity:'1', ease: Bounce.easeOut, delay:0.5});


        $("#uid").focus();

        var $uid = $("#uid");
        var $pwd = $("#pwd");

        $("#uid").on("change",function () {
            clearSmartMsg();
        }).keypress(function (e) {
            if(e.which == 13){
                if($(this).val() == ''){
                    showSmartError("请输入用户名！");
                    return ;
                }else{
                    if($pwd.val() != ''){
                        $("#btn-login").click();
                    }else {
                        $pwd.focus();
                    }
                }
            }
        });

        $("#pwd").on("change",function(){
            clearSmartMsg();
        }).keypress(function (e) {
            if(e.which == 13){
                if($(this).val() == ''){
                    showSmartError("请输入密码！");
                    return ;
                }else{
                    if($uid.val() != ''){
                        $("#btn-login").click();
                    }else{
                        showSmartError("请输入用户名！");
                    }
                }
            }
        });


        $("#btn-login").unbind("click").bind("click",function () {
            var username = $("#uid").val();
            var password = $("#pwd").val();
            var verifyCodee = $("#verifyCode").val();
            if(isSmartBlank(username)){
                showSmartError("请输入用户名！");
                return ;
            }
            if(isSmartBlank(password)){
                showSmartError("请输入密码！");
                return ;
            }
            <#if isVerifyCode?default(false)>
                if(isSmartBlank(verifyCodee)) {
                    showSmartError("请输入验证码！")
                }
            </#if>
            $("#username").val(username);
            $("#password").val(password);
            $("#verifyCode1").val(verifyCodee);
            <#if frameworkEnv.isPassport()?default('true')>
            doForPassportLogin();
            <#else>
                var params = new Object();
                params.username = username;
                params.password = password;
            if(!submit)
            doLoginEis(params);
            </#if>
        });
        login.init_login_eis();

    });
    var submit = false;
    function doLoginEis(params){
        submit = true;
        $.ajax({
            url:'${request.contextPath}/homepage/loginUser/page',
            data:{"username":params.username,"password":params.password},
            type:'post',
            success:function(data){
                var jsonO = JSON.parse(data);
                if(jsonO.success){
                    if($("input[name=cookieSaveType]").is(":checked")){
                        var cookieExpires = new Date();
                        cookieExpires.setTime(cookieExpires.getTime() + (1000 * 60 * 60 * 24 * Constants.LOGIN_SINGLE_COOKIE_LIFE));
                        $.cookies.set(Constants.LOGIN_SINGLE_COOKIE_USERNAME_KEY, params.username, {
                            expiresAt : cookieExpires
                        });
                        $.cookies.set(Constants.LOGIN_SINGLE_COOKIE_PASSWORD_KEY, params.password, {
                            expiresAt : cookieExpires
                        });
                    }
                    else{
                        $.cookies.del(Constants.LOGIN_SINGLE_COOKIE_USERNAME_KEY);
                        $.cookies.del(Constants.LOGIN_SINGLE_COOKIE_PASSWORD_KEY);
                    }
                    showSmartMsg(jsonO.msg);
                    location.href = "${request.contextPath}/desktop/index/page";
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
    //登录
    function doForPassportLogin(){
        login.doLogin('${frameworkEnv.getString("passport_url")!}',"${frameworkEnv.getString("passport_server_id")!}","${frameworkEnv.getString("eis_web_url")!}","<#if request.contextPath?default('')==''>1<#else>0</#if>");
    }

    function showSmartError(msg){
        $(".login-tips").html(msg);
    }
    function showSmartMsg(msg) {
        $(".login-tips").css("color","green").html(msg);
    }
    function clearSmartMsg(){
        $(".login-tips").html("");
    }
    function isSmartBlank(str) {
        return !str || str.replace(/(^s*)|(s*$)/g, "").length ==0;
    }
</script>
</body>
</html>
