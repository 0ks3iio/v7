<div class="head-60">
	<span>我分享的资源</span>
	<span class="pos-right-c" onclick="go2SharedPage();">查看更多</span>
</div>
<div class="row no-padding-top">
	<#if shareList?exists && shareList?size gt 0>
	<#list shareList as share>
		<#if share_index lt 6>
		<div class="col-md-4" onclick="showReportFromDesktop('${share.businessId!}','${share.businessType!}','${share.businessName!}');">
			<div class="data-get data-get-double">
				<p>我分享了<span class="color-blue-old">《${share.businessName!}》</span></p>
				<p class="color-999">分享给：${share.userNames!}</p>
				<p class="color-999">类型：<#if share.businessType! =="1">数据图表<#elseif share.businessType! =="3">数据报表<#elseif share.businessType! =="5">多维报表<#elseif share.businessType! =="6">数据看板<#elseif share.businessType! =="7">数据报告<#else>未知</#if></p>
				<img src="${request.contextPath}/bigdata/v3/static/images/user-home/share-icon.png"/>
			</div>
		</div>
		</#if>
	</#list>
	<#else>
		<div class="no-data-common">
			<div class="text-center">
				<img src="${request.contextPath}/bigdata/v3/static/images/public/no-data-common-50.png"/>
				<p class="color-999">暂无分享的资源</p>
			</div>
		</div>
	</#if>
</div>
<script>
	function go2SharedPage(){
		router.go({
	         path: '/bigdata/share/index?type=1',
	        name:'分享栏',
	        level: 1
	    }, function () {
	    	$('#10000000000000000000000000000006').addClass('active').siblings('li').removeClass('active');
		 	$('.page-content').load('${request.contextPath}/bigdata/share/index?type=1');
	    });
	}
</script>