<div class="img-title">
	<img src="${request.contextPath}/static/eclasscard/verticalshow/images/latest-moral.png" alt="">
</div>

<table class="table table-fixed">
	<thead>
		<tr>
			<th width="25%">考核项目</th>
			<th width="15%">得分</th>
			<th>得分说明</th>
		</tr>
	</thead>
	<tbody>
	<#if weekCheak?exists && weekCheak?size gt 0>
		<#list weekCheak as item>
		    <#if item[0] == '1'>
		    <tr>
				<td class="color-ink">值周卫生</td>
				<td class="color-yellow"><b>${item[1]!}</b></td>
				<td class="cell-ellipsis">${item[2]!}</td>
			</tr>
			<#else>
			<tr>
				<td class="color-ink">值周纪律</td>
				<td class="color-yellow"><b>${item[1]!}</b></td>
				<td class="cell-ellipsis">${item[2]!}</td>
			</tr>
			</#if>
		</#list>
		</#if>
		
		<#if courseRecord?exists && courseRecord?size gt 0>
			<#list courseRecord as item>
			    <#if item[0] == '3'>
			    <tr>
					<td class="color-ink">上课日志</td>
					<td class="color-yellow"><b>+${item[1]!}</b></td>
					<td class="cell-ellipsis">${item[2]!}</td>
				</tr>
			    <#else>
			    <tr>
					<td class="color-ink">晚自习日志</td>
					<td class="color-yellow"><b>+${item[1]!}</b></td>
					<td class="cell-ellipsis">${item[2]!}</td>
				</tr>
			    </#if>
			</#list>
			</#if>
			
			 <#if classResult?exists && classResult?size gt 0>
				<#list classResult as item>
					<tr>
						<td class="color-ink">寝室日志</td>
						<td class="color-yellow"><b>${item[1]!}</b></td>
						<td class="cell-ellipsis">${item[2]!}</td>
					</tr>
				</#list>
				</#if>
				<#if classRemind?exists && classRemind?size gt 0>
				<#list classRemind as item>
					<tr>
						<td class="color-ink">提醒事项</td>
						<td colspan="2" class="cell-ellipsis">${item[2]!}</td>
					</tr>
				</#list>
				</#if>
	</tbody>
</table>
<div class="box-honor">
	<h3 class="box-honor-title">德育红旗</h3>
	<ul class="honor-list">
		<li class="icon <#if showJl>icon-honor <#else>icon-honor-disabled</#if>"><span>纪律</span><span>discipline</span></li>
		<li class="icon <#if showWs>icon-honor <#else>icon-honor-disabled</#if>"><span>卫生</span><span>health</span></li>
	</ul>
</div>