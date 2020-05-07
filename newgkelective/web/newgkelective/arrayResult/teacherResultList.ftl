<#assign weekDay=5 />
<div class="table-container-header">共${size}份结果</div>
<div class="table-container-body print">
	<table class="table table-bordered table-striped table-hover">
		<thead>
			<tr>
				<th>序号</th>
				<th>教师</th>
				<th>授课课程</th>
				<th>总课时（节）</th>
				<#list 0..(weekDays-1) as day>
				<th>${dayOfWeekMap2[day+""]!}</th>
				</#list>
				<th class="noprint">操作</th>
			</tr>
		</thead>
		<tbody>
			<#if teacherResult?exists && teacherResult?size gt 0>
				<#list teacherResult?keys as key>
					<tr>
						<td>${key_index + 1}</td>
						<td>${teacherResult[key].teacherName!}</td>
						<td>${teacherResult[key].courseNames!}</td>
						<td>${teacherResult[key].periodNums}</td>
						<#assign subjectNumberOfDay=teacherResult[key].subjectNumberOfDay>
						<#list 0..(weekDays-1) as period>
							<#if subjectNumberOfDay?exists && subjectNumberOfDay[period+'']?exists>
								<td>${subjectNumberOfDay[period+'']}节</td>
							<#else>
							<td>0节</td>
							</#if>
						</#list>
						<td class="noprint"><a href="javascript:void(0)" onclick="queryById('${teacherResult[key].teacherId!}','${teacherResult[key].oneSubjectId!}')">查看课表</a></td>
					</tr>
				</#list>
			</#if>
		</tbody>
	</table>
</div>


<script>
function queryById(teacherId,oneSubjectId) {
	var sid = $('#subjectId').val();
	var teacherName = $("#teacherName").val();
	var url =  '${request.contextPath}/newgkelective/${arrayId!}/arrayResult/'+teacherId+'/teacherIndex/page?subjectId='+sid +'&teacherName='+teacherName+'&oneSubjectId='+oneSubjectId;
	$("#tableList").load(url);
}
</script>
