<#import "/fw/macro/dataImportMacro.ftl" as import />
<a href="javascript:" class="page-back-btn goBack" onclick="goBack()"><i class="fa fa-arrow-left"></i> 返回</a>
<script>
//每个导入必须实现这个方法
function businessDataImport(){
	$('#busDataImport').addClass('disabled');
	//处理逻辑　并将参数组织成json格式　调用公共的导入方法
	var searchDate=$("#searchDate").val();
	var buildingId=$("#buildingId").val();
	var params='{"buildingId":"'+buildingId+'","searchDate":"'+searchDate+'"}';
	dataimport(params);
}
function goBack(){
	var buildingId=$("#buildingId").val();
	var searchDate=$("#searchDate").val();
	if(searchDate==undefined){
		searchDate="";
	}
	if(buildingId==undefined){
		buildingId="";
	}
	$("#itemShowDivId").load("${request.contextPath}/stuwork/dorm/check/list/page?searchDate="+searchDate+"&searchBuildId="+buildingId);
}
</script>
<@import.import businessName="${businessName}" businessUrl="${businessUrl}" templateDownloadUrl="${templateDownloadUrl}" objectName="${objectName}" description="${description}" businessKey="${businessKey}" contextPath="${request.contextPath}" resourceUrl="${resourceUrl}">
<div class="import-step clearfix">
	<span class="import-step-num">✔</span>
	<div class="import-content">
		<p>导入信息相关属性</p>
		<div class="filter clearfix">
			<div class="filter-item">
				<label for="" class="filter-name">日期：</label>
				<input type="hidden" name="searchDate" id="searchDate" value="${searchDate?string('yyyy-MM-dd')!}">
				<div class="filter-content">
				${searchDate?string('yyyy-MM-dd')!}
				</div>
			</div>
			<div class="filter-item">
				<label for="" class="filter-name">寝室楼：</label>
				<input type="hidden" name="buildingId" id="buildingId" value="${buildingId!}">
				<div class="filter-content">
				${buildingName!}
				</div>
			</div>
		</div>
	</div>
</div>
</@import.import>