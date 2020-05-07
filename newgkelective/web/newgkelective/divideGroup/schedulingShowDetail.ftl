<script type="text/javascript" src="${request.contextPath}/static/tablesort/js/jquery.tablesorter.js"></script>
<link href="${request.contextPath}/static/tablesort/css/style.css" rel="stylesheet" type="text/css" />
<div class="table-switch-data default">
	<span>总数：<em class="maxCount">${maxCount?default(0)}</em></span>
	<span>男：<em class="manCount">${manRatio!}%</em></span>
	<span>女：<em class="womanCount">${womanRatio!}%</em></span>
	<#if courseList?exists && (courseList?size>0)>
		<#list courseList as course>
		<span>${course.subjectName!}：<em class="courseScore_${course.id!}">${avgMap[course.id]?string("#.##")}</em></span>
		</#list>
	</#if>
</div>
<table class="table table-bordered table-striped js-sort-table tablesorter" id="classViewTable" style="font-size:10pt;">
	<thead>
		<tr>
			<th>序号</th>
			<th>姓名</th>
			<th>性别</th>
			<#if courseList?exists && (courseList?size>0)>
				<#list courseList as course>
				<th>${course.subjectName!}</th>
				</#list>
				<th>总分</th>
			</#if>
		</tr>
	</thead>
	<tbody>
	<#if stuResultDtoList?exists && (stuResultDtoList?size>0)>
		<#list stuResultDtoList as stu>
		<tr>
			<td>
				${stu_index+1}
			</td>
			<td>${stu.studentName!}</td>
			<td>${mcodeSetting.getMcode("DM-XB","${stu.sex}")}</td>
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
$(function(){
	$("#classViewTable").tablesorter({sortInitialOrder: 'desc'});
});
</script>