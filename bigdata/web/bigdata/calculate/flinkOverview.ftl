<div class="two-part-wrap clearfix">
    <div class="two-part-box mr-20">
        <ul class="content-detail">
            <li>
                <svg class="icon" aria-hidden="true">
                    <use xlink:href="#icon-run"></use>
                </svg>
                <h3>${overview.taskmanagers!}</h3>
                <p>集群机器数</p>
            </li>
            <li>
                <svg class="icon" aria-hidden="true">
                    <use xlink:href="#icon-folder-fill1"></use>
                </svg>
                <h3>${overview.slotsTotal!}</h3>
                <p>总的计算节点数</p>
            </li>
            <li>
                <svg class="icon" aria-hidden="true">
                    <use xlink:href="#icon-folder-bell"></use>
                </svg>
                <h3>${overview.slotsAvailable!}</h3>
                <p>可用的计算节点数</p>
            </li>
        </ul>
    </div>
    <div class="two-part-box">
        <ul class="list-group job-detail no-margin">
            <li class="list-group-item">
                <b>job状态监控</b>
            </li>
            <li class="list-group-item">
                <span class="badge badge-info">${overview.jobsRunning!}</span>
                运行中
            </li>
            <li class="list-group-item">
                <span class="badge badge-success">${overview.jobsFinished!}</span>
                已完成
            </li>
            <li class="list-group-item">
                <span class="badge badge-warning">${overview.jobsCancelled!}</span>
                已取消
            </li>
            <li class="list-group-item">
                <span class="badge badge-danger">${overview.jobsFailed!}</span>
                已失败
            </li>
        </ul>
    </div>
</div>

<div class="table-detail">
    <div class="table-detail-title">
        <b>运行中的job</b>
    </div>
    <div class="">
        <#if runningjobs?exists && runningjobs?size gt 0>
            <table class="tables">
                <thead>
                <tr>
                    <th>开始时间</th>
                    <th>持续时间</th>
                    <th>job名称</th>
                    <th>jobID</th>
                    <th>状态</th>
                </tr>
                </thead>
                <tbody>
                <#list runningjobs as job>
                        <tr>
                            <td>${job.startTime?number?number_to_datetime}</td>
                            <td>${job.duration!}</td>
                            <td>${job.name!}</td>
                            <td>${job.jid!}</td>
                            <td><span class="badge badge-primary badge-sm">运行中</span></td>
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
    </div>
</div>
<div class="table-detail">
    <div class="table-detail-title">
        <b>已完成的job</b>
    </div>
    <div class="">
        <#if completedjobs?exists && completedjobs?size gt 0>
            <table class="tables">
                <thead>
                <tr>
                    <th>开始时间</th>
                    <th>结束时间</th>
                    <th>持续时间</th>
                    <th>job名称</th>
                    <th>jobID</th>
                    <th>状态</th>
                </tr>
                </thead>
                <tbody>
                <#list completedjobs as job>
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
    </div>
</div>