<#import "/fw/macro/htmlcomponent.ftl" as htmlcomponent>
<#assign ms=timesMap['1']?default(0)>
<#assign am=timesMap['2']?default(0)>
<#assign pm=timesMap['3']?default(0)>
<#assign nm=timesMap['4']?default(0)>
<#assign oneDayAll=ms+am+pm+nm>
<#assign scheduleDtoMap=scheduleDtoMap>

<div class="filter">
	<div class="filter-item filter-item-left">
		<#if type == "1">
			<div class="filter-item filter-item-right">
				<input type="hidden" class="additionSwitch" id="showPlace" value="1">
				<a href="javascript:" class="btn btn-blue" onclick="additionSwitch('.addition_place', this)">隐藏场地</a>
			</div>
			<div class="filter-item filter-item-right">
				<input type="hidden" class="additionSwitch" id="showTeacher" value="1">
				<a href="javascript:" class="btn btn-blue" onclick="additionSwitch('.addition_teacher', this)">隐藏老师</a>
			</div>
		<#elseif type == "2">
			<div class="filter-item filter-item-right">
				<input type="hidden" class="additionSwitch" id="showPlace" value="1">
				<a href="javascript:" class="btn btn-blue" onclick="additionSwitch('.addition_place', this)">隐藏场地</a>
			</div>
			<div class="filter-item filter-item-right">
				<input type="hidden" class="additionSwitch" id="showClass" value="1">
				<a href="javascript:" class="btn btn-blue" onclick="additionSwitch('.addition_class', this)">隐藏班级</a>
			</div>
		<#else>
			<div class="filter-item filter-item-right">
				<input type="hidden" class="additionSwitch" id="showClass" value="1">
				<a href="javascript:" class="btn btn-blue" onclick="additionSwitch('.addition_class', this)">隐藏班级</a>
			</div>
			<div class="filter-item filter-item-right">
				<input type="hidden" class="additionSwitch" id="showTeacher" value="1">
				<a href="javascript:" class="btn btn-blue" onclick="additionSwitch('.addition_teacher', this)">隐藏老师</a>
			</div>
		</#if>
		<div class="filter-item filter-item-right">
			<a href="javascript:" class="btn btn-blue" onclick="exportClassAll()" >导出</a>
		</div>
	</div>
</div>
<div class="table-container">
	<div class="table-container-header">共<#if entityList?exists>${entityList?size!}<#else>0</#if>份结果</div>
	<div class="table-container-body print js-scroll">
		<table class="table table-bordered table-hover text-center">
			<thead>
				<tr>
					<th rowspan="2"><#if type?default('1')=='1'>班级<#elseif type?default('1')=='2'>教师<#else>场地</#if></th>
					
					<#list 0..(weekDays-1) as day>
					<th class="text-center" colspan="${oneDayAll?default(1)}">${dayOfWeekMap2[day+""]!}</th>
					</#list>
				</tr>
				<tr>
					
					<#list 1..weekDays as i>
						<#list 1..oneDayAll as j>
						<th class="text-center" width="200">${j}</th>
						</#list>
					</#list>
				</tr>
			</thead>
			<tbody>
				<#if entityList?exists && entityList?size gt 0>
				<#list entityList as xzb>
					<#assign classDtoMap=scheduleDtoMap[xzb[0]]>
					<tr>
					<td><p style="white-space: nowrap;">${xzb[1]!}</p></td>
					
					<#list 0..(weekDays-1) as r>
						<#if ms gt 0>
							<#list 1..ms as t>
								<#assign key=r+'_1_'+t>
								<#if classDtoMap[key]?exists>
									<#assign showList=classDtoMap[key].showSchedule>
									<#if showList?exists && showList?size gt 0>
										<td class="text-center">
										<#list showList as showItem>
											<p style="white-space: nowrap;">${showItem!}</p>
										</#list>
										</td>
									<#else>
										<td class="text-center">&nbsp;</td>
									</#if>
								<#else>
									<td class="text-center">&nbsp;</td>
								</#if>
							</#list>
						</#if>
						<#if am gt 0>
							<#list 1..am as t>
								<#assign key=r+'_2_'+t>
								<#if classDtoMap[key]?exists>
									<#assign showList=classDtoMap[key].showSchedule>
									<#if showList?exists && showList?size gt 0>
										<td class="text-center">
										<#list showList as showItem>
											<p style="white-space: nowrap;">${showItem!}</p>
										</#list>
										</td>
									<#else>
										<td class="text-center">&nbsp;</td>
									</#if>
								<#else>
									<td class="text-center">&nbsp;</td>
								</#if>
								
							</#list>
						</#if>
						<#if pm gt 0>
							<#list 1..pm as t>
								<#assign key=r+'_3_'+t>
								<#if classDtoMap[key]?exists>
									<#assign showList=classDtoMap[key].showSchedule>
									<#if showList?exists && showList?size gt 0>
										<td class="text-center">
										<#list showList as showItem>
											<p style="white-space: nowrap;">${showItem!}</p>
										</#list>
										</td>
									<#else>
										<td class="text-center">&nbsp;</td>
									</#if>
								<#else>
									<td class="text-center">&nbsp;</td>
								</#if>
							</#list>
						</#if>
						<#if nm gt 0>
							<#list 1..nm as t>
								<#assign key=r+'_4_'+t>
								<#if classDtoMap[key]?exists>
									<#assign showList=classDtoMap[key].showSchedule>
									<#if showList?exists && showList?size gt 0>
										<td class="text-center">
										<#list showList as showItem>
											<p style="white-space: nowrap;">${showItem!}</p>
										</#list>
										</td>
									<#else>
										<td class="text-center">&nbsp;</td>
									</#if>
								<#else>
									<td class="text-center">&nbsp;</td>
								</#if>
							</#list>
						</#if>
						</#list>
					</tr>
				</#list>
				</#if>
			</tbody>
		</table>
	</div>
</div>
<script>
$(function(){
	$('.js-scroll').css({
		overflow: 'auto',
		height: $(window).innerHeight() - $('.js-scroll').offset().top - 45
	})
})

function exportClassAll(){
	var param = 'type=${type!"1"}';
	$(".additionSwitch").each(function(i,obj){
		param += "&"+$(obj).attr("id")+"="+$(obj).val();
	});
	var url = "${request.contextPath}/newgkelective/${arrayId!}/arrayResult/classroomAll/export?"+param;
	console.log(param);
	console.log(url);
	window.location=url;
}
function additionSwitch(name, obj) {
	if ($(obj).prev().val() == "1") {
		$(obj).prev().val("0");
		$(obj).text("显示" + $(obj).text().substring(2, 4));
		$(name).hide();
		$(".addition").each(function () {
			var tmp = true;
			$(this).find("span").each(function () {
				if($(this).is(":visible")) {
					tmp = false;
				}
			});
			if (tmp) {
				$(this).hide();
			}
		});
	} else {
		$(obj).prev().val("1");
		$(obj).text("隐藏" + $(obj).text().substring(2, 4));
		$(".addition").show();
		$(name).show();
	}
}
</script>