<#import "/fw/macro/htmlcomponent.ftl" as component>
<#import "/fw/macro/commonmacro.ftl" as commonmacro>
<@component.moduleDiv>
<span id="${dataUuid!}">
<@component.tableDetail divClass="table-form">
	<#assign columnSize = fwDataGroup.columnSize />
	<#list fwDataDetailDatas as data>
		<#assign dataId = data.id?default("") />
		<#assign haveClosed = false />
		<#assign tdCount = 0 />
		<#assign occupyCount = 0 />
		<#assign remainCount = columnSize />
		<#assign remainCountReal = columnSize />
		<#list fwDataDetails as detail>
			<#assign dataValue = data[detail.entityName]?default("") />
			<#if detail.isHiden == 1>
				<#if dataId != "">
				<input type="hidden" value="${dataValue}" dataId="${dataId!}" id="${detail.entityName!}_${dataId}" name="${detail.entityName!}" />
				<#else>
				<input type="hidden" value="${dataValue}" dataId="${dataId!}" id="${detail.entityName!}_${detail_index}" name="${detail.entityName!}" />
				</#if>
			<#else>
				<#assign remainCount = remainCountReal />
				<#assign remainCount = remainCount - detail.columnSize />
				<#if remainCount &lt; 0 && remainCountReal &gt; 0 && remainCountReal != columnSize>
					<td colspan="${remainCountReal* 2}">&nbsp;</td>
				</#if>
				<#--预测剩下的空间小于0，或者-->
				<#if occupyCount % columnSize == 0 || remainCount &lt; 0>
					<#if occupyCount &gt; 0>
						</tr>
						<#assign haveClosed = true />
						<#assign remainCount = columnSize />
						<#assign remainCountReal = columnSize />
					</#if>
					<tr>
					<#assign occupyCount = 0 />
					<#assign tdCount = 0 />
					<#assign haveClosed = false />
				</#if>
				<#assign dataValue = data[detail.entityName]?default("") />
				<th dataId="${dataId!}" id="th_${(detail.entityName + "_" + dataId)!}">${detail.displayName!}：</th>
				<#assign tdCount = tdCount + detail.columnSize />
				<#assign occupyCount = occupyCount + detail.columnSize />
				<#assign colspan = remainCountReal />
				<#if detail.columnSize &lt; colspan>
					<#assign colspan = detail.columnSize />
				</#if>
				<#assign remainCountReal = remainCountReal - colspan />
				<td <#if colspan &gt; 1>colspan="${colspan * 2 -1}"</#if>>
				
			<#-- 开始组装数据 -->	
			
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
		<#if haveClosed == false && (columnSize - tdCount) &gt; 0>
		<td colspan="${(columnSize - tdCount)*2}">&nbsp;</td>
		</tr>
		</#if>
		
		<#if fwDataDetailDatas?size != data_index + 1>
			<tr id="tr_${dataId}" colspan="${columnSize}" height="10"></tr>
			<#--记录之间的分隔行-->
			<tr colspan="${columnSize}" height="10"></tr>
		</#if>
	</#list>
</@component.tableDetail>
</span>
</@component.moduleDiv>

