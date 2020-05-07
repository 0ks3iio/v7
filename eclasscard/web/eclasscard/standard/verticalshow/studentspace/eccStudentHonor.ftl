<div id="ee" class="tab-pane scroll-container active">
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
			<img src="${request.contextPath}/static/eclasscard/standard/verticalshow/images/no-content.png" alt="">
			<p>暂时没有个人荣誉哦~</p>
		</div>
	</#if>
</div>
<script>
	$(document).ready(function(){
		$('.scroll-container').each(function(){
			$(this).css({
				overflow: 'auto',
				height: $(window).height() - 480 - 160
			});
		});
	});
</script>