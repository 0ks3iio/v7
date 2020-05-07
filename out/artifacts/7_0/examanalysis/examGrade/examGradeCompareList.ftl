<div class="table-container">
     <div class="table-container-body">
		<table class="table table-striped table-hover">
			<thead>
				<tr>
					<th class="text-center">班级</th>
					<th class="text-center">成绩类型</th>
					<th class="text-center">班级平均分</th>
					<th class="text-center">最高分</th>
					<th class="text-center">最低分</th>
					<th class="text-center">统计人数</th>
					<th class="text-center">平均分名次</th>
				</tr>
			</thead>
			<tbody>
				<#if emStatRangeList?exists && emStatRangeList?size gt 0>
					<#list emStatRangeList as item>
					<tr>
						<td class="text-center">${item.rangeName!}</td>
						<td class="text-center">${item.subjectName}</td>
						<td class="text-center">${item.avgScore?string('#.##')}</td>
						<td class="text-center">${item.maxScore?string('#.##')}</td>
						<td class="text-center">${item.minScore?string('#.##')}</td>
						<td class="text-center">${item.statNum}</td>
						<td class="text-center">${item.rank}</td>
					</tr>
					</#list>
				</#if>
			</tbody>
		</table>
     </div>
</div>
<script>
	$(function(){
		var headMessHtml='';
		$("#headMess").text('');
		<#if statSpaceList?exists && statSpaceList?size gt 0>
			<#list statSpaceList as item>
			headMessHtml=headMessHtml+'<span class="tip tip-grey" style="margin-right: 10px">年级${item.spaceItemName!}:${item.scoreNum!}</span>';
			</#list>
		</#if>
		$("#headMess").append(headMessHtml);
	})
</script>