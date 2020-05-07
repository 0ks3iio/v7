<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<table class="table table-bordered table-striped table-hover no-margin">
	<thead>
		<tr>
			<th class="text-center" style="width:15%">考试编号</th>
 			<th class="text-center" style="width:20%">考试名称</th>
			<th class="text-center" style="width:15%">考试类型</th>
			<th class="text-center" style="width:15%">填报起始时间</th>
			<th class="text-center" style="width:15%">填报截止时间</th>
 			<th class="text-center" style="width:15%">状态</th>
 			<th class="text-center" style="width:5%">操作</th>
		</tr>
	</thead>
	<tbody>
		<#if dtolist?exists && (dtolist?size > 0)>
			<#list dtolist as dto>
				<tr>
					<td class="text-center">${dto.examCode!}</td>
					<td class="text-center">${dto.examName!}</td>
					<td class="text-center">${mcodeSetting.getMcode("DM-KSLB","${dto.examType!}")}</td>
					<td class="text-center">${dto.startDate?string('yyyy-MM-dd')}</td>
					<td class="text-center">${dto.endDate?string('yyyy-MM-dd')}</td>
					<td class="text-center"><#if dto.state?default("0")=='0'>待填报<#elseif dto.state?default("0")=='1'>已填报</#if></td>
					<td class="text-center">
						<a href="javascript:editStuAims('${dto.aimsId!}');" class="table-btn show-details-btn">填报</a>
					</td>
				</tr>
			</#list>
		<#else>
			<tr>
				<td colspan="7" align="center">
					暂无数据
				</td>
			<tr>
		</#if>
	</tbody>
</table>
<script>
	function editStuAims(aimsId){
	   var url = "${request.contextPath}/exammanage/edu/stuAims/edit?aimsId="+aimsId;
	   indexDiv = layerDivUrl(url,{title: "信息",width:450,height:500});
	}
</script>