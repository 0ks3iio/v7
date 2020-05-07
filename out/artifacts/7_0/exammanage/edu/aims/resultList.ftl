<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<table class="table table-bordered table-striped table-hover no-margin">
	<thead>
		<tr>
			<th class="text-center" style="width:5%">序号</th>
 			<th class="text-center" style="width:10%">考号</th>
			<th class="text-center" style="width:15%">学校</th>
			<th class="text-center" style="width:10%">学生姓名</th>
			<th class="text-center" style="width:15%">学籍号</th>
 			<th class="text-center" style="width:15%">身份证号</th>
 			<th class="text-center" style="width:5%">性别</th>
 			<th class="text-center" style="width:15%">志愿学校</th>
		</tr>
	</thead>
	<tbody>
		<#if list?exists && (list?size > 0)>
			<#list list as dto>
				<tr>
					<td class="text-center">${dto_index+1!}</td>
					<td class="text-center">${dto.examCode!}</td>
					<td class="text-center">${dto.schoolName!}</td>
					<td class="text-center">${dto.studentName!}</td>
					<td class="text-center">${dto.studentCode!}</td>
					<td class="text-center">${dto.identityCard!}</td>
					<td class="text-center">${mcodeSetting.getMcode("DM-XB","${dto.sex!}")}</td>
					<td class="text-center">${dto.aimsSchoolId!}</td>
				</tr>
			</#list>
		<#else>
			<tr>
				<td colspan="8" align="center">
					暂无数据
				</td>
			<tr>
		</#if>
	</tbody>
</table>
<#if list?exists&&list?size gt 0>
		<@htmlcom.pageToolBar container="#resultDiv" class=""/>
</#if>
<script>

</script>