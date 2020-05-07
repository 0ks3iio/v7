<div class="slider-wrap">
	<div class="slider-btn"><a href="" class="slider-prev">&lt;</a><a href="" class="slider-next">&gt;</a></div>
	<div class="slider">
	<#if albums?exists&&albums?size gt 0>
  		<#list albums as item>
		<div class="slider-item">
			<div class="img-wrap <#if infoType=='10'||infoType=='30'>img-wrap-16by9<#else>img-wrap-big</#if>"  style="background-image:url(${request.contextPath}/eccShow/eclasscard/showpicture?id=${item.id!});">
			</div>
			<h4 class="slider-title">${item.pictrueName!}</h4>
		</div>
		</#list>
	<#else>
		<div class="slider-item">
			<div class="img-wrap <#if infoType=='10'||infoType=='30'>img-wrap-16by9<#else>img-wrap-big</#if>">
				<img src="${request.contextPath}/static/eclasscard/show/images/img-default.png" alt="">
			</div>
			<h4 class="slider-title">暂无图片</h4>
		</div>
	</#if>
	</div>
</div>
<script src="${request.contextPath}/static/eclasscard/show/js/myscript.js"></script>
