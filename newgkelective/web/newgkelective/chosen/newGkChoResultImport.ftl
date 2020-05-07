<#import "/fw/macro/dataImportMacro.ftl" as import />
<#--<a href="javascript:" class="page-back-btn goBack" onclick="goBack()"><i class="fa fa-arrow-left"></i> 返回</a>-->
<script>
showBreadBack(goBack,false,"选课导入");
//每个导入必须实现这个方法
function businessDataImport(){
	$('#busDataImport').addClass('disabled');
	//处理逻辑　并将参数组织成json格式　调用公共的导入方法
	var chioceId = '${chioceId!}';
	var params='{"chioceId":"'+chioceId+'"}';
	dataimport(params);
}

function goBack(){
    var chioceId = '${chioceId!}';
    var url =  '${request.contextPath}/newgkelective/'+chioceId+'/chosen/tabHead/page?chosenType=${chosenType!}';		
	$("#showList").load(url);
}
</script>
<@import.import businessName="${businessName!}" businessUrl="${businessUrl!}" templateDownloadUrl="${templateDownloadUrl!}" exportErrorExcelUrl="${exportErrorExcelUrl}" objectName="${objectName!}" description="${description!}" businessKey="${businessKey!}" contextPath="${request.contextPath!}" resourceUrl="${resourceUrl!}" validateUrl="${validateUrl!}"  validRowStartNo="${validRowStartNo!}">
<div class="import-step clearfix">
	<span class="import-step-num">✔</span>
	<div class="import-content">
		<p>导入信息相关属性</p>
		<div class="filter clearfix">
			<div class="filter-item">
				<label for="" class="filter-name">选课名称：</label>
				<div class="filter-content">
				${chioceName!}
				</div>
			</div>
		</div>
	</div>
</div>
</@import.import>