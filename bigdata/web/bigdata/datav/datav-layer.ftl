<#if layers?? && layers?size gt 0>
    <#list layers as layer>
        <#if layer.chilren?? && layer.children?size gt 0>
                    <div class="team-name">
                        <i class="arrow fa fa-angle-up" data-toggle="collapse" href="#a0"></i>
                        <svg class="icon" aria-hidden="true">
                            <use xlink:href="#icon-folder-fill">
                            </use>
                        </svg>
                        <span>组</span>
                    </div>
                    <dl>
                    <#list layer.children as child>
                        <dd data-index="1">
                            <img src="${request.contextPath}${child.iconUrl!}">
                            <span>${child.name}</span>
                        </dd>
                    </#list>
                    </dl>
        <#else >
                    <dd data-index="${layer.id}">
                        <img src="${request.contextPath}${layer.iconUrl!}">
                        <span>${layer.name?default("已经移除的组件")}</span>
                    </dd>
        </#if>
    </#list>
</#if>