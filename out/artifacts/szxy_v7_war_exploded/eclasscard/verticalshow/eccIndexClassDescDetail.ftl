<div class="article">
	<h2 class="article-title">班级简介</h2>
	<div class="article-content">
	<div id="introduction" class="tab-pane active">
	<div class="class-introduction">
		<#if classDesc.pictrueId?exists>
		<img src="${request.contextPath}/eccShow/eclasscard/showpicture?id=${classDesc.pictrueId!}" alt="">
		<#else>
		<img src="${request.contextPath}/static/eclasscard/verticalshow/images/nothing.png" alt="">
		</#if>
		<p>${classDesc.content!}</p>
	</div>
</div>
	</div>
</div>