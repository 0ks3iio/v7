<table class="table table-striped table-hover no-margin">
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
			<#list examStudentDtoList as list>
			<tr>
				<td class="text-center">${list.subjectName!}</td>
				<td class="text-center">${list.total!}</td>
				<td class="text-center">${list.gradeAverage!}</td>
				<td class="text-center">${list.classAverage!}</td>
				<td class="text-center">${list.classMax!}</td>
				<td class="text-center">${list.classMin!}</td>
				<td class="text-center">${list.gradeRanking!}</td>
				<td class="text-center">${list.classRanking!}</td>
			</tr>
			</#list>
		</#if>
		</tbody>
	</table>