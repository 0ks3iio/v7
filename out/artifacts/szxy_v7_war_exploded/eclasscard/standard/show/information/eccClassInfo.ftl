<div class="role">
<#if teacherUserName?exists>
	<span class="role-img"><img src="${request.contextPath}/zdsoft/crop/doPortrait?type=big&userName=${teacherUserName!}" alt=""></span>
<#else>
	<span class="role-img"><img src="${request.contextPath}/static/eclasscard/standard/show/images/female-teacher.png" alt=""></span>
</#if>
	<span class="label label-fill label-fill-bluePurple">班主任</span>
	<h4 class="role-name">${teacherName!}</h4>
</div>

<ul class="data-list">
	<li><span>班级人数</span><span><em>${classStuNum!}</em></span></li>
	<li><span>请假人数</span><span><em>${leaveStus!}</em></span></li>
	<li>
		<span>请假名单</span>
		<span>${leaveStuscut!}<#if showMore>... <a class="js-show-leave" href="javascript:void(0);">更多</a></#if></span>
		<div class="leave-list" style="z-index: 999;">
			<h4>请假名单</h4>
			<div class="leave-list-content">${leaveStusName!}</div>
		</div>
	</li>
</ul>
<script>
$(document).ready(function(){
	// 查看请假人员名单
	$('.js-show-leave').on('click', function(e){
		e.preventDefault();
		$('.leave-list').toggleClass('show');
	}).on('blur', function(){
		$('.leave-list').removeClass('show');
	});
})
</script>