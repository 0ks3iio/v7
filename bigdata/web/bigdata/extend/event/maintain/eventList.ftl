<div class="tab-pane active" role="tabpanel">
    <div class="filter-made mb-10">
		<div class="filter-item">
			 <span class="filter-name">事件组：</span>
			<div class="form-group">
	            <select id="eventTypeFilter" onchange="changeEventType(this)" style="width: 250px;" class="form-control">
                    <option value="">所有事件组</option>
                    <#list eventTypes as item>
                        <option value="${item.id!}" <#if item.id == typeId?default('')> selected="selected"</#if> >${item.typeName}</option>
                    </#list>
            	</select>
	        </div>
		</div>
		<div class="filter-item filter-item-right clearfix">
	        <button class="btn btn-lightblue"  onclick="editEvent('')">新增事件</button>
		</div>
	</div>
    <div class="table-container">
        <div class="table-container-body">
            <table class="tables tables-border no-margin">
                <thead>
                <tr>
                    <th width="200">名称</th>
                    <th>code</th>
                    <th>事件组</th>
                    <th>时间属性</th>
                    <th>用户属性</th>
                    <th>系统属性</th>
                    <th>统计颗粒度</th>
                    <!--
                    <th>数据采集时间间隔</th>
                    -->
                    <th>排序号</th>
                    <th>备注</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                    <#if eventList?exists&&eventList?size gt 0>
                        <#list eventList as item>
                        <tr>
                            <td style="white-space:nowrap" title="${item.eventName!}"><#if item.eventName! !="" && item.eventName?length gt 10>${item.eventName?substring(0, 10)}...<#else>${item.eventName!}</#if></td>
                            <td title="${item.eventCode!}"><#if item.eventCode! !="" && item.eventCode?length gt 10>${item.eventCode?substring(0, 10)}......<#else>${item.eventCode!}</#if></td>
                            <td>${item.eventTypeName!}</td>
                            <td><#if item.timeProperty?default(0) == 1>是<#else>否</#if></td>
                            <td><#if item.userProperty?default(0) == 1>是<#else>否</#if></td>
                            <td><#if item.envProperty?default(0) == 1>是<#else>否</#if></td>
                            <td>
                                <#if item.granularity?default('') == 'second'>按秒</#if>
                                <#if item.granularity?default('') == 'minute'>按分钟</#if>
                                <#if item.granularity?default('') == 'fifteen_minute'>按15分钟</#if>
                                <#if item.granularity?default('') == 'thirty_minute'>按30分钟</#if>
                                <#if item.granularity?default('') == 'hour'>按小时</#if>
                                <#if item.granularity?default('') == 'day'>按天</#if>
                                <#if item.granularity?default('') == 'week'>按周</#if>
                                <#if item.granularity?default('') == 'month'>按月</#if>
                                <#if item.granularity?default('') == 'quarter'>按季度</#if>
                                <#if item.granularity?default('') == 'year'>按年</#if>
                            </td>
                            <!--
                            <td>${item.intervalTime!}</td>
                            -->
                            <td>${item.orderId!}</td>
                            <td title="${item.remark!}"><#if item.remark! !="" && item.remark?length gt 10>${item.remark?substring(0, 10)}......<#else>${item.remark!}</#if></td>
                            <td style="white-space:nowrap">
                                <a href="javascript:void(0);" onclick="configure('${item.id!}')">配置</a>
                                <#if item.isCustom?default(0) != 0>
                                    <span class="tables-line">|</span><a href="javascript:void(0);" onclick="editEvent('${item.id!}')">编辑</a>
                                    <span class="tables-line">|</span><a href="javascript:void(0);" onclick="deleteEvent('${item.id!}')">删除</a>
                                </#if>
                                <#if item.importSwitch?default(0) == 1>
                                    <span class="tables-line">|</span><a href="javascript:void(0);" onclick="eventDataImport('${item.id!}')">数据导入</a>
                                </#if>
                            </td>
                        </tr>
                        </#list>
                    <#else>
                        <tr >
                            <td  colspan="10" align="center">
                                暂无事件
                            </td>
                        <tr>
                    </#if>
                </tbody>
            </table>
        </div>
    </div>
</div>