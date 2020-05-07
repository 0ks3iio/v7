<#-- 智慧校园首页 -->

<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta charset="utf-8" />
    <title>智慧校园-首页</title>

    <meta name="description" content="" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
    <link rel="shortcut icon" href="${request.contextPath}/static/images/icons/favicon.ico" type="image/x-icon" />
    <!-- bootstrap & fontawesome -->
    <link rel="stylesheet" href="${request.contextPath}/static/components/bootstrap/dist/css/bootstrap.css" />
    <link rel="stylesheet" href="${request.contextPath}/static/components/font-awesome/css/font-awesome.css" />

    <link rel="stylesheet" href="${request.contextPath}/static/css/components.css">
    <link rel="stylesheet" href="${request.contextPath}/static/css/smart-campus.css">
    <link rel="stylesheet" href="${request.contextPath}/static/css/test1.css">
    <!--[if lte IE 9]>
    <link rel="stylesheet" href="${request.contextPath}/static/css/pages-ie.css" />
    <style type="text/css">
        .layout-bg{display: none;}
    </style>
    <![endif]-->
    <!-- inline styles related to thispage -->

    <!-- HTML5shiv and Respond.js for IE8 to support HTML5 elements and media queries -->

    <!--[if lte IE 8]>
    <script src="${request.contextPath}/static/components/html5shiv/dist/html5shiv.min.js"></script>
    <script src="${request.contextPath}/static/components/respond/dest/respond.min.js"></script>
    <![endif]-->
</head>

<body>
<div class="smart-header">
    <div class="smart-inner clearfix">
        <div class="smart-logo">
            <a href="http://www.msyk.cn/">
                <img src="${request.contextPath}/static/images/smart-campus/logo.png" alt="">
            </a>
        </div>
        <div class="smart-login"  >
					<span class="smart-login-before" <#if login?default(false)>style="display: none;"</#if>>
						<a href="#" class="js-login">登 录</a>
					</span>
            <span class="smart-login-after" <#if !login?default(true)>style="display: none;"</#if>>
						<img src="${request.contextPath}/static/images/smart-campus/avatar_default.png" alt="默认头像" class="avatar" />
						<a href="#" class="name">${realName!}</a>
						<span class="line">|</span>
						<a href="${request.contextPath}/homepage/smartLogout/page">退出</a>
					</span>
        </div>
        <div class="smart-menu">
            <a href="http://www.msyk.cn/" class="active">首 页</a>
            <a href="http://my.msyk.cn/" target="_blank">智慧校园</a>
            <a href="http://pad.msyk.cn/" target="_blank">智慧课堂</a>
        </div>
    </div>
</div>

<div class="smart-container">
    <div class="index-caput-wrap">
        <div class="index-caput-bg">
            <div class="index-caput-bg-animt">
                <div class="layout-bg">
                    <div class="layout-bg-circle"></div>
                    <div class="layout-bg-square"></div>
                    <div class="layout-bg-triangle fa fa-send"></div>
                </div>
            </div>
        </div>
    </div>
    <div class="smart-inner smart-banner">
        <span class="smart-banner-txt1"></span>
        <span class="smart-banner-txt2"></span>
        <span class="smart-banner-txt3"></span>
        <div class="smart-banner-img-outer">
            <span class="smart-banner-img1"></span>
            <span class="smart-banner-img2"></span>
            <span class="smart-banner-img3"></span>
        </div>
    </div>
    <div class="smart-inner smart-content">
        <ul class="fun-list clearfix">
            <li>
                <p><img src="${request.contextPath}/static/images/smart-campus/fun-1.png" alt="" /></p>
                <p class="tt">开放式平台</p>
                <p class="dd">构建开放共享的统一规范平台<br />实现服务汇聚、互联互通</p>
            </li>
            <li>
                <p><img src="${request.contextPath}/static/images/smart-campus/fun-2.png" alt="" /></p>
                <p class="tt">一站式服务</p>
                <p class="dd">为师生提供办公、教务、教学、<br />学习等丰富的一站式信息服务</p>
            </li>
            <li>
                <p><img src="${request.contextPath}/static/images/smart-campus/fun-3.png" alt="" /></p>
                <p class="tt">泛在化应用</p>
                <p class="dd">实现管理和教学<br />服务化、自主化、移动化</p>
            </li>
            <li>
                <p><img src="${request.contextPath}/static/images/smart-campus/fun-4.png" alt="" /></p>
                <p class="tt">大数据动态监测</p>
                <p class="dd">对区域舆情、综合校情、教学质量<br />进行全面的监测和动态分析</p>
            </li>
            <li>
                <p><img src="${request.contextPath}/static/images/smart-campus/fun-5.png" alt="" /></p>
                <p class="tt">可持续运营</p>
                <p class="dd">采用云端部署，用户无需软硬件<br />投入及维护，即时入驻，安全高效</p>
            </li>
        </ul>
        <div class="floor-item">
            <h2>美师优课</h2>
            <p class="floor-img floor-img1"></p>
            <h2>智慧办公</h2>
            <p class="floor-img floor-img2"></p>
            <h2>智能教务</h2>
            <p class="floor-img floor-img3"></p>
            <h2>名师直播</h2>
            <p class="floor-img floor-img4"></p>
            <h2>人人通空间</h2>
            <p class="floor-img floor-img5"></p>
            <h2>大数据分析</h2>
            <p class="floor-img floor-img6"></p>
        </div>
    </div>
</div>

<div class="smart-footer">
    <div class="smart-inner">
        <p><a href="http://wk.wanpeng.com" target="_blank">微课掌上通</a>|<a href="http://www.kehou.com" target="_blank">课后网</a>|<a href="http://www.edu88.com/" target="_blank">三通两平台</a></p>
        <p>浙江万朋教育科技股份有限公司版权所有 备案号:浙ICP备05070430号</p>
        <p>客服热线：400-863-2003（教育云平台）&emsp;400-617-1997（课后网）&emsp;400-667-1997（家校互联）&emsp;4000-150-150（微课掌上通）</p>
    </div>
</div>
<!-- basic scripts -->

<form id="loginForm" name="loginForm" method="post">
    <input type="hidden" value="0" id="loginMode" name="loginMode"/>
    <input type="hidden" id="verifyCode1" name="verifyCode1" class="txt" maxlength="4" />
    <input type="hidden" value="0" id="username" name="username" value=""/>
    <input type="hidden" value="0" id="password" name="password" value=""/>
</form>
<div class="login-box layer layer-login">
        <form>
        <fieldset>
            <h3 class="login-title">
                <img class="logo" src="${request.contextPath}/static/images/smart-campus/logo.png" alt="">
            </h3>
            <h4 class="login-title-tips">
                <p><span>使用用户名或邮箱登录</span></p>
            </h4>
            <p class="login-tips" id="error_span"></p>

            <div class="login-form">
                <label for="" class="sr-only">用户名</label>
                <div class="input-group">
							<span class="input-group-addon">
								<i class="fa fa-user"></i>
							</span>
                    <input type="text" id="uid" class="input-lg form-control" placeholder="请输入用户名">
                </div>
            </div>

            <div class="login-form">
                <label for="" class="sr-only">密码</label>
                <div class="input-group">
							<span class="input-group-addon">
								<span class="fa fa-lock"></span>
							</span>
                    <input type="password" id="pwd" class="input-lg form-control" placeholder="请输入密码">
                </div>
            </div>
            <div class="login-form">
                <input type="button" id="btn-login" class="btn btn-block btn-lg btn-blue" value="立即登录" />
            </div>

            <div class="login-form clearfix">
                <span class="pull-right" href="">还没有账号？<a href="#">立即注册</a></span>
            </div>
        </fieldset>
        </form>

</div>

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
<script src="${request.contextPath}/static/components/layer/layer.js"></script>

<script src="${request.contextPath}/static/js/login/jquery.cookies.js"></script>
<script src="${request.contextPath}/static/cookie/jquery.cookie.js"></script>
<script src="${request.contextPath}/static/js/login/md5.js"></script>
<script src="${request.contextPath}/static/js/login/sha1.js"></script>
<script src="${request.contextPath}/static/js/login/login.js"></script>
<script src="${request.contextPath}/static/js/login/constants.js"></script>
<!-- inline scripts related to this page -->
<script>

    $(document).ready(function () {
        $('.js-login').click(function(){
            layer.open({
                type: 1,
                shade: .5,
                area: '500px',
                title: false,
                content: $('.layer-login')
            })
        })
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
            if(isSmartBlank(username)){
                showSmartError("请输入用户名！");
                return ;
            }
            if(isSmartBlank(password)){
                showSmartError("请输入密码！");
                return ;
            }
            $("#username").val($("#uid").val());
            $("#password").val($("#pwd").val());
            doLogin();
        });
    });

    //登录
    function doLogin(){
        login.doLogin('${frameworkEnv.getString("passport_url")!}',"${frameworkEnv.getString("passport_server_id")!}","${request.contextPath!}/smart.html","<#if request.contextPath?default('')==''>1<#else>0</#if>");
    }
    function showSmartError(msg){
        $(".login-tips").html(msg);
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
