<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta charset="utf-8" />
    <title>${loginOption.headerName!"数字校园"}</title>

    <meta name="description" content="" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />

    <!-- bootstrap & fontawesome -->
    <link rel="stylesheet" href="${request.contextPath}/static/components/font-awesome/css/font-awesome.css" />
    <link rel="stylesheet" href="${request.contextPath}/static/components/bootstrap/dist/css/bootstrap.css" />
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

    <style type="text/css">
    	.login-sso{background:#fff url(${loginOption.loginBgUrl!}) no-repeat top center;}
    </style>
</head>

<body class="login-sso">
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
                     <#if loginOption.enablePageLogoImage!true>
                       <img class="logo" src="${loginOption.logoUrl!}" alt="">
                     </#if>
                     <#if loginOption.enablePageLogoName!true>
                       <span>${loginOption.logoName!"数字校园"}</span>
                     </#if>
                </a>

                <!-- /section:basics/navbar.layout.brand -->
            </div>
        </div>
    </div><!-- /.navbar-container -->
</div>
<#--<form id="loginForm" name="loginForm" method="post">-->
    <input type="hidden" value="0" id="loginMode" name="loginMode"/>
    <input type="hidden" id="verifyCode1" name="verifyCode1" class="txt" maxlength="4" />
    <input type="hidden" id="username" name="username" />
    <input type="hidden" value="0" id="password" name="password" />
    <input type="hidden" value="1" id="multiAccount" name="multiAccount" />
    <input type="hidden" value="0" id="multiAccountByOneMatch" name="multiAccountByOneMatch" />
<#--</form>-->
<div class="main-sso">
    <div class="container pos-rel">
        <div class="login-box">
            <#--<form id="loginForm1" name="loginForm1" type="post">-->
                <fieldset>
                    <h3 class="login-title">用户登录</h3>
                    <p class="login-tips" id="error_span">${loginError!}</p>

                    <div class="login-form">
                        <label for="" class="sr-only">用户名</label>
                        <div class="input-group">
									<span class="input-group-addon">
										<i class="fa fa-user"></i>
									</span>
                            <input type="text" id="uid" name="uid" class="form-control" placeholder="请输入手机号/用户名">
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

                    <#if loginOption.connectPassport!false>
                        <div class="login-form clearfix" id="verifyField" style="display: none;">
                            <label for="" class="sr-only">验证码</label>
                            <div class="input-group login-verCode">
                                        <span class="input-group-addon">
                                            <span class="fa fa-barcode"></span>
                                        </span>
                                <input id="verifyCode" type="text" class="form-control" placeholder="请输入验证码">
                            </div>

                            <img id="verifyImage" src="" width="84" height="36" alt="">
                        </div>
                    <#else>
                        <#if loginOption.verifyCode!false>
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
                        <#if  loginOption.showRegister!false >
                        <a class="pull-right" href="${request.contextPath}/homepage/register/register">注册</a>
                        </#if>
                        <#if loginOption.showForgetPassword?default(false)>
                            <span class="pull-right">&nbsp;&nbsp;&nbsp;</span>
                            <a class="pull-right" target="_blank" href="${request.contextPath}/homepage/register/resetPwdPage">忘记密码</a>
                            <#--span class="pull-right">&nbsp;&nbsp;&nbsp;</span>
                            <a class="pull-right js-changePassword" href="${loginOption.passportUrl!}/forgotPassword">忘记密码?</a-->
                        </#if>
                    </div>

                    <input type="button" class="btn btn-block btn-blue" id="btn-login" value="登&emsp;录" />
                    <#if loginOption.warn?default('') != ''>
                    	<p class="h4 color-red">${loginOption.warn!}</p>
                    </#if>
                </fieldset>
            <#--</form>-->
            <#if  loginOption.showQQ!false >
            <div class="login-other">
	        	<a href="http://passport.dqedu.net:83/qqOauth.htm?state=3282301" class="login-qq">QQ登录</a>
	        	<span class="login-other-line">|</span>
	        	<a href="http://passport.dqedu.net:83/weixinOauth.htm?state=3282301" class="login-wx">微信登录</a>
		    </div>
		    </#if> 
        </div>
        <#if loginOption.player!true>
        <div class="sso-animate-scene">
            <div class="sso-animate-people"></div>
            <div class="sso-animate-bubble sso-animate-bubble01"></div>
            <div class="sso-animate-bubble sso-animate-bubble02"></div>
            <div class="sso-animate-bubble sso-animate-bubble03"></div>
            <div class="sso-animate-bubble sso-animate-bubble04"></div>
        </div>
        </#if>
    </div>
</div>
<div class="footer">
    <div class="container">
        <div class="footer-body">
            <p>${loginOption.footer!}</p>
            <#if deployRegion?default("")=='qzxh'>
            <p>
                <br/>
                <img src="${request.contextPath}/desktop/images/${deployRegion}-qrcode.jpg" />
            </p>
            </#if>
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
<script src="${request.contextPath}/static/js/login/loginBase64.js"></script>
<script src="${request.contextPath}/static/js/login/login.js?v=1.3"></script>
<script src="${request.contextPath}/static/js/login/login_ext.js?v=1.4"></script>
<script src="${request.contextPath}/static/js/login/constants.js"></script>
<script src="${request.contextPath}/static/js/jquery-browser.update.min.js"></script>

<script>
    $(document).ready(function(){
        <#if loginOption.player!true>
        var $bubble01=$('.sso-animate-bubble01'),
                $bubble02=$('.sso-animate-bubble02'),
                $bubble03=$('.sso-animate-bubble03'),
                $bubble04=$('.sso-animate-bubble04');

        TweenLite.fromTo($bubble01, 1.5,{top:"155px",left:"462px",opacity:"0"}, {top:"-8px",left:"394px",opacity:'1', ease: Bounce.easeOut, delay:0.5});
        TweenLite.fromTo($bubble02, 1.5,{top:"155px",left:"462px",opacity:"0"}, {top:"70px",left:"393px",opacity:'1', ease: Bounce.easeOut, delay:0.5});
        TweenLite.fromTo($bubble03, 1.5,{top:"155px",left:"462px",opacity:"0"}, {top:"82px",left:"445px",opacity:'1', ease: Bounce.easeOut, delay:0.5});
        TweenLite.fromTo($bubble04, 1.5,{top:"155px",left:"462px",opacity:"0"}, {top:"160px",left:"610px",opacity:'1', ease: Bounce.easeOut, delay:0.5});
        </#if>

        var loginOption = {
            connectPassport : <#if loginOption.connectPassport!true>true<#else>false</#if>,
            passportUrl     : '${loginOption.passportUrl!}',
            serverId        : '${loginOption.serverId!}',
            root            : '${loginOption.root!}',
            player          : <#if loginOption.player!true>true<#else>false</#if>,
            initLicense     : <#if loginOption.initLicense!true>true<#else>false</#if>,
            eisLoginUrl     : '${urlPrefix!}/homepage/loginUser/page',
            <#if callBack??>
                indexUrl        : '${callBack!}'
            <#else >
                indexUrl        : '${urlPrefix!}/desktop/index/page'
            </#if>
        };

        var option = {
            usernameId : 'uid',
            passwordId : "pwd",
            tipsId     : 'error_span',
            loginBtnId : 'btn-login',
            loginOption: loginOption,
            passportUsernameId : 'username',
            passportPasswordId : 'password',
            verifyCodeId : 'verifyCode',
            passportVerifyCode : 'verifyCode1',
            loginModelId : 'loginMode',
            spanError : '${loginError?default("")}'
        };
            eisLogin.init_login(option);
            login.init_login_eis();

    });

</script>
<!--初始化验证码-->
<#if loginOption.connectPassport!false&&loginOption.verifyCode!false >
<script src="${loginOption.passportUrl!}/scriptLogin?action=init&server=${loginOption.serverId!}"></script>
</#if>
</body>
</html>
