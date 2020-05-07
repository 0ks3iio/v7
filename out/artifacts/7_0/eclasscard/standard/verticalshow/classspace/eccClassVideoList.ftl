<div class="class-videos container-wrap">
	<ul class="video-list clearfix">
		<#if attachments?exists&&attachments?size gt 0>
		<#list attachments as video>
		<li>
			<div class="video-wrap video-wrap-16by9">
				<video src="${request.contextPath}${video.dirPath!}" preload="meta" controlsList="nodownload" controls="controls" width="100%"></video>
			</div>
			<h4>${video.filename!}</h4>
		</li>
		</#list>
		</#if>
	</ul>
</div>
<script>
$(document).ready(function(){	
	$('.container-wrap').each(function(){
		$(this).css({
			height: $(window).height() - $(this).offset().top - 220
		});
	});
	
	$("video").each(function(){
		$(this)[0].addEventListener('play',function(){
           $(this).parent().parent().siblings().find("video").trigger('pause');
        });  
	});
});
</script>