<#import "/fw/macro/dataImportMacro.ftl" as import />
<#--<a href="javascript:" class="page-back-btn goBack" onclick="goBack()"><i class="fa fa-arrow-left"></i> 返回</a>-->
<script>
// showBreadBack(goBack,false,"选课导入");
//每个导入必须实现这个方法
function businessDataImport(){
	$('#busDataImport').addClass('disabled');
	// 处理逻辑, 并将参数组织成json格式　调用公共的导入方法
	var params = '{"divideId": "${divideId!}", "subjectType": "${subjectType!}"}';
	dataimport(params);
}

function goBack(){
	$("#aa").load("${request.contextPath}/newgkelective/${divide.id!}/floatingPlan/teachClassSet?planType=${subjectType!}");
}
</script>
<button class="btn btn-blue" onClick="goBack();">返回教学班设置</button>
<div style="height: 10px"></div>
<@import.import businessName="${businessName!}" businessUrl="${businessUrl!}" templateDownloadUrl="${templateDownloadUrl!}" exportErrorExcelUrl="${exportErrorExcelUrl}" objectName="${objectName!}" description="${description!}" businessKey="${businessKey!}" contextPath="${request.contextPath!}" resourceUrl="${resourceUrl!}" validateUrl="${validateUrl!}"  validRowStartNo="${validRowStartNo!}">
<div class="import-step clearfix">
	<span class="import-step-num">✔</span>
	<div class="import-content">
		<p>导入信息相关属性</p>
		<div class="filter clearfix">
			<div class="filter-item">
				<div class="filter-content">
				${divideName!}
				</div>
			</div>
		</div>
	</div>
</div>
</@import.import>