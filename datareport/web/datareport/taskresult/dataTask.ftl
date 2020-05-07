<#import "/fw/macro/webUploaderMacro.ftl" as upload />
<div class="row">
	<div class="col-xs-12">
	   <div class="box box-default">
			<div class="box-body clearfix">
				<div class="filter">
					<div class="filter-item">
						<span class="filter-name">问卷名称：</span>
						<div class="filter-content">${dataReportInfo.title!}</div>
					</div>
					<div class="filter-item">
						<span class="filter-name">上级单位：</span>
						<div class="filter-content">${unitName!}</div>
					</div>
					<div class="filter-item">
						<span class="filter-name">问卷收集时间：</span>
						<div class="filter-content">${dataReportInfo.startTime!} 至 ${dataReportInfo.endTime!}</div>
					</div>
					<div class="filter-item filter-item-right">
						<#if dataReportInfo.isAttachment == 1>
							<a class="color-blue more pos-rel" href="#">共<span id="attSize">${attSize!(0)}</span>个附件<i class="fa fa-angle-down ml10"></i></a>
							<span id="uploadAccFile" title="上传多份同名文件，仅保存最后一份">
							</span>
							&nbsp;
						</#if>
						<label ><input type="checkbox" id="coverage" class="wp" <#if dataReportInfo.tableType == 3>checked</#if> <#if dataReportTask.state!=1 || dataReportInfo.state==4 || dataReportInfo.tableType == 3>disabled="disabled"</#if>><span class="lbl"> 覆盖</span></label>
						<@upload.importFileUpload businessKey="${fileDirId!}" contextPath="${request.contextPath}" resourceUrl="${resourceUrl}" handler="analyzeAndSaveTable()">
							<a type="button" class="btn btn-white js-addImportFiles <#if dataReportTask.state!=1 || dataReportInfo.state==4>disabled</#if>" href="javascript:;" >上传问卷</a>
							<!--这里的id就是存放附件的文件夹地址 必须维护-->
							<input type="hidden" id="${fileDirId!}-path" value="">
						</@upload.importFileUpload>
						&nbsp;
						<a type="button" class="btn btn-white <#if dataReportTask.state!=1 || dataReportInfo.state==4>disabled</#if>" href="javascript:;" onClick="loadExcel('${dataReportInfo.id!}')">下载模板</a>
					</div>
				</div>
				<div class="box-body" style="background: #f2f6f9;" id="dataResultDiv">
					
				</div>
				<div class="box-body" style="background: #f2f6f9;display:none;" id="errorDataDiv">
					
				</div>
			</div>
		</div>
	</div><!-- /.col -->
</div><!-- /.row -->
<div class="navbar-fixed-bottom opt-bottom">
	<#if dataReportInfo.tableType != 3>
	<span id="addNewResult" class="btn <#if dataReportTask.state!=1 || dataReportInfo.state==4>btn-white disabled<#else>btn-blue</#if>" href="#" <#if dataReportTask.state!=1 || dataReportInfo.state==4><#else>onClick="addNewResult()"</#if>>新增</span>
    </#if>
    <a id="saveTaskbutton" class="btn <#if dataReportTask.state!=1 || dataReportInfo.state==4>btn-white disabled<#else>btn-blue</#if>" href="#" onClick="saveTaskResults('${dataReportTask.id!}','${dataReportTask.reportId!}')">保存</a>
    <a id="subTaskbutton" class="btn <#if dataReportTask.state!=1 || dataReportInfo.state==4>btn-white disabled<#else>btn-blue</#if>" href="#" onClick="subTaskResult('${dataReportTask.id!}')">上报</a>
</div>
<script>
	$(function(){
	    showBreadBack(goBacktaskList,true,"提交问卷");
		showDataResult('${dataReportTask.id!}','${dataReportTask.reportId!}');
		
		<#-- 附件列表隐藏 -->
		$(document).click(function(e){
			if(!$(e.target).hasClass("more") && !$(e.target).parent().hasClass("more") && !$(e.target).parents().hasClass("more")){
				$(".modify-name-layer").hide();//点页面其他地方 隐藏表情区
			}
		});
		
		<#-- 附件文件列表 -->
		$(".more").each(function(){
			var deleteLayer = '<div class="modify-name-layer" style="width: 175px;">\
							<ul class="mt20">\
								<#if attachments?exists&&attachments?size gt 0>
								<#list attachments as att>
									<li class="clearfix"><span class="float-left ellipsis w100 mr30" title="${att.filename!}"><#if att.filename?exists&&att.filename?length gt 10>${att.filename?substring(0,10)}...<#else>${att.filename!}</#if></span><a class="float-left" href="#" onClick="deleteAtt(\'${att.id!}\',this)"><i class="wpfont icon-close"></i></a></li>\
								</#list>
								</#if>
							</ul>\
						</div>';
			$(this).append(deleteLayer);
		});
		
		<#-- 附件列表显现 -->
		$(".more").click(function(e){
			e.preventDefault();
			$(this).children(".modify-name-layer").show();
		});
		
		<#if dataReportInfo.isAttachment == 1>
			refUploadFileDiv();
		</#if>
	});
	
	<#-- 显示数据列表 -->
	function showDataResult(taskId,reportId) {
		var url = "${request.contextPath}/datareport/taskresult/showtaskresult?taskId="+taskId+"&reportId="+reportId;
		$("#dataResultDiv").load(url);
	}
	
	<#-- 下载模板 -->
	function loadExcel(infoId) {
		location.href = '${request.contextPath}/datareport/table/loadexceltable?objId='+infoId+"&objectType=REPORT_INFO_TEMPLATE";
	}
	
	<#-- 校验并保存数据 -->
	var isSubmit=false;
	function analyzeAndSaveTable() {
		if(isSubmit){
       		return;
    	}
    	var coverage = $("#coverage").is(':checked');
		isSubmit=true;
		layerTime();
		$.ajax({
			url:"${request.contextPath}/datareport/taskresult/saveexceldata",
			data:{"path":"${filePath!}","taskId":"${dataReportTask.id!}","reportId":"${dataReportTask.reportId!}","tableType":"${dataReportInfo.tableType!}","coverage":coverage},
			type:'post',
			dataType : 'json',
			success:function(data) {
				layerClose();
				isSubmit=false;
				if (data.success) {
					showDataResult('${dataReportTask.id!}','${dataReportTask.reportId!}');
				} else {
					if (data.msg=="emptyFile") {
						layer.msg("未上传文件或文件不存在！");
					} else if (data.msg=="emptyContent") {
						layer.msg("请勿上传空文件！");
					} else if (data.msg=="unlike") { 
						layer.msg("文件和模板不一致！");
					} else if (data.msg=="error") {
						layer.msg("校验失败！");
					} else {
						showErrorList(data.msg);
					}
				}
			},
 			error : function(XMLHttpRequest, textStatus, errorThrown) {  
 				layer.msg(XMLHttpRequest.status);
			}
		});
	}
	
	<#-- 显示错误Div -->
	function showErrorList(msg) {
		var jsonO = JSON.parse(msg);
		var errHtml = '<div class="filter"><div class="filter-item filter-item-right">\
						<a type="button" class="btn btn-white" href="javascript:;" onClick="backResultDiv()">返回</a></div></div>\
						<h3 class="text-center">上传文件错误</h3><div class="table-container no-margin"><div class="table-container-body">\
						 <table class="table table-bordered table-striped table-hover">\
							<thead><tr><th>行数</th><th>列名</th><th>错误信息</th></tr></thead><tbody>';
			for (var i=0;i<jsonO.length;i++) {
				errHtml += '<tr>';
				for (var j=0;j<3;j++) {
					errHtml += '<td>' + jsonO[i][j] + '</td>';
				}
				errHtml += '</tr>';
			}
			errHtml += '</tbody></table></div></div>';
		$("#errorDataDiv").html(errHtml);
		$("#errorDataDiv").attr("style","background: #f2f6f9;display:block;");
		$("#dataResultDiv").attr("style","background: #f2f6f9;display:none;");
		$(".navbar-fixed-bottom").attr("style","display:none;");
	}
	
	<#-- 显示在线填报 -->
	function backResultDiv() {
		$("#errorDataDiv").attr("style","background: #f2f6f9;display:none;");
		$("#dataResultDiv").attr("style","background: #f2f6f9;display:block;");
		$(".navbar-fixed-bottom").removeAttr("style");
	}
	
	<#-- 上报问卷 -->
	var haveData = false;
	function subTaskResult(taskId) {
		if (!haveData) {
			layer.msg("未上传问卷或未保存数据不能上报！");
			return;
		}
		var dellayer = layer.confirm('上报后将无法更新数据！确定要上报吗？', function(index){
		$.ajax({
			url:"${request.contextPath}/datareport/taskresult/submitdata",
			data:{"taskId":taskId,"state":3},
			type:'post',
			dataType : 'json',
			success:function(data) {
				layer.close(dellayer);
				if (data.success) {
					showTaskHead();
					goBacktaskList();
				} else {
					layerTipMsg(jsonO.success,"失败",jsonO.msg);
				}
			},
 			error : function(XMLHttpRequest, textStatus, errorThrown) {  
 				layer.msg(XMLHttpRequest.status);
			}
		});
		});
	}
	
	<#-- 返回列表 -->
	function goBacktaskList() {
		$("#dataTaskDiv").attr("style","display:none");
		$("#dataTaskDiv").html("");
		$("#dataTaskListDiv").attr("style","display:block");
	}
	
	<#-- 附件上传按钮刷新 -->
	function refUploadFileDiv() {
		var fileNum = 10 - parseInt($("#attSize").html());
		var canUse;
		<#if dataReportTask.state!=1 || dataReportInfo.state==4>
			canUse = false;
		<#else>
			canUse = true;
		</#if>
		var url = "${request.contextPath}/datareport/taskresult/refuploadfile?accFileDitId=${accFileDitId!}&fileNum="+fileNum+"&canUse="+canUse;
		$("#uploadAccFile").load(url);
	}
	
	<#-- 上传附件文件 -->
	function loadAttFiles() {
		layerTime();
		$.ajax({
		url:"${request.contextPath}/webuploader/dirfiles",
		data:{"path":"${accFilePath!}"},
		type:'post',
		dataType : 'json',
		success:function(data) {
			var array = data;
			if(array.length > 0){
				var jsonStr = JSON.stringify(array);
    			saveAttFiles(jsonStr);
    		} else {
    			layerClose();
    		}
		},
 		error : function(XMLHttpRequest, textStatus, errorThrown) {  
 			layer.msg(XMLHttpRequest.status);
 			layerClose();
		}
		});
	}
	
	<#-- 保存附件文件 -->
	function saveAttFiles(array) {
		var options = {
			url : "${request.contextPath}/datareport/table/attfiles/save",
			data:{taskId:"${dataReportTask.id!}",array:array},
			dataType : 'json',
			success : function(data){
				layerClose();
		 		if(!data.success){
		 			layerTipMsg(data.success,"附件保存失败",data.msg);
		 		} else {
		 			layerMsg("上传成功!");
		 			var msg = JSON.parse(data.msg);
		 			var index = 0;
		 			var fileName = "";
					$(".modify-name-layer ul").empty();
		 			for (var json in msg) {
		 				if (msg[json].length > 10) {
		 					fileName = msg[json].substring(0,10)+"...";
		 				} else {
		 					fileName = msg[json];
		 				}
		 				$(".modify-name-layer ul").append('<li class="clearfix"><span class="float-left ellipsis w100 mr30" title="'+ msg[json] +'">'+ fileName +'</span><a class="float-left" href="#" onClick="deleteAtt(\''+json+'\',this)"><i class="wpfont icon-close"></i></a></li>');
						index++;
					}
					$("#attSize").html(index);
					refUploadFileDiv();
		 		}
			},
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){alert(XMLHttpRequest.status);}//请求出错 
		};
		$.ajax(options);
	}
	
	<#-- 删除附件 -->
	function deleteAtt(attId,objthis) {
	<#if dataReportTask.state!=1 || dataReportInfo.state==4>
		return;
	<#else>
		if(isSubmit){
       		return;
    	}
    	isSubmit=true;
		var attSize = parseInt($("#attSize").html());
		$.ajax({
			url:"${request.contextPath}/datareport/table/attfiles/delete",
			data:{"attId":attId},
			dataType : 'json',
			success:function(data) {
				isSubmit=false;
				if (data.success) {
					$("#attSize").html(attSize-1+"");
					$(objthis).parent().remove();
					layer.msg("删除成功");
					refUploadFileDiv();
				} else {
					layerTipMsg(jsonO.success,"失败",jsonO.msg);
				}
			},
 			error : function(XMLHttpRequest, textStatus, errorThrown) {  
 				layer.msg(XMLHttpRequest.status);
 				isSubmit=false;
			}
		});
	</#if>
	}
</script>