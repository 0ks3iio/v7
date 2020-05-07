<form>
<table class="table table-bordered table-striped table-hover no-margin">
	<tbody>
	<#if (dtoList?exists && dtoList?size>0)>
            <thead>
            <tr>
                <th class="">座位号</th>
                <th class="">考号</th>
                <th class="">考生</th>
                <th class="">学号</th>
                <th class="">行政班</th>
                <th class="">考试场地</th>
            </tr>
            </thead>
		<#list dtoList as dto>
		<tr>
			<td class="">${dto.seatNum!}</td>
			<td class="">${dto.examNumber!}</td>
			<td class="">${dto.student.studentName!}</td>
			<td class="">${dto.student.studentCode!}</td>
			<td class="">${dto.className!}</td>
			<td class="">${dto.placeName!}</td>
		</tr>
		</#list>
	<#else>
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
	</#if>
	</tbody>
</table>
</form>