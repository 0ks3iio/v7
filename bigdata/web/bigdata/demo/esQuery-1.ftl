<div class="table-container no-margin">
<div class="table-container-body ">
	<table class="table table-bordered table-striped table-hover">
		<thead>
			<tr>
				<th >key</th>
				<th >value</th>
				<th >serials</th>

			</tr>
		</thead>
		<tbody>
			<#if esList?exists&&esList?size gt 0>
	          	<#list esList as es>
					<tr>
						<td>${es.key!}</td>
						<td>${es.value!}</td>
						<td>${es.serials!}</td>
					</tr>
	      	    </#list>
	  	    <#else>
				<tr >
					<td  colspan="3" align="center">
					暂无数据
					</td>
				<tr>
	        </#if>
		</tbody>
	</table>
</div>
</div>