<div class="col-sm-${(data.col)!12}">
    <div class="box box-default">
		<div class="box-header">
			<h4 class="box-title">${(data.title)!"我的课程表"}</h4>
		</div>
		<div class="box-body">
			<table class="table table-bordered layout-fixed">
				<thead>
					<tr>
						<#if data.timeIntervalMap??>
						<th class="text-center"></th>
						<th class="text-center" width="40"></th>
						</#if>
						<th class="text-center">周一</th>
						<th class="text-center">周二</th>
						<th class="text-center">周三</th>
						<th class="text-center">周四</th>
						<th class="text-center">周五</th>
						<#if data.weekEnd?default(false)>
						<th class="text-center">周六</th>
						<th class="text-center">周日</th>
						</#if>
					</tr>
				</thead>
				<tbody>
				<#if data.timeIntervalMap??>
					<#assign mc = 0/>
					<#list 1..data.allCourseNum?default(8) as x>
					<#assign day = "p" + x /> 
					<#assign v = (data.values[day])! />
					<tr>
						<#if x lte data.timeIntervalMap["1"] && x==1>
                            <td class="text-center" rowspan="${data.timeIntervalMap["1"]}">早自习</td>
						<#elseif x == (data.timeIntervalMap["1"]+1) >
                            <td class="text-center" rowspan="${data.timeIntervalMap["2"]!}">上午</td>
                            <#assign mc = x-1/>
						<#elseif x == (data.timeIntervalMap["2"]+data.timeIntervalMap["1"] + 1) >
                            <td class="text-center" rowspan="${data.timeIntervalMap["3"]}">下午</td>
                            <#assign mc = x-1/>
						<#elseif x == (data.timeIntervalMap["3"]+data.timeIntervalMap["2"]+data.timeIntervalMap["1"] + 1) >
                            <td class="text-center" rowspan="${data.timeIntervalMap["4"]}">晚上</td>
                            <#assign mc = x-1/>
						</#if>
                        <td class="text-center">${x - mc}</td>
						<td class="">
							<div class="infor"><b>${v.courseName1!}</b><br />${v.className1!}</br><span title="${v.placeName1!}">${v.placeName1!}</span></div>
							<#if v.bgColor1?default('') !='' || v.bdColor1?default('') != ''>
		                	<div class="node noprint" style="<#if v.bgColor1?default('') !=''>background:${v.bgColor1};</#if><#if v.bdColor1?default('') !=''>border-color:${v.bdColor1};</#if>"></div>
		                	</#if>
							<#if v.courseNameRe1?exists>
								<div class="infor"><b>${v.courseNameRe1!}</b><br />${v.classNameRe1!}</br><span title="${v.placeNameRe1!}">${v.placeNameRe1!}</span></div>
								<#if v.bgColorRe1?default('') !='' || v.bdColorRe1?default('') != ''>
									<div class="node noprint" style="<#if v.bgColorRe1?default('') !=''>background:${v.bgColorRe1};</#if><#if v.bdColorRe1?default('') !=''>border-color:${v.bdColorRe1};</#if>"></div>
								</#if>
							</#if>
						</td>
						<td class="">
							<div class="infor"><b>${v.courseName2!}</b><br />${v.className2!}</br><span title="${v.placeName2!}">${v.placeName2!}</span></div>
							<#if v.bgColor2?default('') !='' || v.bdColor2?default('') != ''>
		                	<div class="node noprint" style="<#if v.bgColor2?default('') !=''>background:${v.bgColor2};</#if><#if v.bdColor2?default('') !=''>border-color:${v.bdColor2};</#if>"></div>
		                	</#if>
							<#if v.courseNameRe2?exists>
								<div class="infor"><b>${v.courseNameRe2!}</b><br />${v.classNameRe2!}</br><span title="${v.placeNameRe2!}">${v.placeNameRe2!}</span></div>
								<#if v.bgColorRe2?default('') !='' || v.bdColorRe2?default('') != ''>
									<div class="node noprint" style="<#if v.bgColorRe2?default('') !=''>background:${v.bgColorRe2};</#if><#if v.bdColorRe2?default('') !=''>border-color:${v.bdColorRe2};</#if>"></div>
								</#if>
							</#if>
						</td>
						<td class="">
							<div class="infor"><b>${v.courseName3!}</b><br />${v.className3!}</br><span title="${v.placeName3!}">${v.placeName3!}</span></div>
							<#if v.bgColor3?default('') !='' || v.bdColor3?default('') != ''>
		                	<div class="node noprint" style="<#if v.bgColor3?default('') !=''>background:${v.bgColor3};</#if><#if v.bdColor3?default('') !=''>border-color:${v.bdColor3};</#if>"></div>
		                	</#if>
							<#if v.courseNameRe3?exists>
								<div class="infor"><b>${v.courseNameRe3!}</b><br />${v.classNameRe3!}</br><span title="${v.placeNameRe3!}">${v.placeNameRe3!}</span></div>
								<#if v.bgColorRe3?default('') !='' || v.bdColorRe3?default('') != ''>
									<div class="node noprint" style="<#if v.bgColorRe3?default('') !=''>background:${v.bgColorRe3};</#if><#if v.bdColorRe3?default('') !=''>border-color:${v.bdColorRe3};</#if>"></div>
								</#if>
							</#if>
						</td>
						<td class="">
							<div class="infor"><b>${v.courseName4!}</b><br />${v.className4!}</br><span title="${v.placeName4!}">${v.placeName4!}</span></div>
							<#if v.bgColor4?default('') !='' || v.bdColor4?default('') != ''>
		                	<div class="node noprint" style="<#if v.bgColor4?default('') !=''>background:${v.bgColor4};</#if><#if v.bdColor4?default('') !=''>border-color:${v.bdColor4};</#if>"></div>
		                	</#if>
							<#if v.courseNameRe4?exists>
								<div class="infor"><b>${v.courseName1!}</b><br />${v.className1!}</br><span title="${v.placeName1!}">${v.placeName1!}</span></div>
								<#if v.bgColor1?default('') !='' || v.bdColor1?default('') != ''>
									<div class="node noprint" style="<#if v.bgColor1?default('') !=''>background:${v.bgColor1};</#if><#if v.bdColor1?default('') !=''>border-color:${v.bdColor1};</#if>"></div>
								</#if>
							</#if>
						</td>
						<td class="">
							<div class="infor"><b>${v.courseName5!}</b><br />${v.className5!}</br><span title="${v.placeName5!}">${v.placeName5!}</span></div>
							<#if v.bgColor5?default('') !='' || v.bdColor5?default('') != ''>
		                	<div class="node noprint" style="<#if v.bgColor5?default('') !=''>background:${v.bgColor5};</#if><#if v.bdColor5?default('') !=''>border-color:${v.bdColor5};</#if>"></div>
		                	</#if>
							<#if v.courseNameRe5?exists>
								<div class="infor"><b>${v.courseNameRe5!}</b><br />${v.classNameRe5!}</br><span title="${v.placeNameRe5!}">${v.placeNameRe5!}</span></div>
								<#if v.bgColorRe5?default('') !='' || v.bdColorRe5?default('') != ''>
									<div class="node noprint" style="<#if v.bgColorRe5?default('') !=''>background:${v.bgColorRe5};</#if><#if v.bdColorRe5?default('') !=''>border-color:${v.bdColorRe5};</#if>"></div>
								</#if>
							</#if>
						</td>
						<#if data.weekEnd?default(false)>
						<td class="">
							<div class="infor"><b>${v.courseName6!}</b><br />${v.className6!}</br><span title="${v.placeName6!}">${v.placeName6!}</span></div>
							<#if v.bgColor6?default('') !='' || v.bdColor6?default('') != ''>
		                	<div class="node noprint" style="<#if v.bgColor6?default('') !=''>background:${v.bgColor6};</#if><#if v.bdColor6?default('') !=''>border-color:${v.bdColor6};</#if>"></div>
		                	</#if>
							<#if v.courseNameRe6?exists>
								<div class="infor"><b>${v.courseNameRe6!}</b><br />${v.classNameRe6!}</br><span title="${v.placeNameRe6!}">${v.placeNameRe6!}</span></div>
								<#if v.bgColorRe6?default('') !='' || v.bdColorRe6?default('') != ''>
									<div class="node noprint" style="<#if v.bgColorRe6?default('') !=''>background:${v.bgColorRe6};</#if><#if v.bdColorRe6?default('') !=''>border-color:${v.bdColorRe6};</#if>"></div>
								</#if>
							</#if>
						</td>
						<td class="">
							<div class="infor"><b>${v.courseName7!}</b><br />${v.className7!}</br><span title="${v.placeName7!}">${v.placeName7!}</span></div>
							<#if v.bgColor7?default('') !='' || v.bdColor7?default('') != ''>
		                	<div class="node noprint" style="<#if v.bgColor7?default('') !=''>background:${v.bgColor7};</#if><#if v.bdColor7?default('') !=''>border-color:${v.bdColor7};</#if>"></div>
		                	</#if>
							<#if v.courseNameRe7?exists>
								<div class="infor"><b>${v.courseNameRe7!}</b><br />${v.classNameRe7!}</br><span title="${v.placeNameRe7!}">${v.placeNameRe7!}</span></div>
								<#if v.bgColorRe7?default('') !='' || v.bdColorRe7?default('') != ''>
									<div class="node noprint" style="<#if v.bgColorRe7?default('') !=''>background:${v.bgColorRe7};</#if><#if v.bdColorRe7?default('') !=''>border-color:${v.bdColorRe7};</#if>"></div>
								</#if>
							</#if>
						</td>
						</#if>
					</tr>
					</#list>
					<#else>
					<tr>
						<td colspan = "<#if data.weekEnd?default(false)>7<#else>5</#if>" style="text-align:center;vertical-align:middle;">暂无数据</td>
					</tr>
				</#if>
				</tbody>
			</table>
		</div>
    </div>
</div>