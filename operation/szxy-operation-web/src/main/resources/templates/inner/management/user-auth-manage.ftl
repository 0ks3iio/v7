<#import "../../macro/R.ftl" as r />
<form id="user-auth-form">
    <div class="form-horizontal mt20">
        <div class="form-group">
            <label class="col-sm-2 control-label no-padding-right">运营组：</label>
            <div class="col-sm-8">
                <div class="clearfix">
                    <div class="tree-wrap">
                        <table class="table table-striped table-hover no-margin">
                            <thead>
                            <tr>
                                <th>所有运营组<span class="badge badge-blue float-right" id="group-counter">${groups?size}</span></th>
                            </tr>
                            </thead>
                        </table>
                        <div class="tree-list" style="height:344px;margin-top: 0;">
                        <#if groups?? && groups?size gt 0>
                            <#list groups as group>
                            <a href="javascript:void(0);" class="groups" data-id="${group.id}">
                                <label class="ellipsis mt4">
                                    <input name="groups[${group_index}]" type="checkbox" class="wp groups-check" value="${group.id}" <#if group.hasAuth>checked="checked"</#if>>
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
            $('#user-auth-form').data("bootstrapValidator").updateStatus('user.regionCode', 'STATUS_NOT_VALIDATED', null);
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

        $('#user-auth-form').bootstrapValidator({
            excluded:[":disabled"],
            feedbackIcons: {
                valid: 'glyphicon /*glyphicon-ok*/',
                invalid: 'glyphicon /*glyphicon-remove*/',
                validating: 'glyphicon /*glyphicon-refresh*/'
            },
            autoFocus: true,
            fields: {
                "user.regionCode": {
                    validators: {
                        notEmpty: {
                            message: "行政区划不能为空"
                        }
                    }
                }
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