<a href="javascript:void(0)" onclick="goback()" class="btn btn-sm btn-blue mb10">返回</a>
<table class="table table-bordered layout-fixed table-editable" data-label="">
	<thead>
		<tr>
			<th class="text-center">时间点</th>
			<th class="text-center">科目</th>
			<th class="text-center">班级</th>
			<th class="text-center">班级类型</th>
			<th class="text-center">班级人数</th>
			<th class="text-center">总人数</th>
			<th class="text-center">操作</th>
		</tr>
	</thead>
	<tbody>
	<#if dtoMap?exists && dtoMap?size gt 0>
	<#list dtoMap?keys as batch>
			<#assign dtoList = dtoMap[batch]>
			
			<#if dtoList?exists && dtoList?size gt 0>
			<#list dtoList as dto>
				<#if dto.devideClassList?size gt 0>
				<#list dto.devideClassList as claz>
				<tr>
					<#if dto_index == 0 && claz_index == 0>
					<td rowspan="${batchCountMap[batch]!}">
						时间点${batch!}
					</td>
					</#if>
					<#if claz_index == 0>
					<td rowspan="${dto.devideClassList?size!}">
						${dto.course.subjectName!}
					</td>
					</#if>
					<td>
						${claz.className!}
					</td>
					<td>
						${claz.relateName!}
					</td>
					<td>
						${claz.studentCount!}
					</td>
					<td>
						${claz.studentList?size!}
					</td>
					<td>
						<a href="javascript:void(0)" onclick="dismiss('${claz.id!}')">解散合班</a>
					</td>
				</tr>
				</#list>
				</#if>
			</#list>
			</#if>
	</#list>
	<#else>
		<tr><td colspan="7" class="text-center">没有合班班级</td></tr>
		
	</#if>
	</tbody>
</table>

<script>
function dismiss(classId){
	layer.confirm('确定解散吗？', function(index){
		$.ajax({
			url:"${request.contextPath}/newgkelective/${divideId!}/dismissClass",
			data: 'classId='+classId,
			dataType: "JSON",
			success: function(data){
				if(data.success){
					refresh();
					layer.msg("解散成功！", {
						offset: 't',
						time: 2000
					});
				}else{
					layerTipMsg(data.success,"失败",data.msg);
				}
			},
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		});
	});	
}
function refresh(){
	var url = '${request.contextPath}/newgkelective/${divideId!}/showCombineInfo';
	$('#batchDiv').load(url);
}
function goback(){
	var url = '/newgkelective/${divideId!}/showStudentDistribution/page';
	$("#batchDiv").load(url);
}
</script>