<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <title>登录</title>
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/static/qth/css/style.css">
</head>

<body class="loginContainer">

<input type="hidden" value="0" id="loginMode" name="loginMode"/>
<input type="hidden" id="verifyCode1" name="verifyCode1" class="txt" maxlength="4" />
<input type="hidden" id="username" name="username" />
<input type="hidden" value="0" id="password" name="password" />
<input type="hidden" value="1" id="multiAccount" name="multiAccount" />
<input type="hidden" value="0" id="multiAccountByOneMatch" name="multiAccountByOneMatch" />

<div class="container-wrap">
    <div class="header-wrap">
        <div class="header-inner">
            <div class="logo">${loginOption.logoName!"数字校园"}</div>
        </div>
    </div>
    <div id="container" class="fn-clear">
        <div class="login-outter">
            <h3 class="login-title">用户登录<span>USER LOGIN</span></h3>
            <p class="login-tips" id="error_span">${loginError!}</p>
            <div class="login-form">
                <input id="uid" name="uid" type="text" class="form-control name" placeholder="用户名">
            </div>
            <div class="login-form">
                <input id="pwd" name="pwd" type="password" class="form-control password" placeholder="密码">
            </div>
            <div class="login-form f-16 c-888">
                <span class="ui-checkbox">
                    <input type="checkbox" class="chk" id="cookieSaveType" name="cookieSaveType" />
                    记住用户名和密码
                </span>
            </div>
            <a href="javascript:void(0);" hidefocus="true" class="abtn" id="btn-login">登 录</a>
<#--            <div class="f-16 c-888 mt-20">还没有账号？<a href="#">立即注册</a></div>-->
        </div>
    </div>
    <div class="footer-wrap footer-img-box">
        <a href="http://bszs.conac.cn/sitename?method=show&id=8C1D619B895AEB0BE05310291AAC930F" style="height: 70px;" target="_blank">
            <img src="${request.contextPath}/static/qth/images/biaoshi.png" class="login-bottom-img">
        </a>
        <span>${loginOption.footer!}</span>
    </div>
</div>

<script src="${request.contextPath}/static/qth/js/jquery-1.9.1.min.js"></script>
<script src="${request.contextPath}/static/qth/js/jquery.FormNice.js"></script>
<script src="${request.contextPath}/static/js/login/jquery.cookies.js"></script>
<script src="${request.contextPath}/static/js/login/md5.js"></script>
<script src="${request.contextPath}/static/js/login/sha1.js"></script>
<script src="${request.contextPath}/static/js/login/loginBase64.js"></script>
<script src="${request.contextPath}/static/js/login/login.js?v=1.2"></script>
<script src="${request.contextPath}/static/js/login/login_ext.js?v=1.4"></script>
<script src="${request.contextPath}/static/js/login/constants.js"></script>
<script src="${request.contextPath}/static/js/jquery-browser.update.min.js"></script>

<script>
    $(document).ready(function() {
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
    })
</script>
</body>
</html>
