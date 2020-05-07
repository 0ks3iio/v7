<div id="student" class="tab-pane scroll-container active">
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
			<img src="${request.contextPath}/static/eclasscard/standard/verticalshow/images/no-content.png" alt="">
			<p>暂时没有学生哦~</p>
		</div>
	</#if>
</div>
<script>
	$(document).ready(function(){
		$('.scroll-container').each(function(){
			$(this).css({
				overflowY: 'auto',
				overflowX: 'hidden',
				height: $(window).height() - 480 - 160
			});
		});
	});
</script>