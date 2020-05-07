<div class="box box-default">
	<div class="box-header">
		<h3 class="box-title">${className!}</h3>
	</div>
	<div class="box-body">
	<div class="stat">
		<div class="stat-item">
			<div class="stat-item-content">
				<strong>${disciplineScore?string('0.#')}</strong>
				<p>纪律总得分</p>
			</div>
			<div class="stat-item-content">
				<strong>${disciplineRank}</strong>
				<p>学段排名</p>
			</div>
		</div>
		<div class="stat-item">
			<div class="stat-item-content">
				<strong>${healthScore?string('0.#')}</strong>
				<p>卫生总得分</p>
			</div>
			<div class="stat-item-content">
				<strong>${healthRank}</strong>
				<p>学段排名</p>
			</div>
		</div>
		<div class="flags">
		<#if isHealthExcellen>
			<span class="flag flag-health"></span>
			</#if>
			<#if isDisciplineExcellen>
			<span class="flag flag-discipline"></span>
			</#if>
		</div>
	</div>
	<table class="table table-bordered table-striped">
		<thead>
			<tr>
				<th>考核项</th>
				<th>类别</th>
				<th>周日</th>
				<th>周一</th>
				<th>周二</th>
				<th>周三</th>
				<th>周四</th>
				<th>周五</th>
				<th>周六</th>
			</tr>
		</thead>
		<tbody>
			
			<#assign days = [7,1,2,3,4,5,6]>
			<#if dtos?exists && dtos?size gt 0>
			<#list dtos as listDto>
				<tr>
					<td>${listDto.itemName!}</td>
					<td><#if listDto.itemType == '1'>卫生
					<#else>
					纪律
					</#if>
					</td>
					<#list days as day>
						<#list listDto.dtos as dto>
							<#if day = dto.day>
								<#if dto.unCheck>
								<td>/</td>
								<#elseif listDto.itemType == '3'>	
								<td>${dto.score?string('0.#')}</td>
								<#else>
									<#if dto.allUnSubmint>
									<td><font size="1px">没有任<br/>何管理<br/>员提交</font></td>
									<#else>
									<#assign noSubRole = ''>
									<#list dto.unSubRole as roleStr>
									<#if noSubRole == ''>
										<#assign noSubRole = "未录入："+roleStr>
									<#else>
										<#assign noSubRole = noSubRole + '、'+roleStr>
										</#if>
									</#list>
									<#assign remark = ''>
									<#list dto.result as result>
									<#if result.remark?default('') !=''>
										<#assign remark= remark + result.roleName+"备注:"+result.remark+";">
										</#if>
									</#list>
									<td data-toggle="popover" title="录入详情" data-content="${noSubRole!}<#if noSubRole?exists && noSubRole?length gt 0>&emsp;&emsp;</#if>${remark!}">${dto.score?string('0.#')}</td>
									</#if>
								</#if>
							</#if>
						</#list>
					</#list>
				</tr>
				</#list>
			</#if>
		</tbody>
	</table>
</div>
</div>
<script>
			$(function(){
				$('[data-toggle="popover"]').popover({
					container: 'body',
					trigger: 'hover'
				})
			});
</script>