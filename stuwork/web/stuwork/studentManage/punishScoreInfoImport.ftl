<#import "/fw/macro/dataImportMacro.ftl" as import />
<a href="javascript:" class="page-back-btn goBack" onclick="goBack()"><i class="fa fa-arrow-left"></i> 返回</a>
<script>
//每个导入必须实现这个方法
function businessDataImport(){
	$('#busDataImport').addClass('disabled');
	//处理逻辑　并将参数组织成json格式　调用公共的导入方法
	var importType=$("#importType").val();
	var params='{"importType":"'+importType+'"}';
	dataimport(params);
}

function goBack(){
    $("#importDiv").load("${request.contextPath}/stuwork/studentManage/punishScoreInfo?acadyear=${acadyear!}&semester=${semester!}");
}
</script>
<@import.import businessName="${businessName}" businessUrl="${businessUrl}" templateDownloadUrl="${templateDownloadUrl}" objectName="${objectName}" description="${description}" businessKey="${businessKey}" contextPath="${request.contextPath}" resourceUrl="${resourceUrl}">
<div class="import-step clearfix">
	<div class="import-content">
		<div class="filter clearfix">
			<div class="filter-item">
				<span class="filter-name">是否覆盖更新：</span>
				<div class="filter-content">
					<select name="importType" id="importType" class="form-control" >
						<option value="0">新增导入</option>
						<option value="1">覆盖导入</option>
					</select>
				</div>
			</div>
		</div>
	</div>
</div>
</@import.import>