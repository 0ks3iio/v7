<#if classDtos?exists&&(classDtos?size>0)>
<table class="table table-bordered eva-inquiry-table">
    <thead>
    <tr>
        <th>班级</th>
        <th>学生学业发展水平</th>
        <th>综合素质发展水平</th>
    </tr>
    </thead>
    <tbody>
    <#list classDtos as item>
        <tr>
            <td>${item.className!}</td>
            <td>42.158</td>
            <td>42.158</td>
        </tr>
    </#list>
    </tbody>
</table>
<#else >
    <div class="no-data-container">
        <div class="no-data">
                        <span class="no-data-img">
                            <img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
                        </span>
            <div class="no-data-body">
                <p class="no-data-txt">暂无数据</p>
            </div>
        </div>
    </div>
</#if>