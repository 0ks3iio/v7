<#if statList?exists && statList?size gt 0>
<div class="explain">
	<p>说明：1、该综合能力分即反映学生各科均衡度的综合能力；
	2、若各科的权重都设为1，即视为各个科目同等重要，在这样的情况下原始考试总分名次高的学生，该综合能力分名次不一定高，可能会改变原始考试分排名；
	3、也可以重新自定义各科权重来计算综合能力分使其排名更接近原始考试总分排名</p>
</div>
<div class="table-container no-margin">
	<div class="table-container-body js-scroll">
		<#assign titleSize=7+subjectDtoList?size>
		<form class="print">
		<table class="table table-bordered table-striped table-hover">
			<thead>
				<tr>
					<th>序号</th>
					<th>学号</th>
					<th>姓名</th>
					<th>行政班</th>
					<#if subjectDtoList?exists && subjectDtoList?size gt 0>
						<#list subjectDtoList as subjectDto>
							<th>${subjectDto.subjectName!}标准分T（年级）</th>
						</#list>
					</#if>			
					<th>综合能力分</th>
					<th>考试总分年级排名</th>
					<th>综合能力分年级排名</th>
				</tr>
			</thead>
			<tbody>
				<#list statList as item>
	 				<tr>
					    <td>${item_index+1}</td>
					    <td><span class="ellipsis">${item.studentCode!}</span></td>
					    <td><span class="ellipsis">${item.studentName!}</span></td>
					    <td><span class="ellipsis">${item.className!}</span></td>
					    <#if subjectDtoList?exists && subjectDtoList?size gt 0>
							<#list subjectDtoList as subjectDto>
								<td><#if emStatMap[item.studentId+subjectDto.subjectId]?exists>${emStatMap[item.studentId+subjectDto.subjectId].scoreT?default(0.0)?string("0.#")}<#else>-</#if></td>
							</#list>
						</#if>
						<td>${item.abilityScore?default(0.0)?string("0.#")}</td>
					    <td>${item.gradeRank!}</td>
					    <td>${item.abilityRank!}</td>
					</tr>
				</#list>
			</tbody>
		</table>
		</form>
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
			height: $(window).innerHeight() - $('.js-scroll').offset().top - 45
		})
	}
})
</script>