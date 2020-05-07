<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<table class="table table-bordered table-striped table-hover no-margin">
	<thead>
		<tr>
			<th class="text-center">序号</th>
			<th class="text-center">学校名称</th>
			<th class="text-center">总人数</th>
			<th class="text-center">待审核人数</th>
			<th class="text-center">已通过人数</th>
			<th class="text-center">未通过人数</th>
		</tr>
	</thead>
	<tbody>
		<#if emEnrollStuCounts?exists && (emEnrollStuCounts?size > 0)>
			<#list emEnrollStuCounts as dto>
				<tr>
					<td class="text-center">${dto_index+1}</td>
					<td class="text-center">${dto.schoolName!}</td>
					<td class="text-center">${dto.sumCount!}</td>
					<td class="text-center">${dto.otherNum!}</td>
					<td class="text-center">${dto.passNum!}</td>
					<td class="text-center">${dto.notPassNum!}</td>
				</tr>
			</#list>
		<#else>
			<tr>
				<td colspan="6" align="center">
					暂无数据
				</td>
			<tr>
		</#if>
	</tbody>
</table>
<#if emEnrollStuCounts?exists&&emEnrollStuCounts?size gt 0>
		<@htmlcom.pageToolBar container="#showStatisticalDiv" class="noprint"/>
</#if>