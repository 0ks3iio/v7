
<div class="table-container">
	<div class="table-header">
		<table class="table">
			<thead>
				<tr>
					<th width="40%">寝室号</th>
					<th>提醒事项</th>
				</tr>
			</thead>
		</table>
	</div>
	<div class="table-body" style="height:244px;">
		<table class="table">
			<tbody>
			<#if dormremindList?exists && dormremindList?size gt 0>
			<#list dormremindList as item>
				<tr>
					<td width="40%" class="color-ink">${item[0]!}</td>
					<td>${item[1]!}</td>
				</tr>
			</#list>
			</#if>
			</tbody>
		</table>
	</div>
</div>