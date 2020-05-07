<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<table class="table table-bordered table-striped table-hover no-margin">
	<thead>
		<tr>
			<th class="text-center" style="width:15%">考试编号</th>
 			<th class="text-center" style="width:20%">考试名称</th>
			<th class="text-center" style="width:15%">考试类型</th>
			<th class="text-center" style="width:15%">年级</th>
			<th class="text-center" style="width:20%">是否开启填报</th>
 			<th class="text-center" style="width:15%">操作</th>
		</tr>
	</thead>
	<tbody>
		<#if list?exists && (list?size > 0)>
			<#list list as dto>
				<tr>
					<td class="text-center">${dto.examCode!}</td>
					<td class="text-center">${dto.examName!}</td>
					<td class="text-center">${mcodeSetting.getMcode("DM-KSLB","${dto.examType!}")}</td>
					<td class="text-center">${dto.gradeCode!}</td>
					<td class="text-center">
					<#if dto.isOpen == '1'>
						开启
					<#else>
						关闭
					</#if>
					</td>
					<td class="text-center">
						<a href="javascript:doAims('${dto.examId!}');" class="table-btn show-details-btn">编辑</a>
						<a href="javascript:showResult('${dto.examId!}');" class="table-btn show-details-btn">查看志愿结果</a>
					</td>
				</tr>
			</#list>
		<#else>
			<tr>
				<td colspan="6" align="center">
					暂无数据
				</td>
			<tr>
		</#if>
	</tbody>
</table>
<script>
	function doAims(examId){
	   var url = "${request.contextPath}/exammanage/edu/aims/edit/page?examId="+examId;
	   indexDiv = layerDivUrl(url,{title: "信息",width:600,height:400});
	}
	function showResult(examId){
		var url = '${request.contextPath}/exammanage/edu/aims/result/page?examId='+examId;
		$("#model-div-36512").load(url);
	}
</script>