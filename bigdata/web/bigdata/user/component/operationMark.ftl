<p class="head-60"><span>最近操作的模块</span></p>
<div class="row no-padding-top">
	<#if operationMarkList?exists && operationMarkList?size gt 0>
		<#list operationMarkList as operationMark>
			<div class="col-md-4" onclick="loadModuleFromDesktop('${operationMark.modelId!}','${operationMark.name!}','${operationMark.url!}');">
				<div class="data-get text-center">
					${operationMark.name!}
				</div>
			</div>
			</#list>
	<#else>
	<div class="no-data-common">
		<div class="text-center">
			<img src="${request.contextPath}/bigdata/v3/static/images/public/no-data-common-50.png"/>
			<p class="color-999">暂无操作的模块</p>
		</div>
	</div>
	</#if>
</div>
<script>
	function loadModuleFromDesktop(id,name,url){
		router.go({
	        path: url,
	        name:name,
	        level: 1
	    }, function () {
		 	$('#'+id).addClass('active').siblings('li').removeClass('active');
			$('.page-content').load('${request.contextPath}'+url);
	    });
	}
</script>