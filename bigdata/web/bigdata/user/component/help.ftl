<div class="head-60">
	<span>帮助与支持</span>
	<span class="pos-right-c" onclick="go2HelpMore();">查看更多</span>
</div>
<div class="my-document-wrap">
	<#if helpList?exists && helpList?size gt 0>
	<#list helpList as help>
		<div class="data-get" onclick="go2HelpDetailPage('${help.id!}','${help.url!}')">
			<div class="mb-20">
				<img src="${request.contextPath}/bigdata/v3/static/images/user-home/icon-document.png"/>
			</div>
			<#if help.moduleName! ==help.name!>
				<div class="p-ellipsis">${help.name!}</div>
			<#else>
				<div class="p-ellipsis">${help.moduleName!}--${help.name!}</div>
			</#if>
			<div class="p-ellipsis color-999">${help.description!}</div>
		</div>
	</#list>
	<#else>
		<div class="no-data-common">
			<div class="text-center">
				<img src="${request.contextPath}/bigdata/v3/static/images/public/no-data-common-50.png"/>
				<p class="color-999">暂无可用的帮助文档</p>
			</div>
		</div>
	</#if>		
</div>
<script>
	function go2HelpDetailPage(id) {
	 	window.open("${request.contextPath}/bigdata/help/preview2np?id="+id,id)
    }

	function go2HelpMore() {
		router.go({
	         path: 'bigdata/help/tree?showAll=no',
	        name:'帮助与支持',
	        level: 1
	    }, function () {
	   		$('.page-content').load('${request.contextPath}/bigdata/help/tree?showAll=no'); 
	    });
    }
</script>