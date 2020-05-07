<div class="col-sm-6">
	<h3>选考教学班</h3>
	<table class="table table-bordered layout-fixed table-editable" data-label="不可排课">
		<thead>
			<tr>
				<th style="width:10%">序号</th>
				<th style="width:20%">班级</th>
				<th style="width:10%">人数</th>
				<th>组成学生所属行政班</th>
			</tr>
		</thead>
		<tbody>
		<#if aDtoList?exists && aDtoList?size gt 0>
		<#list aDtoList as dto>
			<tr>
				<td >${dto_index+1}</td>
				<td >${dto.className!}</td>
				<td >${dto.totalNum!}</td>
				<td >
					<#if dto.normalClassList?exists &&  dto.normalClassList?size gt 0>
					<#list dto.normalClassList as dto2>
					<#if dto2_index!=0>，</#if>${dto2.className!}(${dto2.classNum!})
					</#list>
					</#if>
				</td>
			</tr>
		</#list>
		</#if>
		</tbody>
	</table>
</div>
<div class="col-sm-6">
	<h3>学考教学班</h3>
	<table class="table table-bordered layout-fixed table-editable" data-label="不可排课">
		<thead>
			<tr>
				<th style="width:10%">序号</th>
				<th style="width:20%">班级</th>
				<th style="width:10%">人数</th>
				<th>组成学生所属行政班</th>
			</tr>
		</thead>
		<tbody>
		<#if bDtoList?exists && bDtoList?size gt 0>
		<#list bDtoList as dto>
			<tr>
				<td >${dto_index+1}</td>
				<td >${dto.className!}</td>
				<td >${dto.totalNum!}</td>
				<td >
					<#if dto.normalClassList?exists &&  dto.normalClassList?size gt 0>
					<#list dto.normalClassList as dto2>
					<#if dto2_index!=0>，</#if>${dto2.className!}(${dto2.classNum!})
					</#list>
					</#if>
				</td>
			</tr>
		</#list>
		</#if>
		</tbody>
	</table>
</div>