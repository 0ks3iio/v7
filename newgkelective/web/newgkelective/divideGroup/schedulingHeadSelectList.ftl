<div style="padding-top:0px;padding-bottom:5px;">
	<#assign tableNum = 0>
	<#if gDtoList?exists && (gDtoList?size > 0)>
		<#assign tableNum = tableNum+1>
		<h3>三科组合情况</h3>
		<#assign colCount = 12>
		<#assign xunCount = (gDtoList?size/colCount)?number >
		<#if (gDtoList?size%colCount > 0) >
			<#assign xunCount = xunCount + 1 >
		</#if>
		<#list 1..xunCount as cou>
			<table class="table table-bordered table-striped table-hover <#if (tableNum>0) && (cou>1)>showtableClass</#if>">
				<tr>
					<th width="15%">组合</th>
					<#list gDtoList as dto>
						<#if (dto_index >= (cou-1)*colCount) && (dto_index < cou*colCount)>
							<td <#if dto.notexists==1>class="color-red"</#if>><a href="javascript:" onclick="selectGroup('${dto.subjectIds!}')">${dto.conditionName!}</a></td>
						</#if>
					</#list>
				</tr>
				<tr>
					<th>总人数（班级数）</th>
					<#list gDtoList as dto>
						<#if (dto_index >= (cou-1)*colCount) && (dto_index < cou*colCount)>
							<td>${dto.allNumber?default(0)}<#if (dto.gkGroupClassList?exists) && (dto.gkGroupClassList?size > 0)>(${dto.gkGroupClassList?size})</#if></td>
						</#if>
					</#list>
				</tr>
				<tr>
					<th>剩余人数</th>
					<#list gDtoList as dto>
						<#if (dto_index >= (cou-1)*colCount) && (dto_index < cou*colCount)>
							<td>${dto.leftNumber?default(0)}</td>
						</#if>
					</#list>
				</tr>
			</table>
		</#list>
	</#if>
	<#if gDtoList2?exists && (gDtoList2?size > 0)>
		<#assign tableNum = tableNum+1>
		<h3 <#if (tableNum>0)>class="showtableClass"</#if> >两科组合情况</h3>
		<#assign colCount = 12>
		<#assign xunCount = (gDtoList2?size/colCount)?number >
		<#if (gDtoList2?size%colCount > 0) >
			<#assign xunCount = xunCount + 1 >
		</#if>
		<#list 1..xunCount as cou>
			<table class="table table-bordered table-striped table-hover <#if ((tableNum==0) && (cou>1)) || (tableNum>1)>showtableClass</#if> ">
				<tr>
					<th width="15%">组合</th>
					<#list gDtoList2 as dto>
						<#if (dto_index >= (cou-1)*colCount) && (dto_index < cou*colCount)>
							<td <#if dto.notexists==1>class="color-red"</#if>><a href="javascript:" onclick="selectGroup('${dto.subjectIds!}')">${dto.conditionName!}</a></td>
						</#if>
					</#list>
				</tr>
				<tr>
					<th>总人数（班级数）</th>
					<#list gDtoList2 as dto>
						<#if (dto_index >= (cou-1)*colCount) && (dto_index < cou*colCount)>
							<td>${dto.allNumber?default(0)}<#if (dto.gkGroupClassList?exists) && (dto.gkGroupClassList?size > 0)>(${dto.gkGroupClassList?size})</#if></td>
						</#if>
					</#list>
				</tr>
				<tr>
					<th>剩余人数</th>
					<#list gDtoList2 as dto>
						<#if (dto_index >= (cou-1)*colCount) && (dto_index < cou*colCount)>
							<td>${dto.leftNumber?default(0)}</td>
						</#if>
					</#list>
				</tr>
			</table>
		</#list>
	</#if>
	<#if (gDtoList?exists && (gDtoList?size > 0)) || (gDtoList2?exists && (gDtoList2?size > 0)) ><a href="javascript:" class="tableHideOrShowButtn" id="tableHideOrShowButtn" onclick="tableHideOrShow()">收起详情</a></#if>
</div>
<script>
	var i=0;
	function tableHideOrShow(){
		if(i==0){
			$(".showtableClass").hide();
			$("#tableHideOrShowButtn").html("展开详情");
			i=1;
		}else{
			$(".showtableClass").show();
			$("#tableHideOrShowButtn").html("收起详情");
			i=0;
		}
	}
</script>