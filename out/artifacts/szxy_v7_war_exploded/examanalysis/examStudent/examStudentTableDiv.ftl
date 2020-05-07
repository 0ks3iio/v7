<table class="table table-bordered table-striped table-hover">
	<thead>
		<tr>
			<th class="text-center">类别</th>
			<th class="text-center">个人得分</th>
			<th class="text-center">年级平均分</th>
			<th class="text-center">班级平均分</th>
			<th class="text-center">班级最高分</th>
			<th class="text-center">班级最低分</th>
			<th class="text-center">年级排名</th>
			<th class="text-center">班级排名</th>
		</tr>
	</thead>
	<tbody>
		<#if examStudentDtoList?exists && (examStudentDtoList?size > 0)>
			<#list examStudentDtoList as dto>
			<tr>
				<td class="text-center">${dto.subjectName!}</td>
				<td class="text-center">${dto.total!}</td>
				<td class="text-center">${dto.gradeAverage!}</td>
				<td class="text-center">${dto.classAverage!}</td>
				<td class="text-center">${dto.classMax!}</td>
				<td class="text-center">${dto.classMin!}</td>
				<td class="text-center">${dto.gradeRanking!}</td>
				<td class="text-center">${dto.classRanking!}</td>
			</tr>
			</#list>
		</#if>
	</tbody>
</table>