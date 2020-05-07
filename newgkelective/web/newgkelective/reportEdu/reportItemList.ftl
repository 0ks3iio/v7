<#import  "/fw/macro/htmlcomponent.ftl" as htmlcomponent />
<#if unitDtoList?exists && unitDtoList?size gt 0>
<table id="example" class="table table-bordered table-striped table-hover">
	<thead>
		<tr>
			<th>序号</th>
			<th>学校名称 </th>
			<th>选择人数</th>
			<th>校内占比</th>
		</tr>
	</thead>
	<tbody>
		<#list unitDtoList as dto>
		<tr>
			<td>${dto_index+1}</td>
			<td>${dto.schoolName!}</td>
			<td>${dto.boyStudentNum+dto.girlStudentNum}<span class="color-999">（男生：${dto.boyStudentNum}，女生：${dto.girlStudentNum}）</span></td>
			<td>${dto.ratioNum}%</td>
		</tr>
		</#list>
	</tbody>
</table>
<@htmlcomponent.pageToolBar2 pageInfo=pageInfo function="reload2" allNum=allNum/>
<script>
function reload2(pageIndex,pageSize){
	console.log(115);
	if(!pageSize){
		pageSize = ${pageInfo.pageSize!};
	}
	if(!pageIndex){
		pageIndex = $('#pagebar li.active a').text();
	}
	$("#mylisttable").load("${request.contextPath}/newgkelective/edu/subjectsResultList/page?unitId=${unitId!}&gradeYear=${gradeYear!}&subjectIds=${subjectIds!}&pageSize="+pageSize+"&pageIndex="+pageIndex);
}
</script>
<#else>
	<table class="table table-bordered table-striped table-hover">
		<thead>
			<tr>
				<th>序号</th>
				<th>学校名称 </th>
				<th>选择人数</th>
				<th>校内占比</th>
			</tr>
		</thead>
		<tbody>
			<tr><td colspan="16" align="center">暂无数据</td></tr>
		</tbody>
	</table>
</#if>