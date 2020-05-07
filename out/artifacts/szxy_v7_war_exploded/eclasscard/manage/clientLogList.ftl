<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<table id="example" class="table table-bordered table-striped table-hover">
	<thead>
		<tr>
			<th>文件名</th>
			<th width="5%">操作</th>
		</tr>
	</thead>
	<tbody>
		<#if clientLogs?exists&&clientLogs?size gt 0>
          	<#list clientLogs as item>
				<tr>
					<td>${item.fileName!}</td>
					<td>
					<a target="_blank" href="${request.contextPath}/eccShow/eclasscard/download/clientlog?id=${item.id!}">下载</a>
					</td>
				</tr>
      	    </#list>
  	    <#else>
			<tr >
				<td  colspan="88" align="center">
				暂无数据
				</td>
			<tr>
        </#if>
	</tbody>
</table>
<#if clientLogs?exists&&clientLogs?size gt 0>
	<@htmlcom.pageToolBar container="#showList" class="noprint"/>
</#if>
<script>
$(function(){
	showBreadBack(showList,true,"班牌管理");
})
</script>