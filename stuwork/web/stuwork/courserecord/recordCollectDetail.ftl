<div class="layer-content">
	<h4>课程</h4>
	<table class="table table-striped">
		<thead>
			<tr>
				<th>课程</th>
				<th>任课老师</th>
				<th>考核分</th>
			</tr>
		</thead>
		<tbody>
		<#if type == '1'>
		  <#if dyCourseRecordList?exists && dyCourseRecordList?size gt 0>
		    <#list dyCourseRecordList as item>
			<tr>
				<td>${item.subjectName!}</td>
				<td>${item.teacherName!}</td>
				<td>${item.score!}</td>
			</tr>
			</#list>
		  </#if>
	    </#if>
		</tbody>
	</table>
	<p>最终得分：${score!}分</p>
	<p>违纪人员：${punishStudentName!}</p>				
</div>
