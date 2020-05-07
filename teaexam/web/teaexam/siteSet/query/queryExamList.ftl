<form id="submitForm">
	<div class="table-container-body">
		<table class="table table-striped table-hover">
			<thead>
				<tr>
					<th>序号</th>
					<th>考试名称</th>										
					<th>考试科目</th>
					<th>报名开始时间</th>
					<th>报名结束时间</th>
					<th>考场门贴</th>
					<th>考生桌贴</th>
					<th>考生准考证</th>
				</tr>
				</thead>
				<tbody>
					<#if teaexamInfoList?exists && teaexamInfoList?size gt 0>
						<#list teaexamInfoList as item>
						    <tr>
						       <td>${item_index+1!}</td>
						       <td width="20%" style="word-break:break-all;">${item.examName!}</td>
						       <td width="30%" style="word-break:break-all;">${item.subNames!}</td>
						       <td>${item.registerBegin?string("yyyy-MM-dd")!}</td>
						       <td>${item.registerEnd?string("yyyy-MM-dd")!}</td>
						       <td><a class="color-blue mr10" href="javascript:void(0);" onClick="siteQueryTab('${item.id!}','1');">去查看</a></td>
						       <td><a class="color-blue mr10" href="javascript:void(0);" onClick="siteQueryTab('${item.id!}','2');">去查看</a></td>
						       <td><a class="color-blue mr10" href="javascript:void(0);" onClick="siteQueryTab('${item.id!}','3');">去查看</a></td>
						    </tr>		
						</#list>
					<#else>
						<tr>
							<td colspan="8" align="center">暂无数据</td>
						</tr>
					</#if>
				</tbody>
		</table>
	</div>
</form>
<script>
function siteQueryTab(examId, state){
    var acadyear = $('#year').val();
    var url = "${request.contextPath}/teaexam/siteSet/query/siteQueryTab?year="+acadyear+"&examId="+examId+"&index="+state;
    $(".model-div").load(url);
}
</script>