<#if jobs?exists && jobs?size gt 0>
    <table class="tables">
        <thead>
        <tr>
            <th>开始时间</th>
            <th>结束时间</th>
            <th>持续时间</th>
            <th>Job名称</th>
            <th>JobID</th>
            <th>状态</th>
        </tr>
        </thead>
        <tbody class="kanban-content">
        <#list jobs as job>
                <tr>
                    <td>${job.startTime?number?number_to_datetime}</td>
                    <td>${job.endTime?number?number_to_datetime}</td>
                    <td>${job.duration!}</td>
                    <td>${job.name!}</td>
                    <td>${job.jid!}</td>
                    <td>
                        <#if job.state=='FINISHED'>
                            <span class="badge badge-success badge-sm">成功</span>
                        <#elseif job.state=='CANCELED'>
                            <span class="badge badge-warning badge-sm">取消</span>
                        <#elseif job.state=='FAILED'>
                            <span class="badge badge-danger badge-sm">失败</span>
                        <#else >${job.state!}
                        </#if>
                    </td>
                </tr>
        </#list>
        </tbody>
    </table>
<#else>
    <div class="no-data-common">
        <div class="text-center">
            <img src="${request.contextPath}/bigdata/v3/static/images/public/no-data-common-100.png"/>
            <p class="color-999">
                暂无Job信息
            </p>
        </div>
    </div>
</#if>