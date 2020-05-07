<#import "/fw/macro/webmacro.ftl" as w>
<table class="table table-bordered table-striped table-hover">
	<thead>
		<tr>
			<th>导入文件名</th>
			<th>导入状态</th>
			<th>导入时间</th>
			<th>操作人</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
	<#---->
		<#if importList?exists && importList?size gt 0>
		<#list importList as entityDto>
		<tr>
			<td>${entityDto.importEntity.fileName!}.${entityDto.importEntity.fileType!}</td>
			<td>
				<#if entityDto.importEntity.status?default('99') == '0'>
					<p class="import-state import-state-current">等待中</p>
				<#elseif entityDto.importEntity.status?default('99') == '1'>
					<p class="import-state import-state-current">正在导入</p>
				<#elseif entityDto.importEntity.status?default('99') == '2'>
					<p class="import-state import-state-current">导入完成</p>
				<#elseif entityDto.importEntity.status?default('99') == '3'>
					<p class="import-state import-state-current">失败</p>
				<#elseif entityDto.importEntity.status?default('99') == '4'>
					<p class="import-state import-state-current">导入出错</p>
				<#else>
					<p class="import-state import-state-current">未知</p>
				</#if>
			</td>
			<td>${entityDto.importEntity.creationTime?string('yyyy-MM-dd HH:mm:ss')}</td>
			<td>${entityDto.handlerUserName!}</td>
			<td>
					<#if entityDto.successUrl?default('')!=''>
						<a class="red" href="${request.contextPath}/basedata/studentFlowOut/import/download/result?path=${entityDto.successUrl!}&fileName=${entityDto.importEntity.fileName!}&type=2">正确文件</a>
					</#if>
					<#if entityDto.errorUrl?default('')!=''>
						<a class="red" href="${request.contextPath}/basedata/studentFlowOut/import/download/result?path=${entityDto.errorUrl!}&fileName=${entityDto.importEntity.fileName!}&type=3">错误文件</a>
					</#if>
			</td>
		</tr>
		</#list>
		<#else>
			<tr><td colspan="8">无内容</td></tr>
		</#if>
	</tbody>
</table><!-- 导入数据表格结束 -->

<@w.pagination  container="#importList-div" pagination=pagination page_index=2/>