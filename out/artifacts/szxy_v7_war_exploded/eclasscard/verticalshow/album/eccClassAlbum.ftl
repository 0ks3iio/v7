<div class="slider-wrap">
	<div class="slider-btn">
		<a href="javascript:void(0);" class="slider-prev"><span class="icon icon-angle-left"></span></a>
		<a href="javascript:void(0);" class="slider-next"><span class="icon icon-angle-right"></span></a>
	</div>
	<div class="slider-counter"></div>	
		<div class="slider">
		<#if albums?exists&&albums?size gt 0>
			<#list albums as item>
			<div class="slider-item">
				<div class="img-wrap img-wrap-16by9"  style="background-image:url(${request.contextPath}/eccShow/eclasscard/showpicture?id=${item.id!});">
				</div>
				<h4 class="slider-title">${item.pictrueName!}</h4>
			</div>
			</#list>
		<#else>
		<div class="slider-item">
			<div class="img-wrap img-wrap-16by9">
				<div class="no-data center">
					<div class="no-data-content">
						<img src="${request.contextPath}/static/eclasscard/verticalshow/images/nothing.png" alt="">
						<p>暂无图片，请前往后台发布</p>
					</div>
				</div>
			</div>
		</div>
		</#if>
		</div>
</div>
<script src="${request.contextPath}/static/eclasscard/verticalshow/js/myscript.js"></script>
