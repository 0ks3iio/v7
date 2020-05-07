<form id="myform">
    <div class="add-div">
        <div class="filter">
            <div class="filter-item">
                <span class="filter-name">考试：</span>
                <div class="filter-content">
                    <select class="form-control" id="examId" style="width:280px" nullable="false" name="examId" onChange="showGrade()">
                        <option value="">--请选择--</option>
                        <#if examList?exists && examList?size gt 0>
                            <#list examList as item>
                                <option value="${item.id!}">${item.examName!}</option>
                            </#list>
                        </#if>
                    </select>
                </div>
            </div>
            <div class="filter-item">
                <span class="filter-name">年级：</span>
                <div class="filter-content">
                    <select class="form-control" id="gradeId" style="width: 280px"  nullable="false" name="gradeId">
                        <option value="">--请选择--</option>
                    </select>
                </div>
            </div>
            <div class="filter-item" style="margin-top: 30px">
                <div class="filter-content text-center">
                    <label>
                    <input type="checkbox" class="wp copy-last" value="1"/>
                    <span class="lbl">复制该年级考试方案的上一时间点汇总表，并按照学生名单只更新科目成绩</span>
                    </label>
                </div>
            </div>
        </div>
    </div>
</form>

<#-- 确定和取消按钮 -->
<div class="layer-footer">
    <a class="btn btn-lightblue" id="btn-commit">保存并统计</a>
    <a class="btn btn-grey" id="btn-close">取消</a>
</div>

<script type="text/javascript">

    function showGrade(){
        $("#gradeId").html("");
        var gradeHtml='<option value="">--请选择--</option>';
        var examId=$("#examId").val();
        if(examId!=""){
            $.ajax({
                url:"${request.contextPath}/scoremanage/plan/getGradeList",
                data:{examId: examId},
                dataType: "json",
                async: false,
                success: function(data){
                    if(data && data.length>0){
                        for(var i=0;i<data.length;i++){
                            gradeHtml+='<option value="'+data[i].id+'">';
                            gradeHtml+=data[i].gradeName;
                            gradeHtml+='</option>';
                        }
                    }
                }
            });
        }
        $("#gradeId").html(gradeHtml);
    }

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

        var examId = $("#examId").val();
        var gradeId = $("#gradeId").val();
        var copyLast = $("input.copy-last:checked").val();
        // 提交数据
        var ii = layer.load();
        isSubmit=true;
        $.ajax({
            url :"${request.contextPath}/scoremanage/plan/save",
            data:{acadyear:'${acadyear!}', semester: '${semester!}', examId:examId, gradeId:gradeId, copyLast:copyLast},
            dataType: "json",
            success:function(data) {
                layer.closeAll();
                if(data.success){
                    layer.msg(data.msg,{
                        offset: 't',
                        time: 2000
                    });
                    var url1 =  '${request.contextPath}/scoremanage/plan/list/page?acadyear=${acadyear!}&semester=${semester!}';
                    $("#showItemDiv").load(url1);
                }else{
                    layerTipMsg(data.success,"失败",data.msg);
                    $("#btn-commit").removeClass("disabled");
                    isSubmit=false;
                }

            },
            type:'post',
            error:function(XMLHttpRequest, textStatus, errorThrown){}
        });



    });

</script>

