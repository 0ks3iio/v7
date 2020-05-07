<#import "/fw/macro/dataImportMacro.ftl" as import />
<a href="javascript:" class="page-back-btn goBack" onclick="goBack()"><i class="fa fa-arrow-left"></i> 返回</a>
<script>
	//每个导入必须实现这个方法
	function businessDataImport(){
		$('#busDataImport').addClass('disabled');
		//处理逻辑　并将参数组织成json格式　调用公共的导入方法
		var gradeId = '${gradeId!}';
		var examId = '${examId!}';
		var params='{"gradeId":"'+gradeId+'","examId":"'+examId+'"}';
		dataimport(params);
	}

	function goBack(){
		var gradeId = '${gradeId!}';
		var examId = '${examId!}';
		var subjectId = '${subjectId!}';
		var url =  '${request.contextPath}/comprehensive/subjects/score/toScoreEdit?examId='+examId+'&gradeId='+gradeId+'&subjectId='+subjectId;
		$("#exammanageDiv").load(url);
	}
</script>
<@import.import businessName="${businessName!}" businessUrl="${businessUrl!}" templateDownloadUrl="${templateDownloadUrl!}" objectName="${objectName!}" description="${description!}" businessKey="${businessKey!}" contextPath="${request.contextPath!}" resourceUrl="${resourceUrl!}">
	<div class="import-step clearfix">
		<span class="import-step-num">✔</span>
		<div class="import-content">
			<p>导入信息相关属性</p>
			<div class="filter clearfix">
				<div class="filter-item">
					<label for="" class="filter-name">考试名称：</label>
					<div class="filter-content">
						${examName!}
					</div>
				</div>
				<div class="filter-item">
					<label for="" class="filter-name">年级：</label>
					<div class="filter-content">
						${gradeName!}
					</div>
				</div>
			</div>
		</div>
	</div>
</@import.import>