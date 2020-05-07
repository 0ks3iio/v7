<#--
	taskBtnId	处理任务按钮id ===>必须
	taskUrl		处理任务的action ===>必须
	taskUrlDatas	参数后台对应的属性名，多个逗号隔开
	taskUrlDataIds	参数前台对应的id，多个逗号隔开，与taskUrlDatas个数对应
	isShowTask	是否显示任务列表，默认true ===>必须
	showTaskBtnId	显示任务列表按钮，isShowTask为false下无效
	taskBusinessType	业务类型 ===>必须
-->
<#macro commonWeb taskUrl="" taskUrlDatas="" taskUrlDataIds="" taskBtnId = "" isShowTask = true  showTaskBtnId = "" taskBusinessType="" >
	<#nested>
	<div id="${taskBtnId}panelWindowTip">
	</div>
	<#if isShowTask>
	<div class="row" id="${showTaskBtnId}ListId"></div>
	</#if>
	<script>
		var ${taskBtnId}isSubmit = false;
		var ${taskBtnId}jobId = '';
		var ${taskBtnId}jobIndex = 3;
		$("#${taskBtnId}").on('click',function(){
			if(${taskBtnId}jobId!=''){
				layer.tips('任务正在处理，请稍后......', $("#${taskBtnId}"), {
					tipsMore: true,
					tips: 2
				});
				return;
			}
			if(${taskBtnId}isSubmit){
				return;
			}
			${taskBtnId}isSubmit = true;
			var data={};
			<#if taskUrlDatas?default('') != ''>
			var taskUrlDatas = '${taskUrlDatas}'.split(",");
			var taskUrlDataIds = '${taskUrlDataIds}'.split(",");
			for(var i = 0 ;i < taskUrlDatas.length ;i++){
				data[taskUrlDatas[i]]=$("#"+taskUrlDataIds[i]).val();
			}
			</#if>
			$.ajax({
				url:'${request.contextPath+taskUrl}',
				data: data,
				type:'post',
				success:function(data) {
					var jsonO = JSON.parse(data);
			 		if(jsonO.success){
			 			${taskBtnId}jobId = jsonO.jobId;
			 			fnStartInterval();
			 			<#if isShowTask>
							if($("#${showTaskBtnId}ListId").html()!=''){
								$("#${showTaskBtnId}").click();
							}
						</#if>
			 		}else{
						swal({title: "操作失败!",
							text: jsonO.msg,type: "error",showConfirmButton: true,confirmButtonText: "确定"}
						);
					}
					${taskBtnId}isSubmit = false;
				},
		 		error : function(XMLHttpRequest, textStatus, errorThrown) {  
		 			
				}
			});
		});
		<#if isShowTask>
			var ${showTaskBtnId}taskOnShow = false;
			$("#${showTaskBtnId}").on('click',function(){
				if(${showTaskBtnId}taskOnShow){
					layer.tips('请勿频繁点击', $("#${showTaskBtnId}"), {
						tipsMore: true,
						tips: 2
					});
					return;
				}
				${showTaskBtnId}taskOnShow = true;
				var url =  '${request.contextPath}/basedata/taskJob/index/page?businessType=${taskBusinessType}';
				$("#${showTaskBtnId}ListId").load(url);
				setTimeout("${showTaskBtnId}taskOnShow=false",1000);
			});
		</#if>
		function fnStartInterval(){
			$("#${taskBtnId}panelWindowTip").html("<b>任务处理：</b><p style='color:blue'>==>请等待······</p>");
			window.setTimeout("fnRecycle()",1000);
		}
		
		function fnRecycle(){
			$.ajax({
				url:'${request.contextPath}/basedata/taskJob/fnRecycle/page?jobId='+${taskBtnId}jobId,
				type:'post',
				success:function(data) {
					var result=JSON.parse(data);
					var replyId = '';
					if(result!=null){
						replyId = result.value;
						drawReplyMsg(result);
						${taskBtnId}jobIndex+=2;
					}
					if(replyId == "status_end"){
						stopCycle();
					}else{
						if($("#${taskBtnId}panelWindowTip").length > 0){
							window.setTimeout("fnRecycle()",1000);
						}
					}
				},
		 		error : function(XMLHttpRequest, textStatus, errorThrown) {  
		 			
				}
			});
		}
		
		function stopCycle(){
			$.ajax({
				url:'${request.contextPath}/basedata/taskJob/stopCycle/page?jobId='+${taskBtnId}jobId,
				type:'post',
				success:function(data) {
					var oTd = $("#${taskBtnId}panelWindowTip");	
					oTd.html(oTd.html() + "<p style='color:blue'><==任务处理结束。</p>");
					${taskBtnId}jobId='';
					<#if isShowTask>
						if($("#${showTaskBtnId}ListId").html()!=''){
							$("#${showTaskBtnId}").click();
						}
					</#if>
				},
		 		error : function(XMLHttpRequest, textStatus, errorThrown) {  
		 			
				}
			});
		}
		function drawReplyMsg(result){
			var oTd = $("#${taskBtnId}panelWindowTip");
			if(result.value != "status_end"){
				var shen = '';
				for(var i = 0 ; i < (${taskBtnId}jobIndex%6) ; i ++){
					shen+='·';
				}
				oTd.html("<b>任务处理：</b><br>"+"<p style='color:blue'>==>请等待"+shen+"</p>");
			}else{
				oTd.html("<b>任务处理：</b><br>"+"<p style='color:blue'>==></p>");
			}
			if (result.actionMessages && result.actionMessages.length > 0) {
				for(var i = 0; i < result.actionMessages.length; i ++){
					oTd.html(oTd.html() + "<p style='color:blue'>" + result.actionMessages[i] + "</p>");
				}
		    }
		    if (result.actionErrors && result.actionErrors.length > 0) {
		    	for(var i = 0; i < result.actionErrors.length; i ++){
		    		oTd.html(oTd.html() + "<p style='color:blue'>" + result.actionErrors[i] + "</p>");
				}
		     }
		}
	</script>
</#macro>