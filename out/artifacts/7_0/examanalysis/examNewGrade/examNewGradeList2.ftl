<#if statList?exists && statList?size gt 0>
<div class="explain">
	<p>${summary!}</p>
</div>
<div class="table-container no-margin">
	<div class="table-container-body js-scroll">
		<#if tableType?default("1")=="1">
			<#assign titleSize=9+subjectDtoList?size>
			<form class="print">
			<table class="table table-bordered table-striped table-hover no-margin">
				<thead>
					<tr>
						<th class="text-center" colspan="${titleSize!}">${title!}</th>
					</tr>
					<tr>
						<th>序号</th>
						<th>学号</th>
						<th>行政班</th>
						<th>学生</th>
						<#if subjectDtoList?exists && subjectDtoList?size gt 0>
							<#list subjectDtoList as subjectDto>
								<th>${subjectDto.subjectName!}</th>
							</#list>
						</#if>			
						<th>总分</th>
						<th>标准分T（年级）</th>
						<th>百分等级分数（年级）</th>
						<th>班级排名</th>
						<th>年级排名</th>
					</tr>
				</thead>
				<tbody>
					<#list statList as item>
		 				<tr>
						    <td>${item_index+1}</td>
						    <td><span class="ellipsis">${item.studentCode!}</span></td>
						    <td><span class="ellipsis">${item.className!}</span></td>
						    <td><span class="ellipsis">${item.studentName!}</span></td>
						    <#if subjectDtoList?exists && subjectDtoList?size gt 0>
								<#list subjectDtoList as subjectDto>
									<td><#if emStatMap[item.studentId+subjectDto.subjectId]?exists>${emStatMap[item.studentId+subjectDto.subjectId].score?default(0.0)?string("0.#")}<#else>-</#if></td>
								</#list>
							</#if>
							<td>${item.score?default(0.0)?string("0.#")}</td>
							<td>${item.scoreT?default(0.0)?string("0.#")}</td>
							<td>${item.scoreLevel?default(0.0)?string("0.#")}</td>
						    <td>${item.classRank!}</td>
						    <td>${item.gradeRank!}</td>
						</tr>
					</#list>
				</tbody>
			</table>
			</form>
		<#else>
		<form class="print">
		<table class="table table-bordered table-striped table-hover no-margin">
				<thead>
				<tr>
					<th class="text-center" colspan="${length!}">${title!}</th>
				</tr>
				<tr>
					<th rowspan="2">序号</th>
					<th rowspan="2">学号</th>
					<th rowspan="2">行政班级</th>
					<th rowspan="2">学生</th>
					<#if subjectDtoList?exists && subjectDtoList?size gt 0>
						<#list subjectDtoList as subjectDto>
							<th <#if subjectDto.subType?default("")=="1">colspan="4"<#else>colspan="3"</#if> class="text-center">${subjectDto.subjectName!}</th>
						</#list>
					</#if>
				</tr>
				<tr>
					<#if subjectDtoList?exists && subjectDtoList?size gt 0>
						<#list subjectDtoList as subjectDto>
							<th aria-controls="dynamic-table" aria-sort="descending" >考试分</th>
							<th>班级排名</th>
							<th>年级排名</th>
							<#if subjectDto.subType?default("")=="1">
								<th>赋分</th>
							</#if>
						</#list>
					</#if>
				</tr>
				</thead>
				<tbody>
				<#list statList as item>
					<tr>
						<td>${item_index+1}</td>
					    <td><span class="ellipsis">${item.studentCode!}</span></td>
					    <td><span class="ellipsis">${item.className!}</span></td>
					    <td><span class="ellipsis">${item.studentName!}</span></td>
					    <#if subjectDtoList?exists && subjectDtoList?size gt 0>
							<#list subjectDtoList as subjectDto>
								<#if emStatMap[item.studentId+subjectDto.subjectId]?exists>
									<#assign inStat=emStatMap[item.studentId+subjectDto.subjectId]>
									<td>${inStat.score?default(0.0)?string("0.#")}</td>
									<td>${inStat.classRank?default(0)}</td>
									<td>${inStat.gradeRank?default(0)}</td>
									<#if subjectDto.subType?default("")=="1">
									<td>${inStat.conScore?default(0.0)?string("0.#")}</td>
									</#if>
								<#else>
									<td>-</td><td>-</td><td>-</td>
									<#if subjectDto.subType?default("")=="1"><td>-</td></#if>
								</#if>
							</#list>
						</#if>
					</tr>
				</#list>
				</tbody>
			</table>
			</form>
		</#if>
	</div>
</div>
<#else>
	<div class="no-data-container">
		<div class="no-data">
			<span class="no-data-img">
				<img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
			</span>
			<div class="no-data-body">
				<p class="no-data-txt">暂无记录</p>
			</div>
		</div>
	</div>
</#if>
<script>
$(function(){
	layer.closeAll('dialog');
	if($('.js-scroll').offset()){
		$('.js-scroll').css({
			overflow: 'auto',
			height: $(window).innerHeight() - $('.js-scroll').offset().top -45
		})
	}
})
</script>