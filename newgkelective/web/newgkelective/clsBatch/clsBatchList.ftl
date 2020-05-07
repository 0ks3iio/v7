<table class="table table-bordered table-striped table-hover">
	<thead>
		<tr>
			<th>序号</th>
			<th>班级名称</th>
			<th>班级类型</th>
			<th>总人数</th>
			<#if courseList?exists && courseList?size gt 0>
			<#list courseList as sub>
			<th>${sub.subjectName!}</th>
			</#list>
			</#if>
			<th>行政班科目（时间点）</th>
			<#if canEdit && batchCount gt 0>
			<th>操作</th>
			</#if>
		</tr>
	</thead>
	<tbody>
	<#if hhClsList?exists && hhClsList?size gt 0>
	<#list hhClsList as tcls>
	<tr>
	<td>${tcls_index+1}</td>
	<td>
	<input class="table-input" type="text" data-clazzId="${tcls.id!}" value="${tcls.className!}" onblur="updateGroupClassName2(this,'${tcls.className!}','${tcls.id!}')">
	</td>
	<td>组合班</td>
	<td>${(courseIdCountMap[tcls.id])?default(0)}</td>
	<#if courseList?exists && courseList?size gt 0>
	<#list courseList as sub>
		<td>${(courseIdCountMap[tcls.id+sub.id])?default(0)}</td>
	</#list>
	</#if>
	<td>${tcls.relateName!}</td>
	<#if canEdit && batchCount gt 0>
	<td>
		<a href="javascript:void(0)" onclick="editBatch('${tcls.id!}');">时间点设置</a>
	</td>
	</#if>
	</tr>
	</#list>
	</#if>
	</tbody>
</table>

<script>
function editBatch(cid){
	var url = '${request.contextPath}/newgkelective/clsBatch/${divideId!}/clsBatchEdit/page?divideClsId='+cid;
	layerDivUrl(url,{title: "时间点设置",width:460,height:500});
}

function updateGroupClassName2(obj, oldName, id){
	var nn = $.trim(obj.value);
	if(nn==''){
		layer.tips('名称不能为空！',$(obj), {
				tipsMore: true,
				tips: 3
			});
		return;	
	}
	if(getLength(nn)>80){
		layer.tips('名称内容不能超过80个字节（一个汉字为两个字节）！',$(obj), {
				tipsMore: true,
				tips: 3
			});
		return;
	}
	if(nn==oldName){
		return;
	}
	
	$.ajax({
		url:'${request.contextPath}/newgkelective/${divideId!}/divideClass/updateGroupClassName',
		data: {'classId':id,'className':nn},
		type:'post',
		success:function(data) {
			var jsonO = JSON.parse(data);
	 		if(jsonO.success){
	 			layer.closeAll();
			  	layer.msg(jsonO.msg, {
					offset: 't',
					time: 2000
				});
				clsBatch();
	 		}else{
	 			obj.value=oldName;
	 			layer.tips(jsonO.msg,$(obj), {
					tipsMore: true,
					tips: 3
				});
			}
		},
 		error:function(XMLHttpRequest, textStatus, errorThrown){}
	});
}
</script>