<script language="javascript" type="text/javascript">
$(document).ready(function(){
	
	$("#${rightOrLeft!}_sort").tablesorter({headers:{0:{sorter:false}},sortInitialOrder: 'desc'});
});
</script>

<div class="table-switch-data default">
	<span>总数：<em class="maxCount">${maxCount?default(0)}</em></span>
	<span>男：<em class="manCount">${manCount?default(0)}</em></span>
	<span>女：<em class="womanCount">${womanCount?default(0)}</em></span>
	<#if courseList?exists && (courseList?size>0)>
		<#list courseList as course>
		<span>${course.subjectName!}：<em class="courseScore_${course.id!}">${avgMap[course.id]?string("#.##")}</em></span>
		<input type="hidden" class="course_${course.id!}" value="${allScoreMap[course.id]?default(0)}">
		</#list>
	</#if>
</div>
<table class="table table-striped table-bordered table-hover no-margin mainTable tablesorter" id="${rightOrLeft!}_sort">
	<thead>
		<tr>
			<th>
				<label class="pos-rel">
					<input name="course-checkbox" type="checkbox" class="wp checkboxAllClass">
					<span class="lbl"></span>
				</label>
			</th>
			<th>姓名</th>
			<th>性别</th>
			<th>行政班</th>
			<th>选课</th>
			<#if courseList?exists && (courseList?size>0)>
				<#list courseList as course>
				<th>${course.subjectName!}</th>
				</#list>
				<th>总分</th>
			</#if>
		</tr>
	</thead>
	<tbody>
	<#if stuDtoList?exists && (stuDtoList?size>0)>
		<#list stuDtoList as stu>
		<tr>
			<td>
				<label class="pos-rel">
					<input name="studentIdName" type="checkbox" class="wp checkBoxItemClass" value="${stu.studentId!}" />
					<span class="lbl"></span>
				</label>
			</td>
			<td>${stu.studentName!}</td>
			<td>${mcodeSetting.getMcode("DM-XB","${stu.sex}")}</td>
			<td>${stu.className!}</td>
			<td>${stu.choResultStr!}</td>
			<#assign scoreMap=stu.subjectScore>
			<#if scoreMap?exists && (scoreMap?size>0)>
				<#if courseList?exists && (courseList?size>0)>
					<#assign scoreCount=0.0>
					<#list courseList as course>
						<#if scoreMap[course.id]?exists>
						<#assign scoreCount=scoreCount+scoreMap[course.id]>
							<td>${scoreMap[course.id]?string("#.##")}</td>
						<#else>
							<td>0</td>
						</#if>
					</#list>
					<td>${scoreCount?string("#.##")}</td>
				</#if>
			<#else>
				<#if courseList?exists && (courseList?size>0)>
					<#list courseList as course>
					<td>0</td>
					</#list>
					<td>0</td>
				</#if>
			</#if>
		</tr>
		</#list>
	</#if>
	</tbody>
</table>
<script>
<#if courseList?exists && (courseList?size>0)>
	<#list courseList as course>
		courseMap['${course.id}']='${course.id}';
	</#list>
</#if>
<#if stuDtoList?exists && (stuDtoList?size>0)>
	<#list stuDtoList as stu>
		stuDtoMap['${stu.studentId}']={};
		stuDtoMap['${stu.studentId}']['sex']=${stu.sex};
		<#assign scoreMap=stu.subjectScore>
			<#if scoreMap?exists && (scoreMap?size>0)>
				<#if courseList?exists && (courseList?size>0)>
					<#list courseList as course>
						<#if scoreMap[course.id]?exists>
							stuDtoMap['${stu.studentId}']['course_${course.id}']=${scoreMap[course.id]};
						<#else>
							stuDtoMap['${stu.studentId}']['course_${course.id}']=0.0;
						</#if>
						
					</#list>
					
				</#if>
			<#else>
				<#if courseList?exists && (courseList?size>0)>
					<#list courseList as course>
						stuDtoMap['${stu.studentId}']['course_${course.id}']=0.0;
					</#list>
				</#if>
			</#if>
	</#list>
</#if>

</script>