<table class="table table-bordered table-striped table-hover">
    <tbody>
    <thead>
    <tr>
        <td>序号</td>
        <td>导入文件名</td>
        <td>导入状态</td>
        <td>导入时间</td>
        <td>操作</td>
    </tr>
    </thead>
        <#if importRecords?exists && importRecords?size gt 0>
            <#list importRecords as r>
            <tr>
                <td>${r_index}</td>
                <td>${r.originFilename!}</td>
                <td>
                    <p class="import-state import-state-<#if r.stateCode?default(0) == 1>waiting<#elseif r.stateCode?default(0) == 2>current<#elseif r.stateCode?default(0) == 3>success<#elseif r.stateCode?default(0) == 4>current<#elseif r.stateCode?default(0) == -1>failed</#if>">${r.stateMsg!}</p>
                </td>
                <td>${r.creationTime?string("yyyy-MM-dd HH:mm")}</td>
                <td>
                    <button class="btn btn-sm btn-lightblue viewData-btn" cacheId="${r.cacheId!}">查看</button>
                </td>
            </tr>
            </#list>
        <#else >
            <tr><td colspan="5" style="text-align: center;">无内容</td></tr>
        </#if>
    </tbody>
</table>
<script>
    $(document).ready(function () {

        $(".viewData-btn").each(function () {
            $(this).unbind().bind("click",function () {
                viewData($(this).attr("cacheId"));
            })
        });

        var stopmClient = initWebsocket();
    });
    function initWebsocket() {
        var socket = new SockJS("${request.contextPath}/dataImportEndpoint");
        var stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            stompClient.subscribe("${request.contextPath}/import/status",function (response) {
                var JSONResponse = JSON.parse(response.body);
                $('.viewData-btn[cacheId="'+JSONResponse.businessValue+'"]').parent().parent()
                        .find("p.import-state").removeClass("import-state-waiting")
                        .addClass("import-state-" + (JSONResponse.importStateCode == 1 ? "waiting" : JSONResponse.importStateCode == 2 ? "current" : JSONResponse.importStateCode == -1 ? "failed" : JSONResponse.importStateCode == 3 ? "success" : "success" ))
                        .text((JSONResponse.msg));
            });
        });
        return stompClient;
    }
    
    function queryForImportState(stompClient, cacheId) {
        stompClient.send("${request.contextPath}/status",{}, JSON.stringify({"cacheId":cacheId}));
    }

    function viewData(cacheId) {
        $("#layer-common").load("${request.contextPath}/${action!}import/viewData?cacheId=" + cacheId, function () {
            var index = layer.open({
                type: 1,
                title:"数据处理",
                content:$("#layer-common"),
                maxmin:true,
                //btn:['下载','取消'],
                btn1:function (index) {
                },
                btn2:function (index) {
                    layer.close(index);
                }
            });
            layer.full(index);
            $(".layui-layer-btn").css("text-align","center");
        });
    }
</script>