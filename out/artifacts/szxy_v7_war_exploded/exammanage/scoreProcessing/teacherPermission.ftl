<div class="layer layer-addTerm layer-change" style="display:block;overflow:auto;height:400px;"  id="myDiv">
	<table class="table table-bordered table-striped table-hover" id="showtable${viewType!}">
	<thead>
		<tr>
			<th class="text-center">科目</th>
			<th class="text-center">班级</th>
		</tr>
	</thead>
	<tbody>
	<#if (emLimitList?exists && emLimitList?size>0)>
	    <#list emLimitList as item>
		<tr>
			<td class="text-center">${item.subjectName!}</td>
			<td class="text-center">${item.classNames!}</td>
		</tr>
		</#list>
	<#else>
		<td class="text-center" colspan="3">
			    还没有记录
		</td>
	</#if>												
	</tbody>
</table>	
</div>	
