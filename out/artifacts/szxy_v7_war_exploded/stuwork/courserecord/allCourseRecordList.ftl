<div class="table-wrapper">
    <form id="mannReForm">
	<table class="table table-bordered table-striped table-hover no-margin">
		<thead>
				<tr>
					<th class="t-center">节次</th>
					<th class="t-center">班级</th>
					<th class="t-center">课程</th>
					<th class="t-center">老师</th>
					<th class="t-center">考核分</th>
					<th class="t-center">违纪名单</th>
                    <th class="t-center">备注</th>
				</tr>
		</thead>
		<tbody>
			<#if courseScheduleList?exists && (courseScheduleList?size > 0)>
				<#list courseScheduleList as item>
				   <tr>
				      <td class="t-center">第${item.period!}节</td>
				      <td class="t-center">${item.className!}</td>
				      <td class="t-center">${item.subjectName!}</td>
				      <td class="t-center">${item.teacherName!}</td>
				      <td class="t-center">${item.score!}</td>
				      <td class="t-center" width="25%;" style="word-break:break-all;">${item.punishStudentName!}</td>
				      <td class="t-center" width="25%;" style="word-break:break-all;">${item.remark!}</td>
				   </tr>
                </#list>
            </#if>
		</tbody>
	</table>

	</form>	
</div>
<script>

</script>