<div class="table-container-header filter">
	<div class="filter-item">共<#if (scheduleList?size)?exists>${(scheduleList?size)?if_exists}<#else>0</#if>份结果</div>
	<#if type == "2">
		<div class="filter-item filter-item-right">
			<input type="hidden" class="additionSwitch" id="showPlace" value="1">
			<a href="javascript:" class="btn btn-sm btn-blue" onclick="additionSwitch('.addition_place', this)">隐藏场地</a>
		</div>
		<div class="filter-item filter-item-right">
			<input type="hidden" class="additionSwitch" id="showTeacher" value="1">
			<a href="javascript:" class="btn btn-sm btn-blue" onclick="additionSwitch('.addition_teacher', this)">隐藏老师</a>
		</div>
	<#elseif type == "1">
		<div class="filter-item filter-item-right">
			<input type="hidden" class="additionSwitch" id="showPlace" value="1">
			<a href="javascript:" class="btn btn-sm btn-blue" onclick="additionSwitch('.addition_place', this)">隐藏场地</a>
		</div>
		<div class="filter-item filter-item-right">
			<input type="hidden" class="additionSwitch" id="showClass" value="1">
			<a href="javascript:" class="btn btn-sm btn-blue" onclick="additionSwitch('.addition_class', this)">隐藏班级</a>
		</div>
	<#else>
		<div class="filter-item filter-item-right">
			<input type="hidden" class="additionSwitch" id="showClass" value="1">
			<a href="javascript:" class="btn btn-sm btn-blue" onclick="additionSwitch('.addition_class', this)">隐藏班级</a>
		</div>
		<div class="filter-item filter-item-right">
			<input type="hidden" class="additionSwitch" id="showTeacher" value="1">
			<a href="javascript:" class="btn btn-sm btn-blue" onclick="additionSwitch('.addition_teacher', this)">隐藏老师</a>
		</div>
	</#if>
</div>
<div class="table-container-body js-scroll print" style="overflow: auto; height: 609px;">
<table class="table table-bordered table-striped table-list" width="100%">
	<thead>
		<tr>
		<th colspan="${allCols?default(1)}">
			<#if type?default('1')=='1'>教师<#elseif type?default('1')=='2'>班级<#else>场地</#if>总课表
		</th>
		</tr>
		<tr>
			<th rowspan="2" width="90">
			<#if type?default('1')=='1'>
			教师
			<#elseif type?default('1')=='2'>
			班级
			<#else>
			场地
			</#if>
			</th>
			<#list dayStrs as day>
			<th class="text-center" colspan="${totalPeriods?default(1)}">${day}</th>
			</#list>
		</tr>
		<tr>
		<#list dayStrs as day>
			<#list 1..totalPeriods as period>
			<th class="text-center">${period}</th>
			</#list>
		</#list>
		</tr>
	</thead>
	<tbody id="list">
		<#if scheduleList?exists && scheduleList?size gt 0>
		<#list scheduleList as scs>
		<tr>
			<#list scs as item>
			<td <#if item_index==0>style="white-space: nowrap;"</#if>>${item!}</td>		
			</#list>
		</tr>
		</#list>
		</#if>
	</tbody>
</table>
</div>
<script>
$(function(){
	$('.js-scroll').css({
		overflow: 'auto',
		height: $(window).innerHeight() - $('.js-scroll').offset().top - 60
	});
});

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