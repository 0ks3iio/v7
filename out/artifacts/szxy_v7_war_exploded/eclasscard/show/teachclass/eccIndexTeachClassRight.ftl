<div class="box box-lightblue">
	<div class="box-header">
		<h4 class="box-title">校园风采</h4>
	</div>
	<div class="box-body no-padding">
		<div class="slider-wrap">
			<div class="slider-btn"><a href="" class="slider-prev">&lt;</a><a href="" class="slider-next">&gt;</a></div>
			<div class="slider">
			<#if albums?exists&&albums?size gt 0>
		  		<#list albums as item>
		  		<div class="slider-item">
					<div class="img-wrap img-wrap-big" style="background-image:url(${request.contextPath}/eccShow/eclasscard/showpicture?id=${item.id!});"></div>
					<h4 class="slider-title">${item.pictrueName!}</h4>
				</div>
				</#list>
			<#else>
				<div class="slider-item">
					<div class="img-wrap img-wrap-big" style="background-image: url(${request.contextPath}/static/eclasscard/show/images/img-default-big.png);"></div>
					<h4 class="slider-title">暂无图片</h4>
				</div>
			</#if>
			</div>
		</div>
	</div>
</div>
<script src="${request.contextPath}/static/eclasscard/show/js/myscript.js"></script>