<#if fromDesktop?default(false)>
<div class="box box-default">
    <div class="box-body">
</#if>
<#if initLicense?default(true)>
<table class="table table-striped table-middle">
  <thead>
    <tr>
      <th class="text-center">应用名称</th>
      <th class="text-center">域名/内网网址/serverId</th>
      <th class="text-center">首页/协议/端口/serverKey</th>
      <th class="text-center">上下文</th>
      <th class="text-center">操作</th>
    </tr>
  </thead>
  <tbody>
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
                      <input type="text" name="" id="domain_${server.id}" class="form-control" value="${server.domain!}" />
                    </div>
                  </div>
                  <div class="filter-item block">
                    <span class="filter-name">内网地址：</span>
                    <div class="filter-content">
                      <input type="text" name="" id="secondDomain_${server.id}" class="form-control"
                        value="${server.secondDomain!}" />
                    </div>
                  </div>
                  <div class="filter-item block">serverId：${server.id!}</div>
                </td>
                <td>
                  <div class="filter-item block">
                    <span class="filter-name">首页：</span>
                    <div class="filter-content">
                      <input type="text" name="" id="indexUrl_${server.id}" class="form-control" value="${server.indexUrl!}"/>
                    </div>
                  </div>
                  <div class="clearfix">
                    <div class="filter-item">
                      <span class="filter-name">协议：</span>
                      <div class="filter-content">
                        <input type="text" name="" id="protocol_${server.id}" class="form-control" value="${server.protocol!}" />
                      </div>
                    </div>
                    <div class="filter-item">
                      <span class="filter-name">端口：</span>
                      <div class="filter-content">
                        <input type="text" name="" id="port_${server.id}" class="form-control" value="${server.port!}" />
                      </div>
                    </div>
                  </div>
                  <div class="filter-item block">serverKey：${server.serverKey!}</div>
                </td>
                <td>
                  <div class="filter-item block">
                    <span class="filter-name">上下文：</span>
                    <div class="filter-content">
                      <input type="text" name="" id="context_${server.id}" class="form-control" value="${server.context!}"/>
                    </div>
                  </div>
                    <#--<div class="filter-item block">-->
                        <#--<span class="filter-name">verifyUrl：</span>-->
                        <#--<div class="filter-content">-->
                            <#--<input type="text" name="" id="verifyUrl_${server.id}" class="form-control" value="${server.verifyUrl!}"/>-->
                        <#--</div>-->
                    <#--</div>-->
                    <#--<div class="filter-item block">-->
                        <#--<span class="filter-name">invalidateUrl：</span>-->
                        <#--<div class="filter-content">-->
                            <#--<input type="text" name="" id="invalidateUrl_${server.id}" class="form-control" value="${server.invalidateUrl!}"/>-->
                        <#--</div>-->
                    <#--</div>-->
                </td>
                <td class="text-center">
                  <button type="button" class="btn btn-blue btn-server-update" id="${server.id}">更新</button>
                </td>
            </tr>
        </#list>
    </#if>
  </tbody>
</table>
<script>
    $(document).ready(function () {
       $(".btn-server-update").bind("click",function () {
           var id = this.id;
           $.post({
               url : '${request.contextPath}/system/ops/modifyServer',
               data : {
                   'id' : id,
                   'protocol' : $('#protocol_' + id).val(),
                   'domain' : $('#domain_' + id).val(),
                   'secondDomain' : $('#secondDomain_' + id).val(),
                   'port' : $('#port_' + id).val(),
                   'indexUrl' : $('#indexUrl_' + id).val(),
                   'context' : $('#context_' + id).val()
//                   'verifyUrl' : $('#verifyUrl_' + id).val(),
//                   'invalidateUrl' : $('#invalidateUrl_' + id).val()
               },
               success : function(msg) {
                   var jsonO = JSON.parse(msg);
                   layer.msg(jsonO.msg);
               }
           });
       });
    });
</script>
<#else>
    <h1 style="text-align: center">请先激活序列号,创建顶级单位管理员</h1>
</#if>
<#if fromDesktop?default(false)>
    </div>
</div>
</#if>