<#import "../../macro/R.ftl" as r />
<form id="group-origin-form">
<div class="form-horizontal mt20" id="group-form">
    <div class="form-group">
        <label class="col-sm-2 control-title">新增组</label>
    </div>
    <div class="form-group">
        <label class="col-sm-2 control-label no-padding-right">组名：</label>
        <div class="col-sm-3"><input id="groupName" name="name" type="text" class="form-control"></div>
    </div>
    <div class="form-group">
        <label class="col-sm-2 control-label no-padding-right">负责区块：</label>
        <div class="col-sm-6">
            <label class="float-left">
                <input type="radio" class="wp area" value="000000" checked="checked" name="regionName">
                <span class="lbl"> 所有地区</span>
            </label>
            <label class="float-left">
                <input type="radio" class="wp area" name="regionName">
                <span class="lbl"> 自定义</span>
            </label>
            <div class="float-left hide" style="width: 50%;">
                <@r.region dataType="management" nestedId='regionCode' regionCode="" call='changeRegionCode'>
                    <input type="hidden" name="regionCode" value="000000" id="regionCode">
                </@r.region>
            </div>
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-2 control-label no-padding-right">模块授权：</label>
        <div class="col-sm-8">
            <div style="border: 1px solid #eee;">
                <div class="box-body padding-10" style="border-bottom: 1px solid #eee;">
                    <b>所有模块</b>
                </div>
                <div class="page-sidebar chosen-tree-outter" style="height: 317px;overflow: auto;">
                    <div class="page-sidebar-body chosen-tree-inner">
                        <ul class="chosen-tree chosen-tree-tier1">
                            <#if modules?? && modules?size gt 0>
                                <#list modules as module>
                                <li class="sub-tree">
                                    <div class="chosen-tree-item" data-index="1">
                                        <span class="arrow"></span>
                                        <label class="name no-margin-top">
                                            <input type="checkbox" class="wp module-item" value="${module.moduleId}"
                                                   name="modules[${module_index}].moduleId">
                                            <span class="lbl"> ${module.moduleName}</span>
                                        </label>
                                    </div>
                                    <ul class="chosen-tree chosen-tree-tier2">
                                        <#if module.operates?? && module.operates?size gt 0>
                                            <#list module.operates as operate>
                                            <li id="${module.moduleId}">
                                                <div class="chosen-tree-item" data-index="10">
                                                    <span class="arrow"></span>
                                                    <label class="name no-margin-top">
                                                        <input type="checkbox" class="wp operate"
                                                               data-module="${module.moduleId}" value="${operate.id}"
                                                               name="modules[${module_index}].operates[${operate_index}].id">
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
    </div>
    <div class="form-group">
        <label class="col-sm-2 control-label no-padding-right">所有成员：</label>
        <div class="col-sm-8">
            <div style="border: 1px solid #eee;">
                <div class="box-body padding-10" style="border-bottom: 1px solid #eee;">
                    <label class="no-margin-top">
                        <input type="checkbox" class="wp user-check-all" data-all="<#if users??>${users?size}<#else>0</#if>">
                        <span class="lbl"> 全选</span>
                    </label>
                </div>
                <div class="ml10" style="height:317px;overflow:auto;">
                    <#if users?? && users?size gt 0>
                        <#list users as user>
                        <label class="mr10 w100 ">
                            <input type="checkbox" class="wp user-check" value="${user.id}" name="users[${user_index}].id">
                            <span class="lbl"> ${user.realName}</span>
                        </label>
                        </#list>
                    </#if>
                </div>
            </div>
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-2 control-label no-padding-right"></label>
        <div class="col-sm-8">
            <button class="btn btn-blue" id="save-group" type="button">确定</button>
            <a class="btn btn-default" href="#/operation/management/group">取消</a>
        </div>
    </div>
</div>
</form>
<script>
    $(function () {
        $('.page-sidebar').szxyTree();

        //行政区划选择
        $('.area').change(function () {
            if ($(this).val()==='000000') {
                $(this).parent().next().next().addClass('hide');
                $('#regionCode').val('000000');
            } else {
                $(this).parent().next().removeClass('hide');
                $('#regionCode').val('');
            }
            $('#group-form').data("bootstrapValidator").updateStatus('regionCode', 'STATUS_NOT_VALIDATED', null);
        });

        //模块选择点击事件
        $('.module-item').change(function () {
            var moduleId = $(this).val();
            if ($(this).is(':checked')) {
                $('input[data-module="'+moduleId+'"]').prop('checked', true);
            } else {
                $('input[data-module="'+moduleId+'"]').prop('checked', false);
            }
        });
        $('.operate').change(function () {
            //check operate selected size
            var moduelId = $(this).data('module');
            var selectedOperateSize = $('input[data-module="' + moduelId + '"]:checked').length;
            if (selectedOperateSize === 0) {
                $('input[value="'+moduelId+'"]').prop('checked', false);
            } else {
                $('input[value="'+moduelId+'"]').prop('checked', true);
            }
        });
        //用户选择点击事件
        $('.user-check-all').change(function () {
           if ($(this).is(':checked')) {
               $('.user-check').prop('checked', true);
           } else {
               $('.user-check').prop('checked', false);
           }
        });
        $('.user-check').change(function () {
            $('.user-check-all').prop('checked', $('.user-check:checked').length === parseInt($('.user-check-all').data('all')))
        });

        var submit = false;
        $('#save-group').click(function () {
            $('#group-form').data("bootstrapValidator").validate();
            var valid = $('#group-form').data("bootstrapValidator").isValid();
            if (!valid) {
                return;
            }

            if (submit) {
                message.warnMessage("请不要重复提交");
                return ;
            }
            submit = true;
            $.ajax({
                url:  _contextPath + '/operation/management/group/save',
                data: $('#group-origin-form').serialize(),
                type: 'POST',
                success: function (res) {
                    if (res.success) {
                        message.successMessage(res.message, function () {
                            routeUtils.go('/operation/management/group');
                        })
                    } else {
                        message.errorMessage(res.message);
                        submit = false;
                    }
                },
                error: function () {
                    submit = false;
                }
            })
        });

        //init validators
        $('#group-form').bootstrapValidator({
            excluded:[":disabled"],
            feedbackIcons: {
                valid: 'glyphicon /*glyphicon-ok*/',
                invalid: 'glyphicon /*glyphicon-remove*/',
                validating: 'glyphicon /*glyphicon-refresh*/'
            },
            autoFocus: true,
            fields: {
                "name": {
                    validators: {
                        notEmpty: {
                            message: "分组名称不能为空"
                        },
                        chineseStringLength: {
                            max: 64,
                            message: "组名过长 32个汉字或64个英文字符"
                        },
                        remote: {
                            url: _contextPath + '/operation/management/group/checkGroupName',
                            data: {"name": $('#groupName').val()},
                            type: 'GET'
                        }
                    }
                },
                "regionCode": {
                    validators: {
                        notEmpty: {
                            message: "行政区划不能为空"
                        },
                        chineseStringLength: {
                            max: 6,
                            message: "行政区划不能超过6个字符"
                        }
                    }
                }
            }
        })
    });

    function changeRegionCode() {
        var validator = $('#group-form').data("bootstrapValidator");
        validator.updateStatus('regionCode', validator.STATUS_NOT_VALIDATED, null);
        validator.validateField('regionCode')
    }
</script>