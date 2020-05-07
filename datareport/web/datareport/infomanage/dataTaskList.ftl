<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<div id="a2" class="tab-pane active">
	<div class="filter">
		<div class="filter-item">
			<#if tableType!=3>
			<a class="btn btn-white <#if statistics == false>disabled</#if>" href="#" onClick="<#if haveStats>loadExcel('${infoId!}','REPORT_INFO_STATS')<#else>printExcel('${infoId!}','','${tableType!}','2',this)</#if>">导出excel</a>
			<a class="btn btn-white <#if statistics == false>disabled</#if>" href="#" onClick="printPDF('${infoId!}','',2,'${tableType!}')">导出pdf</a>
			</#if>
			<#if isAttachment == 1>
				<a class="btn btn-white <#if statistics == false>disabled</#if>" href="<#if statistics == false>#<#else>${request.contextPath}/datareport/table/downattfiles?infoId=${infoId!}</#if>">下载总附件</a>
			</#if>
			<#if tableType!=3>
			<span>(回收完成或到截止时间时，才可导出统计数据)</span>
			</#if>
		</div>
	</div>
    <div class="table-container">
		<div class="table-container-body">
			<table class="table table-bordered table-striped table-hover">
				<thead>
					<tr>
						<th width="50px">序号</th>
						<th>对象名称</th>
						<th width="100px">回收情况</th>
						<th width="200px">提交时间</th>
						<#if isAttachment == 1>
						<th width="100px">附件数量</th>
						</#if>
						<th width="280px">操作</th>
					</tr>
				</thead>
				<tbody>
					<#if dataReportTasks?exists&&dataReportTasks?size gt 0>
					<#list dataReportTasks as task>
					<tr>
						<td>${task_index+1}</td>
						<td title="${task.objName!}">
						<#if task.objName?exists&&task.objName?length gt 20>${task.objName?substring(0,20)}...<#else>${task.objName!}</#if>
						</td>
						<td>
							<#if task.state == 3>
								<span class="color-green">已提交</span>
							<#else>
								<span class="color-red">未提交</span>
							</#if>
						</td>
						<td>${task.startTime!}~${task.endTime!}</td>
					    <#if isAttachment == 1>
					    <td>
					    	数量:${task.fileNum?default(0)}个
					    </td>
					    </#if>
					    <td>
					    	<a class="color-blue mr10 <#if task.state!=3>disabled</#if>" href="#" <#if task.state==3>onClick="showTaskResult('${task.reportId!}','${task.id!}','${task.existExcel?string("true","false")}','${task.fileNum!}')"</#if>>详情</a>
					    	<a class="color-blue mr10 <#if task.state!=3>disabled</#if>" href="#" <#if task.state==3>onClick="<#if task.existExcel==true>loadExcel('${task.id!}','REPORT_TASK_ATT')<#else>printExcel('${task.reportId!}','${task.id!}','${tableType!}','1',this)</#if>"</#if>>导出excel</a>
					    	<a class="color-blue mr10 <#if task.state!=3>disabled</#if>" href="#" <#if task.state==3>onClick="printPDF('${task.reportId!}','${task.id!}',1,'${tableType!}')"</#if>>导出pdf</a>
					    	<#if isAttachment == 1>
					    		<a class="color-blue <#if task.state!=3 || task.fileNum==0>disabled</#if>" href="<#if task.state==3&&task.fileNum!=0>${request.contextPath}/datareport/table/downattfiles?taskId=${task.id!}<#else>#</#if>">下载附件</a>
					    	</#if>
					    </td>
					</tr>
					</#list>
					</#if>
				</tbody>
			</table>
		</div>
	</div>
	<@htmlcom.pageToolBar container="#tabContentDiv" class="noprint"/>
</div>
<script>
	function showTaskResult(infoId,taskId,existExcel,fileNum) {
		var url = "${request.contextPath}/datareport/infomanage/showresulthead?infoId="+infoId+"&taskId="+taskId+"&existExcel="+existExcel+"&fileNum="+fileNum;
		$("#dataInfoManageDiv").load(url);
	}
</script>