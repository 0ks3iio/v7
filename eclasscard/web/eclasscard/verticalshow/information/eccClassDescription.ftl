<div class="class-summary-index">
	<h3>班级简介</h3>
	<p>
	<#if classDesc.content?exists>
		${classDesc.content!}<a href="javascript:void(0);" onclick="descShowDetail('${classDesc.id!}')">查看更多</a>
	<#else>
		<div class="no-data-content">
			<img src="${request.contextPath}/static/eclasscard/verticalshow/images/nothing.png" alt="">
			<p>暂无简介，请前往后台发布</p>
		</div>
	</p>
	</#if>
</div>
<script>
function descShowDetail(id){
	var url =  '${request.contextPath}/eccShow/eclasscard/showIndex/description/detail?id='+id+"&view="+_view;
	$("#showDetailLayer").load(url,function() {
		  showDetailLayer();
		});
}
</script>