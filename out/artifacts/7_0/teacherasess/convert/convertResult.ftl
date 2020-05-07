<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<div class="table-container-body">
	<#if resultList?exists && resultList?size gt 0>
	<table class="table table-striped table-bordered table-hover">
		<thead>
			<tr>
				<th>序号</th>
				<th>学生姓名</th>
				<th>学号</th>
				<th>行政班</th>
				<th>折算分</th>
				<#if is73Sub?default(false)&&isShow?default(false)><th>赋分</th></#if>
				<th>年级排名</th>
			</tr>
		</thead>
		<tbody>
			<#list resultList as result>
			<tr>
				<td>${result_index+1}</td>
				<td>${result.studentName!}</td>
				<td>${result.studentCode!}</td>
				<td>${result.className!}</td>
				<td>${result.score?string('#.#')}</td>
				<#if is73Sub?default(false)&&isShow?default(false)><td>${result.conScore?string('#.#')}</td></#if>
				<td>${result.rank}</td>
			</tr>
			</#list>
		
		</tbody>
	</table>
	<@htmlcom.pageToolBar container="#scoreList" class="noprint">
	</@htmlcom.pageToolBar>
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
</div>