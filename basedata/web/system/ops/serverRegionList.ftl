<#if servers?exists && servers?size gt 0>
    <#list servers as server>
    <tr>
        <td class="text-center">
            <p>
                <img src="<#if server.iconUrl?default("")="">/static/images/base/temp/2.png<#else>${server.iconUrl}</#if>" width="64" height="64" />
            </p>
            <p class="base-app-title">${server.name!}</p>
        </td>
        <td>
            <div class="filter-item block">
                <span class="filter-name">域&emsp;&emsp;名：</span>
                <div class="filter-content">
                    <input type="text" name="" id="domain_${server.serverRegionId}" class="form-control" value="${server.domain!}" />
                </div>
            </div>
            <#--<div class="filter-item block">
                <span class="filter-name">内网地址：</span>
                <div class="filter-content">
                    <input type="text" name="" id="secondDomain_${server.serverRegionId}" class="form-control"
                           value="${server.secondDomain!}" />
                </div>
            </div>-->
            <div class="filter-item block">serverId：${server.id!}</div>
        </td>
        <td>
            <div class="filter-item block">
                <span class="filter-name">首页：</span>
                <div class="filter-content">
                    <input type="text" name="" id="indexUrl_${server.serverRegionId}" class="form-control" value="${server.indexUrl!}"/>
                </div>
            </div>
            <div class="clearfix">
                <div class="filter-item">
                    <span class="filter-name">协议：</span>
                    <div class="filter-content">
                        <input type="text" name="" id="protocol_${server.serverRegionId}" class="form-control" value="${server.protocol!}" />
                    </div>
                </div>
                <div class="filter-item">
                    <span class="filter-name">端口：</span>
                    <div class="filter-content">
                        <input type="text" name="" id="port_${server.serverRegionId}" class="form-control" value="${server.port!}" />
                    </div>
                </div>
            </div>
            <div class="filter-item block">serverKey：${server.serverKey!}</div>
        </td>
        <td>
            <div class="filter-item block">
                <span class="filter-name">上下文：</span>
                <div class="filter-content">
                    <input type="text" name="" id="context_${server.serverRegionId}" class="form-control" value="${server.context!}"/>
                </div>
            </div>
        </td>
        <td class="text-center">
            <button type="button" class="btn btn-blue btn-server-update" id="${server.serverRegionId}">更新</button>
            <input id="serverId_${server.serverRegionId!}" type="hidden" value="${server.id!}" />
        </td>
    </tr>
    </#list>
</#if>
<script>
    $(".btn-server-update").bind("click",function () {
        var id = this.id;
        var regionCode = $("#regionCode").val();
        var regionName = $("#regionName").val();
        var protocol = $('#protocol_' + id).val();
        var domain = $('#domain_' + id).val();
        var port = $('#port_' + id).val();
        var contextPath = $('#context_' + id).val();
        var indexUrl = $('#indexUrl_' + id).val();
        if (protocol === "" || protocol === null) {
            layer.msg("请输入协议名称（http）");
            return;
        }
        if (domain === "" || domain === null) {
            layer.msg("请输入域名");
            return;
        }
        if (port === null || port === "") {
            layer.msg("请输入端口号");
            return;
        }
        if (contextPath === null || contextPath === "") {
            layer.msg("请输入上下文");
            return;
        }
        if (indexUrl === null || indexUrl === ""){
            layer.msg("请输入首页地址");
            return;
        }
        $.post({
            url : '${request.contextPath}/system/ops/serverRegion/update',
            data : {
                'id' : id,
                'serverId' : $('#serverId_' + id).val(),
                'region'   : regionCode,
                'regionName' : regionName,
                'protocol' : protocol,
                'domain' : domain,
                //'secondDomain' : $('#secondDomain_' + id).val(),
                'port' : port,
                'indexUrl' : indexUrl,
                'contextPath' : contextPath,
                'unitId' : $('#regionUnitId').val()
            },
            success : function(msg) {
                var jsonO = JSON.parse(msg);
                layer.msg(jsonO.msg);
            }
        });
    });
</script>