<#import "/fw/macro/dataImportMacro.ftl" as import />
<a href="javascript:" class="page-back-btn goBack" onclick="goBack()"><i class="fa fa-arrow-left"></i> 返回</a>
<script>
//每个导入必须实现这个方法
function businessDataImport(){
	$('#busDataImport').addClass('disabled');
	//处理逻辑　并将参数组织成json格式　调用公共的导入方法
	var acadyear=$("#acadyearStr").val();
	var semester=$("#semesterStr").val();
	var roomProperty=$("#roomProperty").val();
	var params='{"acadyear":"'+acadyear+'","semester":"'+semester+'","roomProperty":"'+roomProperty+'"}';
	dataimport(params);
}

function goBack(){
	var buildingId=$("#buildingId").val();
	var acadyear=$("#acadyearStr").val();
	var semester=$("#semesterStr").val();
	var roomProperty=$("#roomProperty").val();
	if(acadyear==undefined){
		acadyear="";
	}
	if(semester==undefined){
		semester="";
	}
	if(buildingId==undefined){
		buildingId="";
	}
	$("#itemShowDivId").load("${request.contextPath}/stuwork/dorm/bed/index/page?acadyearStr="+acadyear+"&semesterStr="+semester+"&buildingId="+buildingId+"&roomProperty="+roomProperty);
}
</script>

<@import.import businessName="${businessName}" businessUrl="${businessUrl}" templateDownloadUrl="${templateDownloadUrl}" objectName="${objectName}" description="${description}" businessKey="${businessKey}" contextPath="${request.contextPath}" resourceUrl="${resourceUrl}">
<div class="import-step clearfix">
	<span class="import-step-num">✔</span>
	<div class="import-content">
		<p>导入信息相关属性</p>
		<div class="filter clearfix">
			<div class="filter-item">
				<label for="" class="filter-name">学年：</label>
				<input type="hidden" name="acadyear"   id="acadyearStr" value="${acadyear!}">
				<div class="filter-content">
				${acadyear!}
				</div>
			</div>
			<div class="filter-item">
				<label for="" class="filter-name">学期：</label>
				<input type="hidden" name="semester"   id="semesterStr" value="${semester!}">
				<div class="filter-content">
				<#if semester?default("")=="1">第一学期<#else>第二学期</#if>
				</div>
			</div>
			<input type="hidden" name="buildingId" id="buildingId" value="${buildingId!}">
			<input type="hidden" name="roomProperty" id="roomProperty" value="${roomProperty!}">
			<#if !(buildingName?default("")=="")>
			<div class="filter-item">
				<label for="" class="filter-name">寝室楼：</label>
				<div class="filter-content">
				${buildingName!}
				</div>
			</div>
			</#if>
		</div>
	</div>
</div>
</@import.import>