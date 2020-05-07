<#if dtoList?exists && (dtoList?size>0)>
<div class="col-sm-12">
	<table class="table table-bordered table-striped table-hover">
		<thead>
			<tr>
				<th>序号</th>
				<th>部门</th>
				<th>姓名</th>
				<th>代课数</th>
				<th>管课数</th>
				<th>被代课数</th>
				<th>被管课数</th>
			</tr>
		</thead>
		<tbody>
				<#list dtoList as item>
					<tr>
						<td>${item_index + 1}</td>
						<td>${item.deptName!}</td>
						<td>${item.teacherName!}</td>
						<td>${item.takeNum!}</td>
						<td>${item.manNum!}</td>
						<td>${item.beTakeNum!}</td>
						<td>${item.beManNum!}</td>
					</tr>
				</#list>
		</tbody>
	</table>
</div>
<#else>
<div class="no-data-container ">
	<div class="no-data">
		<span class="no-data-img">
			<img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
		</span>
		<div class="no-data-body">
			<p class="no-data-txt">暂无相关数据</p>
		</div>
	</div>
</div>
</#if>