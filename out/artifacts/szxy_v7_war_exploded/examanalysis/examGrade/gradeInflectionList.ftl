<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<div class="table-container">
     <div class="table-container-body">
		<table class="table table-striped table-hover">
			<thead>
				<tr>
					<th class="text-center">年级名次</th>
					<th class="text-center">姓名</th>
					<th class="text-center">考号</th>
					<th class="text-center">班级</th>
					<#if courseList?exists && courseList?size gt 0>
					<#list courseList as item>
					<th class="text-center">${item.subjectName!}</th>
					</#list>
					</#if>
				</tr>
			</thead>
			<tbody>
				<#if studentDtoList?exists && studentDtoList?size gt 0>
					<#list studentDtoList as stuDtoItem>
					<tr>
						<td class="text-center">${stuDtoItem.gradeRank}</td>
						<td class="text-center">${stuDtoItem.studentName!}</td>
						<td class="text-center">${stuDtoItem.examCode!}</td>
						<td class="text-center">${stuDtoItem.className!}</td>
						<#assign stuScoreMap=stuDtoItem.statBySubjectMap>
						<#assign gradeRankMap=stuDtoItem.gradeRankBySubjectMap>
						<#if courseList?exists && courseList?size gt 0>
						<#list courseList as item>
							<#if stuScoreMap[item.id]?exists>
								<#assign allrank=stuDtoItem.gradeRank + 100>
								<#if gradeRankMap[item.id] gt allrank>
								<td class="text-center" style="color:red;">${stuScoreMap[item.id]?string('#.##')}</td>
								<#else>
								<td class="text-center">${stuScoreMap[item.id]?string('#.##')}</td>
								</#if>
							<#else>
								<td class="text-center">0</td>
							</#if>
						</#list>
						</#if>
					</tr>
					</#list>
				</#if>
			</tbody>
		</table>
     </div>
</div>
<#if studentDtoList?exists>
<@htmlcom.pageToolBar container="#inflectionDiv"/>
</#if>