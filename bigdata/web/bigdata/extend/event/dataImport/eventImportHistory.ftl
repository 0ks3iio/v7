<#if importResultList?exists&&importResultList?size gt 0>
<div class="table-show height-1of1 over-auto padding-20 scrollBar4">
	<label><span style="color:blue">最近导入的15条记录</span></label>
	<table class="tables">
		<thead>
			<tr>
				<th width=120px;">状态</th>
				<th>详细信息</th>
				<th width=180px;">导入时间</th>
				<th width=150px;">操作</th>
			</tr>
		</thead>
		<tbody>
				<#list importResultList as result>
				<tr>
					<td><#if result.importStatus?default(2) ==2><span class="success-left">成功</span><#elseif result.importStatus?default(2) ==3><span class="fail-left">失败</span></#if></td>
					<td>${result.resultMsg!}</td>
					<td>${result.modifyTime!}</td>
					<td>
						<#if result.errorFile?default('') !="">
							<a target="_blank" href="${request.contextPath}/common/download/file?filePath=${result.errorFile!}" class="table-btn color-blue js-edit" >下载错误数据</a>
						</#if>
					</td>
				</tr>
				</#list>
		</tbody>
	</table>
</div>
<#else>
	<div class="no-data-common">
		<div class="text-center">
			<img src="${request.contextPath}/bigdata/v3/static/images/public/no-data-common-100.png"/>
			<p class="color-999">暂无历史导入记录</p>
		</div>
	</div>
</#if>
<script>
	function downloadErrorData(errorFile){

	}
</script>