<div class="monitoring-wrap clearfix">
    <div class="left scrollBar4">
        <div class="pos-right">
            <span>主题：</span>
            <select id="topicId" name="topicId" class="form-control" onchange="selectTopic()">
                <#if topicList?exists && topicList?size gt 0>
                    <#list topicList as topic>
                        <option value="${topic.id!}">${topic.name!}</option>
                    </#list>
                </#if>
            </select>
        </div>
        <div class="left-num clearfix">
            <div class="left-num-box no-padding ">任务总量：<span>${allTaskNum}</span></div>
            <div class="left-num-box no-padding blue">正在运行：<span>${runningNum}</span></div>
            <div class="left-num-box no-padding green">成功总量：<span>${successNum}</span></div>
            <div class="left-num-box no-padding red">失败总量：<span>${errorNum}</span></div>
        </div>
        <div class="left-box-wrap">
            <div class="row">

            </div>
        </div>
    </div>

    <div class="right">
        <div class="wrap-full node-box">
            <ul class="data-list">
                <#if errorDataxJobs?exists && errorDataxJobs?size gt 0>
                    <#list errorDataxJobs as datax>
                        <li onclick="viewDataxJobLog('${datax.id!}')" time="${datax.endTime!}" title="${datax.jobName!}">
                            <#if datax.jobName?length gt 7>${datax.jobName?substring(0,8)}...<#else>${datax.jobName!}</#if><span>${datax.endTime!}</span>
                        </li>
                    </#list>
                </#if>
                <#if errorEtlJobs?exists && errorEtlJobs?size gt 0>
                    <#list errorEtlJobs as data>
                        <li onclick="viewJobLog('${data.id!}')" time="${data.logTime!}" title="${data.name!}">
                            <#if data.name?length gt 7>${data.name?substring(0,8)}...<#else>${data.name!}</#if><span>${data.logTime!}</span>
                        </li>
                    </#list>
                </#if>
            </ul>
        </div>
    </div>

    <div class="bottom-explain active hide">
        <div class="bottom-explain-head">
            <b id="jobName">元数据</b>
            <div class="pos-right js-close">
                <i class="wpfont icon-close"></i>
            </div>
        </div>
        <div class="bottom-explain-body clearfix">

        </div>
    </div>

    <div id="logDiv" style="padding:20px 20px 0;border:1px;"></div>
</div>
<script src="${request.contextPath}/static/bigdata/editor/ace.js"/>
<script type="text/javascript">
    $(function(){
        selectTopic();

        var ul = $(".data-list");
        var li = $(".data-list li");
        var array = [];
        for(var i=0;i<li.length;i++){
            array[i] = li[i];
        }
        array.sort(function(li1,li2){
            var n = new Date(li1.getAttribute("time").replaceAll("-","/"));
            var m = new Date(li2.getAttribute("time").replaceAll("-","/"));
            if (n == m) {
                return 0;
            } else if (n < m) {
                return 1;
            } else {
                return -1;
            }
        });
        ul.html("");
        for(var i=0;i<array.length;i++){
            ul.append(array[i]);
        }

        $('body').on('click','.js-close',function(){
            $('.bottom-explain').addClass('hide');
        });

        $(".data-list li").hover(function(e){
            clearInterval(jobLogTimer);
        },function(){
            setInterval(slide(),1000);
        })

        var i = 0;
        function slide(){
            var h1 = $('.right').height();
            var h2 = $('.data-list').height();
            if (h1 < h2){
                var num = Math.floor((h2 - h1) / 36);
                jobLogTimer = setInterval(function(){
                    i ++;
                    if (i <= num){
                        $('.data-list').css({
                            transform: 'translateY(-' + 36*i + 'px)'
                        });
                    } else {
                        $('.data-list').css({
                            transform: 'translateY(-' + (h2 - h1) + 'px)'
                        });
                        i = -1
                    }
                },1000)
            }
        }
        if (jobLogTimer != null) {
            clearInterval(jobLogTimer);
        }
        slide();
        $(window).resize(function(){
            clearInterval(jobLogTimer);
            slide();
        });
    })

    function showJobData(objThis) {
        $("#jobName").html($(objThis).find(".pos-centered").html());
        var jobId = $(objThis).find("input[name='jobId']").val();
        var jobType = $(objThis).find("input[name='jobType']").val();
        var url = "${request.contextPath}/bigdata/monitor/job/jobdata?jobId="+jobId+"&jobType="+jobType;
        $(".bottom-explain-body").load(url);
        $('.bottom-explain').removeClass('hide');
    }

    function selectTopic() {
        var topicId = $("#topicId").val();
        $.ajax({
            url: '${request.contextPath}/bigdata/monitor/job/relation',
            data: {"topicId":topicId},
            type: 'post',
            dataType: 'json',
            success: function (response) {
                if (response.success) {
                    var data = JSON.parse(response.data);
                    $(".row").html("");
                    if (data.length != 0) {
                        $.each(data,function(index,item){
                            var str = '<div class="col-md-4" onclick="showJobData(this)">'
                            if (item.jobState == 0) {
                                str += '<div class="left-box grey">';
                            } else if (item.jobState == 1) {
                                str += '<div class="left-box green">';
                            } else {
                                str += '<div class="left-box red">';
                            }
                            str += '<div class="left-box-head"><span>';
                            if (item.sourceName == null) {
                                str += '无';
                            } else {
                                str += item.sourceName;
                            }
                            str += "  -  ";
                            if (item.targetName == null) {
                                str += '无';
                            } else {
                                str += item.targetName;
                            }
                            str += '</span></div><div class="left-box-body"><div class="left-box-pic">';
                            if (item.jobState == 0) {
                                str += '<img src="${request.contextPath}/bigdata/v3/static/images/monitoring/metadata-grey.png" >';
                            } else if (item.jobState == 1) {
                                str += '<img src="${request.contextPath}/bigdata/v3/static/images/monitoring/metadata-green.png" >';
                            } else {
                                str += '<img src="${request.contextPath}/bigdata/v3/static/images/monitoring/metadata-red.png" >';
                            }
                            str += '<div class="left-box-text"><b>----------------------------------------------------------------------------</b>';
                            str += '<span class="pos-centered" title="'+ item.jobName +'">'+ item.jobName +'</span><input name="jobId" type="hidden" value="'+ item.jobId +'"/>' +
                                '<input name="jobType" type="hidden" value="'+ item.jobType +'"/></div>';
                            if (item.jobState == 0) {
                                str += '<img src="${request.contextPath}/bigdata/v3/static/images/monitoring/group-grey.png" >';
                            } else if (item.jobState == 1) {
                                str += '<img src="${request.contextPath}/bigdata/v3/static/images/monitoring/group-green.png" >';
                            } else {
                                str += '<img src="${request.contextPath}/bigdata/v3/static/images/monitoring/group-red.png" >';
                            }
                            str += '</div></div></div></div>';
                            $(".row").append(str);
                        });
                    } else {
                        var str = '<div class="no-data-common"><div class="text-center">' +
                            '<img src="${request.contextPath}/bigdata/v3/static/images/public/no-data-common-100.png"/>' +
                            '<p class="color-999">暂无数据</p></div></div>';
                        $(".row").html(str);
                    }
                } else {
                    var str = '<div class="no-data-common"><div class="text-center">' +
                        '<img src="${request.contextPath}/bigdata/v3/static/images/public/no-data-common-100.png"/>' +
                        '<p class="color-999">暂无数据</p></div></div>';
                    $(".row").html(str);
                }
                $('.bottom-explain').addClass('hide');
            }
        });
    }
    
    function viewDataxJobLog(logId) {
        var url = '${request.contextPath}/bigdata/datax/job/viewLog?logId=' + logId;
        $("#logDiv").html("<div class='pos-middle-center'><h4><img src='${request.contextPath}/static/ace/img/loading.gif' />加载中......</h4></div>");
        $("#logDiv").load(url);
        layer.open({
            type: 1,
            shade: .5,
            title: ['日志', 'font-size:16px'],
            area: ['800px', '600px'],
            maxmin: false,
            btn: ['确定'],
            content: $('#logDiv'),
            resize: true,
            yes: function (index) {
                layer.closeAll();
                $("#logDiv").empty();
            },
            cancel: function (index) {
                layer.closeAll();
                $("#logDiv").empty();
            }
        });
    }

    function viewJobLog(logId){
        var url =  '${request.contextPath}/bigdata/etl/viewLog?logId='+logId;
        $("#logDiv").load(url,function(){
            layer.open({
                type: 1,
                shade: .5,
                title: ['日志','font-size:16px'],
                area: ['800px','600px'],
                maxmin: false,
                btn:['确定'],
                content: $('#logDiv'),
                resize:true,
                yes:function (index) {
                    layer.closeAll();
                    $("#logDiv").empty();
                },
                cancel:function (index) {
                    layer.closeAll();
                    $("#logDiv").empty();
                }
            });
        })
    }
</script>
