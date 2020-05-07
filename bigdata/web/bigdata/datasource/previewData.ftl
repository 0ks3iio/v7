<div class="table-wrap" style="margin-top: 0px; height: 200px;">
    <table class="table table-bordered table-striped table-hover text-center no-margin">
        <thead>
            <tr>
                <#list title as item>
                    <th class="text-center"> <#if item?exists><#if item?length gt 100>${item[0..100]}<#else>${item!}</#if></#if> </th>
                </#list>
            </tr>
        </thead>
        <tbody>
            <#list list as item>
                <tr>
                    <#list item as c>
                        <td title="${c!}"><#if c?exists><#if c?length gt 100>${c[0..100]}<#else>${c!}</#if></#if></td>
                    </#list>
                </tr>
            </#list>
        </tbody>
    </table>
</div>

<div class="stu-tea">
<#if type == "excel">
        <#list excelTab as item>
            <span <#if item == tabKey >class="active"</#if> style="overflow: hidden" title="${item!}" onclick="changeExcelTab(this)">${item!}</span>
        </#list>
</#if>
</div>
