<form name="batchForm" id="batchForm" method="post">
<#assign batchIn = 0 />
<#if hhClsList?exists && hhClsList?size gt 0>
<#list hhClsList as tcls>
<table class="table table-bordered table-striped table-hover">
	<thead>
		<tr>
			<th width="15%">班级名称</th>
			<th width="10%">班级人数</th>
			<th width="15%">选课组合</th>
			<th width="10%">人数</th>
			<#list 1..batchCount?default(1) as ba>
			<th>
			<#assign cbName=(clsXzbMap[tcls.id+ba])?default('') />
				时间点${ba} <#if cbName?default('') !=''>(${cbName})</#if>
			</th>
			</#list>
		</tr>
	</thead>
	<tbody>
	<tr class="checkTr" tsi="${tcls_index}0">
	<#assign dtoSize = tcls.newDtoList?size />
	<#if dtoSize==0>
	<#assign dtoSize = 1 />
	</#if>
	<td rowspan="${dtoSize}">${tcls.className!}</td>
	<td rowspan="${dtoSize}">${tcls.studentCount}</td>
	<#if tcls.newDtoList?exists && tcls.newDtoList?size gt 0>
	<#list tcls.newDtoList as cdto>
		<#if cdto_index gt 0>
		<tr class="checkTr" tsi="${tcls_index}${cdto_index}">
		</#if>
		<td>${cdto.subShortNames!}</td>
		<td>${cdto.sumNum?default(0)}</td>
		<#list 1..batchCount?default(1) as ba>
			<td>
				<#assign baSub = (clsXzbBasMap[tcls.id+cdto.subjectIdstr+ba?string])?default('') />
				<input type="hidden" id="divideClassId${tcls_index}${cdto_index}${ba}" name="clsBatchs[${batchIn}].divideClassId" value="${tcls.id!}">
				<input type="hidden" id="subjectIds${tcls_index}${cdto_index}${ba}" name="clsBatchs[${batchIn}].subjectIds" value="${cdto.subjectIdstr!}">
				<input type="hidden" id="batch${tcls_index}${cdto_index}${ba}" name="clsBatchs[${batchIn}].batch" value="${ba!}">
				<input type="hidden" name="clsBatchs[${batchIn}].exSubId" value="${baSub?default('')}">
				<#if cdto.beXzbSub>
				<input type="hidden" name="clsBatchs[${batchIn}].subjectId" value="${baSub!}">
				</#if>
				<select <#if cdto.beXzbSub>disabled</#if> nullable="false" ex-sub="" class="form-control batch${tcls_index}${cdto_index}" id="subjectId${tcls_index}${cdto_index}${ba}" name="clsBatchs[${batchIn}].subjectId" style="width:150px;" onchange="checkSub('${tcls_index}','${cdto_index}','${ba}',this.value);" data-placeholder="请选择科目" >
				<#list cdto.subjectIds as subId>
					<option value="${subId!}" <#if (baSub?default('')==subId) || (baSub?default('') == '' && ba_index==subId_index)>selected</#if>>${(courseNameMap[subId])?default('')}</option>
				</#list>
				</select>
			</td>
			<#assign batchIn = batchIn+1 />
		</#list>
		</tr>
	</#list>
	<#else>
	<td>无</td>
	<td></td>
	<#list 1..batchCount?default(1) as ba>
	<td></td>
	</#list>
	</#if>
	</tr>
	</tbody>
</table>
</#list>
</#if>
<#if canEdit>
<div class="text-center">
	<a class="btn btn-blue" onclick="saveBatch();" href="javascript:;">保存</a>
</div>
</#if>
</form>
<script>
function checkSub(ti,si,bi,sval){
	var sobid = 'subjectId'+ti+si+bi;
	$('.batch'+ti+si).each(function(){
		var obid = $(this).attr('id');
		if(sobid != obid && $(this).val()==sval){
			layer.tips('各个时间点的科目不可以重复',$("#"+sobid), {
					tipsMore: true,
					tips: 3
				});	
		}
	});
}

var hasSave=false;
function saveBatch(){
	if(hasSave){
		return;
	}
	hasSave=true;
	if(!checkValue('#batchForm')){
		hasSave=false;
	 	return;
	}
	var flag = false;
	$('.checkTr').each(function(){
		var tsi = $(this).attr('tsi');
		var sids='';
		$('.batch'+tsi).each(function(){
			var sval = $(this).val();
			var obid = $(this).attr('id');
			if(sids.indexOf(sval)!=-1){
				//console.log(sids+'==='+sval);
				layer.tips('各个时间点的科目不可以重复',$("#"+obid), {
						tipsMore: true,
						tips: 3
					});	
				flag=true;
			} else {
				sids+=(','+sval);
			}
		});
	});
	if(flag){
		hasSave=false;
	 	return;
	}
	
	var options = {
		url : "${request.contextPath}/newgkelective/clsBatch/${divideId!}/subBatchSave",
		dataType : 'json',
		success : function(data){
 			var jsonO = data;
	 		if(!jsonO.success){
	 			layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
	 			hasSave=false;
	 			return;
	 		}else{
	 			layer.msg("保存成功", {
						offset: 't',
						time: 2000
					});
				subBatch();		
			}
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	};
	$("#batchForm").ajaxSubmit(options);
}
</script>