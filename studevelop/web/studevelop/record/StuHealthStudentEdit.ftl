<title>身心健康详情</title>
<div class="layer layer-addTerm layer-change" style="display:block;" id="myDiv">
    <form id="subForm">
        <input type="hidden" name="id" id="id" value="${healthStudent.id!}">
        <input type="hidden" name="studentId" id="studentId" value="${healthStudent.studentId!}">
        <input type="hidden" name="schoolId" id="schoolId" value="${healthStudent.schoolId!}">
        <input type="hidden" name="classId" id="classId" value="${healthStudent.classId!}">
        <input type="hidden" name="acadyear" id="acadyear" value="${healthStudent.acadyear!}">
        <input type="hidden" name="semester" id="semester" value="${healthStudent.semester!}">
        <input type="hidden" name="creationTime" id="creationTime" value="${(healthStudent.creationTime?string('yyyy-MM-dd HH:mm:ss'))?default('')}">
        <div class="layer-body">
            <div class="filter clearfix">
                <div class="filter clearfix" style="scroll">
                    <table class="table table-bordered table-striped table-hover no-margin">
                        <#assign ind =0 >
                        <#list detailList as mcodeDetail >
                            <tr>
                                <th colspan="2" style="text-align:center;font-size:20px">${mcodeDetail.mcodeContent!}</th>
                            </tr>


                            <#if healthProjectMap[mcodeDetail.thisId]?exists && healthProjectMap[mcodeDetail.thisId]?size gt 0>
                                <#assign projectList = healthProjectMap[mcodeDetail.thisId] >
                                <#list projectList as project>
                                    <tr>
                                        <th>${project.projectName!} <#if project.projectUnit?default('') !=''>(${project.projectUnit?default('')})</#if>：</th>
                                        <td>
                                            <input type="hidden" name="healthStudentDetail[${ind!}].projectId" value="${project.id!}" >
                                            <input name="healthStudentDetail[${ind!}].projectValue"  id="projectValue${ind!}"   maxLength="30"  value="${project.projectValue!}">

                                        </td>
                                    </tr>
                                    <#assign ind =ind + 1 >
                                </#list>
                            </#if>


                        </#list>
                        <tr>
                            <td colspan="2" align="center"> <a class="btn btn-lightblue" id="arrange-commit">保存</a>
                        <#--<a class="btn btn-grey" id="arrange-close">取消</a>-->
                            </td>
                        </tr>
                    </table>
                    <#--<div class="layer-footer">-->
                        <#--<button class="btn btn-lightblue" id="arrange-commit">保存</button>-->
                        <#--<button class="btn btn-grey" id="arrange-close">取消</button>-->
                    <#--</div>-->
                </div>
            </div>
        </div>
    </form>
</div>


<script>
    $(function(){
        var isSubmit = false;

        $("#arrange-close").on("click", function(){
            changeStuId();
        });

        $("#arrange-commit").on("click", function(){
//            if(isSubmit){
//                return;
//            }

            isSubmit = true;
            var stuId = $("#stuId").val();
            if(stuId == ""){
                layerTipMsg(false,"请选择一个学生!","");
                isSubmit=false;
                return;
            }

            var check = checkValue('#subForm');
            if(!check){
                isSubmit=false;
                return;
            }

            var options = {
                url : "${request.contextPath}/stuDevelop/healthyStudent/save",
                dataType : 'json',
                success : function(data){
                    var jsonO = data;
                    if(!jsonO.success){
                        layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
                        $("#arrange-commit").removeClass("disabled");
                        isSubmit = false;
                        return;
                    }else{
                        layer.closeAll();
                        layerTipMsg(jsonO.success,"保存成功",jsonO.msg);
                       // isSubmit = true;
//                        changeStuId();
                        return;
                    }
                },
                clearForm : false,
                resetForm : false,
                type : 'post',
                error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错
            };
            $("#subForm").ajaxSubmit(options);

        });

    });
</script>