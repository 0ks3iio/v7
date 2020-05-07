
<#if acts?exists && acts?size gt 0>
<#list acts as act>
<li class="mui-table-view-cell mui-media" actId="${act.id!}">
	<a class="mui-navigate-right">
		<div class="mui-media-body">
			<h5 class="mui-ellipsis">${act.name!}</h5>
			<p class="mui-ellipsis"><span class="mui-icon"><i class="mui-icon-locationimg"></i></span>${act.activityPlace!}</p>
			<p class="mui-ellipsis"><span class="mui-icon"><i class="mui-icon-clock"></i></span>${(act.activityStartDate?string('yyyy/MM/dd'))?if_exists}-${(act.activityEndDate?string('yyyy/MM/dd'))?if_exists}</p>
		</div>
	</a>
</li>
</#list>
</#if>
<script type="text/javascript" charset="utf-8">
var str = 'teacherId=${teacherId!}&level=${level?default(1)}';

$('li').click(function(){
	var id = $(this).attr('actId');
	load("${request.contextPath}/mobile/open/partybuild7/activity/detail?id="+id);
});
		
</script>  