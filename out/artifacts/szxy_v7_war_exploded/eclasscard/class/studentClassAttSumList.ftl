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
							<th>请假节数</th>
							<th>迟到节数</th>
							<th>缺课节数</th>
							<th>详情</th>
						</tr>
					</thead>
					<tbody>
					<#if attNumSumDtos?exists&&attNumSumDtos?size gt 0>
			          	<#list attNumSumDtos as item>
						<tr>
							<td>${item_index+1}</td>
							<td>${item.studentName!}</td>
							<td>${item.qjNum!}</td>
							<td>${item.cdNum!}</td>
							<td>${item.qkNum!}</td>
							<td><a href="javascript:void(0);" onclick="showDetail('${item.studentId!}')">查看</a></td>
						</tr>
			      	    </#list>
			  	    <#else>
						<tr>
							<td  colspan="88" align="center">
							暂无数据
							</td>
						<tr>
			        </#if>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>