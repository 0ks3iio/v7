<div class="table-switch-data default">
	<span>总数：<em class="maxCount">${maxCount?default(0)}</em></span>
	<span>男：<em class="manCount">${manCount?default(0)}</em></span>
	<span>女：<em class="womanCount">${womanCount?default(0)}</em></span>
	<#if courseList?exists && (courseList?size>0)>
		<#list courseList as course>
		<span>${course.subjectName!}：<em class="courseScore_${course.id!}">${(avgMap[course.id]?default(0))?string("0.#")}</em></span>
		<input type="hidden" class="course_${course.id!}" value="${allMap[course.id]?default(0)}">
		</#list>
	</#if>
</div>
<table class="table table-bordered table-striped js-sort-table">
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
	<#if lastList?exists && lastList?size gt 0>
		<#list lastList as item>
		<tr>
			<td>
				<label class="pos-rel">
					<input name="studentIdName" type="checkbox" class="wp checkBoxItemClass" value="${item.studentId!}" />
					<span class="lbl"></span>
				</label>
			</td>
			<td>${item.studentName!}</td>
			<td>${mcodeSetting.getMcode("DM-XB","${item.sex}")}</td>
			<#assign scoreMap=item.scoreMap>
			<#if scoreMap?exists && (scoreMap?size>0)>
				<#if courseList?exists && (courseList?size>0)>
					<#assign maxScore=0.0>
					<#list courseList as course>
						<#if scoreMap[course.id]?exists>
							<td>${(scoreMap[course.id]?default(0))?string("0.#")}</td>
							<#assign maxScore=maxScore+scoreMap[course.id]>
						<#else>
							<td>0</td>
						</#if>
					
					</#list>
					<#if courseList?? && (courseList?size != 1)>
					<td>${maxScore?string("0.#")}</td>
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
<#if lastList?exists && lastList?size gt 0>
	<#list lastList as item>
		stuDtoMap['${item.studentId}']={};
		stuDtoMap['${item.studentId}']['sex']=${item.sex};
		<#assign scoreMap=item.scoreMap>
			<#if scoreMap?exists && (scoreMap?size>0)>
				<#if courseList?exists && (courseList?size>0)>
					<#list courseList as course>
						<#if scoreMap[course.id]?exists>
							stuDtoMap['${item.studentId}']['course_${course.id}']=${scoreMap[course.id]};
						<#else>
							stuDtoMap['${item.studentId}']['course_${course.id}']=0.0;
						</#if>
						
					</#list>
					
				</#if>
			<#else>
				<#if courseList?exists && (courseList?size>0)>
					<#list courseList as course>
						stuDtoMap['${item.studentId}']['course_${course.id}']=0.0;
					</#list>
				</#if>
			</#if>
	</#list>
</#if>
$(function(){
	$(".tableDivClass").each(function(){
		if($(this).find("table").length == 1){
			tableMap['${rightOrLeft!}'] = $(this).find("table");
			$(this).find("table").DataTable({
				paging:false,
				scrollY:372,
				"language": {
					"emptyTable": "暂无数据"
				},
				info:false,
				searching:false,
				autoWidth:false,
				"order": [[ 2, 'asc' ]],
				columns: [
		            { "orderable": false },
		            { "orderable": false },
		            null,
		            <#if courseList?exists && (courseList?size>0)>
						<#list courseList as course>
						null<#if (courseList?size != 1) && (courseList?size == course_index+1)>,<#else>,</#if>
						</#list>
						<#if courseList?? && (courseList?size != 1)>
						null
						</#if>
					</#if>
		        ]
			});
		}
	});
	
	$('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
		$('.js-sort-table').DataTable().columns.adjust();
	})
	stuIdStr="";
});
</script>