<#assign USER_LAYOUT_TWO2ONE =stack.findValue("@net.zdsoft.desktop.entity.UserSet@LAYOUT_TWO2ONE") />
<#assign USER_LAYOUT_DEFAULT =stack.findValue("@net.zdsoft.desktop.entity.UserSet@LAYOUT_DEFAULT") />
<#assign LAYOUT_TYPE_LEFT =stack.findValue("@net.zdsoft.desktop.entity.FunctionAreaUser@LAYOUT_TYPE_LEFT") />
<#assign LAYOUT_TYPE_RIGHT =stack.findValue("@net.zdsoft.desktop.entity.FunctionAreaUser@LAYOUT_TYPE_RIGHT") />
<#assign LAYOUT_TYPE_MIDDLE =stack.findValue("@net.zdsoft.desktop.entity.FunctionAreaUser@LAYOUT_TYPE_MIDDLE") />
<div class="layer-container">
    <input type="hidden" name="functionAreaUserId" id="functionAreaUserId" value="${functionAreaUserId!}" />
    <ul class="nav nav-tabs" role="tablist">
        <#assign dynamicParamsExists=false />
        <#if dynamicParams?exists && dynamicParams?size gt 0>
        <#assign dynamicParamsExists=true />
        <li role="presentation" class="active"><a href="#set01" role="tab" data-toggle="tab">内容设置</a></li>
        </#if>
        <li role="presentation" <#if !dynamicParamsExists>class="active"</#if>><a href="#set02" role="tab" data-toggle="tab">排序设置</a></li>
    </ul>
    <div class="tab-content">
        <#if dynamicParams?exists && dynamicParams?size gt 0>
        <div id="set01" class="tab-pane active" role="tabpanel">
            <form action="" class="form-horizontal" role="form">
                    <#list dynamicParams as dynamicParam>
                        <div class="form-group">
                            <div class="col-sm-2 control-label no-padding">${dynamicParam.description!}：</div>
                            <div class="col-sm-8">
                                <input class="form-control number pull-left" id="${dynamicParam.name!}" msgName="${dynamicParam.description!}" name="${dynamicParam.name!}" <#if dynamicParam.isNumber()?default(false)>type="number" </#if> min="${dynamicParam.min!}" max="${dynamicParam.max!}" value="${dynamicParam.value!}" />
                            </div>
                        </div>
                    </#list>
            </form>
        </div>
        </#if>
        <div id="set02" class="tab-pane <#if !dynamicParamsExists>active</#if>" role="tabpanel">
            <form action="" class="form-horizontal" role="form">
                <#if layout! == USER_LAYOUT_TWO2ONE>
                <input type="hidden" name="layoutType" id="layoutType" value="${layoutType!}" />
                <div class="form-group">
                    <div class="col-sm-2 control-label no-padding">布局选择：</div>
                    <div class="col-sm-8">
                        <ul class="layout-list clearfix">
                            <li data-action="select" class="<#if layoutType?default('')==LAYOUT_TYPE_LEFT>selected</#if>" val="${LAYOUT_TYPE_LEFT}">
                                <span class="layout-img layout-twoToOne-inLeft"></span>
                                <h5 class="layout-name">左侧显示</h5>
                            </li>
                            <#if isShowTimetable?default(true)>
                            <li data-action="select" class="<#if layoutType?default('')==LAYOUT_TYPE_RIGHT>selected</#if>" val="${LAYOUT_TYPE_RIGHT}">
                                <span class="layout-img layout-twoToOne-inRight"></span>
                                <h5 class="layout-name">右侧显示</h5>
                            </li>
                            </#if>
                        </ul>
                    </div>
                </div>
                </#if>
                <div class="form-group">
                    <div class="col-sm-2 control-label no-padding">排序：</div>
                    <div class="col-sm-8">
                        <input type="hidden" name="displayOrder" id="displayOrder" value="${displayOrder!}" />
                        <input type="hidden" name="functionAreaIndex" id="functionAreaIndex" value="${functionAreaIndex!}" />
                        <input type="hidden" class="form-control number pull-left" msgName="排序" type="number" nullable="false" name="index" id="index" min="1" max="${maxDisplayOrder!}" value="${functionAreaIndex!}" range />
                        <select class="form-control number" onchange="functionAreaSelectOrder(this)">
                            <#list 1..maxDisplayOrder as order>
                                <option value="${order}" <#if order==functionAreaIndex!>selected</#if>>${order}</option>
                            </#list>
                        </select>
                        <#if layout! == USER_LAYOUT_TWO2ONE >
                            <span class="control-tips pull-left">&emsp;当前模块显示为<#if layoutType?default('')==LAYOUT_TYPE_RIGHT>右侧第${functionAreaIndex!}个<#else>左侧第${functionAreaIndex!}个</#if></span>
                        <#else>
                            <span class="control-tips pull-left">&emsp;当前模块显示为第${functionAreaIndex!}个</span>
                        </#if>
                    </div>
                </div>
                <#if false>
                <div class="form-group">
                    <div class="col-sm-2 control-label no-padding">高度：</div>
                    <div class="col-sm-8">
                        <input class="form-control number" name="height" id="height" type="text" value="${height!}" />
                    </div>
                </div>
                </#if>
                <#if (layoutType! != LAYOUT_TYPE_RIGHT && layout! == USER_LAYOUT_TWO2ONE) || layout! == USER_LAYOUT_DEFAULT >
                <div class="form-group">
                    <div class="col-sm-2 control-label no-padding">宽度：</div>
                    <div class="col-sm-8">
                        <select class="form-control number" onchange="functionAreaSelectColum(this)">
                            <#if layout! == USER_LAYOUT_DEFAULT >
                            <option value="4" <#if columns! == 4>selected</#if> >4</option>
                            </#if>
                            <option value="6" <#if columns! == 6>selected</#if> >一半</option>
                            <option value="12" <#if columns! == 12>selected</#if>>铺满</option>
                        </select>
                        <input type="hidden" msgName="宽度" name="columns" nullable="false" id="columns" type="number" max="12" min="1" value="${columns!}" />
                    </div>
                </div>
                </#if>
            </form>
        </div>
    </div>
</div>
<script>
    $(document).ready(function(){
        $('#set02 [data-action=select]').on('click',function(){
            if($(this).hasClass('disabled')){
                return false;
            }else if($(this).parent().hasClass('multiselect') && $(this).hasClass('selected')){
                $(this).toggleClass('selected');
            }else{
                var value = $(this).attr('val');
                $("#layoutType").val(value);
                $(this).addClass('selected').siblings().removeClass('selected');
            }
        });
    });

    function functionAreaSelectColum(obj) {
        $("#columns").val($(obj).val());
    }
    function functionAreaSelectOrder(obj) {
        $("#index").val($(obj).val());
    }
</script>