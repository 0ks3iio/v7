<#macro batchTable isTeaNum=false>
<table class="table table-striped table-hover ">
		<thead>
			<tr>
				<th width="15%">${PCKC!}</th>
				<th width="10%">科目</th>
				<th width="25%">班级数</th>
				<th width="15%">${PCKC!}</th>
				<th width="10%">科目</th>
				<th width="25%">班级数</th>
			</tr>
		</thead>
		<tbody>
			<#if singleOpenResultDtoList?? && (singleOpenResultDtoList?size>0)>
				<#list singleOpenResultDtoList as item>
					<#if item[1]?? >
						<#if (item[0].subjectNum?size > item[1].subjectNum?size)>
							<#assign subSize = item[0].subjectNum?size>
						<#else>
							<#assign subSize = item[1].subjectNum?size>
						</#if>
						<#list 1..subSize as ind>
							<tr>
								<#if ind == 1>
								<td rowspan = "${subSize}">${PCKC!}${item[0].batch}（共${item[0].classNum}个班）</td>
								</#if>
								<td>${item[0].subjectNames[ind-1]!}</td>
								<td>${item[0].subjectNums[ind-1]!}<#if isTeaNum && item[0].subjectNoTeaNums[ind-1]?? && item[0].subjectNoTeaNums[ind-1]!=0><span style="color:red">（其中${item[0].subjectNoTeaNums[ind-1]}个班未设置老师）</span></#if></td>
								<#if ind == 1>
								<td rowspan = "${subSize}">${PCKC!}${item[1].batch}（共${item[1].classNum}个班）</td>
								</#if>
								<td>${item[1].subjectNames[ind-1]!}</td>
								<td>${item[1].subjectNums[ind-1]!}<#if isTeaNum && item[1].subjectNoTeaNums[ind-1]?? && item[1].subjectNoTeaNums[ind-1]!=0><span style="color:red">（其中${item[1].subjectNoTeaNums[ind-1]}个班未设置老师）</span></#if></td>
							</tr>
						</#list>
					<#else>
						<#assign subSize = item[0].subjectNum?size>
						<#list 1..subSize as ind>
							<tr>
								<#if ind == 1>
								<td rowspan = "${subSize}">${PCKC!}${item[0].batch}（共${item[0].classNum}个班）</td>
								</#if>
								<td>${item[0].subjectNames[ind-1]!}</td>
								<td>${item[0].subjectNums[ind-1]!}<#if isTeaNum && item[0].subjectNoTeaNums[ind-1]?? && item[0].subjectNoTeaNums[ind-1]!=0><span style="color:red">（其中${item[0].subjectNoTeaNums[ind-1]}个班未设置老师）</span></#if></td>
								<#if ind == 1>
								<td rowspan = "${subSize}"></td>
								</#if>
								<td></td>
								<td></td>
							</tr>
						</#list>
					</#if>
				</#list>
			<#else>
				<tr>
                  	<td  colspan="88" align="center">
                  		暂无数据
                  	</td>
                  <tr>
			</#if>
		</tbody>
	</table>
</#macro>