<#import "/fw/macro/dataImportMacro.ftl" as import />
<a href="javascript:" class="page-back-btn gotoRoleUserIndex" onclick="gotoRoleUserIndex()"><i class="fa fa-arrow-left"></i> 返回</a>
<script>
//每个导入必须实现这个方法
function businessDataImport(){
	$('#busDataImport').addClass('disabled');
	//处理逻辑　并将参数组织成json格式　调用公共的导入方法
	var params="业务参数";
	//layerTipMsgWarn("提示",params);
	dataimport(params);
}

function gotoRoleUserIndex(){
	$(".model-div").load("${request.contextPath}/stuwork/checkweek/roleUserTeacher/page?roleType=02");
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
				<div class="filter-content">
				${acadyear!}
				</div>
			</div>
			<div class="filter-item">
				<label for="" class="filter-name">学期：</label>
				<div class="filter-content">
					<#if semester == 1>
					第一学期
					<#elseif semester == 2>
					第二学期
					</#if>
				</div>
			</div>
		</div>
	</div>
</div>
</@import.import>