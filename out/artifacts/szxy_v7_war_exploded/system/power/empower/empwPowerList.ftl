<a href="javascript:void(0);" class="page-back-btn" onclick="showTab('3');"><i class="fa fa-arrow-left"></i> 返回</a>
<div class="table-container-body">
	<table class="table table-bordered table-striped layout-fixed">
		<thead>
			<tr>
				<th class="text-center">类型</th>
				<th>权限名称</th>
				<th>权限类型</th>
				<th>特征值</th>
				<th>是否启用</th>
			</tr>
		</thead>
		<tbody>
		   <#if showMap??&& showMap?size gt 0>
             <#list showMap?keys as key>
                 <#list showMap[key] as tr>
                    <tr>
                       <#if tr_index == 0>
							<td class="text-center" rowspan="${showMap[key]?size!}">${key!}</td>
					   </#if>
						<td>${tr.powerName!}</td>
						<td><#if tr.source == 1>默认<#else>第三方ap</#if></td>
						<td>${tr.value!}</td>
						<td><#if tr.isActive == '0'>不启用<#else>启用</#if></td> 
					</tr>
                 </#list>
            </#list>
          <#else>
			<tr>
				<td  colspan="5" align="center">
				 暂无权限
				</td>
			<tr>
		  </#if>
		</tbody>
	</table>
</div>
