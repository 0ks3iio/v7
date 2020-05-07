<div class="layer layer-openDetail">
	<div class="layer-content">
		<table class="table table-striped no-margin">
			<thead>
				<tr>
					<th>姓名</th>
					<th>寝室</th>
					<th>扣分</th>
					<th>扣分原因</th>
					<th>违纪日期</th>
				</tr>
			</thead>
			<tbody>
				<#if checkDisList?exists && checkDisList?size gt 0>
					<#list checkDisList as item>
					<tr>
						<td>${studentName!}</td>
						<td>${roomName!}</td>
						<td>${(item.score?default(0))?string("0.#")}</td>
						<td>${item.reason!}</td>
						<td>${(item.checkDate?string("yyyy-MM-dd"))!}</td>
					</tr>
					</#list>
				</#if>
			</tbody>
		</table>
		
	</div>
</div><!-- E 个人违纪明细 -->
<script>
$(function(){
	$(".layer-openDetail").show();
});
</script>