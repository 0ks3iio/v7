<div class="box box-default">
	<div class="box-body clearfix">
		<div class="filter">
			<div class="filter-item">
				<span class="filter-name">年份：</span>
				<div class="filter-content">
                    <select name="year" id="year" class="form-control" onchange="doSearch()" style="width:135px">
		                    <#list minYear..maxYear as item>
		                        <option value="${item!}" <#if '${year!}'=='${item!}'>selected="selected"</#if>>${item!}年</option>
		                    </#list>			                       
		               </select>
				</div>
			</div>
		</div>
        <div class="table-container">
			<div class="table-container-body">
				<table class="table table-bordered table-striped table-hover">
					<thead>
						<tr>
							<th>序号</th>
							<th width="15%">考试名称</th>
							<th width="30%">考试科目</th>
							<th>报名时间</th>
							<th>考场设置</th>
							<th>考号设置</th>
							<th>考场考生设置</th>
						</tr>
					</thead>
					<tbody>
						<#if teaexamInfoList?exists && teaexamInfoList?size gt 0>
						<#list teaexamInfoList as item>
						    <tr>
						       <td>${item_index+1}</td>
						       <td style="word-break:break-all;">${item.examName!}</td>
						       <td style="word-break:break-all;">${item.subNames!}</td>
						       <td>${item.registerBegin?string("yyyy-MM-dd")!}~${item.registerEnd?string("yyyy-MM-dd")!}</td>
						       <td>
							          <a class="color-blue mr10" href="javascript:void(0);" onClick="toSet('${item.id!}','1',1);">去设置</a>
						       </td>
						       <td>
							          <a class="color-blue mr10" href="javascript:void(0);" onClick="toSet('${item.id!}','2',1);">去设置</a>
						       </td>
						       <td>
							          <a class="color-blue mr10" href="javascript:void(0);" onClick="toSet('${item.id!}','3',1);">去设置</a>
						       </td>						       
						    </tr>		
						</#list>
					<#else>
						<tr>
							<td colspan="7" align="center">暂无数据</td>
						</tr>
					</#if>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>
<script>
function doSearch(){
	var ay = $('#year').val();
	var url = '${request.contextPath}/teaexam/siteSet/index/page?year='+ay;
	$('.model-div').load(url);
}

function toSet(examId, type, canEdit){
	var ay = $('#year').val();
	var url = '${request.contextPath}/teaexam/siteSet/setIndex/'+examId+'/page?type='+type+'&canEdit='+canEdit+'&year='+ay;
	$('.model-div').load(url);
}
</script>