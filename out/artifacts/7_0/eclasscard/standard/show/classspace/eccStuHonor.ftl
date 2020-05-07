<#if eccStuHonor?exists&&eccStuHonor?size gt 0>
<ul class="honor-list honor-list-v">
	<#list eccStuHonor as stuhonor>
	<li class="honor-item">
		<div class="honor-photo-frame">
			<div class="honor-photo-wrap"><img width="272" height="204" src="${request.contextPath}${stuhonor.pictureUrl!}" alt=""></div>
		</div>
		<div class="honor-info">
			<h4 class="honor-winner">${stuhonor.studentName!}</h4>
			<p class="honor-name">${stuhonor.title!}</p>
			<p class="honor-time">获得于${(stuhonor.awardTime?string("yyyy-MM-dd"))?if_exists}</p>
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