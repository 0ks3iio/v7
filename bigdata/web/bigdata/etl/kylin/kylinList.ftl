<div class="table-show">
	<table class="tables tables-border no-margin">
		<thead>
			<tr>
				<th>任务名称</th>
				<th>定时执行</th>
				<th>上次执行时间</th>
				<th>上次执行状态</th>
				<th>操作</th>	
			</tr>
		</thead>
		<tbody>
		<#if kylinList?exists && kylinList?size gt 0>
			<#list kylinList as kylin>
			<tr>
				<td>${kylin.etlJob.name!}</td>
				<td><#if kylin.etlJob.isSchedule?default(0) ==1>是<#else>否</#if></td>
				<td>${kylin.etlJob.lastCommitTime!}</td>
				<td>
						<#if kylin.lastCommitState?default(0) ==1>
							<span class="success-left">成功</span>
						<#elseif kylin.lastCommitState?default(0) ==2>
							<span class="fail-left">失败</span>
						<#else>
							<span>&nbsp;<img src="${request.contextPath}/static/images/big-data/not-begin-icon.png" />&nbsp;&nbsp;未开始</span>	
						</#if>
				</td>
				<td>
					<#if kylin.etlJob.lastCommitState?default(3) ==2>
						<a class="look-over" href="javascript:void(0);" onclick="viewErrorLog('${kylin.etlJob.lastCommitLogId!}')">
						错误日志</a><span class="tables-line">|</span>
					</#if>
					<#if kylin.etlJob.id! !="">
					<a href="javascript:;" onclick="execKylinJob('${kylin.etlJob.id!}')">执行</a><span class="tables-line">|</span>
					</#if>
					<a class="look-over" href="javascript:void(0);" onclick="editKylinJob('${kylin.etlJob.id!}','${kylin.name!}')">编辑</a>
					<#if kylin.etlJob.id! !="">
					<span class="tables-line">|</span>
					<a class="remove" href="javascript:void(0);" onclick="deleteKylinJob('${kylin.etlJob.id!}','${kylin.etlJob.name!}')">删除</a>
					<span class="tables-line">|</span>
					<a id="log-${kylin.etlJob.id!}-button"  href="javascript:;" class="look-over js-log-show" onclick="loadKylinLogList('${kylin.etlJob.id!}');" >日志</a>
					</#if>
				</td>
			</tr>
			 </#list>
	 <#else>
			<tr >
				<td  colspan="5" align="center">
						暂无kylin调度任务数据
				</td>
			<tr>
      </#if>
		</tbody>
	</table>
</div>
<div id="logListDiv" class="log-show height-1of1 col-md-3 no-padding-right hide"></div>
<div id="logDiv" style="padding:20px 20px 0;border:1px;"></div>
<script>
	function editKylinJob(id,name){
		router.go({
	        path: 'bigdata/etl/kylin/edit?cubeName='+name,
	        name:'编辑',
	        level: 3
	    }, function () {
		    var url = "${request.contextPath}/bigdata/etl/kylin/edit?cubeName="+name;
			$("#contentDiv").load(url);
	    });
	}
	
	function loadKylinLogList(id){
			var logButtonObj=$("#log-"+id+"-button");
        	if(logButtonObj.text() == '日志'){
        		logButtonObj.parents('.table-show').addClass('col-md-9 no-padding-left');
        		$('.log-show').removeClass('hide');
        		logButtonObj.text('关闭');
        		if(logButtonObj.parents('tr').siblings('tr').find('.js-log-show').text().indexOf('关闭') != -1){
        			logButtonObj.parents('tr').siblings('tr').find('.js-log-show').text('日志')
        		}
        		var logUrl = "${request.contextPath}/bigdata/etl/log/jobId?jobId="+id;
				$("#logListDiv").load(logUrl);
        	}else{
        		logButtonObj.parents('.table-show').removeClass('col-md-9 no-padding-left');
        		$('.log-show').addClass('hide');
        		logButtonObj.text('日志');
				$("#logListDiv").empty();
        	}
     }
	  
	function viewErrorLog(logId){
		var url =  '${request.contextPath}/bigdata/etl/viewLog?logId='+logId;
		 $("#logDiv").load(url,function(){
	        layer.open({
	            type: 1,
	            shade: .5,
	            title: ['错误日志','font-size:16px'],
	            area: ['800px','600px'],
	            maxmin: false,
	            btn:['确定'],
	            content: $('#logDiv'),
	            resize:true,
	            yes:function (index) {
	                layer.closeAll();
	                 $("#logDiv").empty();
	            },
	            cancel:function (index) {
	                layer.closeAll();
	                 $("#logDiv").empty();
	            }
	        });
	        $("#logDiv").parent().css('overflow','auto');
	    })
	}

	function execKylinJob(id){
		 $.ajax({
	            url:'${request.contextPath}/bigdata/etl/job/exec',
	            data:{
	              'id':id
	            },
	            type:"post",
	            dataType: "json",
	            success:function(data){
			 		if(!data.success){
			 			 showLayerTips('error',data.msg,'t');
			 		}else{
						 showLayerTips('success',data.msg,'t');	
	    			}
	    			$("#contentDiv").load("${request.contextPath}/bigdata/etl/list?etlType=2");
	          }
	    });
	}
    
	function deleteKylinJob(id,name){
		showConfirmTips('prompt',"提示","您确定要删除["+name+"]吗？",function(){
			 $.ajax({
		            url:"${request.contextPath}/bigdata/etl/kylin/delete",
		            data:{
		              'id':id
		            },
		            type:"post",
		            dataType: "json",
		            success:function(data){
						layer.closeAll();
				 		if(!data.success){
				 			showLayerTips4Confirm('error',data.msg);
				 		}else{
				 		    showLayerTips('success',data.msg,'t');
							$("#contentDiv").load("${request.contextPath}/bigdata/etl/list?etlType=2");
		    			}
		          },
		          error:function(XMLHttpRequest, textStatus, errorThrown){}
		    });
		},function(){
	
		});
	}
</script>