
<link rel="stylesheet" href="${request.contextPath}/static/css/iconfont.css">
<link rel="stylesheet" href="${request.contextPath}/static/css/components.css">
<link rel="stylesheet" href="${request.contextPath}/static/css/style.css">
<link rel="stylesheet" href="${request.contextPath}/static/components/bootstrap/dist/css/bootstrap.css"/>
<script src="${request.contextPath}/static/components/jquery/dist/jquery.min.js"></script>

<div class="box-body" style="background: #f2f6f9;">
	<h3>${titleName!}</h3>
	<div class="table-container no-margin">
		<div class="table-container-body">
			<table class="table table-bordered table-striped table-hover">
				<#if tableType==1>
				<thead>
					<tr>
						<#if rowColumns?exists&&rowColumns?size gt 0>
						<#list rowColumns as row>
							<th width="190px" style="text-align:center;min-width: 190px"><#if row.isNotnull == 1><span class="color-red">*</span>&nbsp;</#if>${row.columnName!}</th>
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
					<#if sumResults?exists&&sumResults?size gt 0>
						<tr>
						<#list sumResults as sum>
							<td>
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
				
				<#elseif tableType==2>
				
				<tbody>
				<#if rankColumns?exists&&rankColumns?size gt 0>
					<#list rankColumns as rank>
						<tr id="rankTr${rank_index}">
							<th width="190px" style="white-space: nowrap;min-width: 190px"><#if rank.isNotnull == 1><span class="color-red">*</span>&nbsp;</#if>${rank.columnName!}</th>
						</tr>
					</#list>
				</#if>
				</tbody>
				
				<#else>
				
				<thead>
					<tr>
						<th width="190px" style="min-width: 190px"></th>
						<#if rowColumns?exists&&rowColumns?size gt 0>
						<#list rowColumns as row>
							<th style="white-space: nowrap;min-width: 190px"><#if row.isNotnull == 1><span class="color-red">*</span></#if>${row.columnName!}</th>
						</#list>
						</#if>
					</tr>
				</thead>
				<tbody>
					<#if rankColumns?exists&&rankColumns?size gt 0>
						<#list rankColumns as rank>
							<tr id="rankTr${rank_index}">
								<th>${rank.columnName!}</th>
							</tr>
						</#list>
					</#if>
					<#if sumResults?exists&&sumResults?size gt 0>
					<tr>
						<th></th>
						<#list sumResults as sum>
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
				
				</#if>
			</table>
		</div>
	</div>
</div>
<script>
	$(function(){
	<#if tableType==2>
		<#if resultsList?exists&&resultsList?size gt 0>
			<#list resultsList as results>
				<#list results as result>
					$("#rankTr${result_index}").append('<td style="min-width: 190px"><#if result!="null">${result!}</#if></td>');
				</#list>
			</#list>
		</#if>
		<#if sumResults?exists&&sumResults?size gt 0>
			<#list sumResults as sum>
				$("#rankTr${sum_index}").append('<td style="white-space: nowrap;"><#if sum!="null"><#if rankColumns[sum_index].methodType==1>取平均：</#if><#if rankColumns[sum_index].methodType==2>求和：</#if>${sum!}</#if></td>');
			</#list>
		</#if>
	</#if>
	<#if tableType==3>
		<#if resultsList?exists&&resultsList?size gt 0>
			<#list resultsList as results>
				<#list results as result>
					$("#rankTr${results_index}").append('<td><#if result!="null">${result!}</#if></td>');
				</#list>
			</#list>
		</#if>
	</#if>
	})
</script>
<style>
	thead {
    	display: table-row-group;
	}
	
	tr {
    	page-break-before: always;
    	page-break-after: always;
    	page-break-inside: avoid;
	}
	
	table {
    	word-wrap: break-word;
	}
	
	table td {
    	word-break: break-all;
	}
</style>