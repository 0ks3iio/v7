<#if back>
<div class="back">
	<a class="arrow" href="首页.html"></a>
	<span>班级空间</span>
</div>
<#else>
<div class="logo"><img src="${request.contextPath}/static/eclasscard/show/images/logo.png" alt=""></div>
	<#if type?exists&&(type=='10'||type=='20')>
	<!--div class="notice"><span class="icon icon-audio"></span>课程预告：10:00~10:40 物理</div-->
	</#if>
</#if>
<div class="date">
	<span class="time"></span>
	<div class="right">
		<span class="day"></span>
		<span class="week"></span>
	</div>
</div>