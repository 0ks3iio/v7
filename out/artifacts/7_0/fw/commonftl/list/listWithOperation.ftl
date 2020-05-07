<#--
1. 说明：
标准的普通列表页面，只显示列表
2. Action传递的参数
fwDataDetails： 字段名 List<FwDataDetail>
fwDataDetailDatas：数据，可以是各种数据，教师/学生/单位等等
-->
<#import "/fw/macro/htmlcomponent.ftl" as component>
<#import "/fw/macro/commonmacro.ftl" as commonmacro>
<@component.tableList id="tablelist">
<tr>
	<#list fwDataDetails as detail>
		<#if detail.isHiden == 0>
			<th width="${detail.columnSize?default(1)}%" class="${detail.className!}">${detail.displayName!}</th>	
		</#if>
	</#list>
		<th width="1%">操作</th>
</tr>
<tr>
	<#if fwDataDetailDatas?exists && fwDataDetailDatas?size &gt; 0>
	<#list fwDataDetailDatas as data>
	<tr>
		<#list fwDataDetails as detail>
			<#if detail.isHiden == 0>
				<td class="${detail.className!}">
				<#if detail.displayType == "SELECT" || detail.displayType == "CHECKBOX" || detail.displayType == "RADIO" || detail.displayType == "TEXT" || detail.displayType == "TEXTAREA" || detail.displayType == "INPUT">
					<#if detail.mcodeId?exists>
						${mcodeSetting.getMcode(detail.mcodeId, data[detail.entityName]?default(""))}
					<#else>
						${data[detail.entityName]?default("")}
					</#if>
				<#elseif detail.displayType == "DATEEDIT" || detail.displayType == "DATE">
					<#if detail.mcodeId?exists>
						${(data[detail.entityName]?string(detail.mcodeId))!}
					<#else>
						${data[detail.entityName]?default("")}
					</#if>
				<#else>
				&nbsp;
				</#if>
				</td>
			</#if>
		</#list>
		<td>ddd</td>
		</tr>
	</#list>
	<#else>
	<td colspan="${fwDataDetails?size}"> <p class="no-data mt-20">还没有任何记录哦！</p></td>
	</#if>
</tr>
</@component.tableList>