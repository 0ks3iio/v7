<#import "../../macro/R.ftl" as r />
<form id="management-user-form">
<div class="form-horizontal mt20">
    <div class="form-group">
        <label class="col-sm-2 control-title">
            <#if opUser.id??>
            编辑用户
            <#else >
            新增用户
            </#if>
        </label>
    </div>
    <div class="form-group">
        <input type="hidden" name="user.id" value="${opUser.id!}" />
        <label class="col-sm-2 control-label no-padding-right">姓名：</label>
        <div class="col-sm-3">
            <input id="realName" value="${opUser.realName!}" name="user.realName" type="text" class="form-control">
        </div>
        <label class="col-sm-2 control-label no-padding-right">账号：</label>
        <div class="col-sm-3">
            <input id="username" <#if opUser.id??>disabled</#if> value="${opUser.username!}" name="user.username" type="text" class="form-control">
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-2 control-label no-padding-right">手机号：</label>
        <div class="col-sm-3">
            <input name="user.phone" value="${opUser.phone!}" type="text" class="form-control">
        </div>
        <label class="col-sm-2 control-label no-padding-right">邮箱：</label>
        <div class="col-sm-3">
            <input name="user.email" value="${opUser.email!}" type="text" class="form-control">
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-2 control-label no-padding-right">性别：</label>
        <div class="col-sm-3">
            <label class="inline">
                <input value="1" type="radio" class="wp state state-1" name="user.sex"
                               <#if opUser.sex?default(1)==1>checked="checked"</#if>>
                <span class="lbl"> 男</span>
            </label>
            <label class="inline">
                <input value="2" type="radio" class="wp state state-0" name="user.sex"
                               <#if opUser.sex?default(1)==2>checked="checked"</#if>>
                <span class="lbl"> 女</span>
            </label>
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-2 control-label no-padding-right">运营组：</label>
        <div class="col-sm-8">
            <div class="clearfix">
                <div class="tree-wrap">
                    <table class="table table-striped table-hover no-margin">
                        <thead>
                        <tr>
                            <th>所有运营组<span class="badge badge-blue float-right" id="group-counter">${authGroupSize}</span></th>
                        </tr>
                        </thead>
                    </table>
                    <div class="tree-list" style="height:344px;margin-top: 0;">
                        <#if groups?? && groups?size gt 0>
                            <#list groups as group>
                            <a href="javascript:void(0);" class="groups" data-id="${group.id}">
                                <label class="ellipsis mt4">
                                    <input name="groups[${group_index}]" type="checkbox" class="wp groups-check" value="${group.id}" <#if group.hasAuth>checked="checked"</#if> >
                                    <span class="lbl">${group.name}</span>
                                </label>
                            </a>
                            </#list>
                        </#if>
                    </div>
                </div>
                <div style="margin-left: 200px;">
                    <table class="table table-bordered table-striped table-hover no-margin">
                        <thead>
                        <tr>
                            <th>拥有权限</th>
                        </tr>
                        </thead>
                    </table>
                    <div style="border-right: 1px solid #ddd;border-bottom: 1px solid #ddd;" id="groups-auth-container">

                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-2 control-label no-padding-right">自定义授权：</label>
        <div class="col-sm-8">
            <div class="filter">
                <div class="filter-item">
                    <div class="filter-content">
                        <label class="no-margin-top">
                            <input type="radio" value="000000" class="wp area" name="area" <#if opUser.authRegionCode?default('000000')=='000000'>checked="checked"</#if> >
                            <span class="lbl"> 所有地区</span>
                        </label>
                        <label class="no-margin-top">
                            <input type="radio" class="wp area" name="area" <#if opUser.authRegionCode?default('000000')!='000000'>checked="checked"</#if> >
                            <span class="lbl"> 自定义</span>
                        </label>
                        <div class="float-right <#if opUser.authRegionCode?default('000000')=='000000'>hide</#if>" style="width: 40%;">
                            <@r.region dataType='management' nestedId='regionCode' regionCode='${opUser.authRegionCode!}' call='changeRegionCode' >
                                <input name="user.regionCode" type="hidden" id="regionCode" value="${opUser.authRegionCode!}" />
                            </@r.region>
                        </div>
                    </div>
                </div>
            </div>
            <div class="page-sidebar chosen-tree-outter" style="height: 317px;overflow: auto;border: 1px solid #ddd;">
                <div class="page-sidebar-body chosen-tree-inner">
                    <ul class="chosen-tree chosen-tree-tier1">
                        <#if modules?? && modules?size gt 0>
                            <#list modules as module>
                            <li class="sub-tree">
                                <div class="chosen-tree-item" data-index="1">
                                    <span class="arrow"></span>
                                    <label class="name no-margin-top">
                                        <input type="checkbox" class="wp module-item" value="${module.moduleId}"
                                               name="modules[${module_index}].moduleId" <#if module.hasAuth>checked="checked"</#if>>
                                        <span class="lbl">${module.moduleName}</span>
                                    </label>

                                </div>
                                <ul class="chosen-tree chosen-tree-tier2">
                                    <#if module.operates?? && module.operates?size gt 0>
                                        <#list module.operates as operate>
                                            <li>
                                                <div class="chosen-tree-item" data-index="10">
                                                    <span class="arrow"></span>
                                                    <label class="name no-margin-top">
                                                        <input data-module="${module.moduleId}" type="checkbox"
                                                               class="wp operate" value="${operate.id}"
                                                               name="modules[${module_index}].operates[${operate_index}].id"
                                                                <#if operate.hasAuth>checked="checked"</#if>>
                                                        <span class="lbl"> ${operate.operateName}</span>
                                                    </label>
                                                </div>
                                            </li>
                                        </#list>
                                    </#if>
                                </ul>
                            </li>
                            </#list>
                        </#if>
                    </ul>
                </div>
            </div>
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-2 control-label no-padding-right"></label>
        <div class="col-sm-8">
            <button class="btn btn-blue" type="button" id="group-user-save">确定</button>
            <a class="btn btn-default" href="#/operation/management/user">取消</a>
        </div>
    </div>
</div>
</form>
<script>
    $(function () {
        $('.page-sidebar').szxyTree();

        //切换地区选择
        $('.area').change(function () {
            if ($(this).val()==='000000') {
                $(this).parent().next().next().addClass('hide');
                $('#regionCode').val('');
                $('#regionCode').attr('disabled', true)
            } else {
                $(this).parent().next().removeClass('hide');
                $('#regionCode').val('');
                $('#regionCode').attr('disabled', false)
            }
            $('#management-user-form').data("bootstrapValidator").updateStatus('user.regionCode', 'STATUS_NOT_VALIDATED', null);
        });

        $('.module-item').change(function () {
            var moduleId = $(this).val();
            if ($(this).is(':checked')) {
                $('input[data-module="' + moduleId + '"]').prop('checked', true);
            } else {
                $('input[data-module="' + moduleId + '"]').prop('checked', false);
            }
        });
        $('.operate').change(function () {
            //check operate selected size
            var moduelId = $(this).data('module');
            var selectedOperateSize = $('input[data-module="' + moduelId + '"]:checked').length;
            if (selectedOperateSize === 0) {
                $('input[value="' + moduelId + '"]').prop('checked', false);
            } else {
                $('input[value="' + moduelId + '"]').prop('checked', true);
            }
        });
        $('.groups').click(function (e) {
            var id = $(this).data('id');
            $(this).addClass('active').siblings('a').removeClass('active');
            $('#groups-auth-container').loading(_contextPath + '/operation/management/group/' + id + '/viewAuth');
        });

        $('.groups-check').on('change', function () {
            var count = parseInt($('#group-counter').text());
            if ($(this).is(":checked")) {
                count ++;
            } else {
                count --;
            }
            $('#group-counter').text(count);
        });

        $('#management-user-form').bootstrapValidator({
            excluded:[":disabled"],
            feedbackIcons: {
                valid: 'glyphicon /*glyphicon-ok*/',
                invalid: 'glyphicon /*glyphicon-remove*/',
                validating: 'glyphicon /*glyphicon-refresh*/'
            },
            autoFocus: true,
            fields: {
                "user.realName": {
                    validators: {
                        notEmpty: {
                            message: "姓名不能为空"
                        },
                        chineseStringLength: {
                            max: 64,
                            message: "姓名过长 32个汉字或64个英文字符"
                        },
                        <#if !opUser.id??>
                        callback: {
                            callback: function (value, validator, $field) {
                                if ($.trim(value) !== '') {
                                    $.get(_contextPath + '/operation/management/user/generate-username?realName=' + value, function (res) {
                                        if (typeof res.message !== 'undefined') {
                                            message.warnMessage(res.message);
                                        }
                                        $('#username').val(res.username);
                                        validator.updateStatus('user.username', validator.STATUS_NOT_VALIDATED, null);
                                        validator.validateField($field);
                                    });
                                }
                                return true;
                            }
                        }
                        </#if>
                    }
                },
                <#if !opUser.id??>
                "user.username": {
                    validators: {
                        notEmpty: {
                            message: "用户名不能为空"
                        },
                        regexp:{
                            regexp: /[a-zA-Z]|[0-9]{64}/,
                            message: "用户名不合法 只能为数字和字母"
                        },
                        stringLength: {
                            max: 64,
                            message: "用户名过长 最大为64个字符"
                        },
                        remote: {
                            url: _contextPath + '/operation/management/user/checkUsername',
                            type: 'GET',
                            name: "username"
                        }
                    }
                },
                </#if>
                "user.phone": {
                    validators: {
                        notEmpty: {
                            message: "手机号不能为空",
                        },
                        chineseStringLength: {
                            max: 11,
                            message: "手机号长度只能为11位"
                        },
                        regexp: {
                            regexp: /^1((3[0-9]|4[579]|5[0-35-9]|6[6]|7[135-8]|8[0-9]|9[198])\d{8}|(70[0-9])\d{7})$/,
                            message: "不合法的手机号"
                        }
                    }
                },
                "user.email": {
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
                },
                "user.regionCode": {
                    validators: {
                        notEmpty: {
                            message: "行政区划不能为空"
                        }
                    }
                }
            }
        });

        var submit = false;
        $('#group-user-save').click(function () {
            var validator = $('#management-user-form').data('bootstrapValidator');
            validator.validate();
            if (validator.isValid()) {
                submit = true;
                $.ajax({
                    url: _contextPath + '/operation/management/user/save',
                    type: 'POST',
                    data: $('#management-user-form').serialize(),
                    success: function (res) {
                        if (res.success) {
                            message.successMessage(res.message, function(){
                                routeUtils.go('/operation/management/user');
                            })
                        } else {
                            message.errorMessage(res.message)
                            submit = false;
                        }
                    },
                    error: function () {
                        submit = false;
                    }
                })
            }
        });




        //init
        $('.groups:eq(0)').click();
    });

    function changeRegionCode(nestId) {
        var validator = $('#management-user-form').data('bootstrapValidator');
        validator.updateStatus('user.regionCode', validator.STATUS_NOT_VALIDATED, null);
        validator.validateField('user.regionCode');
    }
</script>