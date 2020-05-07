<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta charset="utf-8"/>
    <title>开发者-注册</title>

    <meta name="description" content=""/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0"/>

    <!-- bootstrap & fontawesome -->
    <link rel="stylesheet" type="text/css"
          href="${springMacroRequestContext.contextPath}/bigdata/v3/static/css/bootstrap.css"/>
    <link rel="stylesheet"
          href="${springMacroRequestContext.contextPath}/bigdata/v3/static/font-awsome/css/font-awesome.css"/>

    <link rel="stylesheet" type="text/css"
          href="${springMacroRequestContext.contextPath}/bigdata/v3/static/css/all.css"/>
    <link rel="stylesheet" type="text/css"
          href="${springMacroRequestContext.contextPath}/bigdata/v3/static/css/style.css"/>
    <!--[if lte IE 9]>
    <link rel="stylesheet" href="${springMacroRequestContext.contextPath}/static/css/pages-ie.css"/>
    <![endif]-->
    <!-- inline styles related to this page -->

    <!-- HTML5shiv and Respond.js for IE8 to support HTML5 elements and media queries -->

    <!--[if lte IE 8]>
    <script src="${request.contextPath}/static/components/html5shiv/dist/html5shiv.min.js" async="async"
            defer="defer"></script>
    <script src="${request.contextPath}/static/components/respond/dest/respond.min.js" async="async"
            defer="defer"></script>
    <![endif]-->
</head>

<body>
<!--总体 E-->
<div class="xj-box">
    <div class="dv-reg-header">
        <div>开发者注册</div>
        <span onclick="javascript: location.href='${request.contextPath}/bigdata/api/index';">登录</span>
    </div>

    <div class="content dv-reg-content">
        <div class="container">
            <h1 class="dv-reg-title">注册开发者账号</h1>
            <form name="developer" id="developer" class="form-horizontal">
                <div class="form-group">
                    <label class="col-sm-4 control-title no-padding dv-reg-bold" for="form-field-1">基本信息</label>
                </div>
                <div class="form-group">
                    <label class="col-sm-4 control-label no-padding" for="form-field-1"><span
                                class="must">*</span>用户名</label>

                    <div class="col-sm-4">
                        <input name="username" type="text" value="" placeholder="3-16个字符组成，区分大小写，注册成功后不可修改"
                               placemsg="3-16个字符组成，区分大小写，注册成功后不可修改" minLength="3" maxLength="16"
                               class="form-control yz">
                    </div>
                    <span class="form-tips-danger col-sm-4 form-tips hideblock hide" id="userNameError">
                    </span>
                </div>

                <div class="form-group">
                    <label class="col-sm-4 control-label no-padding" for="form-field-2"><span
                                class="must">*</span>企业名称</label>
                    <div class="col-sm-4">
                        <input name="unitName" type="text" value="" placeholder="2-30个中文字符，填写企业名称"
                               placemsg="2-30个中文字符，填写企业名称或个人姓名" minLength="4" maxLength="60" class="form-control yz">
                    </div>
                    <span class="form-tips-danger col-sm-4 form-tips hideblock hide" id="unitNameError">
                    </span>
                </div>
                <div class="form-group">
                    <label class="col-sm-4 control-label no-padding" for="form-field-1"><span
                                class="must">*</span>密码</label>

                    <div class="col-sm-4">
                        <input id="password" name="password" type="text" onfocus="this.type='password'" value=""
                               placeholder="6-16个字母或数字" placemsg="6-16个字母或数字" minLength="6" maxLength="16"
                               class="form-control yz">
                    </div>
                    <span class="form-tips-danger col-sm-4 form-tips hideblock hide" id="passwordError">
                    </span>
                </div>
                <div class="form-group">
                    <label class="col-sm-4 control-title no-padding dv-reg-bold" for="form-field-1">联系方式</label>

                </div>
                <div class="form-group">
                    <label class="col-sm-4 control-label no-padding" for="form-field-3"><span class="must">*</span>联系人姓名</label>

                    <div class="col-sm-4">
                        <input name="realName" type="text" value="" placeholder="2-20个中文字符" placemsg="2-20个中文字符"
                               minLength="4" maxLength="40" class="form-control yz">
                    </div>
                    <span class="form-tips-danger col-sm-4 form-tips hideblock hide" id="realNameError">
                    </span>
                </div>
                <div class="form-group">
                    <label class="col-sm-4 control-label no-padding" for="form-field-4"><span class="must">*</span>联系人地址</label>

                    <div class="col-sm-4">
                        <input name="address" type="text" value="" placeholder="1-40个中文字符，请输入详细地址"
                               placemsg="1-40个中文字符，请输入详细地址" minLength="2" maxLength="80" class="form-control yz">
                    </div>
                    <span class="form-tips-danger col-sm-4 form-tips hideblock hide">
                    </span>
                </div>
                <div class="form-group">
                    <label class="col-sm-4 control-label no-padding" for="form-field-5"><span class="must">*</span>手机号码</label>

                    <div class="col-sm-4">
                        <input name="mobilePhone" type="text" value="" placeholder="填写11位数字号码" placemsg="手机号格式不正确"
                               class="form-control yz">
                    </div>
                    <span class="form-tips-danger col-sm-4 form-tips hideblock hide" id="mobileError">
                    </span>
                </div>
                <div class="form-group">
                    <label class="col-sm-4 control-label no-padding" for="email"><span class="must">*</span>邮箱</label>

                    <div class="col-sm-4">
                        <input maxlength="40" id="email" name="email" type="text"
                               class="typeahead scrollable form-control yz" placeholder="请填写有效邮箱以便接收平台下发消息。"
                               placemsg="请填写有效邮箱以便接收平台下发消息。" autocomplete="off" data-provide="typeahead">
                    </div>
                    <span id="emailErrorDiv" class="form-tips-danger col-sm-4 form-tips hideblock hide">
                    </span>
                </div>
                <!--  
                <div class="form-group">
                    <label class="col-sm-4 control-label no-padding" for="form-field-7">官网地址</label>

                    <div class="col-sm-4">
                        <input id="homepage" name="homepage" type="text" value="" class="form-control yz">
                    </div>
                    <span class="form-tips-danger col-sm-4 form-tips hideblock hide">
                    </span>
                </div>
                -->
                <div class="form-group">
                    <label for="" class="sr-only">验证码</label>
                    <label class="col-sm-4 control-label no-padding" for="form-field-9"><span
                                class="must">*</span>验证码</label>

                    <div class="col-sm-2">
                        <input maxlength="4" name="verify" id="verify" type="text" value="" placeholder="填写验证码" placemsg="填写验证码"
                               class="form-control yz">
                    </div>
                    <div class="col-sm-2">
                        <a href="javascript:;" class="verCode-img"><img id="verCode"
                                                                        src="${request.contextPath}/bigdata/api/verify-code/get?key=${key}"
                                                                        alt=""></a>
                        <a href="javascript:;" id="refreshVerifyCode" class="verCode-change">点击换一张</a>
                    </div>
                    <span class="form-tips-danger col-sm-4 form-tips hideblock hide">
                    </span>
                </div>
                <div class="form-group">
                    <label class="col-sm-4 control-label no-padding" for="form-field-1"></label>
                    <div class="col-sm-4 clearfix">
                        <label class="inline pull-left">
                            <input id="checkBox" type="checkbox" class="wp" checked/>
                            <span class="lbl no-margin"> 我已阅读并同意</span>
                        </label>
                        <a class="pull-left" target="_blank" href="${request.contextPath}/bigdata/api/openapiSup/home/register-agreement">《开发者服务条款》</a>
                    </div>
                    <span class="form-tips-danger col-sm-4 form-tips hideblock hide" id="check-error">
                    </span>
                </div>
                <div class="form-group">
                    <label class="col-sm-4 control-label no-padding" for="form-field-1"></label>
                    <div class="col-sm-4">
<#--                        <input type="button" id="save" class="btn btn-block btn-blue" value="同意并注册开发者"/>-->
                        <button id="save" type="button" class="btn btn-block btn-blue">同意并注册开发者</button>
                    </div>
                </div>
            </form>
        </div>
    </div>

    <div class="dv-reg-footer">
        <span>Copyright ©2013-2017   浙江万朋教育科技股份有限公司版权所有   备案号:浙ICP备05070430号 </span>
    </div>
    <!-- basic scripts -->
</div>
<script src="${springMacroRequestContext.contextPath}/bigdata/v3/static/js/jquery/jquery.min.js" type="text/javascript" charset="utf-8"></script>
<script src="${springMacroRequestContext.contextPath}/bigdata/v3/static/js/bootstrap/bootstrap.min.js" type="text/javascript"></script>

<script src="${springMacroRequestContext.contextPath}/bigdata/v3/static/assets/js/jquery-mail.auto.js"></script>
<script src="${request.contextPath}/bigdata/v3/static/datav/layer/layer.js"></script>

<script>

    $(function () {
        //邮箱自动填充
        //$('#email').mailAuto({
        //    emails: ['qq.com', '163.com', '126.com', 'sina.com', 'sohu.com', 'yahoo.cn', 'gmail.com', 'hotmail.com', 'live.cn']
        //});
    });

    $('#checkBox').click(function () {
        if ($('#checkBox').prop("checked") == true) {
            $('#check-error').html('').addClass('hide');
        }
    });
    var submit = false;
    $('#save').click(function () {
        $('.yz').trigger('blur');
        var $username = $('#username');
        var isOk = validateUsername($username, $username.val(), $username.parent().next());
        if (!isOk) {
            return ;
        }
        if ($('#checkBox').prop("checked") == false) {
            promot($('#check-error'), 3, "请阅读条款");
            return false;
        }
        var len = $('.form-tips-danger.hide').length;
        if (submit) {
            return ;
        }
        if (len == 9) {
            submit = true;
            $.post('${request.contextPath}/bigdata/api/openapiSup/home//regist/save/page', $('#developer').serialize(), function (data) {
                var jsonO = JSON.parse(data);
                if (jsonO.success) {
                    layer.alert('注册成功，前往登录页', {
                        //skin: 'layui-layer-molv' //样式类名
                        closeBtn: 0
                    }, function(){
                        location.href = '${request.contextPath}/bigdata/api/index';
                    });
                } else {
                    submit = false;
                    alert("网络异常");
                }
            });
        } else {
            return false;
        }
    });
    $("#refreshVerifyCode").bind("click", function () {
        var srcUrl = $('#verCode').attr("src");
        $('#verCode').attr("src", srcUrl + "&date=" + new Date().getTime());
    });
    $('.yz').blur(function () {
        var $this = $(this);

        var name = $this.attr("name");
        var val = $this.val();
        var isVal = (val && val != "");
        var ts = $this.parent().next();
        // promot(ts, null, '');
        if (isVal == false && "verify" != name ) {
            if ("email" == name) {
                ts = $('#emailErrorDiv');
            }
            promot(ts, 3, $this.attr('placemsg'));
            return false;
        }
        <!--
        if ("homepage" == name && val == "") {
            ts.empty();
        }
        -->
        if (name && name != "") {
            if (name == "username") {
                validateUsername($this, val, ts);
            } else if (name == 'unitName') {
                if (isLen($this, val)) {
                    promot(ts, 1, '');
                } else {
                    promot(ts, 3, $this.attr('placemsg'));
                }
            } else if (name == 'password') {
                if (isPsw(val) && isLen($this, val)) {
                    promot(ts, 1, '');
                } else {
                    promot(ts, 3, $this.attr('placemsg'));
                }
            } else if (name == 'realName') {
                if (isLen($this, val)) {
                    promot(ts, 1, '');
                } else {
                    promot(ts, 3, $this.attr('placemsg'));
                }
            } else if (name == 'address') {
                if (isLen($this, val)) {
                    promot(ts, 1, '');
                } else {
                    promot(ts, 3, $this.attr('placemsg'));
                }
            } else if (name == 'mobilePhone') {
                if (!/^\d+$/.test(val) || val.length != 11) {
                    promot(ts, 3, $this.attr('placemsg'));
                } else {
                    promot(ts, 1, '');
                }
            } else if (name == 'email') {
                ts = $('#emailErrorDiv');
                if (!/^[a-zA-Z0-9.!#$%&'*+\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/.test(val)) {
                    promot(ts, 3, $this.attr('placemsg'));
                } else {
                    promot(ts, 1, '');
                }
            } 
            <!-- 
            else if (name == 'homepage' && isVal != false) {
                if (getLength(val) > 180) {
                    promot(ts, 3, '不能超过180个字符');
                } else {
                    promot(ts, 1, '');
                }
            }
            -->
            else if (name == 'verify') {
                if (isVal == false) {
                    promot(ts.next(), 3, $this.attr('placemsg'));
                } else {
                    $.ajax({
                        url: '${request.contextPath}/bigdata/api/verify-code/validate?key=${key}&verifyCode=' + val,
                        type: 'GET',
                        dataType: 'json',
                        async: false,
                        success: function (msg) {
                            if (msg.success) {
                                promot(ts.next(), 1, '');
                            } else {
                                promot(ts.next(), 2, '验证码错误');
                                var srcUrl = $('#verCode').attr("src");
                                $('#verCode').attr("src", srcUrl + "&date=" + new Date());
                                $this.val('');
                            }
                        }
                    });
                }
            }
        }
    });

    function validateUsername($this, val, ts) {
        var t = isStr(val);
        var l = isLen($this, val);
        if (isStr(val) && isLen($this, val)) {
            var isOk = false;
            $.ajax({
                url: '${request.contextPath}/bigdata/api/openapiSup/home/validate-username?username=' + val,
                type: 'GET',
                dataType: 'json',
                async: false,
                success: function (msg) {
                    if (msg.success) {
                        isOk = true;
                        promot(ts, 1, '');
                    } else {
                        isOk = false;
                        promot(ts, 2, '用户已存在');
                    }
                }
            });
            return isOk;
        } else {
            promot(ts, 3, $this.attr('placemsg'));
            return false;
        }
    }

    function promot($this, level, msg) {
        if ($.trim(msg) !== '') {
            $this.text(msg).removeClass('hide');
        } else {
            $this.addClass('hide');
        }
    }

    function isStr(value) {
        var reg = new RegExp("[\\u4E00-\\u9FFF]+", "g");
        return reg.test(value) ? false : true;
    };

    function isPsw(value) {
        var reg = /^[0-9a-zA-Z]*$/g;
        return reg.test(value);
    }

    function isLen($this, value) {
        var maxLength = $this.attr("maxLength");
        if (maxLength && maxLength > 0 && getLength(value) > maxLength) {
            return false;
        }
        var minLength = $this.attr("minLength");
        if (minLength && minLength > 0 && getLength(value) < minLength) {
            return false;
        }
        return true;
    };

    function getLength(str) {
        if (str == null) return 0;
        if (typeof str != "string") {
            str += "";
        }
        return str.replace(/[^\x00-\xff]/g, "01").length;
    };
</script>
</body>
</html>
