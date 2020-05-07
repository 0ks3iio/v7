<#import "/fw/macro/webmacro.ftl" as w>
    <table class="table table-bordered table-striped table-hover">
        <thead>
        <tr>
            <th>Index</th>
            <th>Key</th>
            <th>DataType</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <#if cKeys?exists && cKeys?size gt 0>
            <#list cKeys as key>
            <tr>
                <td>${key_index}</td>
                <td><a href="javascript:viewValue('${key.key}','${dbIndex}','${key.typeName!}');" alt="${key.key!}"><#if key.key?length gt 50>${key.key?substring(0,50)}<#else>${key.key}</#if></a></td>
                <td>${key.typeName!}</td>
                <td><a href="javascript:deleteKV('${key.key!}');" class="table-btn color-red js-delete">删除</a></td>
            </tr>
            </#list>
        </#if>
        </tbody>
    </table>
<script>
    function viewValue(key,dbIndex,dataType) {
        var parameter = "";
        parameter += "?dbIndex=" + dbIndex;
        parameter += "&key=" +  encodeURI(key);
        parameter += "&dataType=" + dataType;
        parameter += "&redis_host=${redis_host!}";
        parameter += "&redis_port=${redis_port!}";
        $("#common-Layer").load("${request.contextPath}/redis/view" +parameter,function () {
            layer.open({
                type: 1,
                shade: .5,
                title: ['View/Updaate','font-size:16px'],
                area: ['940px'],
                content: $('#common-Layer'),
                resize:true,
                yes:function (index) {

                },
                btn2:function (index) {
                    layer.closeAll();
                }
            });
        });
    }

    function deleteKV(key) {
        $.ajax({
            type: "post",
            url: '${request.contextPath}/redis/delete',
            dataType: "json",
            data: {
                redis_host: "${redis_host!}",
                redis_port: "${redis_port!}",
                dbIndex: "${dbIndex!}",
                key: key,
            },
            success : function(data) {
                showSuccessMsgWithCall(data.msg, function () {
                    var parameter = "";
                    parameter += "?redis_host=${redis_host!}";
                    parameter += "&redis_port=${redis_port!}";
                    parameter += "&dbIndex=${dbIndex!}&time="+new Date().getTime();
                    refreshKeyList(parameter); //index。ftl
                });
            }
        });
    }
</script>
<#if cKeys?exists && cKeys?size gt 0>
<@w.pagination2  container="#keyContainer" pagination=pagination page_index=2/>
</#if>