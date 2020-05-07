<#if courseList?exists && courseList?size gt 0>
<style>
<#list courseList as course>
<#assign colo = course.bgColor!?split(",")>
<#if colo?size gt 0>
	.mystudenttable .${course.subjectName}${arrayId!}{background:${colo[0]!};border-color:${colo[1]!}}
</#if>
</#list>
</style>
</#if>
<div class="filter">
	<#if entityName??>
	<div class="filter-item">
		<span class="filter-name">${entityHead}：</span>
		<div class="filter-content">
			<p>${entityName!}</p>
		</div>
	</div>
	</#if>
	<div class="filter-item">
		<span class="filter-name">上课班级：</span>
		<div class="filter-content">
			<p>${newClassName!}</p>
		</div>
	</div>
</div>
<#assign weekDays = (weekDays!7) - 1>
<#assign wratio = (100 - 12.5)/(weekDays+1)>
<table class="table table-bordered layout-fixed table-view no-margin mystudenttable">
	<thead>
		<tr>
			<th width="8%" class="text-center"></th>
			<th width="4.5%" class="text-center"></th>
			<#list 0..weekDays as day>
            <th width="${wratio}%" class="text-center">${dayOfWeekMap[day+""]!}</th>
            </#list>
		</tr>
	</thead>
	<tbody>
	<#list piMap?keys as piFlag>
	    <#if piMap[piFlag]?? && piMap[piFlag] gt 0>
	    <#assign interval = piMap[piFlag]>
	    <#assign intervalName = intervalNameMap[piFlag]>
	    <#list 1..interval as pIndex>
	    <tr>
	    <#if pIndex == 1>
	    	<td rowspan="${interval!}" class="text-center">${intervalName!}</td>
	    </#if>
			<td class="text-center">${pIndex!}</td>
			<#list 0..weekDays as day>
				<td class="item">
					<#if newGkTimetableOtherList?exists && newGkTimetableOtherList?size gt 0>
						<#list newGkTimetableOtherList as item>

							<#if piFlag == item.periodInterval && day == item.dayOfWeek && pIndex == item.period>
								<div class="infor ${item.subjectName!}${arrayId!}">
									<b>  ${item.subjectName!}</b><br>
									<#if INDEX! != "TEACHER"><p class="ellipsis">${item.teacherName!}</p></#if>
									<#if INDEX! != "CLASS"><p class="ellipsis">${item.className!}</p></#if>
									<#if INDEX! != "PLACE">${item.placeName!}</#if>
									<#if item.firstsdWeek?default(3)==1>(单)<#elseif item.firstsdWeek?default(3)==2>(双)</#if>
								</div>
							</#if>
						</#list>
					</#if>
				</td>
			</#list>
	    </tr>
	    </#list>
	    </#if>
    </#list>
	</tbody>
</table>

<div style="display:none;">
<div class="print">
<style>
	.table-print,.table-print > tbody > tr > td,.table-print > thead > tr > th{ border: 2px solid #333;}
</style>
<div class="filter">
	<#if entityName??>
	<div class="filter-item">
		<span class="filter-name">${entityHead}：</span>
		<div class="filter-content">
			<p>${entityName!}</p>
		</div>
	</div>
	</#if>
	<div class="filter-item">
		<span class="filter-name">上课班级：</span>
		<div class="filter-content">
			<p>${newClassName!}</p>
		</div>
	</div>
</div>
<table class="table table-bordered table-view table-print no-margin">
	<thead>
		<tr>
			<th width="8%" class="text-center"></th>
			<th width="4.5%" class="text-center"></th>
			<#list 0..weekDays as day>
            <th width="${wratio}%" class="text-center">${dayOfWeekMap[day+""]!}</th>
            </#list>
		</tr>
	</thead>
	<tbody>
	<#list piMap?keys as piFlag>
	    <#if piMap[piFlag]?? && piMap[piFlag] gt 0>
	    <#assign interval = piMap[piFlag]>
	    <#assign intervalName = intervalNameMap[piFlag]>
	    <#list 1..interval as pIndex>
	    <tr>
	    <#if pIndex == 1>
	    	<td rowspan="${interval!}" class="text-center">${intervalName!}</td>
	    </#if>
        	<td class="text-center">${pIndex!}</td>
			<#list 0..weekDays as day>
             <td class="item">
	           <#if newGkTimetableOtherList?exists && newGkTimetableOtherList?size gt 0>
	               <#list newGkTimetableOtherList as item>
	                   <#if piFlag == item.periodInterval && day == item.dayOfWeek && pIndex == item.period>
	                   <div class="infor">
	                         <p class="ellipsis"><b>${item.subjectName!}</b></p> 
	                         <#if INDEX! != "TEACHER"><p class="ellipsis"><b>${item.teacherName!}</b></p></#if>
	                         <#if INDEX! != "CLASS"><p class="ellipsis"><b>${item.className!}</b></p></#if>
	                         <#if INDEX! != "PLACE"><p class="ellipsis"><b>${item.placeName!}</b></p></#if>
	                          <#if item.firstsdWeek?default(3)==1>(单)<#elseif item.firstsdWeek?default(3)==2>(双)</#if>
	                   </div>
	                   <div class="node ${item.subjectName!}${arrayId!}" ></div>
	                   </#if>
	               </#list>
	           </#if>
	         </td>
			</#list>
	    </tr>
	    </#list>
	    </#if>
    </#list>
	</tbody>
</table>
</div>
</div>


<script>
$(function(){
	$("td.item").each(function(){
		if($(this).find(".infor").length>1){
			$(this).addClass("double");
			var cls = $(this).find(".infor:eq(0)").attr("class");
			$(this).find(".infor").attr("class","");
			var $div = $('<div></div>').attr("class",cls).html($(this).html());
			$(this).empty().append($div);
		}
	})
	$(".table-view tbody tr").each(function(){
	  var height = $(this).height();
	  $(this).find(".node").height(height);
	})
})
</script>
