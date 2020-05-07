<div class="box-body">
	<h2 class="ecard-name">${className!}</h2>
	<div class="role">
		<#if teacherUserName?exists>
		<span class="role-img"><img src="${request.contextPath}/zdsoft/crop/doPortrait?type=big&userName=${teacherUserName!}" alt=""></span>
		<span class="label label-fill label-fill-yellow">班主任</span>
		<h4 class="role-name">${teacherName!}</h4>
		<#else>
		<span class="role-img"><img src="${request.contextPath}/static/eclasscard/verticalshow/images/avatar.png" alt=""></span>
		<span class="label label-fill label-fill-yellow">班主任</span>
		<h4 class="role-name">未设置</h4>
		</#if>
	</div>

	<ul class="data-list">
		<li><span>班级人数</span><span><em>${classStuNum!}</em></span></li>
		<li><span>请假人数</span><span><em>${leaveStus!}</em></span></li>
		<li><span>请假名单</span><span>${leaveStusName!}</span></li>
	</ul>

</div>