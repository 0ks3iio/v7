<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<div class="table-container">
    <div class="table-container-header">共有<#if scoreList?exists>${scoreList?size}<#else>0</#if>个数据</div>
    <div class="table-container-body">
        <form id="checkForm">
            <div style="overflow: auto">
            <table class="table table-bordered table-striped table-hover">
                <thead>
                <tr>
                    <th>序号</th>
                    <th>学号</th>
                    <th>姓名</th>
                    <th>班级</th>
                    <#if courseList?exists && courseList?size gt 0>
                        <#list courseList as course>
                            <th>${course.subjectName!}</th>
                        </#list>
                    </#if>
                    <#if type != "5">
                        <th>排名</th>
                    </#if>
                </tr>
                </thead>
                <tbody>
                <#if scoreList?exists && scoreList?size gt 0>
                    <#list scoreList as item>
                    <tr>
                        <td>${item_index+1}</td>
                        <td>${item.studentCode!}</td>
                        <td>${item.studentName!}</td>
                        <td>${item.className!}</td>
                        <input type="hidden" name="scoreList[${item_index}].studentId" value="${item.studentId!}">
                        <#--<input type="hidden" name="scoreList[${item_index}].classId" value="${item.classId!}">-->
                        <#if item.scoreMap?exists && item.scoreMap?size gt 0>
                            <#if courseList?exists && courseList?size gt 0>
                                <#list courseList as course>
                                    <td id="scoreList${item_index}${course_index}" value=""  maxlength="1" name="scoreList[${item_index}].scoreMap['${course.id}']" nullable="true" style="width: 75px">${item.scoreMap[course.id]!}</td>
                                </#list>
                            </#if>
                        <#else>
                            <#if courseList?exists && courseList?size gt 0>
                                <#list courseList as course>
                                    <td id="scoreList${item_index}${course_index}" value=""  maxlength="1" name="scoreList[${item_index}].scoreMap['${course.id}']" nullable="true" style="width: 75px"></td>
                                </#list>
                            </#if>
                        </#if>
                        <#if type != "5">
                        <td>${item.ranking!}</td>
                        </#if>
                    </tr>
                    </#list>
                <#else>
                    <#if isCounted?default(false)>
                    <tr>
                        <td colspan="14" align="center">暂无数据</td>
                    </tr>
                    <#else>
                    <tr>
                        <td colspan="16" align="center">请先进行统计</td>
                    </tr>
                    </#if>
                </#if>
                </tbody>
            </table>
            </div>
            <#if scoreList?exists && scoreList?size gt 0>
                <@htmlcom.pageToolBar container="#itemDetailDiv" >
                </@htmlcom.pageToolBar>
            </#if>
        </form>
    </div>
</div>

<script>
$(function(){
	<#if isCounted?default(false)>
		$("#countButton").hide();
	<#else>
		$("#countButton").show();
	</#if>
})
</script>