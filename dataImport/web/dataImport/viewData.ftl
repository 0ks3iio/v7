<table class="table table-bordered table-striped table-hover">
    <tbody>
    <thead>
    <tr>
        <#if headers?exists && headers?size gt 0>
            <#list headers as header>
                <td>${header!}</td>
            </#list>
        </#if>
    </tr>
    </thead>
<tr>
        <#if javaObjects?exists && javaObjects?size gt 0>
            <#list javaObjects as data>
            <tr>
                ${ViewDataFreemarkerUtils.parseJavaObject(data, headers)}
            </tr>
            </#list>
        </#if>
    </tr>
    </tbody>
</table>