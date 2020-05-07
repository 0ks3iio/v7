<div class="head-60">
	<span>最新公告</span>
	<!--<span class="pos-right-c">查看更多</span>-->
</div>
<div class="my-news-wrap">
	<#if noticeList?exists && noticeList?size gt 0>
	<#list noticeList as notice>
	<p onclick="previewNotice('${notice.id!}')"><b>【${notice.noticeTypeName!}】</b>
		${notice.title!}
	</p>
	</#list>
	<#else>
		<div class="no-data-common">
			<div class="text-center">
				<img src="${request.contextPath}/bigdata/v3/static/images/public/no-data-common-50.png"/>
				<p class="color-999">暂无公告信息</p>
			</div>
		</div>
	</#if>
</div>
<script>
	function previewNotice(id) {
		var url = '${request.contextPath}/bigdata/notice/preview?id='+id;
	 	window.open(url,id)
    }
</script>