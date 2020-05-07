<div class="clearfix height-1of1">
    <div class="col-xs-6 height-1of1 no-padding-left">
        <div class="box box-default shadow show-where height-1of1 no-margin padding-20">
            <p class="warning-title no-margin">预警结果</p>
            <p class="warning-num">共${totalRecord!}条数据</p>
            <div class="box-body scroll-height">
                <ul class="data-detail scrollBar4" id="dataDetail">

                </ul>
            </div>
        </div>
    </div>

    <div class="col-xs-6 height-1of1 no-padding-right">
        <div class="box box-default shadow show-detail height-1of1 no-margin padding-20">
        <p class="warning-title no-margin">规则信息</p>
        <div class="box-body scroll-height" style="padding-left:0px;">
            <div class="clearfix">
                <h4 class="warning-title no-margin"><span>基本信息</span></h4>
                <p class="warning-name"><span>项目名称：</span>${warningProject.projectName!}</p>
                <p class="warning-name"><span>生效时间：</span>${warningProject.startTime!}&nbsp;至&nbsp;<#if warningProject.isAllTime==2>永久<#else>${warningProject.endTime!}</#if></p>
                <p class="warning-name"><span>预警次数：</span><t style="color: red;font-size: 30px;">${warningProject.warnTimes!}</t>&nbsp;次</p>
                <h4 class="warning-title no-margin"><span>基本规则</span></h4>
                <#list tagRuleMap?keys as key>
                    <div class="warning-and">
                        <#if key_index != 0>
                            <span class="text-blue">并且&nbsp;</span>
                        </#if>
                        <#assign items = tagRuleMap[key]>
                        <#list items as item>
                            <div class="warning-or" style="margin-left: 20px;padding-left: 0px;">
                                <span class="text-blue"><#if item_index != 0>或者&nbsp;<#else></#if></span> ${item.metadataName!}&nbsp;${item.metadataColumnName!}&nbsp;${item.ruleName!}（${item.ruleSymbolName!}${item.result}）
                            </div>
                        </#list>
                    </div>
                </#list>
            </div>
        </div>
    </div>
    </div>
</div>
<input type="hidden" id="projectId" value="${projectId!}">
<script>

    $(function () {
        $('#dataDetail').css("height", $('.show-where').height()-150 + "px");
        detailList(1);
    });

    function detailList(pageIndex) {
        $('#moreDetail').parent().remove();
        $.ajax({
            url: '${request.contextPath}/bigdata/warningResult/detailList',
            type: 'POST',
            data : {
                id : $('#projectId').val(),
                pageIndex : pageIndex
            },
            dataType: 'html',
            success: function (val) {
                $('#dataDetail').append(val);
            }
        });
    }
</script>