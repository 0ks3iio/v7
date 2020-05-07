<div class="box box-default lay-made twice">
	<div class="head-60">
		<span>最新日志列表</span>
		(<div><span class="circle circle-very-wrong pos-left"></span>严重错误</div>
		<div><span class="circle circle-wrong pos-left"></span>错误</div>
		<div><span class="circle circle-warning pos-left"></span>警告</div>)
	</div>
	<div class="clearfix">
		<table class="table table-striped table-hover">
				<thead>
					<tr>
						<th width="12px;"></th>
						<th width="200px;">时间</th>
						<th>错误信息</th>
					</tr>
				</thead>
				<tbody>
					<#if logList?exists&&logList?size gt 0>
			          	<#list logList as log>
							<tr>
								<td <#if log.log_type =="ERROR">bgcolor="#f5222d"<#elseif  log.log_type =="FATAL">bgcolor="#a8071a"<#else>bgcolor="#faad14"</#if></td>
								<td>${log.log_time!}</td>					
								<td title="${log.log_class!}-${log.log_line!}行"><#if log.log_message! !="" && log.log_message?length gt 50>${log.log_message?substring(0, 50)}......<#else>${log.log_message!}</#if></td>
							</tr>
			      	    </#list>
			  	    <#else>
						<tr >
							<td  colspan="5" align="center">
								暂无数据
							</td>
						<tr>
			        </#if>
				</tbody>
			</table>
	</div>
</div>