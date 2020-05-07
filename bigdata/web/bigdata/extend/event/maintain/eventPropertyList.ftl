<#import  "/bigdata/v3/templates/commonWebMacro.ftl" as cwm />
<div id="propertyList">
<div class="tab-pane active" role="tabpanel">
	<div class="filter-made mb-10">
		<div class="filter-item">
			 <span class="filter-name">事件：</span>
			 <div class="form-group">
	            <select id="eventFilter" onchange="changeEvent(this)" style="width: 250px;" class="form-control">
                    <option value="">所有事件</option>
                    <option <#if eventId?exists><#if eventId == '00000000000000000000000000000env'>selected="selected"</#if></#if> value="00000000000000000000000000000env">系统</option>
                    <option <#if eventId?exists><#if eventId == '0000000000000000000000000000time'>selected="selected"</#if></#if> value="0000000000000000000000000000time">时间</option>
                    <option <#if eventId?exists><#if eventId == '0000000000000000000000000000user'>selected="selected"</#if></#if> value="0000000000000000000000000000user">用户</option>
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
	        <button class="btn btn-lightblue"  onclick="editEventProperty('')">新增事件属性</button>
		</div>
	</div>
    <div class="table-container">
        <div class="table-container-body" style="overflow: auto">
            <table class="tables tables-border no-margin">
                <thead>
                <tr>
                    <th>名称</th>
                    <th>字段名</th>
                    <th>事件</th>
                    <th style="width: 85px;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;">排序号</th>
                    <th style="width: 85px;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;">是否显示</th>
                    <th style="width: 85px;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;">是否时序字段</th>
                    <th>备注</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                    <#if eventPropertyList?exists&&eventPropertyList?size gt 0>
                     <#list eventPropertyList as item>
                     <tr>
                         <td style="white-space:nowrap">${item.propertyName!}</td>
                         <td>${item.fieldName!}</td>
                         <td style="white-space:nowrap">${item.eventName!}</td>
                         <td>${item.orderId!}</td>
                         <td><#if item.isShowChart??><#if item.isShowChart?default(0) == 1>是<#else>否</#if><#else>否</#if></td>
 						 <td><#if item.isSequential??><#if item.isSequential?default(0) == 1>是<#else>否</#if><#else>否</#if></td>
                         <td style="width: 85px;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;" title="${item.remark!}"><#if item.remark! !="" && item.remark?length gt 10>${item.remark?substring(0, 10)}......<#else>${item.remark!}</#if></td>
                         <td style="white-space:nowrap">
                             <#if item.unitId == '00000000000000000000000000000000'>
                                 <#if item.isShowChart??>
                                     <#if item.isShowChart?default(0) == 1>
                                        <a href="javascript:void(0);" onclick="stopEventProperty('${item.id!}')">停用</a>
                                     <#else>
                                        <a href="javascript:void(0);" onclick="startEventProperty('${item.id!}')">启用</a>
                                     </#if>
                                 <#else>
                                    <a href="javascript:void(0);" onclick="startEventProperty('${item.id!}')">启用</a>
                                 </#if>

                             <#else>
                                <a href="javascript:void(0);" onclick="editEventProperty('${item.id!}')">编辑</a><span class="tables-line">|</span>
                                <a href="javascript:void(0);" onclick="deleteEventProperty('${item.id!}')">删除</a>
                             </#if>
                         </td>
                     </tr>
                     </#list>
                    <#else>
                        <tr >
                            <td  colspan="8" align="center">
                                暂无事件属性
                            </td>
                        <tr>
                    </#if>
                </tbody>
            </table>
            <@cwm.pageToolBar container="#propertyList" class="text-right"/>
        </div>
    </div>
</div>
</div>
