<div class="table-container">
	<div class="table-header">
		<table class="table">
			<thead>
				<tr>
					<th width="30%">寝室号</th>
					<th width="30%">得分</th>
					<th>得分说明</th>
				</tr>
			</thead>
		</table>
	</div>
	<div class="table-body" style="height:244px;">
		<table class="table">
			<tbody>
			<#if dormscoreList?exists && dormscoreList?size gt 0>
			<#list dormscoreList as item>
				<tr>
					<td width="30%" class="color-ink">${item[0]!}</td>
					<td width="30%" class="color-yellow"><b>${item[1]!}</b></td>
					<td>${item[2]!}</td>
				</tr>
			</#list>
			</#if>	
			</tbody>
		</table>
	</div>
</div>