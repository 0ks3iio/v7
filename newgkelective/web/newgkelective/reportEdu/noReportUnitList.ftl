<#import  "/fw/macro/htmlcomponent.ftl" as htmlcomponent />
<#if unitDtoList?exists && unitDtoList?size gt 0>
	<table class="table table-bordered table-striped table-hover">
		<thead>
			<tr>
				<th>序号</th>
				<th>学校名称 </th>
				<th>所属教育局</th>
				<th>单位负责人</th>
				<th>联系电话</th>
				<#if isShowTime>
				<th>选课时间</th>
				</#if>
			</tr>
		</thead>
		<tbody>
			<#list unitDtoList as item1>
				<tr>
					<td>${item1_index+1}</td>
					<td>${item1.schoolName!}</td>
					<td>${item1.parentUnitName!}</td>
					<td>${item1.schoolLeader!}</td>
					<td>${item1.telephoneNumber!}</td>
					<#if isShowTime>
					<td>${item1.chooseTime!}</td>
					</#if>
				</tr>
			</#list>
		</tbody>
	</table>
	<@htmlcomponent.pageToolBar2 pageInfo=pageInfo function="reload" allNum=allNum/>
	<script>
	function reload(pageIndex,pageSize){
		if(!pageSize){
			pageSize = ${pageInfo.pageSize!};
		}
		if(!pageIndex){
			pageIndex = $('#pagebar li.active a').text();
		}
		var chooseUnitId=$("#chooseUnitId").val();
		$("#unitTableList").load("${request.contextPath}/newgkelective/edu/noreportList/page?gradeYear=${gradeYear!}&type=${type!}&unitId="+chooseUnitId+"&pageSize="+pageSize+"&pageIndex="+pageIndex);
	}
</script>
<#else>
	<table class="table table-bordered table-striped table-hover">
		<thead>
			<tr>
				<th>序号</th>
				<th>学校名称 </th>
				<th>所属教育局</th>
				<th>单位负责人</th>
				<th>联系电话</th>
				<#if isShowTime>
				<th>选课时间</th>
				</#if>
			</tr>
		</thead>
		<tbody>
			<tr><td colspan="16" align="center">暂无数据</td></tr>
		</tbody>
	</table>
</#if>

