<#import "../../macro/R.ftl" as r />
<form id="group-auth-form">
<div class="form-horizontal">
    <div class="form-group">
        <label class="col-sm-2 control-label no-padding-right">负责区块：</label>
        <div class="col-sm-8">
            <label >
                <input type="radio" class="wp area" value="000000" <#if group.regionCode=='000000'>checked="checked"</#if> name="regionName">
                <span class="lbl"> 所有地区</span>
            </label>
            <label >
                <input type="radio" class="wp area" name="regionName" <#if group.regionCode!='000000'>checked="checked"</#if>>
                <span class="lbl"> 自定义</span>
            </label>
            <div class="float-right <#if group.regionCode=='000000'>hide</#if>" style="width: 50%;">
                <@r.region dataType="management" nestedId='regionCode' regionCode="${group.regionCode}" call='changeRegionCode'>
                    <input type="hidden" name="regionCode" value="${group.regionCode}" id="regionCode">
                </@r.region>
            </div>
        </div>
    </div>
    <div class="form-group no-margin-bottom">
        <label class="col-sm-2 control-label no-padding-right">模块授权：</label>
        <div class="col-sm-10">
            <div style="border: 1px solid #eee;">
                <div class="box-body padding-10" style="border-bottom: 1px solid #eee;">
                    <b>所有模块</b>
                </div>
                <div class="page-sidebar chosen-tree-outter" style="height: 317px;overflow: auto;">
                    <div class="page-sidebar-body chosen-tree-inner">
                        <ul class="chosen-tree chosen-tree-tier1">
                            <#if modules?? && modules?size gt 0>
                                <#list modules as module>
                                    <li class="sub-tree ">
                                        <div class="chosen-tree-item" data-index="1">
                                            <span class="arrow"></span>
                                            <label class="name no-margin-top">
                                                <input name="modules[${module_index}].moduleId" data-module="${module.moduleId}" type="checkbox" class="wp module-item" value="${module.moduleId}" <#if module.hasAuth>checked="checked"</#if>>
                                                <span class="lbl"> ${module.moduleName}</span>
                                            </label>
                                        </div>
                                        <ul class="chosen-tree chosen-tree-tier2">
                                            <#if module.operates?? && module.operates?size gt 0>
                                                <#list module.operates as operate>
                                                    <li>
                                                        <div class="chosen-tree-item" data-index="10">
                                                            <span class="arrow"></span>
                                                            <label class="name no-margin-top">
                                                                <input name="modules[${module_index}].operates[${operate_index}].id" data-module="${module.moduleId}" type="checkbox" class="wp operate" value="${operate.id}" <#if operate.hasAuth>checked="checked"</#if>>
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
</div>
</form>
<script>
    $(function(){
        $('.page-sidebar').szxyTree();
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

        $('#group-auth-form').bootstrapValidator({
            excluded:[":disabled"],
            feedbackIcons: {
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon /*glyphicon-remove*/',
                validating: 'glyphicon /*glyphicon-refresh*/'
            },
            autoFocus: true,
            fields: {
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

        //行政区划选择
        $('.area').change(function () {
            if ($(this).val()==='000000') {
                $(this).parent().next().next().addClass('hide');
                $('#regionCode').val('000000');
            } else {
                $(this).parent().next().removeClass('hide');
                $('#regionCode').val('');
            }
            $('#group-auth-form').data("bootstrapValidator").updateStatus('regionCode', 'STATUS_NOT_VALIDATED', null);
        });
    });
    function changeRegionCode() {
        var validator = $('#group-auth-form').data("bootstrapValidator");
        validator.updateStatus('regionCode', validator.STATUS_NOT_VALIDATED, null);
        validator.validateField('regionCode')
    }
</script>