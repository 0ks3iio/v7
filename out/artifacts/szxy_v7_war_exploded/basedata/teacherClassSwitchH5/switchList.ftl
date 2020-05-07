<#if resultList?exists && resultList?size gt 0>
<#list resultList as item>
    <div class="mui-card edu-list" adjustedId="${item.id!}">
        <div class="mui-card-header">
            <span class="adjustingName">${item.adjustingName!}</span>
            <span class="adjustingTeacherName" style="display: none">${item.adjustingTeacherName!}</span>
        </div>
        <div class="mui-card-content" onclick="switchDetail(this)">
            <div class="mui-card-content-inner">
                <table width="100%">
                    <tr>
                        <td width="62%"><span class="c-999">被调至：</span><span class="beenAdjustedName">${item.beenAdjustedName!}</span></td>
                        <td width="38%"><span class="c-999">被调教师：</span><span class="beenAdjustedTeacherName">${item.beenAdjustedTeacherName!}</span></td>
                    </tr>
                    <tr>
                        <td><span class="c-999">班级：</span><span class="className">${item.className!}</span></td>
                        <td><span class="c-999">备注：</span><span class="remark">${item.remark!}</span></td>
                    </tr>
                </table>
            </div>
        </div>
        <div class="mui-card-footer switch-state" state="${item.state}">
            <#if item.state == "0">
                <span class="c-orange">审核中</span>
            <#elseif item.state == "1">
                <span class="c-orange"><i class="label">调</i>${item.beenAdjustedTeacherName!}</span>
            <#elseif item.state == "2">
                <span class="c-red">未通过</span>
            </#if>
            <span class="switch-cancel-span" candelete="${item.canDelete?c}" adjustedId="${item.id}">
                <#if item.canDelete?default(false)>
                    <a class="mui-btn mui-btn-primary mui-btn-outlined btn-radius-16" onclick="switchState(this, '${item.id}')">撤销</a>
                <#else>
                    <a class="mui-btn mui-btn-outlined btn-radius-16">撤销</a>
                </#if>
            </span>
        </div>
    </div>
</#list>
<#else>
    <div class="mui-page-noData">
        <i></i>
        <p class="f-16">没有找到相关数据</p>
    </div>
</#if>

<script>
    var cancelObj;
    var isCanceling = false;
    function switchState(obj, adjustedId) {
        if (isCanceling) {
            return;
        }
        cancelObj = obj;
        mui.confirm('确定要撤销吗？', '',['取消','确认'], function(e) {
            if (e.index == 1) {
                console.log("撤销" + adjustedId);
                isCanceling = true;
                $.ajax({
                    url: "${request.contextPath}/mobile/open/adjusttipsay/list/cancel",
                    data: {"adjustedId": adjustedId},
                    success: function (result) {
                        var jsonResult = JSON.parse(result);
                        if (jsonResult.success) {
                            $(cancelObj).parents(".edu-list").remove();
                            toastMsg("已撤销", 2000);
                            isCanceling = false;
                        } else {
                            toastMsg(jsonResult.msg, 2000);
                            isCanceling = false;
                        }
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                        isCanceling = false;
                    }
                });
            }
        });
    }

    function switchDetail(obj) {
        cancelObj = obj;
        var $cell = $(obj).parents(".edu-list");
        var $layer = $(".layer-detail");
        $layer.find(".adjustedId").text($cell.attr("adjustedId"));
        if ($cell.find(".switch-state").attr("state") == "0") {
            $layer.find(".switch-state").text("待审核");
        } else if ($cell.find(".switch-state").attr("state") == "1") {
            $layer.find(".switch-state").text("已通过");
        } else {
            $layer.find(".switch-state").text("未通过");
        }
        $layer.find(".adjustingTeacherName").text($cell.find(".adjustingTeacherName").text());
        $layer.find(".adjustingName").text($cell.find(".adjustingName").text());
        $layer.find(".className").text($cell.find(".className").text());
        $layer.find(".beenAdjustedTeacherName").text($cell.find(".beenAdjustedTeacherName").text());
        $layer.find(".beenAdjustedName").text($cell.find(".beenAdjustedName").text());
        $layer.find(".remark").text($cell.find(".remark").text());
        if ($cell.find(".switch-cancel-span").attr("canDelete") == "true") {
            $layer.find(".switchCancel").html("<a class=\"mui-tab-item f-16\" href=\"#\" style=\"background: #2f7bff;color: #fff;\">撤销</a>");
            $layer.find(".switchCancel").children("a").on("tap", function () {
                // switchState(cancelObj, $cell.find(".switch-cancel-span").attr("adjustedId"));
                if (isCanceling) {
                    return;
                }
                mui.confirm('确定要撤销吗？', '',['取消','确认'], function(e) {
                    if (e.index == 1) {
                        console.log("撤销" + $(".layer-detail").find(".adjustedId").text());
                        isCanceling = true;
                        $.ajax({
                            url: "${request.contextPath}/mobile/open/adjusttipsay/list/cancel",
                            data: {"adjustedId": $(".layer-detail").find(".adjustedId").text()},
                            success: function (result) {
                                var jsonResult = JSON.parse(result);
                                if (jsonResult.success) {
                                    $(cancelObj).parents(".edu-list").remove();
                                    $(".layer-detail").hide();
                                    $(".layer-index").show();
                                    toastMsg("已撤销", 3000);
                                    isCanceling = false;
                                } else {
                                    toastMsg(jsonResult.msg, 3000);
                                    isCanceling = false;
                                }
                            },
                            error: function (XMLHttpRequest, textStatus, errorThrown) {
                                isCanceling = false;
                            }
                        });
                    }
                });
            });
        } else {
            $layer.find(".switchCancel").html("<a class=\"mui-tab-item f-16\" href=\"#\" style=\"background: #BDBDBD;color: #fff;\">撤销</a>");
        }
        $(".layer-index").hide();
        $layer.show();
    }
</script>