<#if planType! == "A">
	<#assign batchstr = "选考">
<#else>
	<#assign batchstr = "学考">
</#if>
<table class="table table-striped table-bordered table-hover no-margin">
	<thead>
		<tr>
			<th>科目</th>
			<#if allBatchs?? && allBatchs?size gt 0>
			<#list allBatchs as batch>
			<th>${batchstr!}${batch!}</th>
			</#list>
			</#if>
		</tr>
	</thead>
	<tbody>
	<#if stuCountMap?? && stuCountMap?size gt 0>
	<#list stuCountMap?keys as sub>
	<#assign subCounts = stuCountMap[sub]!>
		<tr>
			<td>${sub!}</td>
			<#list subCounts as count>
			<td>${(count[0]+count[1])!}</td>
			</#list>
		</tr>
	</#list>
	</#if>
	</tbody>
</table>