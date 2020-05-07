<div class="table-show">
<table class="tables tables-border no-margin">
	<thead>
		<tr>
			<th>任务名称</th>
			<th>任务类型</th>
			<th>备注</th>
		</tr>
	</thead>
	<tbody>
	<#if systemJobList?exists&&systemJobList?size gt 0>
		<#list systemJobList as sysJob>
		<tr>
			<td>${sysJob.name!}</td>
			<td>系统内置定时任务</td>
			<td>${sysJob.remark!}</td>
		</tr>
		 </#list>
 <#else>
		<tr >
			<td  colspan="6" align="center">
			暂无shell调度任务数据
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

	function addKettleJob(){
		var url =  '${request.contextPath}/bigdata/etl/kettle/edit';
		$("#contentDiv").load(url);
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
	            }
	        });
	        $("#logDiv").parent().css('overflow','auto');
	    })
	}
	
	function editKettleJob(id){
		var url =  '${request.contextPath}/bigdata/etl/kettle/edit?id='+id;
		$("#contentDiv").load(url);
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
				 			layer.msg(data.msg, {offset: 't',time: 2000});
				 		}else{
				 			layer.msg(data.msg, {offset: 't',time: 2000});
		    			}
		    		  	showList('1');
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
					 			layer.msg(data.msg, {offset: 't',time: 2000});
					 		}else{
					 			layer.msg(data.msg, {offset: 't',time: 2000});
			    			}
			    			showList('1');
			    			parent.layer.close(index);
		                }
	                })
	            },
		    	content: $('.layer-param')
		    })
	    }
	}
    
	function deleteKettleJob(id,name){
		var options = {btn: ['确定','取消'],title:'确认信息', icon: 1,closeBtn:0};
		showConfirm("您确定要删除["+name+"]吗？",options,function(){
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
				 		if(!data.success){
				 			layer.closeAll();
				 			layerTipMsg(data.success,"",data.msg);
				 		}else{
				 		    layer.msg(data.msg, {offset: 't',time: 2000});
				 		   	showList('1');
		    			}
		          },
		          error:function(XMLHttpRequest, textStatus, errorThrown){}
		    });
		},function(){
	
		});
	}

	$(document).ready(function(){
         $('.js-scroll-height').each(function () {
            $(this).css({
                height: $(window).height() - $(this).offset().top - 20,
                overflow: 'auto'
            })
        });
	});
</script>