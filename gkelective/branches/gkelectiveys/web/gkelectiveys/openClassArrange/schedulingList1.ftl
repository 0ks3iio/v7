<script type="text/javascript" src="${request.contextPath}/static/tablesort/js/jquery.tablesorter.js"></script>
<link href="${request.contextPath}/static/tablesort/css/style.css" rel="stylesheet" type="text/css" />
<div class="table-switch-data default">
	<span>总数：<em class="maxCount">${maxCount?default(0)}</em></span>
	<span>男：<em class="manCount">${manRatio!}%</em></span>
	<span>女：<em class="womanCount">${womanRatio!}%</em></span>
	<#if courseList?exists && (courseList?size>0)>
		<#list courseList as course>
		<span>${course.subjectName!}：<em class="courseScore_${course.id!}">${avgMap[course.id]}</em></span>
		</#list>
	</#if>
</div>
<table class="table table-bordered table-striped js-sort-table tablesorter" id="viewTable">
	<thead>
		<tr>
			<th>序号</th>
			<th>姓名</th>
			<th>性别</th>
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
				${stu_index+1}
			</td>
			<td>${stu.stuName!}</td>
			<td>${mcodeSetting.getMcode("DM-XB","${stu.sex}")}</td>
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
$(function(){
	$("#viewTable").tablesorter();
});
</script>
