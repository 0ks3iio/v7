<div style="overflow: auto">
    <table class="table table-striped table-bordered table-hover no-margin mainTable tablesorter" style="font-size:1em;">
        <thead>
        <tr>
            <th colspan="30" style="text-align: center">
                ${title!}
            </th>
        </tr>
        <tr>
            <th>序号</th>
            <th>班级</th>
            <th>姓名</th>
            <#if courseNameList?? && courseNameList?size gt 0>
                <#list courseNameList as item>
                    <th>${item!}</th>
                </#list>
            </#if>
        </tr>
        </thead>
        <tbody>
        <#if studentList?exists && studentList?size gt 0>
            <#list studentList as student>
                <tr>
                    <td>${student_index + 1}</td>
                    <td>${student.className!}</td>
                    <td>${student.studentName!}</td>
                    <#if courseNameList?? && courseNameList?size gt 0>
                        <#list courseNameList as item>
                            <td>
                                <#if infoMap[student.id]?exists && infoMap[student.id][item]?exists>
                                    ${infoMap[student.id][item].score}
                                </#if>
                            </td>
                        </#list>
                    </#if>
                </tr>
            </#list>
        </#if>
        </tbody>
    </table>
</div>