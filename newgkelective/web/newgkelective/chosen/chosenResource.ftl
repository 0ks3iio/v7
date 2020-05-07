<div id="logListId">
    <table class="table table-bordered table-striped no-margin">
        <thead>
        <tr>
            <th>科目</th>
            <th>类型</th>
            <th>选择人数</th>
            <th>理想开班数</th>
            <th>平均人数</th>
            <th>科目课时</th>
            <th>总课时</th>
            <th>合计课时</th>
            <th>教师数</th>
            <th>平均课时数</th>
        </tr>
        </thead>
        <tbody>
        <#if resultList?exists && resultList?size gt 0>
            <#list resultList as result>
                <tr>
                    <td rowspan="2">${result.courseName!}</td>
                    <td>选考</td>
                    <td>${result.examChosenNum!}</td>
                    <td>${result.examClassNum!}</td>
                    <td>${result.examClassStudentAverage!}</td>
                    <td>${result.examTimeCount!}</td>
                    <td>${result.examTotalTimeCount!}</td>
                    <td rowspan="2">${result.totalTimeCount!}</td>
                    <td rowspan="2">${result.teacherCount!}</td>
                    <td rowspan="2">${result.totalTimeAverage!}</td>
                </tr>
                <tr>
                    <td>学考</td>
                    <td>${result.studyChosenNum!}</td>
                    <td>${result.studyClassNum!}</td>
                    <td>${result.studyClassStudentAverage!}</td>
                    <td>${result.studyTimeCount!}</td>
                    <td>${result.studyTotalTimeCount!}</td>
                </tr>
            </#list>
        <#else>
            <tr>
                <td colspan="10">无任何数据</td>
            </tr>
        </#if>
        </tbody>
    </table>
</div>