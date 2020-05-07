<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<#if dataReportTasks?exists&&dataReportTasks?size gt 0>
<div class="table-container no-margin">
	<div class="table-container-body">
		<table class="table table-bordered table-striped table-hover">
			<thead>
				<tr>
					<th width="50px">序号</th>
					<th>问卷名称</th>
					<th>上级单位</th>
					<th width="200px">问卷收集日期</th>
					<th width="100px">状态</th>
					<th width="100px">操作</th>
				</tr>
			</thead>
			<tbody>
				<#list dataReportTasks as task>
				<tr>
				    <td>${task_index+1}</td>
				    <td title="${task.title!}">
				    	<#if task.title?exists&&task.title?length gt 20>${task.title?substring(0,20)}...<#else>${task.title!}</#if>
				    </td>
				    <td>${task.unitName!}</td>
				    <td>${task.startTime!}~${task.endTime!}</td>
				    <td>
				    	<#if task.state == 3>
				    		已提交
				    	<#else>
				    		<span class="color-red">未提交</span>
				    	</#if>
				    </td>
				    <td><a class="color-blue" href="#" onClick="showTaskResult('${task.id!}')">提交问卷</a></td>
				</tr>
				</#list>
			</tbody>
		</table>
	</div>
</div>
<@htmlcom.pageToolBar container="#taskListDiv" class="noprint"/>
<#else>
	<div class="no-data-container">
		<div class="no-data">
			<span class="no-data-img">
				<img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
			</span>
			<div class="no-data-body">
				<p class="no-data-txt">暂无相关数据</p>
			</div>
		</div>
	</div>
</#if>
<script>
	function showTaskResult(taskId) {
		var url = "${request.contextPath}/datareport/taskresult/showtask?taskId="+taskId;
		$("#dataTaskDiv").load(url);
		$("#dataTaskDiv").attr("style","display:block");
		$("#dataTaskListDiv").attr("style","display:none");
	}
</script>