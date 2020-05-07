<div class="filter-made mb-10">
    <div class="filter-item filter-item-right clearfix">
        <button class="btn btn-lightblue" onclick="editQualityRelatedTable('');">新增关联表</button>
    </div>
</div>
<div class="table-container-body">
    <table class="tables">
        <thread>
            <tr>
                <th>主表</th>
                <th>关联表</th>
                <th>主表列</th>
                <th>关联表列</th>
                <th>操作</th>
            </tr>
        </thread>
        <tbody>
            <#if relatedTableVos?exists && relatedTableVos?size gt 0>
              <#list relatedTableVos as item>
                <tr>
                    <td>${item.masterTableName!}</td>
                    <td>${item.followerTableName!}</td>
                    <td>${item.masterTableColumnName!}</td>
                    <td>${item.followerTableColumnName!}</td>
                    <td>
                        <a href="javascript:void(0)" onclick="editQualityRelatedTable('${item.id!}')">编辑</a><span class="tables-line">|</span>
                        <a href="javascript:void(0)" onclick="deleteQuityRelatedTable('${item.id!}')">删除</a>
                    </td>
                </tr>
              </#list>
            <#else>
                <tr >
                    <td  colspan="5" align="center">
                        暂无关联关系
                    </td>
                <tr>
            </#if>
        </tbody>
    </table>
</div>
