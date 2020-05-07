<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
		<table class="table table-bordered table-striped table-hover no-margin">
			<thead>
				<tr>
					<th class="text-center" style="width:4%">序号</th>
					<th class="text-center" style="width:8%">姓名</th>
					<th class="text-center" style="width:10%">考号</th>
					<th class="text-center" style="width:8%">行政班</th>
					<#if type == "all">
						<#if subjectInfoList?exists && (subjectInfoList?size > 0)>
							<#list subjectInfoList as list>
								<th class="text-center">${list.courseName!}</th>
							</#list>
						</#if>
					<#else>
						<th class="text-center">${subjectName!}</th>
					</#if>
					<#if inputType && type == "all">
					<th class="text-center" style="width:5%">总分</th>
					</#if>
				</tr>
			</thead>
			<tbody>
			<#if examGradeDtoList?exists && (examGradeDtoList?size > 0)>
				<#list examGradeDtoList as gradedto>
				<tr>
					<td class="text-center">${gradedto_index+1}</td>
					<td class="text-center">${gradedto.studentName!}</td>
					<td class="text-center" title="${gradedto.examNumber!}"><#if (gradedto.examNumber!) != "" && (gradedto.examNumber!)?length gt 12>${gradedto.examNumber?substring(0,12)}...<#else>${gradedto.examNumber!}</#if></td>
					<td class="text-center">${gradedto.className!}</td>
					<#if ((gradedto.scoresList)?exists) && ((gradedto.scoresList)?size > 0)>
						<#list (gradedto.scoresList) as scoresList>
							<td class="text-center">${scoresList!}</td>
						</#list>
					</#if>
					<#if inputType && type == "all">
					<td class="text-center">${gradedto.total!}</td>
					</#if>
				</tr>
				</#list>
			</#if>	
			</tbody>
		</table>
<#if examGradeDtoList?exists && (examGradeDtoList?size > 0) && queryName == "no">
<@htmlcom.pageToolBar container="#examGradeList"/>
</#if>