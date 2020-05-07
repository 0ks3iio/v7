<div class="box-header">
	<ul class="tabs">
		<li class="active"><a href="javascript:void(0);" data-action="tab">我的荣誉</a></li>
	</ul>
</div>
<div class="box-body scroll-container">
<div class="space-content">
<div class="tab-content">
<div class="tab-pane active" role="tabpanel">
<#if eccHonors?exists&&eccHonors?size gt 0>
<ul class="honor-list honor-list-v">
	<#list eccHonors as honor>
	<li class="honor-item">
		<div class="honor-photo-frame">
			<div class="honor-photo-wrap"><img width="272" height="204" src="${request.contextPath}${honor.pictureUrl!}" alt=""></div>
		</div>
		<div class="honor-info">
			<h4 class="honor-winner">${honor.studentName!}</h4>
			<p class="honor-name">${honor.title!}</p>
			<p class="honor-time">获得于${(honor.awardTime?string("yyyy-MM-dd"))?if_exists}</p>
		</div>
	</li>
	</#list>
</ul>
<#else>
	<div class="nothing">
		<img src="${request.contextPath}/static/eclasscard/standard/show/images/no-content.png" alt="">
	</div>
</#if>
</div>
</div>
</div>
</div>
<script>
	$(document).ready(function(){
		$('.scroll-container').each(function(){
			$(this).css({
				overflow: 'auto',
				height: $(window).height() - $(this).offset().top - 160
			});
		});
		
		$('.nothing').height($('.scroll-container').height() - 100 );
	})
</script>