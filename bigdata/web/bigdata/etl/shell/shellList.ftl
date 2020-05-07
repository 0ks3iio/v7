<div class="table-show">
    <div class="filter-made mb-10">
		<div class="filter-item filter-item-right clearfix">
	        <button class="btn btn-lightblue" onclick="addShellJob();">新增</button>
		</div>
	</div>
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
		<#if shellList?exists&&shellList?size gt 0>
			<#list shellList as shell>
			<tr>
				<td>${shell.name!}</td>
				<td><#if shell.isSchedule?default(0) ==1>是<#else>否</#if></td>
				<td>${shell.lastCommitTime!}</td>
				<td>
						<#if shell.lastCommitState?default(0) ==1>
							<span class="success-left">成功</span>
						<#elseif shell.lastCommitState?default(0) ==2>
							<span class="fail-left">失败</span>
						<#else>
							<span>&nbsp;<img src="${request.contextPath}/static/images/big-data/not-begin-icon.png" />&nbsp;&nbsp;未开始</span>	
						</#if>
				</td>
				<td>
					<#if shell.lastCommitState?default(3) ==2>
						<a class="look-over" href="javascript:void(0);"  onclick="viewErrorLog('${shell.lastCommitLogId!}')">
						错误日志</a><span class="tables-line">|</span>
					</#if>
                    <a href="javascript:;" class="look-over" onclick="execShellJob('${shell.id!}','${shell.hasParam?default(0)}')">执行</a><span class="tables-line">|</span>
					<a class="look-over" href="javascript:void(0);" onclick="editShellJob('${shell.id!}')">编辑</a><span class="tables-line">|</span>
					<a class="remove" href="javascript:void(0);" onclick="deleteShellJob('${shell.id!}','${shell.name!}')">删除</a><span class="tables-line">|</span>
					<a id="log-${shell.id!}-button"  href="javascript:;" class="look-over js-log-show" onclick="loadShellLogList('${shell.id!}');" >日志</a>
				</td>
			</tr>
			 </#list>
	 <#else>
			<tr >
				<td  colspan="5" align="center">
				暂无Sqoop调度任务数据
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
	function addShellJob(){
		router.go({
	        path: '/bigdata/etl/shell/edit',
	        name:'新增',
	        level: 3
	    }, function () {
		    var url =  '${request.contextPath}/bigdata/etl/shell/edit';
			$("#contentDiv").load(url);
	    });
	}
	
	function editShellJob(id){
		router.go({
	        path: '/bigdata/etl/shell/edit?id='+id,
	        name: '编辑',
	        level: 3
	    }, function () {
		    var url =  '${request.contextPath}/bigdata/etl/shell/edit?id='+id;
			$("#contentDiv").load(url);
	    });
	}
	
	function loadShellLogList(id){
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
	
	function execShellJob(id,hasParam){
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
				 			showLayerTips('success',data.msg,'t');
				 		}else{
				 			showLayerTips('success','后台正在执行，请稍后刷新页面查看结果','t');
		    			}
		    			var url =  "${request.contextPath}/bigdata/etl/list?etlType=3";
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
								showLayerTips('success',data.msg,'t');
					 		}else{
					 			showLayerTips('success',data.msg,'t');
			    			}
			    			parent.layer.close(index);
			    			var url =  "${request.contextPath}/bigdata/etl/list?etlType=3";
							$("#contentDiv").load(url);	
		                }
	                })
            	},
	    		content: $('.layer-param')
	    	})
   		}
	}
    
	function deleteShellJob(id,name){
			showConfirmTips('prompt',"提示","您确定要删除"+name+"吗？",function(){
			 $.ajax({
		            url:"${request.contextPath}/bigdata/etl/shell/delete",
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
	                     	var url =  "${request.contextPath}/bigdata/etl/list?etlType=3";
							$("#contentDiv").load(url);	
	                    }
		          },
		          error:function(XMLHttpRequest, textStatus, errorThrown){}
		    });
		},function(){
	
		});
	}
</script>