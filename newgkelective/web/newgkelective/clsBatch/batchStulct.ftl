<#if dtoMap?exists && dtoMap?size gt 0>
<#if canEdit?default(false)>
<a class="btn btn-sm btn-blue" onclick="combine()" style="margin-bottom:10px;">合班设置</a>
<a class="btn btn-sm btn-blue" onclick="showInf()" style="margin-bottom:10px;">合班统计</a>
</#if>
<table class="table table-bordered layout-fixed table-editable" data-label="">
	<thead>
		<tr>
			<th class="text-center" rowspan="2">序号</th>
			<th class="text-center" rowspan="2">班级名称</th>
			<th class="text-center" colspan="2">时间点1</th>
			<th class="text-center" colspan="2">时间点2</th>
			<th class="text-center" colspan="2">时间点3</th>
		</tr>
		<tr>
			<th class="text-center">走班科目</th>
			<th class="text-center">走班人数</th>
			<th class="text-center">走班科目</th>
			<th class="text-center">走班人数</th>
			<th class="text-center">走班科目</th>
			<th class="text-center">走班人数</th>
		</tr>
	</thead>
	<tbody>
	<#list dtoMap?keys as classId>
		<#assign dto = dtoMap[classId]>
		<#if dto.maxCourseNum! gt 0>
		<#list 0..(dto.maxCourseNum-1) as num>
		<tr>
			<#if num == 0>
				<td rowspan="${dto.maxCourseNum}">
					${classId_index + 1}
				</td>
				<td rowspan="${dto.maxCourseNum}">
					${dto.devideClass.className!}
				</td>
			</#if>
			<#if dto.map['1']?exists>
			<#assign courseMap1 = dto.map['1']>
			<td><#-- 时间点1 -->
				<#list courseMap1?keys as course>
					<#if course_index == num>${courseMap[course].subjectName!}</#if>
				</#list>
			</td>
			<td>
				<#list courseMap1?keys as course>
					<#if course_index == num>${courseMap1[course]!}</#if>
				</#list>
			</td>
			<#else>
			<td></td>
			<td></td>
			</#if>
			<#if  dto.map['2']?exists>
			<#assign courseMap2 = dto.map['2']>
			<td><#-- 时间点2 -->
				<#list courseMap2?keys as course>
					<#if course_index == num>${courseMap[course].subjectName!}</#if>
				</#list>
			</td>
			<td>
				<#list courseMap2?keys as course>
					<#if course_index == num>${courseMap2[course]!}</#if>
				</#list>
			</td>
			<#else>
			<td></td>
			<td></td>
			</#if>
			<#if dto.map['3']?exists>
			<#assign courseMap3 = dto.map['3']>
			<td><#-- 时间点3 -->
				<#list courseMap3?keys as course>
					<#if course_index == num>${courseMap[course].subjectName!}</#if>
				</#list>
			</td>
			<td>
				<#list courseMap3?keys as course>
					<#if course_index == num>${courseMap3[course]!}</#if>
				</#list>
			</td>
			<#else>
			<td></td>
			<td></td>
			</#if>
		</tr>
		</#list>
		</#if>
	</#list>
	</tbody>
</table>
<#else>
<p class="text-center">没有走班学生</p>
</#if>
<script>
function combine(){
	var url = '${request.contextPath}/newgkelective/${divideId!}/showBatchJxbClass/index';
	$("#batchDiv").load(url);
}
function showInf(){
	var url = '${request.contextPath}/newgkelective/${divideId!}/showCombineInfo?divideId=${divideId!}';
	$("#batchDiv").load(url);
}
</script>