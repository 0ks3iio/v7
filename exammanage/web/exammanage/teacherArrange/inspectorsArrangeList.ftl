<#import  "/fw/macro/htmlcomponent.ftl" as htmlcomponent />
<#if teacherDtoList?exists && (teacherDtoList?size > 0)>
<div class="filter-item">
	<@htmlcomponent.printToolBar container=".print"/>
</div>
</#if>
<div class="print">
<#if teacherDtoList?exists && (teacherDtoList?size > 0)>
<table class="table table-bordered table-striped table-hover no-margin">
	<thead>
		<tr>
			<th class="text-center">序号</th>
			<th class="text-center">姓名</th>
			<th class="text-center">考试名称（场数）</th>
			<th class="text-center">合计场数</th>
		</tr>
	</thead>
	<tbody>
			<#list teacherDtoList as dto>
				<tr>
					<td class="text-center">${dto_index+1}</td>
					<td class="text-center">${dto.teacherName!}</td>
					<td class="text-center">${dto.examNames!}</td>
					<td class="text-center">${dto.examSize!}</td>
				</tr>
			</#list>
	</tbody>
</table>
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
</div>