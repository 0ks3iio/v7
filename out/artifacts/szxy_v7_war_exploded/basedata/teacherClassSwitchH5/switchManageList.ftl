<div>
    <#if resultList?exists && resultList?size gt 0>
    <#list resultList as item>
    <div class="mui-card edu-list" adjustedId="${item.id!}">
        <div class="mui-card-header">
            <span class="adjustingTeacherName">${item.adjustingTeacherName!}</span><span class="adjustingName" style="padding-left: 0px;margin-left: 0px;border-left: 0px;font-size: 12px;" adjustingName="${item.adjustingName!}">（${item.adjustingName!}）</span>
        </div>
        <div class="mui-card-content">
            <div class="mui-card-content-inner" onclick="switchDetail(this)">
                <table width="100%">
                    <tr>
                        <td width="60%"><span class="c-999">被调至：</span><span class="beenAdjustedName"> ${item.beenAdjustedName}</span></td>
                        <td><span class="c-999">被调教师：</span><span class="beenAdjustedTeacherName"> ${item.beenAdjustedTeacherName!}</span></td>
                    </tr>
                    <tr>
                        <td width="40%"><span class="c-999">班级：</span><span class="className"> ${item.className}</span></td>
                        <td><span class="c-999">申请类型：</span>${item.applyType}</td>
                    </tr>
                    <tr>
                        <td colspan="2"><span class="c-999">备注：</span><span class="remark">${item.remark?default("无")}</span></td>
                    </tr>
                </table>
            </div>
        </div>
        <div class="mui-card-footer switch-state" state="${item.state}" canDelete="${item.canDelete?c}" adjustedId="${item.id}">
            <#if item.state == "0">
                <span class="c-orange">审核中</span>
                <span>
                    <a class="mui-btn btn-radius-16" onclick="switchAgree(this, '${item.id}', '2')">不通过</a>
                    <a class="mui-btn mui-btn-primary mui-btn-outlined btn-radius-16" onclick="switchAgree(this, '${item.id}', '1')">通过</a>
                </span>
            <#elseif item.state == "1">
                <span class="c-orange"><i class="label">调</i>${item.beenAdjustedTeacherName!}</span>
                <span>
                    <#if item.canDelete?default(false)>
                        <a class="mui-btn mui-btn-primary mui-btn-outlined btn-radius-16" onclick="switchCancel(this, '${item.id}')">撤销</a>
                    <#else>
                        <a class="mui-btn mui-btn-outlined btn-radius-16">撤销</a>
                    </#if>
                </span>
            <#else>
                <span class="c-red">未通过</span>
                <span>
                    <#if item.canDelete?default(false)>
                        <a class="mui-btn mui-btn-primary mui-btn-outlined btn-radius-16" onclick="switchCancel(this, '${item.id}')">撤销</a>
                    <#else>
                        <a class="mui-btn mui-btn-outlined btn-radius-16">撤销</a>
                    </#if>
                </span>
            </#if>
        </div>
    </div>
    </#list>
    <#else>
    <div class="mui-page-noData">
        <i></i>
        <p class="f-16">没有找到相关数据</p>
    </div>
    </#if>
</div>

<script>
    var submitObj;
    var isSubmiting = false;
    function switchCancel(obj, adjustedId) {
        if (isSubmiting) {
            return;
        }
        submitObj = obj;
        mui.confirm('确定要撤销吗？', '',['取消','确认'], function(e) {
            if (e.index == 1) {
                isSubmiting = true;
                $.ajax({
                    url: "${request.contextPath}/mobile/open/adjusttipsay/list/cancel?adjustedId=" + adjustedId,
                    success: function (result) {
                        var jsonResult = JSON.parse(result);
                        if (jsonResult.success) {
                            $(submitObj).parents(".edu-list").remove();
                            toastMsg("已撤销", 2000);
                            isSubmiting = false;
                        } else {
                            toastMsg(jsonResult.msg, 2000);
                            isSubmiting = false;
                        }
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                        isSubmiting = false;
                    }
                });
            }
        });
    }

    function switchAgree(obj, adjustedId, state) {
        if (isSubmiting) {
            return;
        }
        submitObj = obj;
        stateTmp = state;
        mui.confirm('确定要提交吗？', '',['取消','确认'], function(e) {
            if (e.index == 1) {
                isSubmiting = true;
                $.ajax({
                    url: "${request.contextPath}/mobile/open/adjusttipsay/manage/agree",
                    data: {
                        "teacherId": "${teacherId!}",
                        "adjustedId": adjustedId,
                        "state": state
                    },
                    success: function (result) {
                        var jsonResult = JSON.parse(result);
                        if (jsonResult.success) {
                            $(submitObj).parents(".edu-list").remove();
                            toastMsg("审核已通过", 2000);
                            isSubmiting = false;
                        } else {
                            toastMsg(jsonResult.msg, {offset: 't', time: 3000});
                            isSubmiting = false;
                        }
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                        isSubmiting = false;
                    }
                });
            }
        });
    }

    function switchDetail(obj) {
        submitObj = obj;
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
        $layer.find(".adjustingName").text($cell.find(".adjustingName").attr("adjustingName"));
        $layer.find(".className").text($cell.find(".className").text());
        $layer.find(".beenAdjustedTeacherName").text($cell.find(".beenAdjustedTeacherName").text());
        $layer.find(".beenAdjustedName").text($cell.find(".beenAdjustedName").text());
        $layer.find(".remark").text($cell.find(".remark").text());
        if ($cell.find(".switch-state").attr("state") == "0") {
            $layer.find(".switchCancel").html("<a class=\"mui-tab-item f-16\" state=\"2\" href=\"#\" style=\"color:black\">不通过</a>\n" +
                    "        <a class=\"mui-tab-item f-16\" state=\"1\" href=\"#\" style=\"background: #2f7bff;color: #fff;\">通过</a>");
            $layer.find(".switchCancel").children("a").on("tap", function () {
                // switchAgree(submitObj, $(".layer-detail").find(".adjustedId").text(), $(this).attr("state"));
                if (isSubmiting) {
                    return;
                }
                mui.confirm('确定要提交吗？', '',['取消','确认'], function(e) {
                    if (e.index == 1) {
                        isSubmiting = true;
                        $.ajax({
                            url: "${request.contextPath}/mobile/open/adjusttipsay/manage/agree",
                            data: {
                                "teacherId": "${teacherId!}",
                                "adjustedId": $(".layer-detail").find(".adjustedId").text(),
                                "state": $(this).attr("state")
                            },
                            success: function (result) {
                                var jsonResult = JSON.parse(result);
                                if (jsonResult.success) {
                                    $(submitObj).parents(".edu-list").remove();
                                    $(".layer-detail").hide();
                                    $(".layer-index").show();
                                    toastMsg("审核已通过", 3000);
                                    isSubmiting = false;
                                } else {
                                    toastMsg(jsonResult.msg, {offset: 't', time: 3000});
                                    isSubmiting = false;
                                }
                            },
                            error: function (XMLHttpRequest, textStatus, errorThrown) {
                                isSubmiting = false;
                            }
                        });
                    }
                });
            });
        } else {
            if ($cell.find(".switch-state").attr("canDelete") == "true") {
                $layer.find(".switchCancel").html("<a class=\"mui-tab-item f-16\" href=\"#\" style=\"background: #2f7bff;color: #fff;\">撤销</a>");
                $layer.find(".switchCancel").children("a").on("tap", function () {
                    // switchState(submitObj, $cell.find(".switch-state").attr("adjustedId"));
                    if (isSubmiting) {
                        return;
                    }
                    mui.confirm('确定要撤销吗？', '',['取消','确认'], function(e) {
                        if (e.index == 1) {
                            isSubmiting = true;
                            $.ajax({
                                url: "${request.contextPath}/mobile/open/adjusttipsay/list/cancel?adjustedId=" + $(".layer-detail").find(".adjustedId").text(),
                                success: function (result) {
                                    var jsonResult = JSON.parse(result);
                                    if (jsonResult.success) {
                                        $(submitObj).parents(".edu-list").remove();
                                        $(".layer-detail").hide();
                                        $(".layer-index").show();
                                        toastMsg("已撤销", 3000);
                                        isSubmiting = false;
                                    } else {
                                        toastMsg(jsonResult.msg, 3000);
                                        isSubmiting = false;
                                    }
                                },
                                error: function (XMLHttpRequest, textStatus, errorThrown) {
                                    isSubmiting = false;
                                }
                            });
                        }
                    });
                });
            } else {
                $layer.find(".switchCancel").html("<a class=\"mui-tab-item f-16\" href=\"#\" style=\"background: #BDBDBD;color: #fff;\">撤销</a>");
            }
        }
        $(".layer-index").hide();
        $layer.show();
    }
</script>