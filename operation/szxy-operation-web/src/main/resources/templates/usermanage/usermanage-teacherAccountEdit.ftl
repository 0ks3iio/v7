<form id="user-edit-form">
    <input type="hidden" name="id" value="${teacherAccount.id}" id="teacherAccountId"/>
    <div class="layer-content">
        <div class="form-horizontal" role="form">
            <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right"><span
                        class="color-red">*</span>排序号：</label>
                <div class="col-sm-4">
                    <div class="mm">
                        <input id="orderNumber" type="number" min="0" name="orderNumber"
                               value="${teacherAccount.orderNumber?default(0)}" class="width100-105">
                        <span class="plus-minus js-click">
                            <button type="button" class="btn btn-default" onclick="addNumber('orderNumber');">
                                <i class="arrow fa fa-angle-up"></i>
                            </button>
                            <button type="button" class="btn btn-default" onclick="delNumber('orderNumber');">
                                <i class="arrow fa fa-angle-down"></i>
                            </button>
                        </span>
                    </div>
                </div>
                <label class="col-sm-2 control-label no-padding-right">账户：</label>
                <div class="col-sm-4">
                    <input id="username" type="text" value="${teacherAccount.username}" name="username"
                           class="form-control radius-4"
                           disabled="disabled">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right">创建日期：</label>
                <div class="col-sm-4 mt7" id="creationTime">${teacherAccount.creationTime}</div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right">有效日期：</label>
                <div class="col-sm-10">
                    <label class="float-left">
                        <input type="radio" class="wp expire-time" id="noExpireTime" name="expireTimeType"
                               <#if !teacherAccount.expireTime??>checked="checked"</#if> >
                        <span class="lbl"> 永久</span>
                    </label>
                    <label class="float-left">
                        <input type="radio" class="wp expire-time" id="hasExpireTime" name="expireTimeType"
                               <#if teacherAccount.expireTime??>checked="checked"</#if>>
                        <span class="lbl"> 自定义</span>
                    </label>

                    <div class="input-group float-left <#if !teacherAccount.expireTime??>hide</#if>"
                         style="width: 40%;">
                        <input id="user-edit-expireTime" name="expireDate" class="form-control date-picker" type="text"
                               value="${teacherAccount.expireTime!}" <#if !teacherAccount.expireTime??>disabled</#if>>
                        <span class="input-group-addon">
                            <i class="fa fa-calendar"></i>
                        </span>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right"><span
                        class="color-red">*</span>用户状态：</label>
                <div class="col-sm-10">
                    <label class="inline">
                        <input value="1" type="radio" class="wp state state-1" name="userState"
                               <#if teacherAccount.userState==1>checked="checked"</#if>>
                        <span class="lbl"> 正常</span>
                    </label>
                    <label class="inline">
                        <input value="0" type="radio" class="wp state state-0" name="userState"
                               <#if teacherAccount.userState==0>checked="checked"</#if>>
                        <span class="lbl"> 未审核</span>
                    </label>
                    <label class="inline">
                        <input value="2" type="radio" class="wp state state-2" name="userState"
                               <#if teacherAccount.userState==2>checked="checked"</#if>>
                        <span class="lbl"> 锁定</span>
                    </label>
                    <label class="inline">
                        <input value="3" type="radio" class="wp state state-3" name="userState"
                               <#if teacherAccount.userState==3>checked="checked"</#if>>
                        <span class="lbl"> 注销</span>
                    </label>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right">登录密码：</label>
                <div class="col-sm-10 mt7" id="password">
                ${teacherAccount.password}
                </div>
            </div>
        </div>
    </div>
</form>
<script>
    $(function () {
        var setting = {
            language: 'zh-CN',
            autoclose: true,
            todayHighlight: true,
            format: 'yyyy-mm-dd',
            startDate: new Date(),
        };
        if (parseInt('${unitExpireTime?default(0)}') >0)
        {
            setting.endDate = new Date(${unitExpireTime?default(0)});
        }
        $('.date-picker').datepicker(setting).next().on('click', function () {
            $(this).prev().focus();
        });
        $('.expire-time').click(function () {
            var validator = $('#user-edit-form').data('bootstrapValidator');
            if ($(this).attr('id') == 'hasExpireTime') {
                $(this).parent().next().removeClass('hide').children('input').attr('disabled', false);
                validator.validateField('expireDate')
            } else {
                validator.updateStatus('expireDate', validator.STATUS_NOT_VALIDATED, null);
                $(this).parent().next().next().addClass('hide').children('input').attr('disabled', true);
                validator.validateField('expireDate');
            }
        });
        $('#user-edit-expireTime').on('change', function () {
            var validator = $('#user-edit-form').data('bootstrapValidator');
            validator.updateStatus('expireDate', 'NOT_VALIDATED', null);
            validator.validateField('expireDate');
        });

        $('#user-edit-form').bootstrapValidator({
            excluded: [":disabled"],
            feedbackIcons: {
                valid: 'glyphicon /*glyphicon-ok*/',
                invalid: 'glyphicon /*glyphicon-remove*/',
                validating: 'glyphicon /*glyphicon-refresh*/'
            },
            autoFocus: true,
            fields: {
                "orderNumber": {
                    validators: {
                        notEmpty: {
                            message: "排序号不能为空"
                        },
                        number: {
                            message: "排序号只能为数字"
                        }
                    }
                },
                "expireDate": {
                    validators: {
                        notEmpty: {
                            message: "有效日期不能为空"
                        }
                    }
                }
            }
        });
    })
    function addNumber(id) {
        var number = parseInt($('#'+id).val());
        $('#'+id).val(number + 1);
    }

    function delNumber(id) {
        var number = parseInt($('#'+id).val());
        if (number > 0) {
            $('#' + id).val(number - 1);
        }
    }
</script>