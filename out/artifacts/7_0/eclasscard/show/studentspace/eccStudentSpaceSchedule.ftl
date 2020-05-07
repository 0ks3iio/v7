<div class="space-course">
	<table class="table table-course">
		<thead>
			<tr>
				<th width="12.5%"></th>
				<th width="5%"></th>
				<th width="12.5%">周一</th>
				<th width="12.5%">周二</th>
				<th width="12.5%">周三</th>
				<th width="12.5%">周四</th>
				<th width="12.5%">周五</th>
				<#if weekEnd?default(false)>
				<th width="12.5%">周六</th>
				<th width="12.5%">周日</th>
				</#if>
			</tr>
		</thead>
		<tbody>
			<#list 1..allCourseNum?default(8) as x>
				<#assign day = "p" + x /> 
				<#assign course = (values[day])! />
				<#if intervalMap["1"] gt 0 && x == (intervalMap["1"] + 1)>
					<tr><td colspan="8"></td></tr>
				</#if>
				<#if x == (intervalMap["2"]+intervalMap["1"] + 1)>
					<tr><td colspan="8"></td></tr>
				</#if>
				<#if x == (intervalMap["3"]+intervalMap["2"]+intervalMap["1"] + 1)>
					<tr><td colspan="8"></td></tr>
				</#if>
				<tr>
					<#if x lte intervalMap["1"] && x==1>
						<td rowspan="${intervalMap["1"]}">早上</td>
						<td>${x}</td>
					<#elseif x == (intervalMap["1"]+1) >
                        <td rowspan="${intervalMap["2"]!}">上午</td>
                        <td>${x-intervalMap["1"]}</td>
					<#elseif x == (intervalMap["2"]+intervalMap["1"] + 1) >
                        <td rowspan="${intervalMap["3"]}">下午</td>
                        <td>${x-intervalMap["1"]-intervalMap["2"]}</td>
					<#elseif x == (intervalMap["3"]+intervalMap["2"]+intervalMap["1"] + 1) >
                        <td rowspan="${intervalMap["4"]}">晚上</td>
                        <td>${x-intervalMap["1"]-intervalMap["2"]-intervalMap["3"]}</td>
					</#if>
					
					<td class="text-center">${course.courseName1!}<br />${course.className1!}</br><span title="${course.placeName1!}">${course.placeName1!}</span></td>
					<td class="text-center">${course.courseName2!}<br />${course.className2!}</br><span title="${course.placeName2!}">${course.placeName2!}</span></td>
					<td class="text-center">${course.courseName3!}<br />${course.className3!}</br><span title="${course.placeName3!}">${course.placeName3!}</span></td>
					<td class="text-center">${course.courseName4!}<br />${course.className4!}</br><span title="${course.placeName4!}">${course.placeName4!}</span></td>
					<td class="text-center">${course.courseName5!}<br />${course.className5!}</br><span title="${course.placeName5!}">${course.placeName5!}</span></td>
					<#if weekEnd?default(false)>
					<td class="text-center">${course.courseName6!}<br />${course.className6!}</br><span title="${course.placeName6!}">${course.placeName6!}</span></td>
					<td class="text-center">${course.courseName7!}<br />${course.className7!}</br><span title="${course.placeName7!}">${course.placeName7!}</span></td>
					</#if>
				</tr>
			</#list>
		</tbody>
	</table>
</div>
<script>
</script>