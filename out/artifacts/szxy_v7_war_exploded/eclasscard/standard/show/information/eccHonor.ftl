<div class="box-horizontal">
	<div class="box-horizontal-header">
		<h3>班级荣誉</h3>
	</div>
	<div class="box-horizontal-body">
		<#if eccClassHonors?exists&&eccClassHonors?size gt 0>
		<ul class="honor-list honor-list-h js-honor-slick">
			<#list eccClassHonors as item>
			<li class="honor-item">
				<div class="honor-photo-frame">
					<div class="honor-photo-wrap">
					<#if item.style == 1>
					<img width="116" height="86" src="${request.contextPath}/static/eclasscard/standard/show/images/honor-flag.png" alt="">
					<#elseif item.style == 2>
					<img width="116" height="86" src="${request.contextPath}/static/eclasscard/standard/show/images/honor-cup.png" alt="">
					<#else>
					<img width="116" height="86" src="${request.contextPath}/static/eclasscard/standard/show/images/honor-medal.png" alt="">
					</#if>
					</div>
				</div>
				<div class="honor-info">
					<h4 class="honor-winner"></h4>
					<p class="honor-name">${item.title!}</p>
					<p class="honor-time">获得于${item.awardTime?string('yyyy-MM-dd')!}</p>
				</div>
			</li>
			</#list>
		</ul>
		<#else>
			<div class="no-data no-data-small center">
				<div class="no-data-content">
					<img src="${request.contextPath}/static/eclasscard/standard/show/images/no-content.png" alt="">
					<p>暂无内容</p>
				</div>
			</div>
		</#if>
	</div>
</div>

<div class="box-horizontal">
	<div class="box-horizontal-header">
		<h3>学生荣誉</h3>
	</div>
	<div class="box-horizontal-body">
		<#if eccStuHonors?exists&&eccStuHonors?size gt 0>
		<ul class="personal-honor">
			<#list eccStuHonors as item>
			<li>
				<div class="personal-honor-img">
					<img src="${request.contextPath}${item.pictureUrl!}" alt="">
					<span class="personal-honor-winner">${item.studentName!}</span>
				</div>
				<h5 class="personal-honor-title">${item.title!}</h5>
			</li>
			</#list>
		</ul>
		<#else>
			<div class="no-data no-data-small center">
				<div class="no-data-content">
					<img src="${request.contextPath}/static/eclasscard/standard/show/images/no-content.png" alt="">
					<p>暂无内容</p>
				</div>
			</div>
		</#if>
	</div>
</div>
<script>
$(document).ready(function(){
	// 班级荣誉滚动
	$('.js-honor-slick').slick({
		slidesToShow: 1,
		slidesToScroll: 1,
		prevArrow: false,
		nextArrow: false
	});
})
</script>