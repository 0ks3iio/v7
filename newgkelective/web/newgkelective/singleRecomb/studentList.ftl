<div class="box box-default">
	<div class="box-header">
		<h3 class="box-title"><#if clazz?exists>${clazz.className!}</#if>学生名单</h3>
		<div class="filter-item filter-item-right">
			<a href="javascript:" class="btn btn-blue" onclick="gobackIndex()">返回</a>
		</div>
	</div>
	<div class="box-body">
		<div class="table-container">
			<#if dtoList?exists && dtoList?size gt 0>
			<div class="table-container-header">共${dtoList?size}份结果</div>
			<#else>
			<div class="table-container-header">没有结果</div>
			</#if>
			<div class="table-container-body">
				<table class="table table-bordered table-striped table-hover">
					<thead>
						<tr>
							<th>序号</th>
							<th>姓名</th>
							<th>学号</th>
							<th>性别</th>
							<th>已选学科</th>
						</tr>
					</thead>
					<tbody>
					<#if dtoList?exists && dtoList?size gt 0>
					<#list dtoList as dto>
						<tr>
							<td>${dto_index+1}</td>
							<td>${dto.studentName!}</td>
							<td>${dto.studentCode!}</td>
							<td>${dto.sex!}</td>
							<td>${dto.chooseSubjects}</td>
						</tr>
					</#list>
					</#if>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>
<script>
	function gobackIndex(){
		var url =  '${request.contextPath}/newgkelective/${divideId!}/divideClass/item';
		$("#showList").load(url);
	}
</script>