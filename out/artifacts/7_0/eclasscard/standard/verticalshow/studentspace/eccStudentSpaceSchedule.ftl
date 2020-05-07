<div id="aa" class="tab-pane scroll-container active">
	<#if nothing?exists && nothing == "true">
		<div class="nothing">
			<img src="${request.contextPath}/static/eclasscard/standard/verticalshow/images/no-content.png" alt="">
			<p>暂时没有课表哦~</p>
		</div>
	<#else>
	<table class="table table-course">
		<thead>
			<tr>
				<th width="12.5%"></th>
				<th <#if today == 1>class="active"</#if> width="12.5%">周一</th>
				<th <#if today == 2>class="active"</#if> width="12.5%">周二</th>
				<th <#if today == 3>class="active"</#if> width="12.5%">周三</th>
				<th <#if today == 4>class="active"</#if> width="12.5%">周四</th>
				<th <#if today == 5>class="active"</#if> width="12.5%">周五</th>
				<#if weekEnd?default(false)>
				<th <#if today == 6>class="active"</#if> width="12.5%">周六</th>
				<th <#if today == 7>class="active"</#if> width="12.5%">周日</th>
				</#if>
			</tr>
		</thead>
		<tbody>
			<#list 1..allCourseNum?default(8) as x>
				<#assign day = "p" + x /> 
				<#assign course = (values[day])! />
				<#if x == (intervalMap["2"]+intervalMap["1"] + 1)>
					<tr><td colspan="8"></td></tr>
				</#if>
				<#if x == (intervalMap["3"]+intervalMap["2"]+intervalMap["1"] + 1)>
					<tr><td colspan="8"></td></tr>
				</#if>
				<tr>
					<#if x lte intervalMap["1"] && x==1>
						<td rowspan="${intervalMap["1"]}">早上</td>
					<#elseif x == (intervalMap["1"]+1) >
                        <td rowspan="${intervalMap["2"]!}">上午</td>
					<#elseif x == (intervalMap["2"]+intervalMap["1"] + 1) >
                        <td rowspan="${intervalMap["3"]}">下午</td>
					<#elseif x == (intervalMap["3"]+intervalMap["2"]+intervalMap["1"] + 1) >
                        <td rowspan="${intervalMap["4"]}">晚上</td>
					</#if>
					<td <#if today == 1>class="active"</#if>>${course.courseName1!}<br />${course.className1!}</br><span title="${course.placeName1!}">${course.placeName1!}</span></td>
					<td <#if today == 2>class="active"</#if>>${course.courseName2!}<br />${course.className2!}</br><span title="${course.placeName2!}">${course.placeName2!}</span></td>
					<td <#if today == 3>class="active"</#if>>${course.courseName3!}<br />${course.className3!}</br><span title="${course.placeName3!}">${course.placeName3!}</span></td>
					<td <#if today == 4>class="active"</#if>>${course.courseName4!}<br />${course.className4!}</br><span title="${course.placeName4!}">${course.placeName4!}</span></td>
					<td <#if today == 5>class="active"</#if>>${course.courseName5!}<br />${course.className5!}</br><span title="${course.placeName5!}">${course.placeName5!}</span></td>
					<#if weekEnd?default(false)>
					<td <#if today == 6>class="active"</#if>>${course.courseName6!}<br />${course.className6!}</br><span title="${course.placeName6!}">${course.placeName6!}</span></td>
					<td <#if today == 7>class="active"</#if>>${course.courseName7!}<br />${course.className7!}</br><span title="${course.placeName7!}">${course.placeName7!}</span></td>
					</#if>
				</tr>
			</#list>
		</tbody>
	</table>
	</#if>
</div>
<script>
	$(document).ready(function(){
		$('.scroll-container').each(function(){
			$(this).css({
				overflow: 'auto',
				height: $(window).height() - 480 - 160
			});
		});
	});
</script>