<div class="filter">
	<div class="filter-item filter-item-right">
		<a class="btn btn-blue" href="#" onClick="doExport1();">导出</a>
	</div>
	<div class="filter-item filter-item-left">
		<span class="filter-name">考试名称：${examName!}</span>
    </div>
</div>
<div class="table-container">
	<div class="table-container-body">
		<table class="table table-bordered table-striped table-hover">
			<thead>
				<tr>
					<th>序号</th>
					<th>学科</th>
					<th>参与考试人数</th>
					<th>优秀人数</th>
					<th>合格人数</th>
					<th>不合格人数</th>
				</tr>
			</thead>
			<tbody>
			<#if subList?exists && subList?size gt 0>
			    <#list subList as item>
				<tr>
				    <td>${item_index+1!}</td>
				    <td>${item.subjectName!}</td>
				    <td>${item.examNum!}</td>
				    <td>${item.yxCount!}</td>
				    <td>${item.hgCount!}</td>				  
				    <td>${item.bhgCount!}</td>				 
				</tr>
				</#list>
			<#else>
				<tr>
					<td colspan="6" align="center">暂无数据</td>
				</tr>
			</#if>
			</tbody>
		</table>
	</div>
</div>
<script>
function doExport1(){
    document.location.href="${request.contextPath}/teaexam/scoreCount/exportGeneral?examId=${examId!}";
}
</script>