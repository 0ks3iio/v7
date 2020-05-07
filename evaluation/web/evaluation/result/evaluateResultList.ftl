<div class="main-content">
	<div class="main-content-inner">
		<div class="model-div">
			
			<div class="box box-default">
				<div class="box-body">
					<div class="table-container">
						<div class="table-container-body">
							<table class="table table-bordered table-striped">
								<thead>
									<tr>
										<th width="5%">序号</th>
										<th width="15%">调查类型</th>
										<th width="30%">调查时间</th>
										<th width="30%">包含类别</th>
										<th width="10%">调查状态</th>
										<th width="10%">操作</th>
									</tr>
								</thead>
								<tbody>
									<#if projectList?exists && projectList?size gt 0>
									<#list projectList as item>
									<tr>
										<td>${item_index+1}</td>
										<td>${mcodeSetting.getMcode("DM-PJLX",item.evaluateType?default("10"))}</td>
										<td>${item.beginTime?string("yyyy-MM-dd HH:mm")!}&nbsp;-&nbsp;${item.endTime?string("yyyy-MM-dd HH:mm")!}</td>
										<td>${item.containsType!}</td>
										<#if item.containsType?default("")!="">
											<#if item.status?default("0")=="0">
												<td>未开始</td>
												<td><a href="javascript:void(0);" onclick="editResult('${item.id!}','${item.evaluateType!}','${item.status!}')" >查看</a></td>
											<#elseif item.status?default("0")=="1">
												<td>进行中</td>
												<td><a href="javascript:void(0);" onclick="editResult('${item.id!}','${item.evaluateType!}','${item.status!}')" >开始答题</a></td>
											<#else>
												<td>已结束</td>
												<td><a href="javascript:void(0);" onclick="editResult('${item.id!}','${item.evaluateType!}','${item.status!}')" >查看</a></td>
											</#if>
										<#else>
											<td></td><td></td>
										</#if>
									</tr>
									</#list>
									<#else>
									<tr><td colspan="6" align="center">暂无数据</td></tr>
									</#if>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
					
		</div><!-- /.model-div -->
	</div>
</div><!-- /.main-content -->
<script>
	function editResult(projectId,evaluateType,status){
		var url="${request.contextPath}/evaluate/stu/edit?projectId="+projectId+"&evaluateType="+evaluateType+"&status="+status;
		$("#itemShowDivId").load(url);
	}
</script>
