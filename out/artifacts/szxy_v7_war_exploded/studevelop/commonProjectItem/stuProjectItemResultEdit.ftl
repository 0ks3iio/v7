<title>身心健康详情</title>
<div class="layer layer-addTerm layer-change" style="display:block;" id="myDiv">
    <form id="subForm">

        <div class="layer-body">
            <div class="filter clearfix">
                <div class="filter clearfix" style="scroll">
                    <table class="table table-bordered table-striped table-hover no-margin">
                        <#assign ind =0 >
                        <#if code?default('2') == '2' >
                            <#list detailList as mcodeDetail >
                            <tr>
                                <th colspan="2" style="text-align:center;font-size:20px">${mcodeDetail.mcodeContent!}</th>
                            </tr>
                                <#if templateItemMap[code+mcodeDetail.thisId]?exists && templateItemMap[code+mcodeDetail.thisId]?size gt 0>
                                    <#assign projectList = templateItemMap[code+mcodeDetail.thisId] >
                                    <#list projectList as project>
                                    <tr>
                                        <th style="word-break: break-all;word-wrap: break-word;" >${project.itemName!} ：</th>

                                        <td>
                                            <#assign result = project.templateResult />
                                            <input type="hidden" name="resultList[${ind!}].templateItemId" value="${result.templateItemId!}" >
                                            <input type="hidden" name="resultList[${ind!}].templateId" value="${result.templateId!}" >
                                            <input type="hidden" name="resultList[${ind!}].id" value="${result.id!}" >
                                            <input type="hidden" name="resultList[${ind!}].studentId" value="${result.studentId!}" >
                                            <input type="hidden" name="resultList[${ind!}].creationTime" value="${result.creationTime!}" >
                                            <input type="hidden" name="resultList[${ind!}].acadyear" value="${result.acadyear!}" >
                                            <input type="hidden" name="resultList[${ind!}].semester" value="${result.semester!}" >
                                            <#if project.singleOrInput == '1'>
                                                <input name="resultList[${ind!}].result"  id="result${ind!}"   maxLength="30"  value="${result.result!}">
                                            <#else>
                                                <#assign optionsList = project.templateOptions >
                                            <div class="filter-item">
                                                <div class="filter-content">
                                                    <select vtype="selectOne" class="form-control" name="resultList[${ind!}].templateOptionId" id="" ">
                                                        <#if optionsList?? && (optionsList?size>0)>
                                                            <#list optionsList as item>
                                                            <option value="${item.id}" <#if item.id == result.templateOptionId?default('') >selected</#if> >${item.optionName!}</option>
                                                            </#list>
                                                        <#else>
                                                              <option value="">暂无数据</option>
                                                        </#if>
                                                    </select>
                                                </div>
                                            </div>
                                            </#if>

                                        </td>


                                    </tr>
                                        <#assign ind =ind + 1 >
                                    </#list>
                                </#if>
                            </#list>
                        <#else>
                            <tr>
                                <th colspan="2" style="text-align:center;font-size:20px">思想素质</th>
                            </tr>
                                <#if templateItemMap[code+1]?exists && templateItemMap[code+1]?size gt 0>
                                    <#assign projectList = templateItemMap[code+1] >
                                    <#list projectList as project>
                                    <tr>
                                        <th width="80%"style="word-break: break-all;word-wrap: break-word;" >${project.itemName!} ：</th>

                                        <td width="20%">
                                            <#assign result = project.templateResult />
                                            <input type="hidden" name="resultList[${ind!}].templateItemId" value="${result.templateItemId!}" >
                                            <input type="hidden" name="resultList[${ind!}].templateId" value="${result.templateId!}" >
                                            <input type="hidden" name="resultList[${ind!}].id" value="${result.id!}" >
                                            <input type="hidden" name="resultList[${ind!}].studentId" value="${result.studentId!}" >
                                            <input type="hidden" name="resultList[${ind!}].creationTime" value="${result.creationTime!}" >
                                            <input type="hidden" name="resultList[${ind!}].acadyear" value="${result.acadyear!}" >
                                            <input type="hidden" name="resultList[${ind!}].semester" value="${result.semester!}" >
                                            <#if project.singleOrInput == '1'>
                                                <input name="resultList[${ind!}].result"  id="result${ind!}"   maxLength="30"  value="${result.result!}">
                                            <#else>
                                                <#assign optionsList = project.templateOptions >
                                            <div class="filter-item">
                                                <div class="filter-content">
                                                    <select vtype="selectOne" class="form-control" name="resultList[${ind!}].templateOptionId" id="" ">
                                                        <#if optionsList?? && (optionsList?size>0)>
                                                            <#list optionsList as item>
                                                            <option value="${item.id}" <#if item.id == result.templateOptionId?default('') >selected</#if> >${item.optionName!}</option>
                                                            </#list>
                                                        <#else>
                                                              <option value="">暂无数据</option>
                                                        </#if>
                                                    </select>
                                                </div>
                                            </div>
                                            </#if>

                                        </td>


                                    </tr>
                                        <#assign ind =ind + 1 >
                                    </#list>
                                </#if>
                        </#if>

                        <tr>
                            <td colspan="2" align="center"> <a class="btn btn-blue" id="arrange-commit">保存</a>
                            </td>
                        </tr>
                    </table>
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
           if(isSubmit){
               return;
           }

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
			var classId=$("#classId").val();
			var isAdmin=$("#isAdmin").val();
   		     var options = {
                url : "${request.contextPath}/stuDevelop/proItemResult/save?classId="+classId+"&isAdmin="+isAdmin,
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
                        layer.msg(jsonO.msg, {
							offset: 't',
							time: 2000
						});
                        isSubmit = false;
//                      changeStuId();
                        doSearch('${code!}');
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