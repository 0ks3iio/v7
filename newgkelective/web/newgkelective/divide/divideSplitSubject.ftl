<form id="myform">
    <div class="layer-syncScore divideDiv">
        <div class="filter">
            <div class="filter-item block">
                <span class="filter-name">待拆分：</span>
                <div class="filter-content">
                    技术
                </div>
            </div>
            <div class="filter-item block">
                <span class="filter-name">拆分为：</span>
                <div class="filter-content">
                    <select class="form-control" id="firstSubjectId" style="width: 280px"  nullable="false" name="firstSubjectId">
                        <option value="">--请选择--</option>
					<#if courseList?exists && courseList?size gt 0>
                        <#list courseList as course>
						<option value="${course.id!}" <#if firstSubjectId?default("") == course.id>selected="selected"</#if>>${course.subjectName!}</option>
                        </#list>
                    </#if>
                    </select>
                </div>
            </div>
            <div class="filter-item block">
                <span class="filter-name">拆分为：</span>
                <div class="filter-content">
                    <select class="form-control" id="secondSubjectId" style="width: 280px" nullable="false" name="secondSubjectId">
                        <option value="">--请选择--</option>
					    <#if courseList?exists && courseList?size gt 0>
                            <#list courseList as course>
						<option value="${course.id!}" <#if secondSubjectId?default("") == course.id>selected="selected"</#if>>${course.subjectName!}</option>
                            </#list>
                        </#if>
                    </select>
                </div>
            </div>
        </div>
    </div>
</form>

<#-- 确定和取消按钮 -->
<div style="text-align: center;">
    <a class="btn btn-blue" id="divide-commit">确定</a>
    <a class="btn btn-white" id="divide-close">取消</a>
</div>

<script type="text/javascript">
    // 取消按钮操作功能
    $("#divide-close").on("click", function(){
        doLayerOk("#divide-commit", {
            redirect:function(){},
            window:function(){layer.closeAll()}
        });
    });

    // 确定按钮操作功能
    var isSubmit=false;
    $("#divide-commit").on("click", function(){
        if(isSubmit){
            return;
        }

        isSubmit=true;
        /*$(this).addClass("disabled");
        var check = checkValue('.divideDiv');
        if(!check){
            $(this).removeClass("disabled");
            isSubmit=false;
            return;
        }*/
        var divideId1=$("#firstSubjectId").val();
        var divideId2=$("#secondSubjectId").val();
        if ("" == divideId1) {
            layer.tips('请选择课程', $("#firstSubjectId"), {
                tipsMore: true,
                tips:3
            });
            $(this).removeClass("disabled");
            isSubmit=false;
            return;
        }
        if ("" == divideId2) {
            layer.tips('请选择课程', $("#secondSubjectId"), {
                tipsMore: true,
                tips:3
            });
            $(this).removeClass("disabled");
            isSubmit=false;
            return;
        }
        if(divideId1==divideId2){
            layer.tips('不能选择跟分班1一样的方案', $("#divideId2"), {
                tipsMore: true,
                tips:3
            });
            $(this).removeClass("disabled");
            isSubmit=false;
            return;
        }
        // 提交数据
        var ii = layer.load();

        $.ajax({
            url :"${request.contextPath}/newgkelective/${divideId!}/divide/saveSplit",
            data:{"splitSubjectCode":"3037","firstSubjectId":$("#firstSubjectId").val(),"secondSubjectId":$("#secondSubjectId").val()},
            dataType: "json",
            success:function(data) {
                layer.close(ii);
                if(data.success){
                    layer.closeAll();
                    layerTipMsg(data.success,"拆分成功","" + data.msg);
                    isSubmit=false;
                }else{
                    layer.closeAll();
                    layerTipMsg(data.success,"拆分失败","" + data.msg);
                    isSubmit=false;
                }
            },
            type:'post',
            error:function(XMLHttpRequest, textStatus, errorThrown){}
        });
    });

</script>