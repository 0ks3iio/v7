<div class="box box-orange">
	<div class="box-header">
		<h4 class="box-title">昨日提醒寝室</h4>
	</div>
	<div class="box-body no-padding" style="height:210px;">
		<table class="table table-bedroom-warn">
			<tbody>
			<#if dormremindList?exists && dormremindList?size gt 0>
			<#list dormremindList as item>
				<tr>
					<td>${item[0]!}</td>
					<td>${item[1]!}</td>
				</tr>
			</#list>
			</#if>
			</tbody>
		</table>
	</div>
</div>