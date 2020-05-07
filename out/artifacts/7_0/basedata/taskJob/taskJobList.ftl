<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<table class="table table-bordered table-striped table-hover no-margin">
	<thead>
	<tr>
		<th style="width:22%">任务名称</th>
		<th style="width:10%">状态</th>
		<th style="width:12%">任务开始</th>
		<th style="width:12%">任务结束</th>
		<th style="width:12%">创建时间</th>
		<th style="width:20%">反馈信息</th>
		<th style="width:12%">操作</th>
	</tr>
	</thead>
	<tbody>
	<#if taskRecordList?exists && (taskRecordList?size>0)>
	<#list taskRecordList as item>
	<tr>
		<td>${item.name!}</td>
		<td>
			<#if item.status?default(0) == 0>
			等待执行
			<#elseif item.status?default(0) == 1>
			正在执行
			<#elseif item.status?default(0) == 2>
			执行成功
			<#elseif item.status?default(0) == 3>
			<span class="color-red">执行失败</span>
			<#elseif item.status?default(0) == 4>
			预处理
			<#elseif item.status?default(0) == 9>
			不需要处理
			<#else>
			未知状态
			</#if>
		</td>
		<td>${(item.jobStartTime?string('yyyy-MM-dd HH:mm'))!}</td>
		<td>${(item.jobEndTime?string('yyyy-MM-dd HH:mm'))!}</td>
		<td>${(item.creationTime?string('yyyy-MM-dd HH:mm'))!}</td>
		<td>${item.resultMsg!}</td>
		<td>
			<a href="javascript:;" class="table-btn color-red js-delete" onclick="doDeleteById${businessType}('${item.id!}',${item.status?default(0)})">删除</a>
		</td>
	</tr>
	</#list>
	</#if>
	</tbody>
</table>
<@htmlcom.pageToolBar container="#taskJobList${businessType}"/>
<script type="text/javascript">
	function doDeleteById${businessType}(id,status){
		var sendIds;
		showConfirmMsg('确认删除当前任务？','提示',function(){
			var ii = layer.load();
			$.ajax({
				url:'${request.contextPath}/basedata/taskJob/delete',
				data: {'id':id,'status':status},
				type:'post',
				success:function(data) {
					var jsonO = JSON.parse(data);
			 		if(jsonO.success){
			 			layer.closeAll();
			 			taskJobList${businessType}();
			 		}
			 		else{
						layerTipMsg(jsonO.success,"失败",jsonO.msg);
					}
					layer.close(ii);
				},
		 		error : function(XMLHttpRequest, textStatus, errorThrown) {  
		 			
				}
			});
		});
	}
</script>
