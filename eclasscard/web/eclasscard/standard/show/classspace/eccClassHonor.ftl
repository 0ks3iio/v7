<#if eccClazzHonor?exists&&eccClazzHonor?size gt 0>
<ul class="honor-list honor-list-v">
	<#list eccClazzHonor as clazzhonor>
	<li class="honor-item">
		<div class="honor-photo-frame">
			<#if clazzhonor.style == 1>
			<div class="honor-photo-wrap"><img width="272" height="204" src="${request.contextPath}/static/eclasscard/standard/show/images/honor-flag.png" alt=""></div>
			<#elseif clazzhonor.style == 2>
			<div class="honor-photo-wrap"><img width="272" height="204" src="${request.contextPath}/static/eclasscard/standard/show/images/honor-cup.png" alt=""></div>
			<#else>
			<div class="honor-photo-wrap"><img width="272" height="204" src="${request.contextPath}/static/eclasscard/standard/show/images/honor-medal.png" alt=""></div>
			</#if>					
		</div>
		<div class="honor-info">
			<p class="honor-name">${clazzhonor.title!}</p>
			<p class="honor-time">获得于${(clazzhonor.awardTime?string("yyyy-MM-dd"))?if_exists}</p>
		</div>
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
		$('.nothing').height($('.scroll-container').height() - 100 );
	});
</script>