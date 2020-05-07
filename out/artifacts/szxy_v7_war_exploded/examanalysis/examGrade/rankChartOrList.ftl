<script src="${request.contextPath}/static/echarts/echarts.min.js"></script>
<#if viewType?default('')==''>
${message!}
<!--width:1611px;-->
<#elseif viewType?default('')=='1'>
	<#import "/fw/macro/chartstructure.ftl" as chartstructure>
	<#if jsonStringData?exists && jsonStringData!="">
		<@chartstructure.histogram isLine=true divClass="noprint" loadingDivId="oneChart" divStyle="height:430px;width:${width?default(1000)}px;border:1px solid #ddd;margin-bottom:20px;" jsonStringData="${jsonStringData!}" isShowLegend=false isShowToolbox=false brLength=0 rotateNum=0/>
	<#else>
		<div style="height:430px;border:1px solid #ddd;margin-bottom:20px;">${message?default('暂无数据')}</div>
	</#if>
<#elseif viewType?default('')=='3'>
	<#import "/fw/macro/chartstructure.ftl" as chartstructure>
	<#if jsonStringData?exists && jsonStringData!="">
		<@chartstructure.histogram isLine=false divClass="noprint" loadingDivId="twoChart" divStyle="height:430px;width:${width?default(1000)}px;border:1px solid #ddd;margin-bottom:20px;" jsonStringData="${jsonStringData!}" isShowLegend=false isShowToolbox=false brLength=0 rotateNum=0 barGap="5%"/>
	<#else>
		<div style="height:430px;border:1px solid #ddd;margin-bottom:20px;">${message?default('暂无数据')}</div>
	</#if>
<#else>
	<div class="table-container">
         <div class="table-container-body">
			<table class="table table-striped table-hover">
				<thead>
					<tr>
						<th class="text-center">班级</th>
						<th class="text-center">年级名次</th>
						<th class="text-center">姓名</th>
						<th class="text-center">考号</th>
						<#if courseList?exists && courseList?size gt 0>
						<#list courseList as item>
						<th class="text-center">${item.subjectName!}</th>
						</#list>
						</#if>
					</tr>
				</thead>
				<tbody>
				<#if classStatList?exists && classStatList?size gt 0>
				<#list classStatList as dto>
					<#assign stuDto=dto.stuDtoList>
				    <#if stuDto?exists && stuDto?size gt 0>
						<#list stuDto as stuDtoItem>
						<tr>
							<#if stuDtoItem_index==0>
								<td class="text-center" rowspan="${dto.stuNum}">${dto.className!}<br/>(共${dto.stuNum}名)</td>
							</#if>
							<td class="text-center">${stuDtoItem.gradeRank}</td>
							<td class="text-center">${stuDtoItem.studentName!}</td>
							<td class="text-center">${stuDtoItem.examCode!}</td>
							<#assign stuScoreMap=stuDtoItem.statBySubjectMap>
							<#if courseList?exists && courseList?size gt 0>
							<#list courseList as item>
								<#if stuScoreMap[item.id]?exists>
									<td class="text-center">${stuScoreMap[item.id]}</td>
								<#else>
									<td class="text-center">0</td>
								</#if>
							</#list>
							</#if>
						</tr>
						</#list>
					</#if>

				</#list>
				</#if>
				</tbody>
			</table>
	     </div>
	</div>

</#if>