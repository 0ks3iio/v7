
<#macro tdInput ind=0 project=""  firstRow=false  size="" result=.data_model  >
    <td <#if firstRow >rowspan="${size}" </#if> >
        <input type="hidden" name="resultList[${ind!}].templateItemId" value="${result.templateItemId!}" >
        <input type="hidden" name="resultList[${ind!}].templateId" value="${result.templateId!}" >
        <input type="hidden" name="resultList[${ind!}].id" value="${result.id!}" >
        <input type="hidden" name="resultList[${ind!}].studentId" value="${result.studentId!}" >
        <input type="hidden" name="resultList[${ind!}].subjectId" value="${result.subjectId!}" >
        <input type="hidden" name="resultList[${ind!}].categoryId" value="${result.categoryId!}" >
        <input type="hidden" name="resultList[${ind!}].acadyear" value="${result.acadyear!}" >
        <input type="hidden" name="resultList[${ind!}].semester" value="${result.semester!}" >
        <input type="hidden" name="resultList[${ind!}].creationTime" value="${result.creationTime!}" >
        <#if project.singleOrInput == '1'>
            <input name="resultList[${ind!}].result"  id="result${ind!}"   maxLength="30"  value="${result.result!}">
        <#else>
            <#if project.templateOptions?exists>
                <#assign optionsList = project.templateOptions >
            </#if>
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
</#macro>