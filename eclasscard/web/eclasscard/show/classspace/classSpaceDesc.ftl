<div class="tab-pane active">
	<div class="class-introduction">
	<#if classDesc.pictrueId?exists>
		<img src="${request.contextPath}/eccShow/eclasscard/showpicture?id=${classDesc.pictrueId!}" alt="">
	<#else>
		<img src="${request.contextPath}/static/eclasscard/show/images/img-default.png" alt="">
	</#if>
		<p>${classDesc.content!}</p>
	</div>
</div>