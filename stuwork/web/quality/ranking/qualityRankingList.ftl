<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<table id="example" class="table table-bordered">
    <thead>
    <tr>
        <th>序号</th>
        <th>姓名</th>
        <th>学号</th>
        <th>班级</th>
        <th>综合素质总分</th>
        <th>综合素质总分年级排名</th>
        <th>英语笔试总折分</th>
        <th>英语笔试总折分年级排名</th>
        <th>总排名分数</th>
        <th>总排名分年级排名</th>
        <th>总排名分班级排名</th>
    </tr>
    </thead>
    <tbody>
    <#if dtoList?exists&&dtoList?size gt 0>
    <#list dtoList as item>
        <tr>
            <td>${item_index+1}</td>
            <td>${item.studentName!}</td>
            <td>${item.studentCode!}</td>
            <td>${item.className!}</td>
            <td>${item.compreScore?string("0.##")}</td>
            <td>${item.compreGradeRank!}</td>
            <td>${item.englishScore?string("0.##")}</td>
            <td>${item.englishGradeRank!}</td>
            <td>${item.totalScore?string("0.##")}</td>
            <td>${item.gradeRank!}</td>
            <td>${item.classRank!}</td>
        </tr>
    </#list>
    <#else>
    <tr>
        <td  colspan="11" align="center">
            暂无数据
        </td>
    <tr>
        </#if>
    </tbody>
</table>
<#if dtoList?exists&&dtoList?size gt 0>
    <@htmlcom.pageToolBar container="#showListDiv" class="noprint"/>
</#if>