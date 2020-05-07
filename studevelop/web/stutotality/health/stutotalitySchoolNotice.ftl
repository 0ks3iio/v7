<#--<div class="evaluate-body col-xs-12 pro-school-box">-->
<#--    <form id="myForm">-->
<#--    <div class="pro-other-box">-->
<#--        <#assign num = 0>-->
<#--        <#if lastList?exists && lastList?size gt 0>-->
<#--            <#list lastList as last>-->
<#--                <div class="evaluate-item clearfix">-->
<#--                    <span class="layer-evaluate-label evaluate-item-left">${last.gradeName}：</span>-->
<#--                    <div class="evaluate-item-right eva-pro-item">-->
<#--                        <div class="evaluate-item-min">-->
<#--                            <input type="hidden" name="noticeList[${last_index}].gradeId" value="${last.gradeId!}"/>-->
<#--&lt;#&ndash;                            <input type="hidden" name="noticeList[${last_index}].notice" id="notice${last_index}" value=""/>&ndash;&gt;-->
<#--                            <textarea name="noticeList[${last_index}].notice" id="schoolNoticeId${last_index}" maxlength="500" class="pro-school-text" id="schoolNotice${last_index}" onchange=""  style="height: 80px;">${last.notice!}</textarea>-->
<#--&lt;#&ndash;                            <textarea name="noticeList[${last_index}].notice" id="schoolNoticeId" maxlength="500" class="pro-school-text" id="schoolNotice${last_index}" onchange="">${last.notice!}</textarea>&ndash;&gt;-->
<#--                        </div>-->
<#--                    </div>-->
<#--                </div>-->
<#--            </#list>-->
<#--        </#if>-->
<#--        <div class="evaluate-item clearfix mt10">-->
<#--            <span class="layer-evaluate-label evaluate-item-left" style="height: 1px;"></span>-->
<#--            <div class="evaluate-item-right">-->
<#--                <div class="btn btn-blue font-14" onclick="save()">保存</div>-->
<#--            </div>-->
<#--        </div>-->
<#--    </div>-->
<#--    </form>-->
<#--</div>-->
<div class="evaluate-body col-xs-12 pro-school-box">
        <div class="student_body_right" style="padding: 0;">
            <div class="student_evaluat_box">
                <form id="myForm">
                <#assign num = 0>
                <#if lastList?exists && lastList?size gt 0>
                    <#list lastList as last>
                        <div class="evaluat_title">${last.gradeName}：</div>
                        <div class="evaluat_content">
                            <input type="hidden" name="noticeList[${last_index}].gradeId" value="${last.gradeId!}"/>

                            <textarea name="noticeList[${last_index}].notice" id="schoolNoticeId${last_index}" maxlength="500" class="typein-con-text" id="schoolNotice${last_index}" onchange=""  style="height: 80px;">${last.notice!}</textarea>
                        </div>
                    </#list>
                </#if>
                </form>
            </div>
            <div class="btn btn-blue student-save-btn"
                    style="position: absolute;z-index: 99;bottom: 100px;" onclick="save()" >保存</div>
            <div class="evaluat_top"></div>
            <div class="evaluat_center">
                <div class="evaluat_center_body"></div>
            </div>
            <div class="evaluat_down"></div>
            <div class="evaluat_back"></div>
        </div>
</div>
<script type="text/javascript">
    $(function () {
        $(".evaluate-body").css("height", $("#sidebar").height() - 160);
        $(".evaluate-right-content").css(
            "height",
            $(".type-in-tree-box").height()
        );
        $(".student-save-btn").css("left", ($(".student_body_right").width() - $(".student_evaluat_box").width()) / 2 + 20);
    });
    $(window).resize(function () {
        $(".evaluate-body").css("height", $("#sidebar").height() - 160);
        $(".evaluate-right-content").css(
            "height",
            $(".type-in-tree-box").height()
        );
        $(".student-save-btn").css("left", ($(".student_body_right").width() - $(".student_evaluat_box").width()) / 2 + 20);
    });
    function save() {
<#--        <#if lastList?exists && lastList?size gt 0>-->
<#--            <#list lastList as last>-->
<#--                $("#notice${last_index}").val($("#schoolNotice${last_index}").val());-->
<#--            </#list>-->
<#--        </#if>-->
       /* var notice = $("#schoolNoticeId").val();
        if(notice){
            layerTipMsg(false, "提示", "字数应在250字之内");
            return;
        }*/
        var check = checkValue("#myForm")
        if(!check){
            return false;
        }
        var ii = layer.load();
        var options= {
            type:"post",
            dataType:"json",
            url: "${request.contextPath}/stutotality/schoolNotice/save",
            success: function (data) {
                //var jsonO = data.parseJSON();
                //var jsonO=JSON.parse(data);
                var jsonO=data;
                //debugger;
                layer.close(ii);
                if (!jsonO.success) {
                    layerTipMsg(jsonO.success, "保存失败", jsonO.msg);
                    return;
                } else {
                    // layer.closeAll();
                    layer.msg("保存成功", {
                        offset: 't',
                        time: 2000
                    });
                }
            }
        }
        $('#myForm').ajaxSubmit(options);
    }


</script>