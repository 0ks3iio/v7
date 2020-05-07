<div class="head-60">
	<span>我收藏的资源</span>
	<span class="pos-right-c" onclick="go2FavoritePage();">查看更多</span>
</div>
<div class="row no-padding-top">
	<#if favoriteList?exists && favoriteList?size gt 0>
	<#list favoriteList as favorite>
		<div class="col-md-4" onclick="showReportFromDesktop('${favorite.businessId!}','${favorite.businessType!}','${favorite.businessName!}');">
			<div class="data-get text-center">
				${favorite.businessName!}
			</div>
		</div>
	</#list>
	<#else>
		<div class="no-data-common">
			<div class="text-center">
				<img src="${request.contextPath}/bigdata/v3/static/images/public/no-data-common-50.png"/>
				<p class="color-999">暂无收藏的资源</p>
			</div>
		</div>
	</#if>		
</div>
<script>
	function go2FavoritePage(){
		router.go({
	        path: '/bigdata/favorite/index',
	        name:'收藏夹',
	        level: 1
	    }, function () {
	    	$('#10000000000000000000000000000005').addClass('active').siblings('li').removeClass('active');
		 	$('.page-content').load('${request.contextPath}/bigdata/favorite/index');
	    });
	}
</script>