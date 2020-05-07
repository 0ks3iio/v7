<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta charset="utf-8" />
    <title>数字校园-登录页</title>
    <link rel="icon" href="${request.contextPath}/static/images/icons/favicon.ico" type="image/x-icon" />
    <link rel="shortcut icon" href="${request.contextPath}/static/images/icons/favicon.ico" type="image/x-icon" />

    <meta name="description" content="" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />

    <!-- bootstrap & fontawesome -->
    <link rel="stylesheet" href="${request.contextPath}/static/components/bootstrap/dist/css/bootstrap.css" />
    <link rel="stylesheet" href="${request.contextPath}/static/components/font-awesome/css/font-awesome.css" />
    <link rel="stylesheet" href="${request.contextPath}/static/css/iconfont.css">

    <!-- page specific plugin styles -->
    <link rel="stylesheet" href="${request.contextPath}/static/components/slick/slick.css">


    <link rel="stylesheet" href="${request.contextPath}/static/css/page-desktop.css">
    <link rel="stylesheet" href="${request.contextPath}/static/css/iconfont.css">
    <link rel="stylesheet" href="${request.contextPath}/static/css/components.css">
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

<body class="no-skin">
<!-- #section:basics/navbar.layout -->
<div id="navbar" class="navbar">
    <script type="text/javascript">
        try{ace.settings.check('navbar' , 'fixed')}catch(e){}
    </script>

    <div class="navbar-container clearfix" id="navbar-container">
        <div class="container">
            <div class="navbar-header">
                <!-- #section:basics/navbar.layout.brand -->
                <a href="#" class="navbar-brand" alt="点击刷新">
                    <img class="logo" src="${logoPicUrl!}" alt="点击刷新">
                    <span>${logoName!}</span>
                </a>

                <!-- /section:basics/navbar.layout.brand -->
            </div>
        </div>
    </div><!-- /.navbar-container -->
</div>
<div>
    <div class="container pos-rel">
        <div class="login-box">

                <fieldset>
                    <form id="loginForm" name="loginForm" method="post">
                    <h3 class="login-title">用户登录</h3>
                    <p class="login-tips" id="error_span"></p>
                    <div class="login-form">
                        <label for="" class="sr-only">用户名</label>
                        <div class="input-group">
									<span class="input-group-addon">
										<i class="fa fa-user"></i>
									</span>
                            <input type="text" id="username" name="username" class="input-lg form-control" placeholder="请输入用户名">
                        </div>
                    </div>

                    <div class="login-form">
                        <label for="" class="sr-only">密码</label>
                        <div class="input-group">
									<span class="input-group-addon">
										<span class="fa fa-lock"></span>
									</span>
                            <input type="password" id="password" name="password" class="input-lg form-control" placeholder="请输入密码">
                        </div>
                    </div>
                    <#if isVerifyCode?default(true)>
                    <div class="login-form clearfix">
                        <label for="" class="sr-only">验证码</label>
                        <input type="text" id="verifyCode1" name="verifyCode1" class="login-verCode input-lg form-control" placeholder="请输入验证码">
                        <a href="javascript:;" id="refreshVerifyCode" ><img src="${request.contextPath}/desktop/verifyImage" alt=""></a>
                    </div>
                    <#elseif frameworkEnv.isPassport()>
                        <input type="hidden" id="verifyCode1" name="verifyCode1">
                    </#if>
                    <#if frameworkEnv.isPassport()?default(true)>
                        <input type="hidden" id="loginMode" name="loginMode" value="0" />
                    </#if>

                    <div class="login-form clearfix">
                        <label class="inline">
                            <input type="checkbox" class="wp rememberusername" id="cookieSaveType" />
                            <span class="lbl"> 记住密码</span>
                        </label>
                        <!--
                        <a class="pull-right" href="">忘记密码</a>
                        --->
                    </div>
                    </form>
                    <button class="btn btn-block btn-lg btn-blue" id="loginBtn">登&emsp;录</button>
                </fieldset>

        </div>
    </div>
    <div class="slide login-slide">
        <#if loginSlidePics?exists && loginSlidePics?size gt 0>
            <#list loginSlidePics as picUrl>
            <a href=""><img src="${picUrl!}" alt=""></a>
            </#list>
        </#if>
    </div>
</div>
<div class="content">
    <div class="container">
        <#if downloadList?exists && downloadList?size gt 0>
            <ul class="download-list clearfix">
                <#list downloadList as download>
                    <li>
                        <a class="download-item" href="">
							<span class="download-img">
								<img src="${request.contextPath}/static/login/images/icons/icon-video.png" alt="">
							</span>
                            <h5 class="download-name">公文管理操作视频</h5>
                        </a>
                    </li>
                </#list>
            </ul>
        </#if>
        <div class="row">
            <#if unitInfo?default('')!=''>
                <div class="col-sm-7">
                <div class="info">
                    <h2 class="info-title">学校介绍</h2>
                    <div class="info-body">
                        <p class="text-indent2">万朋，浙江省著名商标，成立于2003年6月25日，中国互联网教育行业领军企业，中国智慧教育领域杰出品牌，依托领先的技术优势和持续的创新精神，提供三通两平台、课后网校、美师优课、微课掌上通、云课堂等教育云平台解决方案，引领中国智慧教育的发展。万朋，让孩子卓越闯天下，让老师桃李满天下！</p>
                    </div>
                </div>
            </div>
            </#if>
            <#if isQrCode>
            <div class="col-sm-4 col-sm-offset-1">
                <div class="info">
                    <h2 class="info-title">二维码关注下载</h2>
                    <div class="info-body">
                        <div class="media">
                            <a class="media-left" href="#">
                                <img src="${request.contextPath}/static/login/images/qrcode.png" alt="${request.contextPath}/static">
                            </a>
                            <div class="media-body">
                                <h4 class="media-heading">扫一扫OA下载</h4>
                                <p>全新移动端OA系统欢迎您的使用</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            </#if>
        </div>
    </div>
</div>
<#if isFooter>
<div class="footer">
    <div class="container">
        <div class="footer-body">
            <p>浙江万朋教育科技股份有限公司 提供技术支持 Copyright 2013-2017,万朋版权所有</p>
        </div>
    </div>
</div>
</#if>
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
<script src="${request.contextPath}/static/components/slick/slick.min.js"></script>

<!-- ace scripts -->
<script src="${request.contextPath}/static/assets/js/src/ace.ajax-content.js"></script>  		<!-- 异步加载js -->

<script src="${request.contextPath}/static/js/login/jquery.cookies.js"></script>
<script src="${request.contextPath}/static/cookie/jquery.cookie.js"></script>
<script src="${request.contextPath}/static/js/login/md5.js"></script>
<script src="${request.contextPath}/static/js/login/sha1.js"></script>
<script src="${request.contextPath}/static/js/login/login.js"></script>
<script src="${request.contextPath}/static/js/login/constants.js"></script>
<!-- inline scripts related to this page -->
<script>
    $(document).ready(function(){
        $("#refreshVerifyCode").unbind("click").bind("click",refreshVerifyCode);
        $('.slide').slick({
            dots: true,
            slidesToShow: 1,
            slidesToScroll: 1,
            prevArrow:false,
            nextArrow:false,
            mobileFirst:true,
            autoplay:true
        });

        var $username = $("#username");
        var $password = $("#password");
        var $verifyCode = $("#verifyCode1");

        $("#username").on("change",function(){
            clearMsg();
        }).keypress(function (e) {
            if(e.which == 13){
                if($(this).val() == ''){
                    showError("请输入用户名");
                    return ;
                }
                $("#loginBtn").click();
            }
        })
        $password.on("change",function () {
            clearMsg();
        }).keypress(function (e) {
            if(e.which == 13){
                if($(this).val() == ''){
                    showError("请输入密码");
                    return ;
                }
                $("#loginBtn").click();
            }
        });
        $verifyCode.keypress(function (e) {
            if(e.which == 13){
                if($(this).val() == ''){
                    showError("请输验证码");
                    return ;
                }
                $("#loginBtn").click();
            }
        })
        var username = cookie("username");
        var password = cookie("password");
        <#if frameworkEnv.isPassport()>
            var username = $.cookies.get(Constants.LOGIN_SINGLE_COOKIE_USERNAME_KEY);
            var password = $.cookies.get(Constants.LOGIN_SINGLE_COOKIE_PASSWORD_KEY);
        </#if>

        if(username && username != 'undefined'){
            $username.val(username);
        }
        if(password && password != 'undefined'){
            $password.val(password);
        }
        var rememberusername = cookie("rememberusername");
        if(rememberusername && rememberusername != 'undefined' <#if frameworkEnv.isPassport()> || !isBlank(password)</#if>){
            $(".rememberusername").attr("checked",rememberusername == 1?"checked":"");
        }

        var submit = false;
        $("#loginBtn").unbind().on("click",function(){
            <#if !frameworkEnv.isPassport()?default(false)>
            if(submit){
                return ;
            }
            </#if>
            submit = true;
            var username = $username.val();
            var password = $("#password").val();
            var verifyCode = $("#verifyCode1").val();
            if(isBlank(username)){
                showError("请输入用户名");
                $("#username").focus();
                submit = false;
                return ;
            }
            if(isBlank(password)){
                showError("请输入密码");
                $("#password").focus();
                submit = false;
                return ;
            }
            <#if isVerifyCode?default(true)>
            if(isBlank(verifyCode)){
                showError("请输入验证码！");
                $("#verifyCode1").focus();
                submit = false;
                return ;
            }
            </#if>
            var params = new Object();
            params.username = username;
            params.password = password;
            params.verifyCode = verifyCode;
            <#if frameworkEnv.isPassport()?default(false)>
                login.doLogin('${passportUrl!}',"${passportServerId!}","${backUrl!}","${root!}");
                return ;
            </#if>
            $.ajax({
                url:'${request.contextPath}/homepage/loginUser/page',
                data:$.param(params),
                type:'post',
                success:function(data){
                    var jsonO = JSON.parse(data);
                    if(jsonO.success){
                        if($(".rememberusername").is(":checked")){
                            cookie("username", params.username, 30);
                            cookie("password", params.password, 30);
                            cookie("rememberusername", 1, 30);
                        }
                        else{
                            removeCookies();
                        }
                        showMsg(jsonO.msg);
                        location.href = "${request.contextPath}/desktop/index/page";
                    }else{
                        submit = false;
                        showError(jsonO.msg,refreshVerifyCode);
                    }
                },
                error : function(XMLHttpRequest, textStatus, errorThrown) {
                    submit = false;
                    $("#loginCommit").attr("disabled", "false");
                    showError("登录失败，请联系系统管理员");
                }
            });
        })


    });

    function showError(msg) {
        showError(msg,null);
    }
    function showError(msg,callback) {
        if(callback != null && typeof(callback) =='function'){
            callback();
        }
        $(".login-tips").html(msg);
    }
    function showMsg(msg) {
        $(".login-tips").css("color","green").html(msg);
    }
    function clearMsg() {
        $(".login-tips").html("");
    }
    function isBlank(str){
        return !str || str.replace(/(^s*)|(s*$)/g, "").length ==0;
    }
    function cookie(key, value, expire) {
        if (key && value && expire) {
            $.cookie(key, encodeURIComponent(value), {
                "expires": expire
            });
        } else if (key && value) {
            $.cookie(key, encodeURIComponent(value));
        }
        else if (key) {
            return decodeURIComponent($.cookie(key));
        }
    }
    function removeCookie(key) {
        if(key){
            $.removeCookie(key);
        }
    }
    function removeCookies() {
        var cookies = document.cookie ? document.cookie.split('; ') : [];

        for( var i=0; i<cookies.length; i++){
            var key = decodeURIComponent(cookies[i].split("=").shift());
            removeCookie(key);
        }
    }

    function refreshVerifyCode(){
        $img = $("#refreshVerifyCode").find("img");
        var srcUrl = $img.attr("src");
        $img.attr("src",srcUrl+"?date="+new Date());
    }
</script>
</body>
</html>
