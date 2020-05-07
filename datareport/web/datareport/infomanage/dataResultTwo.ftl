<h3>${titleName!}</h3>
<div class="table-container no-margin" style="overflow-x:auto;">
	<div class="table-container-body">
		<table class="table table-bordered table-striped table-hover">
			<tbody>
				<#if rankColumns?exists&&rankColumns?size gt 0>
					<#list rankColumns as rank>
						<tr id="rankTr${rank_index}">
							<th width="180px" style="white-space: nowrap;min-width: 180px;"><#if rank.isNotnull == 1><span class="color-red">*</span>&nbsp;</#if>${rank.columnName!}</th>
						</tr>
					</#list>
				</#if>
			</tbody>
		</table>
	</div>
</div>
<#if remark?exists>
</br>
<div>
	<textarea type="text/plain" style="width:100%;height:50px;">${remark!}</textarea>
</div>
</#if>
<script>
	$(function(){
	<#if resultsList?exists&&resultsList?size gt 0>
		<#list resultsList as results>
			<#list results as result>
				$("#rankTr${result_index}").append('<td style="min-width: 180px;"><#if result!="null">${result!}</#if></td>');
			</#list>
		</#list>
	</#if>
	<#if sumResult?exists&&sumResult?size gt 0>
		<#list sumResult as sum>
			$("#rankTr${sum_index}").append('<td style="white-space: nowrap;min-width: 180px;"><#if sum!="null"><#if rankColumns[sum_index].methodType==1>取平均：</#if><#if rankColumns[sum_index].methodType==2>求和：</#if>${sum!}</#if></td>');
		</#list>
	</#if>
	})
</script>