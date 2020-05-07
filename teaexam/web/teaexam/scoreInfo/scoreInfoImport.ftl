<#import "/fw/macro/dataImportMacro.ftl" as import />
<a href="javascript:" class="page-back-btn goBack" onclick="goBack()"><i class="fa fa-arrow-left"></i> 返回</a>
<script>
//每个导入必须实现这个方法
function businessDataImport(){
	$('#busDataImport').addClass('disabled');
	//处理逻辑　并将参数组织成json格式　调用公共的导入方法
	var params='{"examId":"${examId!}","subjectId":"${subjectId!}"}';
	dataimport(params);
}
function goBack(){
	$(".model-div").load("${request.contextPath}/teaexam/scoreInfo/index/page?year=${year!}&type=${type!}&examId=${examId!}&subjectId=${subjectId!}");
}
</script>
<@import.import businessName="${businessName}" businessUrl="${businessUrl}" templateDownloadUrl="${templateDownloadUrl}" objectName="${objectName}" description="${description}" businessKey="${businessKey}" contextPath="${request.contextPath}" resourceUrl="${resourceUrl}">
<div class="import-step clearfix">
	<span class="import-step-num">✔</span>
	<div class="import-content">
		<p>导入信息相关属性</p>
		<div class="filter clearfix">
			<div class="filter-item">
				<span class="filter-name">年份：${year!}</span>
				<div class="filter-content">
	
				</div>
			</div>
            <div class="filter-item">
				<span class="filter-name">考试名称：${examName!}</span>
				<div class="filter-content">
	
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">科目名称：${subjectName!}</span>
				<div class="filter-content">

				</div>
			</div>
		</div>
	</div>
</div>
</@import.import>