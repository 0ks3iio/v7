<#-- 模板选择页面 -->
<table class="table table-bordered table-striped table-hover">
    <tbody>
    <thead>
    <tr>
        <th width="5%">选择</th>
        <th width="15%">字段名称</th>
        <th width="30%">字段说明</th>
        <th width="50%">范例</th>
    </tr>
    </thead>
    <tr>
        <#if datas?exists && datas?size gt 0>
            <#list datas as data>
                <tr>
                    <td>
                        <input name="header" type="checkbox" value="${data["header"]!}" <#if data["checked"]>checked="checked" disabled</#if>>
                    </td>
                    ${ViewDataFreemarkerUtils.parseTemplate(data)}
                </tr>
            </#list>
        </#if>
    </tr>
    </tbody>
</table>