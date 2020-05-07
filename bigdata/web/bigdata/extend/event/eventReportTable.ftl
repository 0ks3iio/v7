<table class="tables ${selectInterval!} report-table <#if isHide?default(true)>hide</#if>">
    <thead>
    <tr>
        <th style="width: 85px;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;">
            指标
        </th>
        <#list indexArray as index>
            <th style="width: 85px;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;">${index!}</th>
        </#list>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td>总计</td>
        <#list indexArray as index>
            <td>${valueMap['总计' + index]!}</td>
        </#list>
    </tr>
    <#list dateArray as date>
        <tr>
            <td style="width: 85px;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;">${date!}</td>
            <#list indexArray as index>
                <td>${valueMap[date + index]?default(0)}</td>
            </#list>
        </tr>
    </#list>
    </tbody>
</table>