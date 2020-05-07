<#if studentWithInfoList?exists&&studentWithInfoList?size gt 0>
	<ul class="checkin-stu-list">
		<#list studentWithInfoList as info>
		<li>
			<a href="javascript:void(0);">
			<div class="checkin-stu-avatar">
				<img src="${request.contextPath}${info.showPictrueUrl!}">
			</div>
			<span class="checkin-stu-name">${info.student.studentName!}</span>
		    </a>
		</li>
		</#list>
	</ul>
	<#else>
	<div class="nothing">
		<img src="${request.contextPath}/static/eclasscard/standard/show/images/no-content.png" alt="">
	</div>
</#if>
<script>
	$(document).ready(function(){
		$('.scroll-container').each(function(){
			$(this).css({
				overflow: 'auto',
				height: $(window).height() - $(this).offset().top - 160
			});
		});
		
		$('.nothing').height($('.scroll-container').height() - 100 );
	});	
</script>		