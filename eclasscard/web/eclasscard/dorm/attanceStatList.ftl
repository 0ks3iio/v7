<div class="box box-default">
	<div class="box-header">
		<h3 class="box-title">${className!}</h3>
	</div>
	<div class="box-body">
		<div class="table-container">
			<div class="table-container-body">
				<table class="table table-striped">
					<thead>
						<tr>
							<th>序号</th>
							<th>姓名</th>
							<th>寝室号</th>
							<th>已签到次数</th>
							<th>请假次数</th>
							<th>未签到次数</th>
							<th>详情</th>
						</tr>
					</thead>
					<tbody>
						<#if attenceList?exists && attenceList?size gt 0>
							<#list attenceList as item>
							<tr>
								<td>${item_index+1}</td>
								<td>${item.studentName!}</td>
								<td>${item.roomName!}</td>
								<td>${item.inNum!}</td>
								<td>${item.leaveNum!}</td>
								<td>${item.noNum!}</td>
								<td><a href="javascript:toCheck('${item.studentId!}','${(attDto.startTime?string("yyyy-MM-dd"))!}','${(attDto.endTime?string("yyyy-MM-dd"))!}')">查看</a></td>
							</#list>
							</tr>
						<#else>
							<tr>
								<td colspan="7" align="center">暂无数据</td>
							</tr>
						</#if>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>
