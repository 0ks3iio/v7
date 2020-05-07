<form id="user-password-form">
    <div class="form-horizontal mt20">
        <div class="form-group">
            <label class="col-sm-2 control-label no-padding-right">原密码：</label>
            <div class="col-sm-3">
                <input id="password" name="password" type="password" class="form-control">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label no-padding-right">新密码：</label>
            <div class="col-sm-3">
                <input name="newPassword1" id="newPassword1" type="password" class="form-control">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label no-padding-right">确认密码：</label>
            <div class="col-sm-3">
                <input name="newPassword2" id="newPassword2" type="password" class="form-control">
            </div>

        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label no-padding-right"></label>
            <div class="col-sm-8">
                <button class="btn btn-blue" type="button" id="user-password-save">确定</button>
            </div>
        </div>
    </div>
</form>
<script>
    $(function () {
        $('#user-password-save').click(function () {
            var validator = $('#user-password-form').data('bootstrapValidator');
            validator.validate();
            if (!validator.isValid()) {
                return;
            }

            $.ajax({
                url: _contextPath + '/operation/setting/password',
                type: 'POST',
                data: $('#user-password-form').serialize(),
                success: function (res) {
                    if (res.success) {
                        message.successMessage(res.message);
                    } else {
                        message.errorMessage(res.message);
                    }
                }
            })
        });

        $('#user-password-form').bootstrapValidator({
            feedbackIcons: {
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            autoFocus: true,
            fields: {
                "password": {
                    validators: {
                        notEmpty: {
                            message: "原密码不能为空"
                        },
                        remote: {
                            url: _contextPath + '/operation/setting/checkPassword',
                            data: {"password":$('#password').val()},
                            type: 'POST'
                        }
                    }
                },
                "newPassword1": {
                    validators: {
                        notEmpty: {
                            message: "新密码不能为空"
                        },
                        callBack: {
                            callBack: function (value, validator, $field) {
                                validator.validateField('newPassword2');
                                return true;
                            }
                        }
                    }
                },
                "newPassword2": {
                    validators: {
                        notEmpty: {
                            message: "新密码不能为空"
                        },
                        identical: {
                            field: 'newPassword1',
                            message: "密码不一致"
                        }
                    }
                }
            }
        });
    })
</script>