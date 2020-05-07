<input type="hidden" name="batch" id="teachClassId" value="${teachClassId!}"/>

<div style="height: 380px; overflow: auto;">
    <table class="table table-bordered table-striped table-hover">
        <thead>
        <tr>
            <th>序号</th>
            <th>学号</th>
            <th>姓名</th>
            <th>性别</th>
            <th>行政班</th>
            <th><#if subjectType?default("A") == "A">选考<#else>学考</#if>科目</th>
        </tr>
        </thead>
        <tbody>
        <#if studentList?exists && studentList?size gt 0>
            <#list studentList as student>
                <tr>
                    <td class="index_i">${student_index + 1}</td>
                    <td>${student.studentCode!}</td>
                    <td>${student.studentName!}</td>
                    <td>${student.sex!}</td>
                    <td>${student.className!}</td>
                    <td>${student.chooseSubjects!}</td>
                </tr>
            </#list>
        </#if>
        </tbody>
    </table>
</div>

<script src="${request.contextPath}/static/ace/js/excanvas.js"></script>

