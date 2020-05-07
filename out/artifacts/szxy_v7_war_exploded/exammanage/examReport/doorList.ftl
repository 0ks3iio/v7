<#if (dtoList?exists && dtoList?size>0)>
<p class="color-999">
	<span class="mr20">考试场地：${emPlace.placeName!}</span>
	<span class="mr20">考生数量：${emPlace.stuNum?default(0)}人</span>
	<span class="mr20">考号范围：${emPlace.stuNumRange!}</span>
</p>
<div class="print">
	<table class="table table-bordered table-striped table-hover no-margin">
		<thead>
		<tr>
			<th class="text-center">座位号</th>
			<th class="text-center">姓名</th>
			<th class="text-center">考号</th>
			<th class="text-center">行政班</th>
			<th class="text-center">学号</th>
		</tr>
		</thead>
		<tbody>
			<#list dtoList as dto>
			<tr>
				<td class="text-center">${dto.seatNum!}</td>
				<td class="text-center">${dto.student.studentName!}</td>
				<td class="text-center">${dto.examNumber!}</td>
				<td class="text-center">${dto.className!}</td>
				<td class="text-center">${dto.student.studentCode!}</td>
			</tr>
			</#list>
		</tbody>
	</table>
	<#else >
		<div class="filter filter-f16">
			<div class="no-data-container">
				<div class="no-data">
				<span class="no-data-img">
					<img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
				</span>
					<div class="no-data-body">
						<p class="no-data-txt">暂无相关数据</p>
					</div>
				</div>
			</div>
		</div>
	</#if>
</div>