<div class="table-container">
    <div class="table-container-header clearfix mb-5">
        <#if !isAdmin?default(false)>
            <button class="pull-right btn btn-lightblue add-data" onclick="editField('')">新增字段</button>
        </#if>
    </div>
    <div class="table-container-body">
        <table class="tables">
            <thead>
            <tr>
                <th>名称</th>
                <th>字段名</th>
                <th>字段类型</th>
                <th>字段长度</th>
                <th>小数长度</th>
                <th>是否主键</th>
                <th>用途</th>
                <th>备注</th>
                <#if !isAdmin?default(false)>
                    <th>操作</th>
                </#if>
            </tr>
            </thead>
            <tbody>
            <#if  columnList?exists &&columnList?size gt 0>
            <#list columnList as item>
                <tr>
                    <td>${item.name!?html}</td>
                    <td>${item.columnName!}</td>
                    <td>${item.columnType!}</td>
                    <td>${item.columnLength!}</td>
                    <td>${item.decimalLength!}</td>
                    <td><#if item.isPrimaryKey?default(0) == 1>是<#else>否</#if></td>
                    <td>
                        <#if item.statType!?contains('index')?string("yes", "no") == "yes">指标&nbsp;</#if>
                        <#if item.statType!?contains('dimension')?string("yes", "no") == "yes">维度</#if>
                        <#if item.statType!?contains('showColumn')?string("yes", "no") == "yes">展示列</#if>
                    </td>
                    <td title="${item.remark!?html}"><#if item.remark! !="" && item.remark?length gt 10>${item.remark?substring(0, 10)}......<#else>${item.remark!}</#if></td>
                    <#if !isAdmin?default(false)>
                        <td>
                            <a href="javascript:void(0);" onclick="editField('${item.id!}')">编辑</a><span
                                    class="tables-line">|</span>
                            <a href="javascript:void(0);" onclick="deleteField('${item.id!}')">删除</a>
                        </td>
                    </#if>
                </tr>
            </#list>
            <#else>
            <tr>
                <td <#if !isAdmin?default(false)>colspan="9"<#else>colspan="8"</#if> align="center">
                    暂无字段信息
                </td>
            <tr>
                </#if>

            </tbody>
        </table>
    </div>
</div>