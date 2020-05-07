<div class="box box-default">
	<div class="box-header">
		<h3 class="box-title">${deptName!}</h3>
	</div>
	<div class="box-body">
		<div class="table-container">
			<div class="table-container-body">
				<table class="table table-striped">
					<thead>
						<tr>
							<th>姓名</th>
							<th>迟到节数</th>
							<th>缺课节数</th>
							<th>详情</th>
						</tr>
					</thead>
					<tbody>
					<#if teaclzAttences?exists&&teaclzAttences?size gt 0>
			          	<#list teaclzAttences as item>
						<tr>
							<td>${item.teaRealName!}</td>
							<td>${item.cdNum!}</td>
							<td>${item.wqdNum!}</td>
							<td><a href="javascript:void(0);" onclick="showDetail('${item.teacherId!}')">查看</a></td>
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