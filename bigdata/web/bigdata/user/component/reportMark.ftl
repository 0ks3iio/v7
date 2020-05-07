<p class="head-60"><span>最近浏览的资源</span></p>
<div class="row no-padding-top">
	<#if reportMarkList?exists && reportMarkList?size gt 0>
	<#list reportMarkList as reportMark>
	<div class="col-md-4" onclick="showReportFromDesktop('${reportMark.businessId!}','${reportMark.businessType!}','${reportMark.businessName!}');">
		<div class="data-get text-center">
			${reportMark.businessName!}
		</div>
	</div>
	</#list>
	<#else>
	<div class="no-data-common">
		<div class="text-center">
			<img src="${request.contextPath}/bigdata/v3/static/images/public/no-data-common-50.png"/>
			<p class="color-999">暂无浏览记录</p>
		</div>
	</div>
	</#if>
</div>