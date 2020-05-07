<form id="user-info-form">
    <div class="form-horizontal mt20">
        <div class="form-group">
            <input type="hidden" name="id" value="${opUser.id!}"/>
            <label class="col-sm-2 control-label no-padding-right">姓名：</label>
            <div class="col-sm-3">
                <input id="realName" value="${opUser.realName!}" name="realName" type="text" class="form-control">
            </div>
            <label class="col-sm-2 control-label no-padding-right">性别：</label>
            <div class="col-sm-3">
                <label class="inline">
                    <input value="1" type="radio" class="wp state state-1" name="sex"
                               <#if opUser.sex?default(1)==1>checked="checked"</#if>>
                    <span class="lbl"> 男</span>
                </label>
                <label class="inline">
                    <input value="2" type="radio" class="wp state state-0" name="sex"
                               <#if opUser.sex?default(1)==2>checked="checked"</#if>>
                    <span class="lbl"> 女</span>
                </label>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label no-padding-right">手机号：</label>
            <div class="col-sm-3">
                <input name="mobilePhone" value="${opUser.phone!}" type="text" class="form-control">
            </div>
            <label class="col-sm-2 control-label no-padding-right">邮箱：</label>
            <div class="col-sm-3">
                <input name="email" value="${opUser.email!}" type="text" class="form-control">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label no-padding-right"></label>
            <div class="col-sm-8">
                <button class="btn btn-blue" type="button" id="user-info-save">确定</button>
            </div>
        </div>
    </div>
</form>
<script>
    $(function () {

        $('#user-info-save').click(function () {
            var validator = $('#user-info-form').data('bootstrapValidator');
            validator.validate();
            if (!validator.isValid()) {
                return;
            }

            $.ajax({
                url: _contextPath + '/operation/setting/info',
                type: 'POST',
                data: $('#user-info-form').serialize(),
                success: function (res) {
                    if (res.success) {
                        message.successMessage(res.message);
                    } else {
                        message.errorMessage(res.message);
                    }
                }
            })
        });

        $('#user-info-form').bootstrapValidator({
            feedbackIcons: {
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            autoFocus: true,
            fields: {
                "realName": {
                    validators: {
                        notEmpty: {
                            message: "姓名不能为空"
                        },
                        chineseStringLength: {
                            max: 64,
                            message: "姓名过长 32个汉字或64个英文字符"
                        }
                    }
                },
                "mobilePhone": {
                    validators: {
                        notEmpty: {
                            message: "手机号不能为空",
                        },
                        chineseStringLength: {
                            max: 11,
                            message: "手机号长度只能为11位"
                        },
                        chineseMobile: {}
                    }
                },
                "email": {
                    validators: {
                        notEmpty: {
                            message: "邮箱不能为空"
                        },
                        emailAddress: {
                            message: "邮箱格式不合法",
                            multiple: false
                        },
                        chineseStringLength: {
                            max: 32,
                            message: "邮箱长度不能超过32个字符"
                        }
                    }
                }
            }
        });
    })
</script>