<div class="head-60">
	<span>分享给我的资源</span>
	<span class="pos-right-c" onclick="go2BeSharedPage();">查看更多</span>
</div>
<div class="row no-padding-top no-padding-bottom share-me">
	<div class="col-md-12">
		<#if shareList?exists && shareList?size gt 0>
		<#list shareList as share>
		<div class="data-get" onclick="showReportFromDesktop('${share.businessId!}','${share.businessType!}','${share.businessName!}');">
            <div class="surname <#if share_index % 3 ==0>surname-green<#elseif share_index % 3 ==1>surname-blue<#else>surname-grey</#if>"><#if share.userNames?default('')?length gt 0>${share.userNames?substring(0,1)}</#if></div>
			<p class="p-ellipsis">
                <span class="p-ellipsis"><b>${share.userNames!}</b> 向我分享了 </span>
                <span class="color-999">${share.creationTime?string('yyyy年MM月dd日')}</span>
            </p>
            <p class="p-ellipsis color-blue-old">《${share.businessName!}》</p>
		</div>
		</#list>
		<#else>
		<div class="no-data-common">
			<div class="text-center">
				<img src="${request.contextPath}/bigdata/v3/static/images/public/no-data-common-50.png"/>
				<p class="color-999">暂无被分享的资源</p>
			</div>
		</div>
		</#if>
	</div>	
</div>
<script>
function go2BeSharedPage(){
		router.go({
	        path: '/bigdata/share/index?type=2',
	        name:'分享栏',
	        level: 1
	    }, function () {
	   		$('#10000000000000000000000000000006').addClass('active').siblings('li').removeClass('active');
		 	$('.page-content').load('${request.contextPath}/bigdata/share/index?type=2');
	    });
	}
</script>
