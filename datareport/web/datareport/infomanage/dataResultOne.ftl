<h3>${titleName!}</h3>
<div class="table-container no-margin" style="overflow-x:auto;">
	<div class="table-container-body">
		<table class="table table-bordered table-striped table-hover">
			<thead>
				<tr>
					<#if rowColumns?exists&&rowColumns?size gt 0>
					<#list rowColumns as row>
						<th style="white-space: nowrap;min-width: 180px;"><#if row.isNotnull == 1><span class="color-red">*</span></#if>${row.columnName!}</th>
					</#list>
					</#if>
				</tr>
			</thead>
			<tbody>
				<#list resultsList as results>
					<tr>
						<#list results as result>
							<td>
								<#if result!="null">
									${result!}
								</#if>
							</td>
						</#list>
					</tr>
				</#list>
				<#if sumResult?exists&&sumResult?size gt 0>
					<tr>
					<#list sumResult as sum>
						<td style="white-space: nowrap;">
							<#if sum!="null">
								<#if rowColumns[sum_index].methodType==1>
									取平均：
								</#if>
								<#if rowColumns[sum_index].methodType==2>
									求和：
								</#if>
								${sum!}
							</#if>
						</td>
					</#list>
					</tr>
				</#if>
			</tbody>
		</table>
	</div>
</div>
<#if remark?exists>
</br>
<div>
	<textarea type="text/plain" style="width:100%;height:50px;">${remark!}</textarea>
</div>
</#if>