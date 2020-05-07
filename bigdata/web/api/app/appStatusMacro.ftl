<#macro appStatus status>
    <#if status==0>
    <span class="badge badge-purple badge-sm">已停用</span>
    <#elseif status==1>
    <span class="badge badge-lightgreen badge-sm">已上线</span>
    <#elseif status==2>
    <span class="badge badge-yellow badge-sm">未上线</span>
    <#elseif status==3>
    <span class="badge badge-lightblue badge-sm">未提交</span>
    <#elseif status==4>
    <span class="badge badge-red badge-sm">不通过</span>
    <#elseif status==5>
    <span class="badge badge-blue badge-sm">审核中</span>
    </#if>
</#macro>