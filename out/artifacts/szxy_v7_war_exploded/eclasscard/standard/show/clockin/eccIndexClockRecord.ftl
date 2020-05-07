<#if resultDtos?exists&&resultDtos?size gt 0>
	<ul class="checkin-result-list">
		<#list resultDtos as item>
		<li>
			<img src="${request.contextPath}${item.showPictrueUrl!}" alt="">
			<h4>${item.stuRealName!}</h4>
			<p>行政班：${item.rowOneValue!}</p>
			<span>${item.clockTime!}</span>
		</li>
		</#list>
	</ul>
<#else>
	<div class="no-data center">
		<div class="no-data-content">
			<img src="${request.contextPath}/static/eclasscard/standard/show/images/no-content.png" alt="" style="height: 150px">
			<p>没有打卡记录哦~</p>
		</div>
	</div>
</#if>

