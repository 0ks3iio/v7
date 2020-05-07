<#import  "/fw/macro/htmlcomponent.ftl" as htmlcomponent />
<div class="table-container">
	<div class="table-container-header text-right">
		<a href="javascript:doExportAll();" class="btn btn-blue">导出学生名单</a>
	</div>
	<div class="table-container-body">
		<table class="table table-bordered layout-fixed table-editable table-striped table-hover">
			<thead>
				<tr>
					<th >序号</th>
					<th >班级名称</th>
					<th >选考上课科目</th>
					<th >学考上课科目</th>
					<th >总人数</th>
					<th >男</th>
					<th >女</th>
					<th >查看学生</th>
				</tr>
			</thead>
			<tbody>
			<#if dtoList?exists && dtoList?size gt 0 >
			<#list dtoList as dto>
				<tr>
					<td>${dto_index+1}</td>
					<td >${dto.className!}</td>
					<td >${dto.courseA!}</td>
					<td >${dto.courseB!}</td>
					<td >${dto.allNum!}</td>
					<td >${dto.boyNum!}</td>
					<td >${dto.girlNum!}</td>
					<td ><a href="javascript:void(0)" onclick="showDetail('${dto.classId!}','${type!}')">查看</a></td>
				</tr>
			</#list>
			</#if>
			</tbody>
		</table>
	
	</div>
</div>

<script>
	function doExportAll(){
		var url =  '${request.contextPath}/newgkelective/${divideId!}/exportAllClaStu?type=J';
	  	document.location.href=url;
	}
</script>

