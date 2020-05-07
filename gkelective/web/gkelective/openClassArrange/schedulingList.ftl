<script language="javascript" type="text/javascript">
$(document).ready(function(){
	$("#${rightOrLeft!}_sort").tablesorter({headers:{0:{sorter:false}}});
});
</script>
<div class="table-switch-data default">
	<span>总数：<em class="maxCount">${maxCount?default(0)}</em></span>
	<span>男：<em class="manCount">${manCount?default(0)}</em></span>
	<span>女：<em class="womanCount">${womanCount?default(0)}</em></span>
	<#if courseList?exists && (courseList?size>0)>
		<#list courseList as course>
		<span>${course.subjectName!}：<em class="courseScore_${course.id!}">${avgMap[course.id]}</em></span>
		<input type="hidden" class="course_${course.id!}" value="${allScoreMap[course.id]?default(0)}">
		</#list>
	</#if>
</div>
<table class="table table-striped table-bordered table-hover no-margin mainTable  tablesorter" id="${rightOrLeft!}_sort">
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
			<#if courseList?exists && (courseList?size>0)>
				<#list courseList as course>
				<th>${course.subjectName!}</th>
				</#list>
				<#if courseList?? && (courseList?size != 1)>
				<th>总成绩</th>
				</#if>
			</#if>
		</tr>
	</thead>
	<tbody>
	<#if stuSubjectList?exists && (stuSubjectList?size>0)>
		<#list stuSubjectList as stu>
		<tr>
			<td>
				<label class="pos-rel">
					<input name="studentIdName" type="checkbox" class="wp checkBoxItemClass" value="${stu.stuId!}" />
					<span class="lbl"></span>
				</label>
			</td>
			<td>${stu.stuName!}</td>
			<td>${mcodeSetting.getMcode("DM-XB","${stu.sex}")}</td>
			<td>${stu.className!}</td>
			<#assign scoreMap=stu.scoreMap>
			<#if scoreMap?exists && (scoreMap?size>0)>
				<#if courseList?exists && (courseList?size>0)>
					<#assign maxScore=0.0>
					<#list courseList as course>
						<#if scoreMap[course.id]?exists>
							<td>${scoreMap[course.id]}</td>
							<#assign maxScore=maxScore+scoreMap[course.id]>
						<#else>
							<td>0</td>
						</#if>
					
					</#list>
					<#if courseList?? && (courseList?size != 1)>
					<td>${maxScore}</td>
					</#if>
				</#if>
			<#else>
				<#if courseList?exists && (courseList?size>0)>
					<#list courseList as course>
					<td>0</td>
					</#list>
					<#if courseList?? && (courseList?size != 1)>
					<td>0</td>
					</#if>
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
<#if stuSubjectList?exists && (stuSubjectList?size>0)>
	<#list stuSubjectList as stu>
		stuDtoMap['${stu.stuId}']={};
		stuDtoMap['${stu.stuId}']['sex']=${stu.sex};
		<#assign scoreMap=stu.scoreMap>
			<#if scoreMap?exists && (scoreMap?size>0)>
				<#if courseList?exists && (courseList?size>0)>
					<#list courseList as course>
						<#if scoreMap[course.id]?exists>
							stuDtoMap['${stu.stuId}']['course_${course.id}']=${scoreMap[course.id]};
						<#else>
							stuDtoMap['${stu.stuId}']['course_${course.id}']=0.0;
						</#if>
						
					</#list>
					
				</#if>
			<#else>
				<#if courseList?exists && (courseList?size>0)>
					<#list courseList as course>
						stuDtoMap['${stu.stuId}']['course_${course.id}']=0.0;
					</#list>
				</#if>
			</#if>
	</#list>
</#if>
$(function(){
	document.getElementById("bbbbbbbb").scrollIntoView();
});
</script>