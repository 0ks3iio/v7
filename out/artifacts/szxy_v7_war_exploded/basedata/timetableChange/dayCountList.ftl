<#if dtoList?exists && (dtoList?size>0)>
<div class="col-sm-12">
	<table class="table table-bordered table-striped table-hover">
		<thead>
			<tr>
				<th>序号</th>
				<th style="width:120px">日期时间</th>
				<th style="width:120px">班级</th>
				<th>现上课老师</th>
				<th>
				<#if type?default('')==''>调/代/管信息
				<#elseif type?default('')=='1'>代课信息
				<#elseif type?default('')=='2'>管课信息
				<#elseif type?default('')=='3'>调课信息
				</#if>
				</th>
				<#if type?default('')==''>
				<th>类型</th>
				</#if>
				<th style="width:310px">备注</th>
			</tr>
		</thead>
		<tbody>
				<#list dtoList as item>
					<tr>
						<td>${item_index + 1}</td>
						<td>${item.searchDate!}</td>
						<td>${item.className!}</td>
						<td>${item.teacherName!}</td>
						<td>${item.changeStr!}</td>
						<#if type?default('')==''><td>${item.type!}</td></#if>
						<td>${item.remark!}</td>
					</tr>
				</#list>
		</tbody>
	</table>
</div>
<#else>
<div class="no-data-container ">
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