<table class="table table-bordered layout-fixed table-editable" data-label="">
	<thead>
		<tr>
			<th class="text-center">时间点</th>
			<th class="text-center">科目</th>
			<th class="text-center">人数</th>
			<th class="text-center">待安排人数</th>
			<th class="text-center">已安排班级</th>
			<#if canEdit?default(false)><th class="text-center">操作</th></#if>
		</tr>
	</thead>
	<tbody>
	<#if dtoMap?exists && dtoMap?size gt 0>
	<#list dtoMap?keys as batch>
		<#assign dtoList = dtoMap[batch]>
		<#if dtoList?exists && dtoList?size gt 0>
		<#list dtoList as dto>
		<tr>
			<#if dto_index == 0>
			<td rowspan="${dtoList?size}">
				时间点${batch!}
			</td>
			</#if>
			<td>
				${dto.course.subjectName!}
			</td>
			<td>
				${dto.stuNum!}
			</td>
			<td>
				${dto.freeStuNum!}
			</td>
			<td>
				<#if dto.devideClassList?exists && dto.devideClassList?size gt 0>
				<#list dto.devideClassList as cls>
					<#if cls_index != 0>、</#if>
					<#if canEdit?default(false)>
					<a href="javascript:void(0);" onclick="toDivide('${batch!}','${dto.course.id!}','${cls.id!}')">${cls.className!}</a>
					<#else>
					${cls.className!}
					</#if>
				</#list>
				</#if>
			</td>
			<#if canEdit?default(false)>
			<td>
				<a href="javascript:void(0)" onclick="toDivide('${batch!}','${dto.course.id!}')">手动开班</a>
				<#if true><a href="javascript:void(0)" onclick="dismissClass('${batch!}','${dto.course.id!}')">解散组合</a></#if>
			</td>
			</#if>
		</tr>
		</#list>
		</#if>
	</#list>
	</#if>
	</tbody>
</table>
<script>
function toDivide(batch,subjectId,classId){
	if(!classId){
		classId = '';
	}
	var params = 'batch='+batch+'&course.id='+subjectId+'&classId='+classId;
	var url = "${request.contextPath}/newgkelective/${divideId!}/showUndivideStudents?"+params;
	$("#batchDiv").load(url);
}
function dismissClass(batch,subjectId){
	$.ajax({
		url:"${request.contextPath}/newgkelective/${divideId}/dismissAllClass",
		data: {"batch":batch, "course.id":subjectId},
		dataType: "JSON",
		success: function(data){
			if(data.success){
				layer.msg(data.msg+"！", {
					offset: 't',
					time: 2000
				});
				stuStat();
			}else{
				alert(data.msg);
			}
		},
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	});
}
</script>