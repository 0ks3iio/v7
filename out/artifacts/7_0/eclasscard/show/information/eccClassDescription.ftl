<div class="box-header">
	<h4 class="box-title">班级简介</h4>
</div>
<div class="box-body class-introduction-index">
	<#if classDesc.pictrueId?exists>
	<img class="img-classProfile" width="246" height="156" src="${request.contextPath}/eccShow/eclasscard/showpicture?id=${classDesc.pictrueId!}" alt="">
	<#else>
	<img class="img-classProfile" width="246" height="156" src="${request.contextPath}/static/eclasscard/show/images/img-default-square.png" alt="">
	</#if>
	<p>${classDesc.content!}</p>
</div>