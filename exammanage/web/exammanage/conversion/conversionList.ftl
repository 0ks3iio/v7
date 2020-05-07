<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<div class="table-container-body">
	<div class="clearfix">
	</div>
	<#if dtoList?exists && dtoList?size gt 0>
	<table class="table table-bordered table-striped table-hover">
		<thead>
			<tr>
				<th>序号</th>
				<th>学生</th>
				<th>考号</th>
				<th>学号</th>
				<th>行政班</th>
				<th>教学班</th>
				<th>考试原始分</th>
				<th>考试分排名</th>
				<th>赋分</th>
				<th>赋分等级</th>
			</tr>
		</thead>
		<tbody>
			<#list dtoList as item>
			<tr>
				<td>${item_index + 1}</td>
				<td>${item.stuName!}</td>
				<td>${item.examNum!}</td>
				<td>${item.stuCode!}</td>
				<td>${item.className!}</td>
				<td>${item.teachClassName!}</td>
				<td>${item.scoreInfo.score!}</td>
				<td>${item.scoreInfo.rank!}</td>
				<td>${item.scoreInfo.conScore!}</td>
				<td>${item.scoreInfo.scoreRank!}</td>
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
