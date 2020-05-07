<div id="a1" class="tab-pane active">
	<div class="form-horizontal">
		<div class="form-group">
			<label class="col-sm-2 control-title no-padding-right"><span class="form-title">基本信息</span></label>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label no-padding color-999">问卷名称：</label>
			<div class="col-sm-10">${dataReportInfo.title!}</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label no-padding color-999">问卷收集日期：</label>
			<div class="col-sm-10">${dataReportInfo.startTime!} 至 ${dataReportInfo.endTime!}</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label no-padding color-999">是否允许上传附件：</label>
			<div class="col-sm-10"><#if dataReportInfo.isAttachment == 1>是<#else>否</#if></div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label no-padding color-999">接收对象：</label>
			<div class="col-sm-6">
				<p>共${dataReportObjs?size}个对象</p>
				<table class="table table-bordered no-margin">
					<tbody>
						<#if dataReportObjs?exists&&dataReportObjs?size gt 0>
						<tr>
						<#list dataReportObjs as obj>
							<td>${obj.objectName!}</td>
							<#if (obj_index+1) % 5 == 0 && (obj_index+1) != dataReportObjs?size>
							</tr>
							<tr>
							</#if>
						</#list>
						</tr>
						</#if>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<hr style="border-width: 1px 0 0 0;border-style: dashed; border-color: #d9d9d9;">
	<h3>${title!}</h3>
	<p class="color-red text-right">*为必填项</p>
	<div class="table-container no-margin" style="overflow-x:auto;">
		<div class="table-container-body">
			<table class="table table-bordered table-striped table-hover">
				<#if dataReportInfo.tableType == 1>
				<thead>
					<tr>
						<#if rowColumns?exists&&rowColumns?size gt 0>
						<#list rowColumns as row>
							<th style="white-space: nowrap;min-width: 180px;"><#if row.isNotnull == 1><span class="color-red">*</span>&nbsp;</#if>${row.columnName!}</th>
						</#list>
						</#if>
					</tr>
				</thead>
				<tbody>
					<tr>
						<#if rowColumns?exists&&rowColumns?size gt 0>
						<#list rowColumns as row>
							<td><#if (row.methodType)?exists>
									<#if row.methodType == 1>
										求平均
									<#elseif row.methodType == 2>
										求总和
									</#if>
								</#if>
							</td>
						</#list>
						</#if>
					</tr>
				</tbody>
				
				<#elseif dataReportInfo.tableType == 2>
				
				<tbody>
					<#if rankColumns?exists&&rankColumns?size gt 0>
					<#list rankColumns as rank>
					<tr>
						<th width="180px "style="min-width: 180px;"><#if rank.isNotnull == 1><span class="color-red">*</span>&nbsp;</#if>${rank.columnName!}</th>
						<td><#if (rank.methodType)?exists>
								<#if rank.methodType == 1>
									求平均
								<#elseif rank.methodType == 2>
									求总和
								</#if>
							</#if>
						</td>
					</tr>
					</#list>
					</#if>
				</tbody>
				
				<#else>
				
				<thead>
					<tr>
						<th width="180px" style="min-width: 180px;"></th>
						<#if rowColumns?exists&&rowColumns?size gt 0>
						<#list rowColumns as row>
							<th style="white-space: nowrap;min-width: 180px;"><#if row.isNotnull == 1><span class="color-red">*</span>&nbsp;</#if>${row.columnName!}</th>
						</#list>
						</#if>
					</tr>
				</thead>
				<tbody>
					<#if rankColumns?exists&&rankColumns?size gt 0>
					<#list rankColumns as rank>
					<tr>
						<th style="white-space: nowrap;">${rank.columnName!}</th>
						<#if rowColumns?exists&&rowColumns?size gt 0>
						<#list rowColumns as row>
						<td></td>
						</#list>
						</#if>
					</tr>
					</#list>
					</#if>
					<tr>
						<td></td>
						<#if rowColumns?exists&&rowColumns?size gt 0>
						<#list rowColumns as row>
							<td><#if (row.methodType)?exists>
									<#if row.methodType == 1>
										求平均
									<#elseif row.methodType == 2>
										求总和
									</#if>
								</#if>
							</td>
						</#list>
						</#if>
					</tr>
				</tbody>
				
				</#if>
			</table>
		</div>
		</br>
	</div>
	<#if remark?exists>
	<div>
		<textarea type="text/plain" style="width:100%;height:50px;" readonly="readonly">${remark!}</textarea>
	</div>
	</#if>
</div>