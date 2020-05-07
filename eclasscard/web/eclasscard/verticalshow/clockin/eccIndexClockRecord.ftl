<ul class="checkin-result-list">
<#if resultDtos?exists&&resultDtos?size gt 0>
	<#list resultDtos as item>
	<li>
		<img src="${request.contextPath}${item.showPictrueUrl!}" alt="">
		<h4>${item.stuRealName!}</h4>
		<p>行政班：${item.rowOneValue!}</p>
		<span>${item.clockTime!}</span>
	</li>
	</#list>
<#else>
</#if>
</ul>
<script>
$(document).ready(function(){
	<#if resultDtos?exists&&resultDtos?size gt 0>
		$("#recordNodataDiv").hide();
	<#else>
		$("#recordNodataDiv").show();
	</#if>
})
</script>

