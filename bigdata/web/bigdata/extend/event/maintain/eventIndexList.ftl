<div class="tab-pane active" role="tabpanel">
	<div class="filter-made mb-10">
		<div class="filter-item">
			 <span class="filter-name">事件：</span>
			 <div class="form-group">
	            <select id="eventFilter" onchange="changeEventForIndex(this)" style="width: 250px;" class="form-control">
                    <option value="">所有事件</option>
                    <#list eventTypeMap?keys as key>
                        <optgroup label="${eventTypeMap[key].typeName!}"></optgroup>
                        <#if eventMap[key]?exists>
                            <#list eventMap[key] as item>
                              <option <#if eventId?exists><#if eventId == item.id>selected="selected"</#if></#if> value="${item.id!}">&nbsp;&nbsp;&nbsp;${item.eventName!}</option>
                            </#list>
                        </#if>
                    </#list>
                </select>
	        </div>
		</div>
		<div class="filter-item filter-item-right clearfix">
	        <button class="btn btn-lightblue" onclick="editEventIndex('')">新增事件指标</button>
		</div>
	</div>
    <div class="table-container">
        <div class="table-container-body">
            <table class="tables tables-border no-margin">
                <thead>
                <tr>
                    <th>名称</th>
                    <th>聚合方式</th>
                    <th>聚合列</th>
                    <th>聚合输出字段名</th>
                    <th>排序号</th>
                    <th>备注</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                    <#if eventIndicatorList?exists&&eventIndicatorList?size gt 0>
                        <#list eventIndicatorList as item>
                        <tr>
                            <td title="${item.indicatorName!}"><#if item.indicatorName! !="" && item.indicatorName?length gt 10>${item.indicatorName?substring(0, 10)}......<#else>${item.indicatorName!}</#if></td>
                            <td>${item.aggType!}</td>
                            <td title="${item.aggField!}"><#if item.aggField! !="" && item.aggField?length gt 20>${item.aggField?substring(0, 20)}......<#else>${item.aggField!}</#if></td>
                            <td title="${item.aggOutputName!}"><#if item.aggOutputName! !="" && item.aggOutputName?length gt 20>${item.aggOutputName?substring(0, 20)}......<#else>${item.aggOutputName!}</#if></td>
                            <td>${item.orderId!}</td>
                            <td title="${item.remark!}"><#if item.remark! !="" && item.remark?length gt 10>${item.remark?substring(0, 10)}......<#else>${item.remark!}</#if></td>
                            <td>
                                <a href="javascript:void(0);" onclick="editEventIndex('${item.id!}')">编辑</a><span class="tables-line">|</span>
                                <a href="javascript:void(0);" onclick="deleteEventIndex('${item.id!}')">删除</a>
                            </td>
                        </tr>
                        </#list>
                    <#else>
                        <tr >
                            <td  colspan="7" align="center">
                                暂无事件指标
                            </td>
                        <tr>
                    </#if>
                </tbody>
            </table>
        </div>
    </div>
</div>