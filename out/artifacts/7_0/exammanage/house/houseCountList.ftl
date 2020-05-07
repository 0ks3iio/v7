<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<table class="table table-bordered table-striped table-hover no-margin">
	<thead>
		<tr>
			<th class="text-center">教育局单位</th>
			<th class="text-center">转出人数</th>
			<th class="text-center">转入人数</th>
		</tr>
	</thead>
	<tbody>
		<#if emHouseDtos?exists && (emHouseDtos?size > 0)>
			<#list emHouseDtos as dto>
				<tr>
					<td class="text-center">${dto.unitName!}</td>
					<td class="text-center">${dto.turnOut!}</td>
					<td class="text-center">${dto.turnIn!}</td>
				</tr>
			</#list>
		<#else>
			<tr>
				<td colspan="3" align="center">
					暂无数据
				</td>
			<tr>
		</#if>
	</tbody>
</table>