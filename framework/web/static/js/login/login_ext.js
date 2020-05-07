/**
 * 除了login.js之外另外单独独立出login_ex.js 方便模版替换
 */
(function () {
    var root = this;

    function EisLogin() {


    }
    /**
     *
     * 初始化登录页相关配置（绑定点击事件）
     * @param usernameId 用户名input Id
     * @param passwordId 密码 input Id
     * @param tipsId tips Id
     * @param loginBtnId
     * @param loginOption
     */
    EisLogin.prototype.init_login = function (option) {
        //将element初始化为EisLogin全局属性
        var $username = EisLogin.prototype.username = $('#' + option.usernameId);
        var $password = EisLogin.prototype.password = $('#' + option.passwordId);
        var $tips     = EisLogin.prototype.tips     = $('#' + option.tipsId);
        var $loginBtn = EisLogin.prototype.loginBtn = $('#' + option.loginBtnId);
        var $passportUsername = EisLogin.prototype.passportUsername = $('#' + option.passportUsernameId);
        var $passportPassword = EisLogin.prototype.passportPassword = $('#' + option.passportPasswordId);
        var $verifyCode       = EisLogin.prototype.verifyCode       = $('#' + option.verifyCodeId);
        var $passportVerifyCode = EisLogin.prototype.passportVerifyCode = $('#' + option.passportVerifyCode);
                        EisLogin.prototype.loginConfig = option.loginOption;
                        EisLogin.prototype.option = option;
                        EisLogin.prototype.loginModel = $('#' + option.loginModelId);
        //点击事件绑定
        $username.on('change', loginClick.username_change).keypress(loginClick.username_keypress);
        $password.on('change', loginClick.password_change).keypress(loginClick.password_keypress);
        $loginBtn.on('click',  loginClick.login);

    };

    EisLogin.prototype.clearMsg = function () {
        if (root.eisLogin.Utils.isBlank(root.eisLogin.option.spanError)) {
            $(root.eisLogin.tips).html('');
        } else {
            root.eisLogin.option.spanError = "";
        }
    };
    EisLogin.prototype.showMsg = function (msg) {
        $(root.eisLogin.tips).css('color', 'green').html(msg);
    };
    EisLogin.prototype.showError = function (error) {
        $(root.eisLogin.tips).html(error);
    };
    EisLogin.prototype.Utils = {
        isBlank : function (str) {
            return !str || str.replace(/(^s*)|(s*$)/g, "").length ===0;
        },
        validator : function () {
            var username = root.eisLogin.username.val();

            var result = {};
            result.success = false;
            if (root.eisLogin.Utils.isBlank(username)) {
                root.eisLogin.showError('请输入账号');
                return result;
            }
            result.username = username;
            var password = root.eisLogin.password.val();
            if (root.eisLogin.Utils.isBlank(password)) {
                root.eisLogin.showError('请输入密码');
                return result;
            }
            result.verifyCode = root.eisLogin.verifyCode.val();
            result.password = password;
            if (!root.eisLogin.loginConfig.initLicense
                && $.trim(username) !== 'super') {
                root.eisLogin.showError('系统未激活');
                return result;
            }
            result.success = true;
            return result;

        }
    };

    var loginClick = {
        username_change : function () {
            root.eisLogin.clearMsg();
        },
        username_keypress : function (e) {
            if (e.which !== 13) {
                return ;
            }
            if (root.eisLogin.username.val() === '') {
                root.eisLogin.showError('请输入账号');
                return;
            }
            if (root.eisLogin.password.val() === '') {
                root.eisLogin.password.focus();
                return;
            }
            root.eisLogin.loginBtn.click();
        },
        password_change : function (password) {
            root.eisLogin.clearMsg();
        },
        password_keypress : function (e) {
            if (e.which !== 13) {
                return ;
            }
            if (root.eisLogin.password.val() === '') {
                root.eisLogin.showError('请输入密码');
                return;
            }
            if (root.eisLogin.username.val() === '') {
                root.eisLogin.username.focus();
                return;
            }
            root.eisLogin.loginBtn.click();
        },
        login : function () {
            var result = root.eisLogin.Utils.validator();
            if (!result.success) {
                return;
            }
            if (root.eisLogin.loginConfig.connectPassport
                && result.username !== 'super') {
                //pass
                if (root.eisLogin.passportUsername.val() === ""
                    || root.eisLogin.loginModel.val() !== "1") {
                    root.eisLogin.passportUsername.val(result.username);
                }
                root.eisLogin.passportPassword.val(result.password);
                root.eisLogin.passportVerifyCode.val(result.verifyCode);

                var pf = {};
                pf.loginMode = $('#loginMode').val();
                pf.verifyCode1 = result.verifyCode;
                pf.username = $('#username').val();
                pf.password = result.password;
                pf.multiAccount = 1;
                pf.multiAccountByOneMatch=0;

                login.doLogin(root.eisLogin.loginConfig.passportUrl, root.eisLogin.loginConfig.serverId, root.eisLogin.loginConfig.indexUrl, root.eisLogin.loginConfig.root, pf);
                return;
            }
            $.ajax({
                url:root.eisLogin.loginConfig.eisLoginUrl,
                data:{"username":result.username,"password":result.password},
                type:'post',
                success:function(data){
                    var jsonO = data;
                    if(jsonO.success){
                        if($("input[name=cookieSaveType]").is(":checked")){
                            var cookieExpires = new Date();
                            cookieExpires.setTime(cookieExpires.getTime() + (1000 * 60 * 60 * 24 * Constants.LOGIN_SINGLE_COOKIE_LIFE));
                            $.cookies.set(Constants.LOGIN_SINGLE_COOKIE_USERNAME_KEY, result.username, {
                                expiresAt : cookieExpires
                            });
                            $.cookies.set(Constants.LOGIN_SINGLE_COOKIE_PASSWORD_KEY, result.password, {
                                expiresAt : cookieExpires
                            });
                        }
                        else{
                            $.cookies.del(Constants.LOGIN_SINGLE_COOKIE_USERNAME_KEY);
                            $.cookies.del(Constants.LOGIN_SINGLE_COOKIE_PASSWORD_KEY);
                        }
                        root.eisLogin.showMsg(jsonO.msg);
                        location.href = root.eisLogin.loginConfig.indexUrl;
                    }else{
                        root.eisLogin.showError(jsonO.msg)
                    }
                },
                error : function(XMLHttpRequest, textStatus, errorThrown) {
                    root.eisLogin.showMsg("登录失败，请联系系统管理员, statusCode:" + textStatus)
                }
            });
        }
    };

    root.eisLogin = new EisLogin();

}).call(this);
