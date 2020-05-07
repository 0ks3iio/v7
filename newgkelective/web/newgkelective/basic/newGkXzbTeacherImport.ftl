<#import "/fw/macro/dataImportMacro.ftl" as import />
<script>
//每个导入必须实现这个方法
function businessDataImport(){
	$('#busDataImport').addClass('disabled');
	//处理逻辑　并将参数组织成json格式　调用公共的导入方法
	var arrayItemId = '${arrayItemId!}';
	var params='{"arrayItemId":"'+arrayItemId+'"}';
	dataimport(params);
}

function toCourseIndex(){
<#--
<#if openType?default('')=='07'>
	var url =  '${request.contextPath}/newgkelective/xzb/courseFeatures/index?arrayItemId=${arrayItemId!}';	
<#else>
	var url =  '${request.contextPath}/newgkelective/${arrayItemId!}/courseFeatures/index';	
</#if>
-->
    var url = '${request.contextPath}/newgkelective/${divide.id!}/subjectTeacherArrange/add?gradeId=${divide.gradeId!}&itemId=${arrayItemId!}&arrayId=${arrayId!}';
	$("#gradeTableList").load(url);

}
</script>
<div class="filter">
	<div class="filter-item">
		<a href="javascript:void(0)" class="btn btn-blue" onclick="toCourseIndex()">返回</a>
	</div>
</div>
<@import.import businessName="${businessName!}" businessUrl="${businessUrl!}" templateDownloadUrl="${templateDownloadUrl!}" exportErrorExcelUrl="${exportErrorExcelUrl}" objectName="${objectName!}" description="${description!}" businessKey="${businessKey!}" contextPath="${request.contextPath!}" resourceUrl="${resourceUrl!}" validateUrl="${validateUrl!}"  validRowStartNo="${validRowStartNo!}">
<div class="import-step clearfix">
	<span class="import-step-num">✔</span>
	<div class="import-content">
		<p>导入信息相关属性</p>
		<div class="filter clearfix">
			<div class="filter-item">
				<label for="" class="filter-name">排课特征名称：</label>
				<div class="filter-content">
				${arrayItemName!}
				</div>
			</div>
		</div>
		<div class="filter clearfix">
			<div class="filter-item">
				<label for="" class="filter-name">分班方案：</label>
				<div class="filter-content">
				${divideName!}
				</div>
			</div>
		</div>
	</div>
</div>
</@import.import>