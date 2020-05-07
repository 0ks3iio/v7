<div class="table-show scrollBar4">
    <div class="filter-made mb-10">
        <div class="filter-item filter-item-right clearfix">
            <button class="btn btn-lightblue" onclick="editDataxJob('');">新增</button>
        </div>
    </div>
    <table class="tables tables-border no-margin">
        <thead>
        <tr>
            <th>任务名称</th>
            <th>定时执行</th>
            <th>上次执行时间</th>
            <th>执行状态</th>
            <th>执行结果</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <#if dataxJobList?exists&&dataxJobList?size gt 0>
        <#list dataxJobList as job>
            <tr>
                <td>${job.name!}</td>
                <td><#if job.isSchedule?default(0) ==1>是<#else>否</#if></td>
                <td id="jobTime_${job.id!}"><#if job.dataxJobInsLog?exists>${job.dataxJobInsLog.startTime!}</#if></td>
                <td id="jobStatus_${job.id!}">
                    <#if job.dataxJobInsLog?exists>
                        <#if job.dataxJobInsLog.status?default(0) == 1>
                            <span class="success-left">执行完成</span>
                        <#else>
                            <span style="white-space: nowrap;" class="running_job" job_id="${job.id!}"><img
                                        src="${request.contextPath}/static/images/big-data/doing-icon.png"/>&nbsp;执行中...</span>
                        </#if>
                    <#else>
                        <span style="white-space: nowrap;">&nbsp;<img
                                    src="${request.contextPath}/static/images/big-data/not-begin-icon.png"/>&nbsp;&nbsp;未开始</span>
                    </#if>
                </td>
                <td id="jobResult_${job.id!}">
                    <#if job.dataxJobInsLog?exists>
                        <#if job.dataxJobInsLog.result?exists>
                            <#if job.dataxJobInsLog.result == 1>
                                <span class="success-left">成功</span>
                            <#else>
                                <span class="fail-left">失败</span>
                            </#if>
                        <#else>
                            <span style="white-space: nowrap;"><img
                                        src="${request.contextPath}/static/images/big-data/doing-icon.png"/>&nbsp;执行中...</span>


                        </#if>
                    <#else>
                        <span style="white-space: nowrap;">&nbsp;<img
                                    src="${request.contextPath}/static/images/big-data/not-begin-icon.png"/>&nbsp;&nbsp;未开始</span>
                    </#if>
                </td>
                <td style="white-space: nowrap;">
                    <a href="javascript:;" class="look-over" job_id="${job.id!}" onclick="executeDataxJob('${job.id!}')">执行</a><span
                            class="tables-line">|</span>
                    <a href="javascript:;" class="look-over" onclick="editDataxJob('${job.id!}')">编辑</a><span
                            class="tables-line">|</span>
                    <a href="javascript:;" class="" onclick="editDataxJobIns('${job.id!}')">配置</a><span
                            class="tables-line">|</span>
                    <a href="javascript:;" class="delete" onclick="deleteDataxJob('${job.id!}')">删除</a><span
                            class="tables-line">|</span>
                    <a id="log-${job.id!}-button" href="javascript:;" class="look-over js-log-show"
                       onclick="loadDataxLogList('${job.id!}');">日志</a>
                    <span class="tables-line">|</span>
                    <a href="javascript:;" onclick="previewJson('${job.id!}')">预览配置</a>
                </td>
            </tr>
        </#list>
        <#else>
        <tr>
            <td colspan="6" align="center">
                暂无datax任务数据
            </td>
        <tr>
            </#if>
        </tbody>
    </table>
</div>
<div id="logListDiv" class="log-show height-1of1 col-md-3 no-padding-right hide"></div>
<div id="logDiv" style="padding:20px 20px 0;border:1px;"></div>
<div class="layer layer-json">
</div>
<script src="${request.contextPath}/static/bigdata/editor/ace.js"/>
<script>
    var jobMap = {};
    $(function () {
        $('.table-show').css({
            height: $('.page-content').height()
        });

        // 定时刷新状态
        // $.each($('.running_job'), function (i, v) {
        //     monitorJob($(v).attr('job_id'));
        // });

    });

    function editDataxJob(id) {
        var name = id == '' ? '新增' : '编辑';
        router.go({
            path: '/bigdata/datax/job/edit?dataxJobId=' + id,
            name: name,
            level: 3
        }, function () {
            var url = '${request.contextPath}/bigdata/datax/job/edit?dataxJobId=' + id;
            $(".page-content").load(url);
        });
    }

    function editDataxJobIns(id) {
        router.go({
            path: '/bigdata/datax/job/editJobInstance?dataxJobId=' + id,
            name: '任务配置',
            level: 3
        }, function () {
            var url = '${request.contextPath}/bigdata/datax/job/editJobInstance?dataxJobId=' + id;
            $(".page-content").load(url);
        });
    }

    function deleteDataxJob(id) {

        showConfirmTips('prompt', "提示", "您确定要删除该任务吗？", function () {
            // 执行任务
            $.ajax({
                url: _contextPath + '/bigdata/datax/job/deleteDataxJob',
                type: 'POST',
                dataType: 'json',
                data: {jobId: id},
                success: function (response) {
                    if (response.success) {
                        showLayerTips('success', '删除成功', 't');
                        $('.page-content').load(_contextPath + '/bigdata/etl/datax');
                    } else {
                        showLayerTips4Confirm('error', response.message);
                    }
                }
            });
        }, function () {

        });
    }

    function executeDataxJob(id) {

        // 执行任务
        $.ajax({
            url: _contextPath + '/bigdata/datax/job/executeJob',
            type: 'POST',
            dataType: 'json',
            data: {jobId: id},
            success: function (response) {
                if (response.success) {
                    $('#jobStatus_' + id).empty().append('<img src="${request.contextPath}/static/images/big-data/doing-icon.png" />&nbsp;执行中...');
                    $('#jobResult_' + id).empty().append('<img src="${request.contextPath}/static/images/big-data/doing-icon.png" />&nbsp;执行中...');
                    window.setTimeout(function () {
                        monitorJob(id);
                    }, 5000);
                } else {
                    showLayerTips4Confirm('error', response.message);
                }
            }
        });

    }

    function monitorJob(id) {

        $.ajax({
            url: _contextPath + '/bigdata/datax/job/monitorJob',
            type: 'POST',
            dataType: 'json',
            data: {jobId: id},
            success: function (response) {
                if (response.success) {
                    if (response.data.status == 1) {
                        $('#jobStatus_' + id).empty().append('<span class="success-left">执行完成</span>');

                        if (response.data.result == 1) {
                            $('#jobResult_' + id).empty().append('<span class="success-left">成功</span>');
                        } else {
                            $('#jobResult_' + id).empty().append('<span class="fail-left">失败</span>');
                        }
                        var unixTimestamp = new Date(response.data.startTime) ;
                        $('#jobTime_' + id).empty().append(formatDateTime(unixTimestamp));
                    } else {
                        $('#jobStatus_' + id).empty().append('<img src="${request.contextPath}/static/images/big-data/doing-icon.png" />&nbsp;执行中...');
                        window.setTimeout(function () {
                            monitorJob(id);
                        }, 5000);
                    }
                } else {
                    showLayerTips4Confirm('error', response.message);
                }
            }
        });
    }

    function previewJson(id) {

        var url = _contextPath + '/bigdata/datax/job/previewJobJson?jobId=' + id;

        $(".layer-json").html("<div class='pos-middle-center'><h4><img src='${request.contextPath}/static/ace/img/loading.gif' />加载中......</h4></div>");
        $(".layer-json").load(url);
        layer.open({
            type: 1,
            shade: .5,
            title: ['参数配置实例说明', 'font-size:16px'],
            area: ['900px', '600px'],
            maxmin: false,
            btn: ['确定'],
            content: $('.layer-json'),
            resize: true,
            yes: function (index) {
                layer.closeAll();
            },
            cancel: function (index) {
                layer.closeAll();
            }
        });
        $("#paramDiv").parent().css('overflow', 'auto');
    }

    function loadDataxLogList(id) {
        var logButtonObj = $("#log-" + id + "-button");
        if (logButtonObj.text() == '日志') {
            logButtonObj.parents('.table-show').addClass('col-md-9 no-padding-left');
            $('.log-show').removeClass('hide');
            logButtonObj.text('关闭');
            if (logButtonObj.parents('tr').siblings('tr').find('.js-log-show').text().indexOf('关闭') != -1) {
                logButtonObj.parents('tr').siblings('tr').find('.js-log-show').text('日志')
            }
            var logUrl = "${request.contextPath}/bigdata/datax/job/log?jobId=" + id;
            $("#logListDiv").load(logUrl);
        } else {
            logButtonObj.parents('.table-show').removeClass('col-md-9 no-padding-left');
            $('.log-show').addClass('hide');
            logButtonObj.text('日志');
            $("#logListDiv").empty();
        }
    }

    function formatDateTime(inputTime) {
        var date = new Date(inputTime);
        var y = date.getFullYear();
        var m = date.getMonth() + 1;
        m = m < 10 ? ('0' + m) : m;
        var d = date.getDate();
        d = d < 10 ? ('0' + d) : d;
        var h = date.getHours();
        h = h < 10 ? ('0' + h) : h;
        var minute = date.getMinutes();
        var second = date.getSeconds();
        minute = minute < 10 ? ('0' + minute) : minute;
        second = second < 10 ? ('0' + second) : second;
        return y + '-' + m + '-' + d+' '+h+':'+minute+':'+second;
    };
</script>