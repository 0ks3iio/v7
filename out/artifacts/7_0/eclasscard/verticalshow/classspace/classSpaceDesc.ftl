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
<script>
	$(document).ready(function(){
		var container = $('.class-introduction');
		container.css({
			height: $(window).height() - container.offset().top - 220
		})
	})
</script>