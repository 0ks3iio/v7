<#--<div id="divImport">-->
    <div class="student_body_right" style="height: 640px;">
        <div class="student_evaluat_box">
            <div class="evaluat_title">班主任寄语：</div>
            <div class="evaluat_content">
                <textarea class="typein-con-text" id="techContent" maxlength="500">${teacContent!}</textarea>
            </div>
            <#if isAdmin>
                <div class="evaluat_title">家长寄语：</div>
                <div class="evaluat_content">
                    <textarea  class="typein-con-text" id="parentContent" maxlength="500">${parentContent!}</textarea>
                </div>
                <div class="evaluat_title">给自己的话：</div>
                <div class="evaluat_content">
                    <textarea  class="typein-con-text" id="myContent" maxlength="500">${myContent!}</textarea>
                </div>
            </#if>
        </div>
    <#--            <button class="btn btn-blue" style="position: absolute;z-index: 99;" onclick="saveAcad()">保存</button>-->
        <button class="btn btn-blue student-save-btn " onclick="saveAcad()" style="position: absolute;z-index: 99;bottom: 100px;">保存</button>
<#--        <button class="btn  " onclick="doImport()" style="position: absolute;z-index: 200;bottom: 100px;">导入<button>-->
        <div class="evaluat_top"></div>
        <div class="evaluat_center">
            <div class="evaluat_center_body"></div>
        </div>
        <div class="evaluat_down"></div>
        <div class="evaluat_back"></div>
    </div>
<#--</div>-->
<script>
    $(function () {
        container = $("#showTabDiv").html();

        $(".evaluate-right-content").css(
                "height",
                $(".type-in-tree-box").height()
                  );
        $(".student-save-btn").css("left",($(".student_body_right").width()- $(".student_evaluat_box").width())/2 + 20)
        $(window).resize(function() {
            $(".evaluate-right-content").css(
                    "height",
                    $(".type-in-tree-box").height()
                    );
            $(".student-save-btn").css("left",($(".student_body_right").width()- $(".student_evaluat_box").width())/2 + 20)
        })
    })
    function saveAcad() {
        var studentId = "${studentId!}";
        var finalId = "${finalId!}";
        var text = $.trim($("#techContent").val());
        var text1 = $.trim($("#parentContent").val());
        var text2 = $.trim($("#myContent").val());
        if (!text) {
            layer.msg("评语不能为空", { time: 2000 });
        } else {
            <#if isAdmin>
                $.ajax({
                    url:"${request.contextPath}/stutotality/result/saveStuAcad?",
                    data:{'id':finalId,'teacContent':text,'parentContent':text1,'myContent':text2,'studentId':studentId},
                    type:'post',
                    dataType:'json',
                    success:function(data) {
                        var jsonO = data;
                        if(!jsonO.success) {
                            layerTipMsg(jsonO.success, "操作失败！", jsonO.msg);
                            isSubmit = false;
                            return;
                        }else {
                            layer.msg("保存成功", {
                                offset: 't',
                                time: 2000
                            });
                        }
                    },
                    error : function(XMLHttpRequest, textStatus, errorThrown) {
                    }
                })
            <#else>
                $.ajax({
                    url:"${request.contextPath}/stutotality/result/saveStuAcad?",
                    data:{'id':finalId,'teacContent':text,'studentId':studentId},
                    type:'post',
                    dataType:'json',
                    success:function(data) {
                        var jsonO = data;
                        if(!jsonO.success) {
                            layerTipMsg(jsonO.success, "操作失败！", jsonO.msg);
                            isSubmit = false;
                            return;
                        }else {
                            layer.msg("保存成功", {
                                offset: 't',
                                time: 2000
                            });
                        }
                    },
                    error : function(XMLHttpRequest, textStatus, errorThrown) {
                    }
                })
            </#if>
        }
    }

</script>