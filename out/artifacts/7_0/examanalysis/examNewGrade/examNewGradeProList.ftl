<#import "/fw/macro/chartstructure.ftl" as chartstructure />
<#if statList?exists && statList?size gt 0>
<div class="explain">
	<p>说明：1、进步度=本次考试标准分T（年级）-参照考试标准分T（年级）；2、标准分T以50分为成绩好与差的分界线（即整个年级群体标准分T的平均成绩为50）；3、进步度为正数说明进步，负数说明退步</p>
</div>
<div id="scatterDiv" style="display:none;">
	<@chartstructure.scatter loadingDivId="mychart03" divStyle="width: 800px;height: 400px;" jsonStringData=jsonStringData />
	<@chartstructure.scatter loadingDivId="mychart04" divStyle="width: 800px;height: 400px;" jsonStringData=jsonStringData1 />
</div>
<div class="table-container no-margin" id="proTableDiv">
	<div class="table-container-body js-scroll">
		<form class="print">
		<table class="table table-bordered table-striped table-hover no-margin">
				<thead>
				<tr>
					<th rowspan="2">序号</th>
					<th rowspan="2">学号</th>
					<th rowspan="2">姓名</th>
					<th rowspan="2">行政班级</th>
					<th colspan="3" class="text-center">本次考试</th>
					<th colspan="3" class="text-center">参照考试</th>
					<th rowspan="2">进步度</th>
					<th rowspan="2">进步度班级排名</th>
					<th rowspan="2">进步度年级排名</th>
				</tr>
				<tr>
					<th aria-controls="dynamic-table" aria-sort="descending" >考试分</th>
					<th>年级排名</th>
					<th>标准分T（年级）</th>
					<th aria-controls="dynamic-table" aria-sort="descending" >考试分</th>
					<th>年级排名</th>
					<th>标准分T（年级）</th>
				</tr>
				</thead>
				<tbody>
				<#list statList as item>
					<tr>
						<td>${item_index+1}</td>
					    <td><span class="ellipsis">${item.studentCode!}</span></td>
					    <td><span class="ellipsis">${item.studentName!}</span></td>
					    <td><span class="ellipsis">${item.className!}</span></td>
							<td>${item.score?default(0.0)?string("0.#")}</td>
							<td>${item.gradeRank!}</td>
							<td>${item.scoreT?default(0.0)?string("0.#")}</td>
							<td>${item.compareExamScore?default(0.0)?string("0.#")}</td>
							<td>${item.compareExamRank!}</td>
							<td>${item.compareExamScoreT?default(0.0)?string("0.#")}</td>
						<td>${item.progressDegree?default(0.0)?string("0.#")}</td>
						<td>${item.progressDegreeRankClass!}</td>
						<td>${item.progressDegreeRankGrade!}</td>
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
				<p class="no-data-txt"><#if examPro?default("")!="">${examPro!}<#else>暂无数据</#if></p>
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