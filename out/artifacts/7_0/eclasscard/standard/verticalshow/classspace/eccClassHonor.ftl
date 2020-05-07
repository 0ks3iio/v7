<div id="honor-class" class="tab-pane active">
	<#if eccClazzHonor?exists&&eccClazzHonor?size gt 0>
	<ul class="honor-list honor-list-v">
		<#list eccClazzHonor as clazzhonor>
		<li class="honor-item grid-1of3">
			<div class="honor-photo-frame">
				<div class="honor-photo-wrap">
					<#if clazzhonor.style == 1>
						<img src="${request.contextPath}/static/eclasscard/standard/verticalshow/images/honor-flag.png" alt="">
					<#elseif clazzhonor.style == 2>
						<img src="${request.contextPath}/static/eclasscard/standard/verticalshow/images/honor-cup.png" alt="">
					<#else>
						<img src="${request.contextPath}/static/eclasscard/standard/verticalshow/images/honor-medal.png" alt="">
					</#if>	
				</div>
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
			<img src="${request.contextPath}/static/eclasscard/standard/verticalshow/images/no-content.png" alt="">
			<p>暂时没有班级荣誉哦~</p>
		</div>
	</#if>
</div>