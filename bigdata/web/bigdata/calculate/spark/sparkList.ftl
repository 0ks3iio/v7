<div class="table-show">
   <div class="filter-made mb-10">
		<div class="filter-item filter-item-right clearfix">
	        <button class="btn btn-lightblue" onclick="addSparkJob();">新增</button>
		</div>
	</div>
	<table class="tables tables-border no-margin">
		<thead>
			<tr>
				<th>任务名称</th>
				<th>定时执行</th>
				<th>流计算</th>
				<th>执行时间</th>
				<th>执行状态</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<#if sparkJobs?exists&&sparkJobs?size gt 0>
			<#list sparkJobs as spark>
			<tr>
				<td>${spark.name!}</td>
				<td><#if spark.isSchedule?default(0) ==1>是<#else>否</#if></td>
				<td><#if spark.runType?default(0) ==1>是<#else>否</#if></td>
				<td>${spark.lastCommitTime!}</td>
				<td>
					<#if spark.lastCommitState?default(0) ==0>
						<span>&nbsp;<img src="${request.contextPath}/static/images/big-data/not-begin-icon.png" />&nbsp;&nbsp;未开始</span>
            		 <#elseif spark.lastCommitState?default(0) ==1>
						<span>&nbsp;<img src="${request.contextPath}/static/images/big-data/success-icon.png" />&nbsp;&nbsp;成功</span>
		            <#elseif spark.lastCommitState?default(0) ==2>
						<span>&nbsp;<img src="${request.contextPath}/static/images/big-data/fail-icon.png" />&nbsp;&nbsp;失败</span>
		            <#elseif spark.lastCommitState?default(0) ==3>
						<span>&nbsp;<img src="${request.contextPath}/static/images/big-data/doing-icon.png" />&nbsp;&nbsp;运行中</span>
		            <#elseif spark.lastCommitState?default(0) == -1>
		            	<span>&nbsp;<img src="${request.contextPath}/static/images/big-data/over-icon.png" />&nbsp;&nbsp;结束</span>
		            </#if>
				</td>
				<td>
					<#if spark.lastCommitState?default(3) ==2 && spark.runType?default(0) ==0>
						<a href="javascript:;" class="look-over" onclick="viewErrorLog('${spark.lastCommitLogId!}')">错误日志</a><span class="tables-line">|</span>
					</#if>
					<#if spark.runType?default(0) ==0>
						<a href="javascript:;" class="look-over" onclick="execSparkJob('${spark.id!}')">执行</a><span class="tables-line">|</span>
					</#if>
					<#if spark.lastCommitState?default(0) !=3 && spark.runType?default(0) ==1>
						<a href="javascript:;" class="look-over" onclick="execSparkJob('${spark.id!}')">运行</a><span class="tables-line">|</span>
					</#if>
					<#if spark.lastCommitState?default(0) ==3 && spark.runType?default(0) ==1>
						<a href="javascript:;" class="look-over" onclick="stopSparkJob('${spark.id!}')">停止</a><span class="tables-line">|</span>
					</#if>
					<a href="javascript:;" class="look-over" onclick="editSparkJob('${spark.id!}')">编辑</a><span class="tables-line">|</span>
					<a href="javascript:;" class="remove" onclick="deleteSparkJob('${spark.id!}','${spark.name!}')">删除</a><span class="tables-line">|</span>
					<a id="log-${spark.id!}-button"  href="javascript:;" class="look-over js-log-show" onclick="loadSparkLogList('${spark.id!}');" >日志</a>
				</td>
			</tr>
			 </#list>
	 <#else>
			<tr >
				<td  colspan="6" align="center">
				暂无spark调度任务数据
				</td>
			<tr>
      </#if>
		</tbody>
	</table>
</div>
<div id="logListDiv" class="log-show height-1of1 col-md-3 no-padding-right hide"></div>
<div id="logDiv" style="padding:20px 20px 0;border:1px;"></div>
<div class="layer layer-param">
	<div class="layer-content">
		<div class="form-horizontal">
			<div class="form-group">
				<label">请以JSON格式传入参数，格式如下:{"key1":"value1","key2":"value2"}</label>
			</div>
			<div class="form-group">
			<label class="col-sm-2 control-label no-padding-right">参数：</label>
				<div class="col-sm-6">
					<textarea name="params" id="params" type="text/plain" nullable="true" maxLength="200" style="width:320px;height:160px;"></textarea>
				</div>
			</div>
		</div>
	</div>
</div>

<script>
    function addSparkJob() {
    	router.go({
	        path: '/bigdata/calculate/spark/edit',
	        name:'新增',
	        level: 3
	    }, function () {
		     var url = '${request.contextPath}/bigdata/calculate/spark/edit';
        	$("#contentDiv").load(url);
	    });
    }

	function editSparkJob(id) {
		router.go({
	        path: '/bigdata/calculate/spark/edit?id=' + id,
	        name:'编辑',
	        level: 3
	    }, function () {
		    var url = '${request.contextPath}/bigdata/calculate/spark/edit?id=' + id;
        	$("#contentDiv").load(url);
	    });
    }

     function loadSparkLogList(id){
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

    var checkJob;

    function execSparkJob(id){
        $.ajax({
            url:'${request.contextPath}/bigdata/calculate/spark/exec',
            data:{
                'id':id
            },
            type:"post",
            dataType: "json",
            success:function(data){
                if(!data.success){
                    showLayerTips('error',data.msg,'t');
                }else{
                    showLayerTips('success','运行成功!','t');
                    monitorJob(id);
                    if ($('#runType').val() == "0") {
                        checkJob = window.setInterval("monitorJob()", 5000);
                    }
                };
            }
        });
    }
    
    function monitorJob(id) {
        if (id == null) {
            id = $('#jobId').val();
        }
        var logUrl = "${request.contextPath}/bigdata/calculate/spark/status?id=" + id;
        $("#logListDiv").load(logUrl);
        if ($('#status').val() != "RUNNING" && $('#status').val() != "FAILED") {
            window.clearInterval(checkJob);
        }
    }

    function stopSparkJob(id){
        $.ajax({
            url:'${request.contextPath}/bigdata/calculate/spark/stop',
            data:{
                'id':id
            },
            type:"post",
            dataType: "json",
            success:function(data){
                if(!data.success){
                    showLayerTips('error',data.msg,'t');
                }else{
                   showLayerTips('success','停止成功!','t');
                   showList('4');
                }
            }
        });
    }

    function deleteSparkJob(id,name){
       showConfirmTips('prompt',"提示","您确定要删除"+name+"吗？",function(){
            $.ajax({
                url:"${request.contextPath}/bigdata/calculate/kylin/delete",
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
                     	showList('4');
                    }
                },
                error:function(XMLHttpRequest, textStatus, errorThrown){}
            });
        },function(){

        });
    }
</script>