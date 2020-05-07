<li class="checkin-result-tips">
	<#if gradeDtos?exists&&gradeDtos?size gt 0>
      	<#list gradeDtos as item>
		<div>${item.gradeName!}：${item.attacneTime!} 考勤</div>
		</#list>
    <#else>
	<div>无 考 勤</div>
    </#if>
</li>

