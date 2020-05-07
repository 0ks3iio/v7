<div class="table-switch-data default">
	<p>
	<span>总数：<em class="allCountNumClass">${classChangeDetailDto.allCountNum?default(0)}</em></span>
	<span>男：<em class="mCountNumClass">${classChangeDetailDto.mCountNum?default(0)}</em></span>
	<span>女：<em class="wCountNumClass">${classChangeDetailDto.wCountNum?default(0)}</em></span>
	</p>
	<#if courseList?exists && (courseList?size>0)>
		<#list courseList as item>
			<span>${item.subjectName!}均分：<em class="${item.id}Class">${classChangeDetailDto.courseScore[item.id]!}</em></span>
		</#list>
	</#if>
</div>
<table class="table table-bordered table-striped js-sort-table">
	<thead>
		<tr>
			<th>
				<label class="pos-rel">
					<input type="checkbox" class="wp checkboxAllClass" >
					<span class="lbl"></span>
				</label>
			</th>
			<th>学号</th>
			<th>姓名</th>
			<th>性别</th>
			<#if courseList?exists && (courseList?size>0)>
				<#list courseList as item>
					<th>${item.subjectName!}</th>
				</#list>
			</#if>
		</tr>
	</thead>
	<tbody>
		<#if studentList?exists && (studentList?size>0)>
			<#list studentList as item>
				<tr>
				<td>
					<label class="pos-rel">
						<input name="studentIdName" type="checkbox" class="wp checkBoxItemClass" value="${item.id!}">
						<span class="lbl"></span>
					</label>
				</td>
				<td>${item.studentCode!}</td>
				<td>${item.studentName!}</td>
				<td>${mcodeSetting.getMcode("DM-XB","${item.sex!}")}</td>
				<#if courseList?exists && (courseList?size>0)>
					<#list courseList as courseItem>
						<td>${classChangeDetailDto.studentCourseScore[item.id][courseItem.id]!}</td>
					</#list>
				</#if>
				</tr>
			</#list>
		</#if>
	</tbody>
</table>
<script>
$(function(){
	classDtoMap['${classChangeDetailDto.classId}'] = {};
	classDtoMap['${classChangeDetailDto.classId}']['allCountNum'] = ${classChangeDetailDto.allCountNum?default(0)};
	classDtoMap['${classChangeDetailDto.classId}']['mCountNum'] = ${classChangeDetailDto.mCountNum?default(0)};
	classDtoMap['${classChangeDetailDto.classId}']['wCountNum'] = ${classChangeDetailDto.wCountNum?default(0)};
	classDtoMap['${classChangeDetailDto.classId}']['courseIds'] = [];
	<#if courseList?exists && (courseList?size>0)>
		<#list courseList as item>
			classDtoMap['${classChangeDetailDto.classId}']['courseIds'][${item_index}] = '${item.id}';
		</#list>
	</#if>
	classDtoMap['${classChangeDetailDto.classId}']['courseScore'] = {};
	<#list classChangeDetailDto.courseScore?keys as key>
		classDtoMap['${classChangeDetailDto.classId}']['courseScore']['${key}'] = ${classChangeDetailDto.courseScore[key]!};
	</#list>
	<#if studentList?exists && (studentList?size>0)>
		<#list studentList as item>
			studentMap['${item.id}one'] = {};
			studentMap['${item.id}one']['sex'] = ${item.sex?default(-1)};
		</#list>
	</#if>
	<#list classChangeDetailDto.studentCourseScore?keys as key>
		studentMap['${key}'] = {};
		 <#list classChangeDetailDto.studentCourseScore[key]?keys as key2>
		 	studentMap['${key}']['${key2}'] = ${classChangeDetailDto.studentCourseScore[key][key2]!};
		 </#list>
	</#list>
	$(".tableDivClass").each(function(){
		if($(this).find("table").length == 1){
			tableMap['${classChangeDetailDto.classId}'] = $(this).find("table");
			$(this).find("table").DataTable({
				paging:false,
				scrollY:372,
				"language": {
					"emptyTable": "暂无数据"
				},
				info:false,
				searching:false,
				autoWidth:false,
				"order": [[ 3, 'asc' ]],
				columns: [
		            { "orderable": false },
		            null,
		            { "orderable": false },
		            null,
	            	<#if courseList?exists && (courseList?size>0)>
					<#list courseList as courseItem>
						<#if (courseList?size == courseItem_index+1)>
						null
						<#else>
						null,
						</#if>
					</#list>
					</#if>
		        ]
			});
		}
	});
	
	$('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
		$('.js-sort-table').DataTable().columns.adjust();
	})
});
</script>