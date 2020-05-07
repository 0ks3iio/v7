<form id="myform">
    <input type="hidden" name="acadyear" value="${acadyear!}">
    <input type="hidden" name="oldAcadyear" value="${oldAcadyear!}">
    <input type="hidden" name="semester" value="${semester!}">
    <input type="hidden" name="oldGradeId" value="${oldGradeId!}">
    <input type="hidden" name="gradeId" value="${gradeId!}">
    <input type="hidden" name="studentId" value="${studentId!}">
    <div class="add-div">
        <div class="filter">
            <div class="filter-item">
                <span class="filter-name" style="width: 140px;">学生：</span>
                <div class="filter-content">
                    <label class="form-control" style="border: 0px">${studentName!}</label>
                </div>
            </div>
            <div class="filter-item">
                <span class="filter-name" style="width: 140px;">休学前所在年级考试：</span>
                <div class="filter-content">
                    <select class="form-control" id="oldExamId" style="width:280px" nullable="false" name="oldExamId">
                        <option value="">--请选择--</option>
                        <#if oldExamList?exists && oldExamList?size gt 0>
                            <#list oldExamList as item>
                                <option value="${item.id!}">${item.examName!}</option>
                            </#list>
                        </#if>
                    </select>
                </div>
            </div>
            <div class="filter-item">
                <span class="filter-name" style="width: 140px;">当前所在年级考试：</span>
                <div class="filter-content">
                    <select class="form-control" id="examId" style="width: 280px"  nullable="false" name="examId">
                        <option value="">--请选择--</option>
                        <#if examList?exists && examList?size gt 0>
                            <#list examList as item>
                                <option value="${item.id!}">${item.examName!}</option>
                            </#list>
                        </#if>
                    </select>
                </div>
            </div>
        </div>
    </div>
</form>

<#-- 确定和取消按钮 -->
<div class="layer-footer">
    <a class="btn btn-lightblue" id="btn-commit">保存</a>
    <a class="btn btn-grey" id="btn-close">取消</a>
</div>

<script type="text/javascript">

    // 取消按钮操作功能
    $("#btn-close").on("click", function(){
        doLayerOk("#btn-commit", {
            redirect:function(){},
            window:function(){layer.closeAll()}
        });
    });

    // 确定按钮操作功能
    var isSubmit=false;
    $("#btn-commit").on("click", function(){
        if(isSubmit){
            return;
        }
        $(this).addClass("disabled");
        var check = checkValue('.add-div');
        if(!check){
            $(this).removeClass("disabled");
            isSubmit=false;
            return;
        }

        var oldExamId = $("#oldExamId").val();
        var examId = $("#examId").val();
        if(oldExamId==examId){
            layer.tips('休学前所在年级考试与当前所在年级考试不能相同！',$("#examId"), {
                tipsMore: true,
                tips: 3
            });
            $(this).removeClass("disabled");
            isSubmit=false;
            return;
        }

        // 提交数据
        var ii = layer.load();
        isSubmit=true;
        var options = {
            url :"${request.contextPath}/scoremanage/plan/reinstate/save",
            dataType: "json",
            success:function(data) {
                layer.closeAll();
                if(data.success){
                    layer.msg(data.msg,{
                        offset: 't',
                        time: 2000
                    });
                    var str = "?acadyear=${acadyear!}&semester=${semester!}&studentId=${studentId!}&studentName=${studentName!}&identityCard=${identityCard!}&oldGradeId=${oldGradeId!}&gradeId=${gradeId!}";
                    var url = "${request.contextPath}/scoremanage/plan/reinstate/list/page"+str;
                    $("#showList").load(url);
                }else{
                    layerTipMsg(data.success,"失败",data.msg);
                    $("#btn-commit").removeClass("disabled");
                    isSubmit=false;
                }

            },
            type:'post',
            error:function(XMLHttpRequest, textStatus, errorThrown){}
        };

        $("#myform").ajaxSubmit(options);

    });

</script>

