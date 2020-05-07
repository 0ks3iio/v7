<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<table class="table table-bordered table-striped table-hover no-margin">
	<thead>
		<tr>
			<th class="text-center">序号</th>
			<th class="text-center">学校名称</th>
			<th class="text-center">上级教育局单位</th>
			<th class="text-center">优秀生人数</th>
		</tr>
	</thead>
	<tbody>
		<#if emEnrollCountDtos?exists && (emEnrollCountDtos?size > 0)>
			<#list emEnrollCountDtos as dto>
				<tr>
					<td class="text-center">${dto_index+1}</td>
					<td class="text-center">${dto.schoolName!}</td>
					<td class="text-center">${dto.unitTopName!}</td>
					<td class="text-center">${dto.count!}</td>
				</tr>
			</#list>
		<#else>
			<tr>
				<td colspan="4" align="center">
					暂无数据
				</td>
			<tr>
		</#if>
	</tbody>
</table>