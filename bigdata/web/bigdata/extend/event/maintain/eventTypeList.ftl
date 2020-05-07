<div class="tab-pane active" role="tabpanel">
    <div class="filter-made mb-10">
		<div class="filter-item filter-item-right clearfix">
	        <button class="btn btn-lightblue"onclick="editEventType('')">新增事件组</button>
		</div>
	</div>
    <div class="table-container">
        <div class="table-container-body">
            <table class="tables tables-border no-margin">
                <thead>
                <tr>
                    <th>名称</th>
                    <th>排序号</th>
                    <th>备注</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                    <#if eventTypes?exists&&eventTypes?size gt 0>
                        <#list eventTypes as item>
                        <tr>
                            <td title="${item.typeName!}"><#if item.typeName! !="" && item.typeName?length gt 10>${item.typeName?substring(0, 10)}......<#else>${item.typeName!}</#if></td>
                            <td>${item.orderId!}</td>
                            <td title="${item.remark!}"><#if item.remark! !="" && item.remark?length gt 50>${item.remark?substring(0, 50)}......<#else>${item.remark!}</#if></td>
                            <td>
                                <a href="javascript:void(0);" onclick="editEventType('${item.id!}')">编辑</a><span class="tables-line">|</span>
                                <a href="javascript:void(0);" onclick="deleteEventType('${item.id!}')">删除</a>
                            </td>
                        </tr>
                        </#list>
                    <#else>
                        <tr >
                            <td  colspan="4" align="center">
                                暂无事件组
                            </td>
                        <tr>
                    </#if>
                </tbody>
            </table>
        </div>
    </div>
</div>