<#--
文件上传
businessKey 业务id(唯一)
contextPath 
sourceUrl
businessName 业务名称
businessUrl 业务ＵＲＬ
templateDownloadUrl　模板下载地址
exportErrorExcelUrl  错误信息下载地址，不传值将调用原有逻辑
objectName　对象
description　　描述
-->
<#macro import  businessName="" businessUrl="" templateDownloadUrl="" exportErrorExcelUrl="" objectName="" description="" businessKey="" contextPath="" resourceUrl="" validateUrl="" validRowStartNo="">
<#import "/fw/macro/webUploaderMacro.ftl" as upload />
<#if exportErrorExcelUrl?exists && exportErrorExcelUrl?length gt 0>
<!-- 现有错误展现逻辑，将错误信息以Excel形式导出，支持直接修改 -->
<script>
	function dataimport(params){
		//先判断是否有上传文件
		if($('.import-filelist li').length == 0){
			layerTipMsgWarn("提示","请先上传导入文件!");
			$('#busDataImport').removeClass('disabled');
			return;
		}
		$('.import-result').html("");
		$("#errorDataExportDiv").hide();
	    var filePath=$("#${businessKey!}-path").val();
	    var url='${businessUrl!}';
	 	if('${contextPath}'!=''){
	 		url='${contextPath}/${businessUrl!}';
	 	}
	 	var ii = layer.load();
		$.ajax({
			url:url,
			data: {'filePath':filePath,'params':params},
			type:'post',
			success:function(data) {
				layer.close(ii);
				$('.import-result').html("");
				var result = JSON.parse(data);
//				console.log(result.errorExcelPath);
				if (result.errorExcelPath.length > 0) {
                    $("#errorDataExportDiv").show();
                    $("#${businessKey!}-error-path").val(result.errorExcelPath);
                }
				var summary='<ul><li>总数据：<strong>'+result.totalCount+'</strong>条</li>';
				summary+='<li>导入成功：<strong class="color-green">'+result.successCount+'</strong>条</li>';
				summary+='<li>导入失败：<strong class="color-red">'+result.errorCount+'</strong>条</li></ul>';
				$('.import-result').append(summary);
				// console.log(result.errorData.length);
				if(result.errorData.length>0 && result.errorExcelPath.length == 0){
					var errorData='<div class="import-failed-data">';
					errorData+='<table class="table table-striped">'
					errorData+='<thead>';
					errorData+='<tr>';
					errorData+='<th>序号</th>';
					errorData+='<th>对象</th>';
					errorData+='<th>错误数据</th>';
					errorData+='<th>错误原因</th>';
					errorData+='</tr>';
					errorData+='</thead>';
					errorData+='<tbody>';
					for(i in result.errorData){
						errorData+='<tr>';
						errorData+='<td>'+result.errorData[i][0]+'</td>';
						errorData+='<td>'+result.errorData[i][1]+'</td>';
						errorData+='<td>'+result.errorData[i][2]+'</td>';
						errorData+='<td><span class="color-red">'+result.errorData[i][3]+'</span></td>';
						errorData+='</tr>';
					}
					errorData+='</tbody>';
					errorData+='</table>';
					errorData+='</div>';
					$('.import-result').append(errorData);
				}
				$('#busDataImport').removeClass('disabled');
			},
	 		error : function(XMLHttpRequest, textStatus, errorThrown) {
				layer.close(ii);
	 			$('#busDataImport').removeClass('disabled');
			}
		});
	}

	function downloadTemplate(){
		var templateform=document.getElementById('templateform');
			if(templateform){
				templateform.action="${contextPath}${templateDownloadUrl!}";
				templateform.target="hiddenIframe";
				templateform.submit();
			}
	}

    function exportErrorExcel() {
        $("#exportErrorForm").submit();
    }

	function validateFile(){

	}
</script>
<div class="row">
	<div class="col-xs-12">
	  <div class="box box-default">
	      <div class="box-body">
			<!-- PAGE CONTENT BEGINS -->
				<div class="import-wrap">
					<div class="import-header">
						<h4 class="import-title">${businessName!}批量导入步骤：</h4>
					</div>
					<div class="import-body">
						<form id="templateform" name="templateform" method="post">
						<input type="hidden" id="templateParams" name="templateParams" value="">
						<div class="import-step clearfix">
							<iframe id="hiddenIframe" name="hiddenIframe" style="display:none"></iframe>
							<span class="import-step-num">✔</span>
							<div class="import-content">
								<p>下载模板：<a href="javascript:void(0);" onclick="downloadTemplate()">${businessName!}导入模板</a></p>
							</div>
						</div>
						</form>
						<#nested>
						<div class="import-step clearfix">
							<span class="import-step-num">✔</span>
							<div class="import-content">
								<p>根据模板填写信息并选择相应文件</p>
								<ul class="import-filelist"></ul>
								<p>
									<@upload.importFileUpload businessKey="${businessKey!}" contextPath="${contextPath}" resourceUrl="${resourceUrl}" handler="validateFile" validateUrl="${validateUrl}" validRowStartNo="${validRowStartNo}">
									<div class="filter">
										<div class="filter-item">
											<button class="btn btn-blue js-addImportFiles">上传模板</button>
											<span class="color-orange">上传相应模板文件</span>
										</div>
									</div>
									<!--这里的id就是存放附件的文件夹地址 必须维护-->
									<input type="hidden" id="${businessKey!}-path" value="">
                                    <form id="exportErrorForm" style="display: none" action="${request.contextPath}${exportErrorExcelUrl!}" method="post" target="hiddenIframe">
                                    <input id="${businessKey!}-error-path" name="errorPath" value="">
                                    </form>
									</@upload.importFileUpload>
								</p>
							</div>
						</div>
					</div>
					<div class="import-footer">
						<button class="btn btn-blue" id="busDataImport" onclick="businessDataImport()">开始导入</button>
						<span class="color-orange">您需要先上传相应文件，才能开始导入</span>
					</div>
                    <div id="errorDataExportDiv" style="display: none;padding: 10px 2em">
                        <button class="btn btn-blue" id="errorDataExport" onclick="exportErrorExcel()">错误信息</button>
                        <span class="color-orange">修改后可直接导入</span>
                    </div>
				</div>
				<#if description! !="">
				<!-- 导入说明开始 -->
				<div class="box box-graybg">
					${description!}
				</div><!-- 导入说明结束 -->
				</#if>
				<div class="import-result">
				</div>
				<!-- 导入数据表格结束 -->
			<!-- PAGE CONTENT ENDS -->
           </div>
       </div>
	</div><!-- /.col -->
</div><!-- /.row -->

<#else>
<!-- 原有错误展现逻辑，在页面上展示 -->
<script>
    function dataimport(params){
        //先判断是否有上传文件
        if($('.import-filelist li').length == 0){
            layerTipMsgWarn("提示","请先上传导入文件!");
            $('#busDataImport').removeClass('disabled');
            return;
        }
        $('.import-result').html("");
        var filePath=$("#${businessKey!}-path").val();
        var url='${businessUrl!}';
        if('${contextPath}'!=''){
            url='${contextPath}/${businessUrl!}';
        }
        var ii = layer.load(); 
        $.ajax({
            url:url,
            data: {'filePath':filePath,'params':params},
            type:'post',
            success:function(data) {
            	layer.close(ii);
                $('.import-result').html("");
                var result = JSON.parse(data);
                var summary='<ul><li>总数据：<strong>'+result.totalCount+'</strong>条</li>';
                summary+='<li>导入成功：<strong class="color-green">'+result.successCount+'</strong>条</li>';
                summary+='<li>导入失败：<strong class="color-red">'+result.errorCount+'</strong>条</li></ul>';
                $('.import-result').append(summary);
                if(parseInt(result.errorCount)>0){
                    var errorData='<div class="import-failed-data">';
                    errorData+='<table class="table table-striped">'
                    errorData+='<thead>';
                    errorData+='<tr>';
                    errorData+='<th>序号</th>';
                    errorData+='<th>对象</th>';
                    errorData+='<th>错误数据</th>';
                    errorData+='<th>错误原因</th>';
                    errorData+='</tr>';
                    errorData+='</thead>';
                    errorData+='<tbody>';
                    for(i in result.errorData){
                        errorData+='<tr>';
                        errorData+='<td>'+result.errorData[i][0]+'</td>';
                        errorData+='<td>'+result.errorData[i][1]+'</td>';
                        errorData+='<td>'+result.errorData[i][2]+'</td>';
                        errorData+='<td><span class="color-red">'+result.errorData[i][3]+'</span></td>';
                        errorData+='</tr>';
                    }
                    errorData+='</tbody>';
                    errorData+='</table>';
                    errorData+='</div>';
                    $('.import-result').append(errorData);
                }
                $('#busDataImport').removeClass('disabled');
            },
            error : function(XMLHttpRequest, textStatus, errorThrown) {
				layer.close(ii);
                $('#busDataImport').removeClass('disabled');
            }
        });
    }

    function downloadTemplate(){
        var templateform=document.getElementById('templateform');
        if(templateform){
            templateform.action="${contextPath}${templateDownloadUrl!}";
            templateform.target="hiddenIframe";
            templateform.submit();
        }
    }

    function validateFile(){

    }
</script>
<div class="row">
	<div class="col-xs-12">
        <div class="box box-default">
            <div class="box-body">
                <!-- PAGE CONTENT BEGINS -->
                <div class="import-wrap">
                    <div id="a3" class="tab-pane">
                        <div class="import-wrap">
                            <div class="import-header">
                                <h4 class="import-title"><b>${businessName!}批量导入步骤：</b></h4>
                            </div>
                            <div class="import-body">
                                <form id="templateform" name="templateform" method="post">
                                    <input type="hidden" id="templateParams" name="templateParams" value="">
                                    <div class="import-step clearfix">
                                        <iframe id="hiddenIframe" name="hiddenIframe" style="display:none"></iframe>
                                        <span class="import-step-num">✔</span>
                                        <div class="import-content">
                                            <p>下载模板：<a href="javascript:void(0);" onclick="downloadTemplate()">${businessName!}导入模板</a></p>
                                        </div>
                                    </div>
                                </form>
						        <#nested>
                                <div class="import-step clearfix">
                                    <span class="import-step-num">2、</span>
                                    <div class="import-content">
                                        <p>根据模板填写信息并选择相应文件上传模板</p>
                                        <p>
										<@upload.importFileUpload businessKey="${businessKey!}" contextPath="${contextPath}" resourceUrl="${resourceUrl}" handler="validateFile" validateUrl="${validateUrl}" validRowStartNo="${validRowStartNo}">
                                            <button class="btn btn-sm btn-white js-addImportFiles">上传模板</button>
                                            <span class="color-999"><i class="fa fa-exclamation-circle color-yellow"></i> 上传相应模板文件</span>
                                            <input type="hidden" id="${businessKey!}-path" value="">
                                        </@upload.importFileUpload>
                                        </p>
                                        <ul class="import-filelist"></ul>
                                    </div>
                                </div>
                            </div>
                            <div class="import-footer">
                                <button class="btn btn-blue" onclick="businessDataImport()">开始导入</button>
                                <span class="color-999"><i class="fa fa-exclamation-circle color-yellow"></i> 您需要先上传相应文件，才能开始导入</span>
                            </div>
                        </div>
                        <!-- 导入说明开始 -->
						<#if description! !="">
							<div class="box box-graybg color-999">
                                <div>${description!}</div>
                            </div>
                        </#if>
                    </div>
                    <div class="import-result">
                    </div>
                    <!-- 导入数据表格结束 -->
                    <!-- PAGE CONTENT ENDS -->
                </div>
            </div>
        </div><!-- /.col -->
    </div><!-- /.row -->
</#if>
</#macro>