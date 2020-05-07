<#if jobs?exists && jobs?size gt 0>
    <table class="tables">
        <thead>
        <tr>
            <th>开始时间</th>
            <th>持续时间</th>
            <th>Job名称</th>
            <th>JobID</th>
            <th>状态</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody class="kanban-content">
        <#list jobs as job>
                <tr>
                    <td>${job.startTime?number?number_to_datetime}</td>
                    <td>${job.duration!}</td>
                    <td>${job.name!}</td>
                    <td>${job.jid!}</td>
                    <td><span class="badge badge-primary badge-sm">运行中</span></td>
                    <td>
                        <a href="javascript:void(0)" onclick="stopFlink('${job.jid!}','${job.name!}');">停止</a>
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

<script>
    function stopFlink(id,name) {
        showConfirmTips('prompt',"提示","您确定要停止"+name+"任务吗？",function(){
            $.ajax({
                url:"${request.contextPath}/bigdata/calculate/cancel/jobs",
                data:{
                    'id':id
                },
                type:"post",
                clearForm : false,
                resetForm : false,
                dataType: "json",
                success:function(result){
                    layer.closeAll();
                    if(!result.success){
                        showLayerTips4Confirm('error',result.message);
                    }else{
                        showLayerTips('success',result.message,'t');
                        var url =  "${request.contextPath}/bigdata/calculate/realtime/jobs?type=running-jobs";
                        $("#flink-div").load(url);
                    }
                },
                error:function(XMLHttpRequest, textStatus, errorThrown){}
            });
        });
    }
</script>