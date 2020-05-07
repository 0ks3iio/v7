<div id="honor-personal" class="tab-pane active">
	<#if eccStuHonor?exists&&eccStuHonor?size gt 0>
	<ul class="honor-list honor-list-v">
		<#list eccStuHonor as stuhonor>
		<li class="honor-item grid-1of3">
			<div class="honor-photo-frame">
				<div class="honor-photo-wrap">
					<img src="${request.contextPath}${stuhonor.pictureUrl!}" alt="">
				</div>
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
			<img src="${request.contextPath}/static/eclasscard/standard/verticalshow/images/no-content.png" alt="">
			<p>暂时没有学生荣誉哦~</p>
		</div>
	</#if>
</div>