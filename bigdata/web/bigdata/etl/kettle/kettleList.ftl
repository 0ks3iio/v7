<div class="table-show">
    <div class="filter-made mb-10">
		<div class="filter-item filter-item-right clearfix">
	        <button class="btn btn-lightblue" onclick="addKettleJob();">新增</button>
		</div>
	</div>
	<table class="tables tables-border no-margin">
		<thead>
			<tr>
				<th>任务名称</th>
				<th>任务类型</th>
				<th>定时执行</th>
				<th>上次执行时间</th>
				<th>上次执行状态</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<#if kettleList?exists&&kettleList?size gt 0>
			<#list kettleList as kettle>
			<tr>
				<td>${kettle.name!}</td>
				<td>${kettle.jobType!}</td>
				<td><#if kettle.isSchedule?default(0) ==1>是<#else>否</#if></td>
				<td>${kettle.lastCommitTime!}</td>
				<td>
						<#if kettle.lastCommitState?default(0) ==1>
							<span class="success-left">成功</span>
						<#elseif kettle.lastCommitState?default(0) ==2>
							<span class="fail-left">失败</span>
						<#else>
							<span>&nbsp;<img src="${request.contextPath}/static/images/big-data/not-begin-icon.png" />&nbsp;&nbsp;未开始</span>	
						</#if>
				</td>
				<td>
					<#if kettle.lastCommitState?default(3) ==2>
						<a href="javascript:;" class="look-over" onclick="viewErrorLog('${kettle.lastCommitLogId!}')">错误日志</a><span class="tables-line">|</span>
					</#if>
					<a href="javascript:;" class="look-over" onclick="execKettleJob('${kettle.id!}','${kettle.hasParam?default(0)}')">执行</a><span class="tables-line">|</span>
					<a href="javascript:;" class="look-over" onclick="editKettleJob('${kettle.id!}')">编辑</a><span class="tables-line">|</span>
					<a href="javascript:;" class="delete" onclick="deleteKettleJob('${kettle.id!}','${kettle.name!}')">删除</a><span class="tables-line">|</span>
					<a id="log-${kettle.id!}-button"  href="javascript:;" class="look-over js-log-show" onclick="loadKettleLogList('${kettle.id!}');" >日志</a>
				</td>
			</tr>
			 </#list>
	 <#else>
			<tr >
				<td  colspan="6" align="center">
				暂无Kettle调度任务数据
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
	function loadKettleLogList(id){
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
	
	function addKettleJob(){
		router.go({
	        path: '/bigdata/etl/kettle/edit',
	        name:'新增',
	        level: 3
	    }, function () {
		    var url =  '${request.contextPath}/bigdata/etl/kettle/edit';
			$("#contentDiv").load(url);
	    });
	}
	
	function editKettleJob(id){
		router.go({
	        path: 'bigdata/etl/kettle/edit?id='+id,
	        name:'编辑',
	        level: 3
	    }, function () {
		    var url =  '${request.contextPath}/bigdata/etl/kettle/edit?id='+id;
			$("#contentDiv").load(url);
	    });
	}

	function execKettleJob(id,hasParam){
		if(hasParam =="0"){
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
		    		  	var url =  "${request.contextPath}/bigdata/etl/list?etlType=1";
						$("#contentDiv").load(url);	
		          }
		    });
	    }else{
		   layer.open({
		    	type: 1,
		    	shade: 0.5,
		    	closeBtn:1,
		    	title: '请输入执行参数',
		    	area: '500px',
		    	btn: ['确定'],
		    	yes:function(index,layero){
	                 $.ajax({
	                    url:"${request.contextPath}/bigdata/etl/job/exec",
	                    data: {
		                    'id':id,
		                    'params':$("#params").val()
	                    },  
		                dataType:'json',
		                type:'post',
		                success:function (data) {
			                if(!data.success){
					 			showLayerTips('error',data.msg,'t');
					 		}else{
					 			showLayerTips('success',data.msg,'t');
			    			}
			    			var url =  "${request.contextPath}/bigdata/etl/list?etlType=1";
							$("#contentDiv").load(url);	
			    			parent.layer.close(index);
		                }
	                })
	            },
		    	content: $('.layer-param')
		    })
	    }
	}
    
	function deleteKettleJob(id,name){
		  showConfirmTips('prompt',"提示","您确定要删除"+name+"吗？",function(){
			 $.ajax({
		            url:"${request.contextPath}/bigdata/etl/kettle/delete",
		            data:{
		              'id':id
		            },
		            type:"post",
		            clearForm : false,
					resetForm : false,
		            dataType: "json",
		            success:function(data){
						layer.closeAll();
				 		if(!data.success){
				 			showLayerTips4Confirm('error',data.msg);
				 		}else{
				 		    showLayerTips('success',data.msg,'t');
				 		   	var url =  "${request.contextPath}/bigdata/etl/list?etlType=1";
							$("#contentDiv").load(url);	
		    			}
		          },
		          error:function(XMLHttpRequest, textStatus, errorThrown){}
		    });
		},function(){
	
		});
	}


</script>